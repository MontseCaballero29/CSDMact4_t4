package com.dulceluna.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dulceluna.api.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}