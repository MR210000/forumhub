-- Tabela de tópicos
CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ABERTO','FECHADO','SOLUCIONADO') NOT NULL DEFAULT 'ABERTO',
    autor_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_titulo_mensagem (titulo, mensagem),
    CONSTRAINT fk_topicos_autor FOREIGN KEY (autor_id) REFERENCES usuarios (id),
    CONSTRAINT fk_topicos_curso FOREIGN KEY (curso_id) REFERENCES cursos (id)
);
