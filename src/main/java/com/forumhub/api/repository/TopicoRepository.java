package com.forumhub.api.repository;

import com.forumhub.api.entity.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    boolean existsByTituloAndMensagem(String titulo, String mensagem);

    @Query("""
        SELECT t FROM Topico t
        JOIN FETCH t.autor
        JOIN FETCH t.curso
        WHERE (:nomeCurso IS NULL OR LOWER(t.curso.nome) LIKE LOWER(CONCAT('%', :nomeCurso, '%')))
        AND (:ano IS NULL OR YEAR(t.dataCriacao) = :ano)
        ORDER BY t.dataCriacao ASC
        """)
    Page<Topico> findAllComFiltro(
            @Param("nomeCurso") String nomeCurso,
            @Param("ano") Integer ano,
            Pageable pageable);

    @Query("SELECT t FROM Topico t JOIN FETCH t.autor JOIN FETCH t.curso WHERE t.id = :id")
    Optional<Topico> findByIdComDetalhes(@Param("id") Long id);
}
