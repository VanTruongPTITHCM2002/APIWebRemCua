package com.example.APIWebRemCua.service;

import com.example.APIWebRemCua.dto.CT_DonHangDTO;
import com.example.APIWebRemCua.dto.DonHangDTO;

import org.springframework.http.ResponseEntity;

import java.util.List;


public interface DonHangService {
    ResponseEntity<?> getListDonHang();
    ResponseEntity<?> addDonHang(DonHangDTO donHangDTO, List<CT_DonHangDTO> ct_donHangDTOList);
    ResponseEntity<?> getDonHangById(int id);

    ResponseEntity<?> updateDonHang(int id,DonHangDTO donHangDTO);
    ResponseEntity<?> deleteDonHangById(int id);
}
