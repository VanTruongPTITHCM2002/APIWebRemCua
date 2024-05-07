package com.example.APIWebRemCua.repository;

import com.example.APIWebRemCua.entity.TrangThai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrangThaiRepository extends JpaRepository<TrangThai,Integer> {
}
