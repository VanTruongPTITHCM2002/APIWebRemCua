package com.example.APIWebRemCua.utils;

import com.example.APIWebRemCua.dto.DonHangDTO;
import com.example.APIWebRemCua.entity.DonHang;
import com.example.APIWebRemCua.function.getCT_DonHang;
import org.springframework.stereotype.Component;

@Component
public class ConvertDonHang {
    public DonHangDTO convertoDTO(DonHang dh, DonHangDTO donHangDTO){
        donHangDTO.setId(dh.getId());
        donHangDTO.setDiachi(dh.getDiachi());
        donHangDTO.setEmail(dh.getEmail());
        donHangDTO.setNgaytao(dh.getNgaytao());
        donHangDTO.setSdt(dh.getSdt());
        donHangDTO.setThanhtien(dh.getThanhtien());
        donHangDTO.setTrangThai(dh.getTrangThai().getId());
        donHangDTO.setHinhThucThanhToan(dh.getHinhThucThanhToan().getId());
        // donHangDTO.setCt_donHangList(ct_donHangList(dh.getCt_donHangList()));
        donHangDTO.setCt_donHangList(new getCT_DonHang().ct_donHangList(dh.getCt_donHangList()));
        return donHangDTO;
    }
}
