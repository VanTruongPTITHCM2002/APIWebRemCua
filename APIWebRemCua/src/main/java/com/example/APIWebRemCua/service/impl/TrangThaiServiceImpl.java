package com.example.APIWebRemCua.service.impl;

import com.example.APIWebRemCua.entity.TrangThai;
import com.example.APIWebRemCua.repository.TrangThaiRepository;
import com.example.APIWebRemCua.service.TrangThaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrangThaiServiceImpl implements TrangThaiService {
    @Autowired
    TrangThaiRepository trangThaiRepository;
    @Override
    public List<TrangThai> getTrangThai() {
        return trangThaiRepository.findAll();
    }
}
