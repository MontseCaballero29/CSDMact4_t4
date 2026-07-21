package com.dulceluna.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dulceluna.api.dto.categoria.CategoriaRequest;
import com.dulceluna.api.dto.categoria.CategoriaResponse;
import com.dulceluna.api.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /*GET /api/categorias*/
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {

        return ResponseEntity.ok(categoriaService.listar());
    }

    /* POST /api/categorias*/
    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(
            @Valid @RequestBody CategoriaRequest request) {

        CategoriaResponse categoriaCreada =
                categoriaService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaCreada);
    }
}