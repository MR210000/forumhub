package com.forumhub.api.dto;

import com.forumhub.api.entity.StatusTopico;
import com.forumhub.api.entity.Topico;

import java.time.LocalDateTime;

// DTO detalhado para consulta individual
public record DadosDetalhamentoTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        StatusTopico status,
        String autorNome,
        String autorEmail,
        String cursoNome,
        String cursoCateogria
) {
    public DadosDetalhamentoTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getStatus(),
                topico.getAutor().getNome(),
                topico.getAutor().getEmail(),
                topico.getCurso().getNome(),
                topico.getCurso().getCategoria()
        );
    }
}
