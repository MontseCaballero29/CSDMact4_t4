package com.dulceluna.api.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dulceluna.api.dto.categoria.CategoriaRequest;
import com.dulceluna.api.dto.categoria.CategoriaResponse;
import com.dulceluna.api.entity.Categoria;
import com.dulceluna.api.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /* Lista las categorías ordenadas por nombre.*/
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {

        return categoriaRepository
                .findAll(Sort.by(Sort.Direction.ASC, "nombre"))
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /*Registra una categoría nueva.*/
    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {

        String nombre = request.getNombre().trim();

        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException(
                    "Ya existe una categoría con el nombre " + nombre);
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);

        Categoria categoriaGuardada =
                categoriaRepository.save(categoria);

        return convertirAResponse(categoriaGuardada);
    }

    /* Convierte la Entity en un DTO de respuesta.*/
    private CategoriaResponse convertirAResponse(Categoria categoria) {

        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre());
    }
}