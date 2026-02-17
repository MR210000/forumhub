package com.forumhub.api.dto;

import com.forumhub.api.entity.StatusTopico;

public record DadosAtualizacaoTopico(
        String titulo,
        String mensagem,
        StatusTopico status
) {}
