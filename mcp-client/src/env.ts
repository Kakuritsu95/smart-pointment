import 'dotenv/config';
import { z } from 'zod';
const envSchema = z.object({
  APP_PORT: z.preprocess(
    (x) => parseInt(x as string),
    z.number().int().min(0).max(9999),
  ),
  MCP_SERVER_URL: z.string().url(),
  OLLAMA_URL: z.string().url(),
});

export const env = envSchema.parse(process.env);
