import z from 'zod';
const AssertObjectSchema = z.custom<object>(
  (v): v is object =>
    v !== null && (typeof v === 'object' || typeof v === 'function'),
);
const InputSchema = z.object({
  type: z.literal('object'),
  properties: z.record(z.string(), AssertObjectSchema).optional(),
  required: z.array(z.string()).optional(),
});

export type InputSchemaType = z.infer<typeof InputSchema>;
export interface Tool {
  name: string;
  description: string | undefined;
  parameters: InputSchemaType;
}
