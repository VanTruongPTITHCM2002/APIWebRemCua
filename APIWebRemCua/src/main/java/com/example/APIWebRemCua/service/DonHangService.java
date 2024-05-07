package com.example.APIWebRemCua.service;

import com.example.APIWebRemCua.entity.DonHang;
import java.util.List;


public interface DonHangService {
    List<DonHang> getListDonHang();
    DonHang addDonHang(DonHang donhang);
    DonHang getDonHangById(int id);

    DonHang updateDonHang(DonHang donhang);
    void deleteDonHangById(int id);
}
