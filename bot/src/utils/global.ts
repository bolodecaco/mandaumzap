export const port = 7000;
export const socketVersionPage =
  "https://wppconnect-team.github.io/pt-BR/whatsapp-versions";
export const versionFinder = /[1-9]+.[0-9]+.[0-9]+-alpha/;
export const versionParser = /[1-9]+.[0-9]+.[0-9]+/;
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
      {
        name: "Message",
        description: "Endpoints to manage messages",
      },
      {
        name: "Chat",
        description: "Endpoints to manage chats",
      },
    ],
  },
  apis: ["./src/controllers/*.ts"],
};
