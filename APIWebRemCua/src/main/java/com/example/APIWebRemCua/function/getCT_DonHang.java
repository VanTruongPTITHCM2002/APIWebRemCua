package com.example.APIWebRemCua.function;

import com.example.APIWebRemCua.dto.CT_DonHangDTO;
import com.example.APIWebRemCua.entity.CT_DonHang;

import java.util.ArrayList;
import java.util.List;

public class getCT_DonHang {
    public List<CT_DonHangDTO> ct_donHangList(List<CT_DonHang>lst){
        List<CT_DonHangDTO> ct_donHangDTOList = new ArrayList<>();
        for(CT_DonHang ct_donHang:lst){
            CT_DonHangDTO ct_donHangDTO = new CT_DonHangDTO();
            ct_donHangDTO.setIdrem(ct_donHang.getIdrem());
            ct_donHangDTO.setIddonhang(ct_donHang.getDonHang().getId());
            ct_donHangDTO.setGia(ct_donHang.getGia());
            ct_donHangDTO.setSoluong(ct_donHang.getSoluong());
            ct_donHangDTOList.add(ct_donHangDTO);
        }
        return ct_donHangDTOList;
    }
}
