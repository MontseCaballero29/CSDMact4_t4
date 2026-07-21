package com.dulceluna.api.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dulceluna.api.dto.producto.ProductoRequest;
import com.dulceluna.api.dto.producto.ProductoResponse;
import com.dulceluna.api.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /*GET /api/productos?page=0&size=5&sort=id,asc*/
    @GetMapping
    public ResponseEntity<Page<ProductoResponse>> listar(
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "id",
                    direction = Direction.ASC
            ) Pageable pageable) {

        return ResponseEntity.ok(productoService.listar(pageable));
    }

    /*GET /api/productos/{id}*/
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productoService.obtenerPorId(id)
        );
    }

    /*POST /api/productos */
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(
            @Valid @RequestBody ProductoRequest request) {

        ProductoResponse productoCreado =
                productoService.crear(request);

        URI ubicacion = URI.create(
                "/api/productos/" + productoCreado.getId()
        );

        return ResponseEntity
                .created(ubicacion)
                .body(productoCreado);
    }

    /*PUT /api/productos/{id}*/
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {

        return ResponseEntity.ok(
                productoService.actualizar(id, request)
        );
    }

    /*DELETE /api/productos/{id}*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        productoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}