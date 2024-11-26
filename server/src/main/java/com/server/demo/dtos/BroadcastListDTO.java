package com.server.demo.dtos;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.server.demo.mappers.ChatMapper;
import com.server.demo.models.Chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para retorna da lista de transmissão")
public class BroadcastListDTO {

    @Autowired
    private ChatMapper chatMapper;

    @Schema(description = "ID da lista de transmissão")
    private UUID id;

    @Schema(description = "ID do dono da lista de transmissão")
    private UUID owner;

    @Schema(description = "Lista de chats da lista de transmissão")
    private List<ChatDTO> chats;

    @Schema(description = "Título da lista de transmissão")
    private String title;

    @Schema(description = "Descrição da lista de transmissão")
    private Date lastActiveAt;

    @Schema(description = "Quantidade de mensagens enviadas")
    private int messagesSent;

    public BroadcastListDTO(UUID id, UUID owner, List<Chat> chats, String title, Date lastActiveAt, int messagesSent) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.lastActiveAt = lastActiveAt;
        this.messagesSent = messagesSent;
        this.chats = transformChats(chats);
    }

    private List<ChatDTO> transformChats(List<Chat> chats) {
        return chats.stream()
                .map(chatMapper::toDTO)
                .collect(Collectors.toList());
    }
}
