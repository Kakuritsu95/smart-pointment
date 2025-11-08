import express from 'express';
import { MCPClient } from './mcp/MCPClient.js';
import { env } from './env.js';
import z from 'zod';
const app = express();
const mcpClient = new MCPClient();
await mcpClient.connectToServer();
app.listen(env.APP_PORT, (err) => {
  console.log(`App running on port: ${process.env.APP_PORT}`);
});

app.use(express.json());

app.post('/query', async (req, res) => {
  const requestSchema = z.object({
    message: z.string({ message: 'message needs to be a string' }),
  });
  try {
    const parseResult = requestSchema.parse(req.body);
    const queryResult = await mcpClient.query(parseResult.message);
    res.send(queryResult);
  } catch {
    res.status(400).send('not good query');
  }
});
