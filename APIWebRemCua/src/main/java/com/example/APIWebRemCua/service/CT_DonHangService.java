package com.example.APIWebRemCua.service;

import com.example.APIWebRemCua.dto.CT_DonHangDTO;
import com.example.APIWebRemCua.entity.CT_DonHang;
import com.example.APIWebRemCua.entity.DonHang;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CT_DonHangService {
    ResponseEntity<?> getCT_DonHang(int id);
    ResponseEntity<?> insertCT_DonHang(int id, CT_DonHangDTO ct_donHangDTO);
    void deleteCT_DonHang(List<CT_DonHang> ct_donHangList, int id, DonHang donHang);
    CT_DonHang updateCT_DonHang(CT_DonHang ct_donHang);
}
