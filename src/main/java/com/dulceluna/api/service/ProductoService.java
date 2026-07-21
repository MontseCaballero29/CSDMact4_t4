package com.dulceluna.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dulceluna.api.dto.producto.ProductoRequest;
import com.dulceluna.api.dto.producto.ProductoResponse;
import com.dulceluna.api.entity.Categoria;
import com.dulceluna.api.entity.Producto;
import com.dulceluna.api.exception.ResourceNotFoundException;
import com.dulceluna.api.repository.CategoriaRepository;
import com.dulceluna.api.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository) {

        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /*
     * Lista los productos utilizando paginación.
     */
    @Transactional(readOnly = true)
    public Page<ProductoResponse> listar(Pageable pageable) {

        return productoRepository
                .findAll(pageable)
                .map(this::convertirAResponse);
    }

    /*
     * Busca un producto por su identificador.
     */
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {

        Producto producto = buscarProducto(id);

        return convertirAResponse(producto);
    }

    /*
     * Crea un producto nuevo.
     */
    @Transactional
    public ProductoResponse crear(ProductoRequest request) {

        Categoria categoria = buscarCategoria(request.getCategoriaId());

        Producto producto = new Producto();

        producto.setNombre(request.getNombre().trim());
        producto.setDescripcion(request.getDescripcion().trim());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setCategoria(categoria);

        Producto productoGuardado = productoRepository.save(producto);

        return convertirAResponse(productoGuardado);
    }

    /*
     * Actualiza completamente un producto existente.
     */
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {

        Producto producto = buscarProducto(id);
        Categoria categoria = buscarCategoria(request.getCategoriaId());

        producto.setNombre(request.getNombre().trim());
        producto.setDescripcion(request.getDescripcion().trim());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setCategoria(categoria);

        Producto productoActualizado = productoRepository.save(producto);

        return convertirAResponse(productoActualizado);
    }

    /*
     * Elimina un producto existente.
     */
    @Transactional
    public void eliminar(Long id) {

        Producto producto = buscarProducto(id);

        productoRepository.delete(producto);
    }

    /*
     * Busca un producto o genera un error 404.
     */
    private Producto buscarProducto(Long id) {

        return productoRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un producto con el id " + id));
    }

    /*
     * Busca una categoría o genera un error 404.
     */
    private Categoria buscarCategoria(Long categoriaId) {

        return categoriaRepository
                .findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una categoría con el id " + categoriaId));
    }

    /*
     * Convierte una Entity Producto en un ProductoResponse.
     */
    private ProductoResponse convertirAResponse(Producto producto) {

        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre());
    }
}