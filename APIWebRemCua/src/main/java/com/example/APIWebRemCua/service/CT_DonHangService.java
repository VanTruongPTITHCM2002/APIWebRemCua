package com.example.APIWebRemCua.service;

import com.example.APIWebRemCua.dto.CT_DonHangDTO;
import com.example.APIWebRemCua.entity.CT_DonHang;
import com.example.APIWebRemCua.entity.DonHang;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CT_DonHangService {
    ResponseEntity<?> getCT_DonHang(int id);
    ResponseEntity<?> insertCT_DonHang(DonHang donHang, List<CT_DonHangDTO> listct_donHangDTO);
    ResponseEntity<?> deleteCT_DonHang(int orderId, int id);
    ResponseEntity<?> updateCT_DonHang(int orderId,int id, CT_DonHangDTO ct_donHangDTO);
}
