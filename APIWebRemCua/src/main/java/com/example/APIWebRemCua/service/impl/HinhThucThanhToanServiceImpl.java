package com.example.APIWebRemCua.service.impl;

import com.example.APIWebRemCua.entity.HinhThucThanhToan;
import com.example.APIWebRemCua.repository.HinhThucThanhToanRepository;
import com.example.APIWebRemCua.service.HinhThucThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HinhThucThanhToanServiceImpl implements HinhThucThanhToanService {

    @Autowired
    HinhThucThanhToanRepository hinhThucThanhToanRepository;
    @Override
    public List<HinhThucThanhToan> getListHinhThucThanhToan() {
        return hinhThucThanhToanRepository.findAll();
    }
}
