package com.example.APIWebRemCua.repository;

import com.example.APIWebRemCua.entity.CT_DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CT_DonHangRepository extends JpaRepository<CT_DonHang,Integer> {
}
