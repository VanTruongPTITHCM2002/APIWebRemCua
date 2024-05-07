package com.example.APIWebRemCua.service;

import com.example.APIWebRemCua.entity.CT_DonHang;
import com.example.APIWebRemCua.entity.DonHang;

import java.util.List;

public interface CT_DonHangService {
    List<CT_DonHang> getCT_DonHang(int id);
    CT_DonHang insertCT_DonHang(CT_DonHang ct_donHang);
    void deleteCT_DonHang(List<CT_DonHang> ct_donHangList, int id, DonHang donHang);
    CT_DonHang updateCT_DonHang(CT_DonHang ct_donHang);
}
