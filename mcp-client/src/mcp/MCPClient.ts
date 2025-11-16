import { Client } from '@modelcontextprotocol/sdk/client/index.js';
import { StreamableHTTPClientTransport } from '@modelcontextprotocol/sdk/client/streamableHttp.js';
import { Ollama } from 'ollama';
import { env } from '../env.js';
import type { InputSchemaType } from '../types/mcp-client-types.js';
import type { Tool } from 'ollama';
import type { Resource } from '@modelcontextprotocol/sdk/types.js';

export class MCPClient {
  mcp;
  ollamaModel: Ollama | null = null;
  transport: StreamableHTTPClientTransport | null = null;
  tools: Tool[] = [];
  resources: Resource[] = [];
  constructor() {
    this.mcp = new Client({ name: 'mcp-client-cli', version: '1.0.0' });
  }

  async connectToServer() {
    try {
      this.transport = new StreamableHTTPClientTransport(
        new URL(env.MCP_SERVER_URL),
      );
      this.ollamaModel = new Ollama({ host: env.OLLAMA_URL });
      await this.mcp.connect(this.transport);
      const toolsResult = await this.mcp.listTools();
      const resourcesResult = await this.mcp.listResources();
      console.log(toolsResult.tools[0]?.inputSchema);
      this.tools = toolsResult.tools.map((tool) => ({
        type: 'function',
        function: {
          name: tool.name,
          description: tool.description,
          parameters: tool.inputSchema as InputSchemaType,
        },
      }));
      this.resources = resourcesResult.resources;
      console.log([...this.tools, ...this.resources]);
    } catch (e) {
      console.error('Failed to connect to MCP server:', e);
      throw e;
    }
  }
  async processQuery(query: string) {
    const messages = [
      {
        role: 'user',
        content: query,
      },
    ];

    if (!this.ollamaModel) return;
    try {
      const response = await this.ollamaModel.chat({
        model: 'qwen3:1.7b',
        messages: messages,
        tools: this.tools,
      });

      if (response.message.tool_calls) {
        // process each tool call
        for await (const call of response.message.tool_calls) {
          const finalResponse = await this.mcp.callTool({
            ...call.function,
          });
          for (const responseContent of finalResponse.content) {
            messages.push({
              role: 'tool',
              content: responseContent.text,
            });
          }
        }
      } else {
        const resourceResult = await this.mcp.readResource({
          uri: this.resources[0]?.uri as string,
        });

        messages.push({
          role: 'system',
          content: `The schedule of Thodoris as csv format is this use it to provide output to the user ${resourceResult.contents[0].text}`,
        });
      }

      console.log(messages);
      const languageResponse = await this.ollamaModel.chat({
        model: 'qwen3:1.7b',
        messages: messages,
      });
      return languageResponse?.message;
    } catch (e) {
      console.log(e);
    }
  }
  async query(message: string) {
    const response = await this.processQuery(message);
    return response;
  }
  async cleanup() {
    await this.mcp.close();
  }
}
