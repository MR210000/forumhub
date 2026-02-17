-- Inserir usuário admin padrão (senha: Admin@1234)
-- Hash BCrypt de "Admin@1234"
INSERT INTO usuarios (nome, email, senha) VALUES
('Administrador', 'admin@forumhub.com', '$2a$10$Y9e5yKK/1PblTjqX.OlUEuMEClT9v7E/epLHrg0qFBMJq.X9pHo7O');

-- Inserir cursos de exemplo
INSERT INTO cursos (nome, categoria) VALUES
('Spring Boot 3', 'Backend'),
('Java 17', 'Backend'),
('React', 'Frontend'),
('Kubernetes', 'DevOps'),
('MySQL', 'Banco de Dados');
