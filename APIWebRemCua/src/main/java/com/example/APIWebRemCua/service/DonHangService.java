package com.example.APIWebRemCua.service;

import com.example.APIWebRemCua.dto.DonHangDTO;

import org.springframework.http.ResponseEntity;




public interface DonHangService {
    ResponseEntity<?> getListDonHang();
    ResponseEntity<?> addDonHang(DonHangDTO donHangDTO);
    ResponseEntity<?> getDonHangById(int id);

    ResponseEntity<?> updateDonHang(int id,DonHangDTO donHangDTO);
    ResponseEntity<?> deleteDonHangById(int id);

    ResponseEntity<?> updateDonHangStatus(int orderId,int status);
}
