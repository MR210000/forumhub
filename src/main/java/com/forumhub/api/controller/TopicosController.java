package com.forumhub.api.controller;

import com.forumhub.api.dto.*;
import com.forumhub.api.entity.Topico;
import com.forumhub.api.repository.CursoRepository;
import com.forumhub.api.repository.TopicoRepository;
import com.forumhub.api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    // ===================== CRIAR TÓPICO =====================
    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoTopico> cadastrar(
            @RequestBody @Valid DadosCadastroTopico dados,
            UriComponentsBuilder uriBuilder) {

        // Verificar duplicidade
        if (topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return ResponseEntity.unprocessableEntity().build(); // 422 - duplicado
        }

        var autor = usuarioRepository.findById(dados.autorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        var curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        var topico = new Topico();
        topico.setTitulo(dados.titulo());
        topico.setMensagem(dados.mensagem());
        topico.setAutor(autor);
        topico.setCurso(curso);

        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));
    }

    // ===================== LISTAR TÓPICOS =====================
    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer ano,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC)
            Pageable pageable) {

        var page = topicoRepository
                .findAllComFiltro(curso, ano, pageable)
                .map(DadosListagemTopico::new);

        return ResponseEntity.ok(page);
    }

    // ===================== DETALHAR TÓPICO =====================
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id) {
        var topico = topicoRepository.findByIdComDetalhes(id);
        if (topico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico.get()));
    }

    // ===================== ATUALIZAR TÓPICO =====================
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoTopico> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTopico dados) {

        var optional = topicoRepository.findByIdComDetalhes(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var topico = optional.get();

        // Verificar duplicidade se título/mensagem foram informados
        if (dados.titulo() != null && dados.mensagem() != null) {
            boolean duplicado = topicoRepository.existsByTituloAndMensagem(
                    dados.titulo(), dados.mensagem());
            if (duplicado && !topico.getTitulo().equals(dados.titulo())) {
                return ResponseEntity.unprocessableEntity().build();
            }
        }

        topico.atualizar(dados.titulo(), dados.mensagem(), dados.status());
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    // ===================== EXCLUIR TÓPICO =====================
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
