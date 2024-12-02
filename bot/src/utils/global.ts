export const port = 7000;
export const swaggerOptions = {
  definition: {
    openapi: "3.0.0",
    info: {
      title: "Mandaumzap api whatsapp",
      version: "1.0.0",
      description:
        "APIs direcionadas para o bot do whatsapp Mandaumzap, com endpoints para gerenciar as sessões e enviar mensagens.",
    },
    components: {
      schemas: {},
    },
    tags: [
      {
        name: "Session",
        description: "Endpoints to manage session information",
      },
    ],
  },
  apis: ["./src/controllers/*.ts"],
};
