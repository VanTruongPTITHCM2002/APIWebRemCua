package com.example.APIWebRemCua.service.impl;

import com.example.APIWebRemCua.entity.CT_DonHang;
import com.example.APIWebRemCua.entity.DonHang;
import com.example.APIWebRemCua.repository.CT_DonHangRepository;
import com.example.APIWebRemCua.repository.DonHangRepository;
import com.example.APIWebRemCua.service.CT_DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CT_DonHangServiceImpl implements CT_DonHangService {
    @Autowired
    CT_DonHangRepository ct_donHangRepository;

    @Autowired
    DonHangRepository donHangRepository;
    @Override
    public List<CT_DonHang> getCT_DonHang(int id) {
        DonHang dh = donHangRepository.findById(id).orElse(null);
        if(dh == null || dh.getTrangThai().getId() == 2){
            return null;
        }
        return ct_donHangRepository.findAll().stream().filter(o->o.getDonHang().getId() == id).collect(Collectors.toList());
    }

    @Override
    public CT_DonHang insertCT_DonHang(CT_DonHang ct_donHang) {
        return ct_donHangRepository.save(ct_donHang);
    }

    @Override
    public void deleteCT_DonHang(List<CT_DonHang>ct_donHangList,int id,DonHang donHang) {
        for(CT_DonHang ct_donHang: ct_donHangList){
            if(ct_donHang.getIdrem() == id){
                donHang.setThanhtien(donHang.getThanhtien() - (ct_donHang.getSoluong() * ct_donHang.getGia()));
                RestTemplate restTemplate = new RestTemplate();
                String flaskUrl = "http://127.0.0.1:7777/product_update_sl";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                // Tạo một Map để đại diện cho dữ liệu cần gửi
                Map<String, Object> requestData = new LinkedHashMap<>();
                requestData.put("id", ct_donHang.getIdrem());
                requestData.put("method","add");
                requestData.put("sl", ct_donHang.getSoluong());

                // Gửi yêu cầu PUT với dữ liệu là một Map
                HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);
                restTemplate.put(flaskUrl,httpEntity,String.class);
                ct_donHangRepository.delete(ct_donHang);
                break;
            }
        }
    }

    @Override
    public CT_DonHang updateCT_DonHang(CT_DonHang ct_donHang) {
            return ct_donHangRepository.save(ct_donHang);
    }
}
