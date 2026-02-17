package com.forumhub.api.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    // Erros de validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErroValidacao(
            MethodArgumentNotValidException ex) {

        var erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DadosErroValidacao::new)
                .toList();

        return ResponseEntity.badRequest().body(erros);
    }

    // Erros de negócio
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DadosErroNegocio> tratarErroNegocio(RuntimeException ex) {
        return ResponseEntity.badRequest()
                .body(new DadosErroNegocio(ex.getMessage()));
    }

    // Records auxiliares
    public record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    public record DadosErroNegocio(String mensagem) {}
}
