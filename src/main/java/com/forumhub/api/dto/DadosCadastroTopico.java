package com.forumhub.api.dto;

import com.forumhub.api.entity.StatusTopico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO para criação de tópico
public record DadosCadastroTopico(
        @NotBlank(message = "Título é obrigatório")
        String titulo,

        @NotBlank(message = "Mensagem é obrigatória")
        String mensagem,

        @NotNull(message = "ID do autor é obrigatório")
        Long autorId,

        @NotNull(message = "ID do curso é obrigatório")
        Long cursoId
) {}
