import { Client } from '@modelcontextprotocol/sdk/client/index.js';
import { StreamableHTTPClientTransport } from '@modelcontextprotocol/sdk/client/streamableHttp.js';
import { Ollama } from 'ollama';
import { env } from '../env.js';
import type { InputSchemaType } from '../types/mcp-client-types.js';
import type { Tool } from 'ollama';

export class MCPClient {
  mcp;
  ollamaModel: Ollama | null = null;
  transport: StreamableHTTPClientTransport | null = null;
  tools: Tool[] = [];
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
      this.tools = toolsResult.tools.map((tool) => ({
        type: 'function',
        function: {
          name: tool.name,
          description: tool.description,
          parameters: tool.inputSchema as InputSchemaType,
        },
      }));
      console.log(this.tools);
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
        model: 'llama3-groq-tool-use:8b',
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
              role: 'user',
              content: responseContent.text,
            });
          }
          const languageResponse = await this.ollamaModel.chat({
            model: 'llama3-groq-tool-use:8b',
            messages: messages,
          });
          return languageResponse?.message;
        }
      }
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
