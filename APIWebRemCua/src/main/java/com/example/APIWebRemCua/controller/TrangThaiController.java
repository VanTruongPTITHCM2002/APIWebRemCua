package com.example.APIWebRemCua.controller;

import com.example.APIWebRemCua.dto.TrangThaiDTO;
import com.example.APIWebRemCua.service.TrangThaiService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TrangThaiController {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private TrangThaiService trangThaiService;

    @GetMapping("/status")
    public List<TrangThaiDTO> getTrangThai(){
        return trangThaiService.getTrangThai().stream().map(o->modelMapper.map(o,TrangThaiDTO.class)).collect(Collectors.toList());
    }
}
