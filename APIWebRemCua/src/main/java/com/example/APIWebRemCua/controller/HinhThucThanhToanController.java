package com.example.APIWebRemCua.controller;

import com.example.APIWebRemCua.dto.HinhThucThanhToanDTO;
import com.example.APIWebRemCua.service.HinhThucThanhToanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class HinhThucThanhToanController {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private HinhThucThanhToanService hinhThucThanhToanService;

    @GetMapping("/payment")
    public List<HinhThucThanhToanDTO> getHinhThucThanhToan(){
        return hinhThucThanhToanService.getListHinhThucThanhToan().stream().map(o->modelMapper.map(o, HinhThucThanhToanDTO.class)).collect(Collectors.toList());
    }
}
