package com.example.APIWebRemCua.controller;

import com.example.APIWebRemCua.Enum.Status_Class;
import com.example.APIWebRemCua.dto.CT_DonHangDTO;
import com.example.APIWebRemCua.dto.DonHangDTO;
import com.example.APIWebRemCua.entity.CT_DonHang;
import com.example.APIWebRemCua.entity.DonHang;
import com.example.APIWebRemCua.service.CT_DonHangService;
import com.example.APIWebRemCua.service.DonHangService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CT_DonHangController {
    @Autowired
    private CT_DonHangService ct_donHangService;

    @Autowired
    private DonHangService donHangService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("orders/{id}/order/detail_order")
    public ResponseEntity<?> listCT_DonHang(@PathVariable int id){
        return ct_donHangService.getCT_DonHang(id);
    }

    @PostMapping("orders/{id}/order/detail_order")
    public ResponseEntity<?> addCT_DonHang(@PathVariable int id,@RequestBody CT_DonHangDTO ct_donHangDTO) throws JsonProcessingException {
        return ct_donHangService.insertCT_DonHang(id,ct_donHangDTO);
    }
    @PutMapping("orders/{orderId}/order/detail_order/{id}")
    public ResponseEntity<?> updateCT_DonHang(@PathVariable("id") Integer id, @RequestBody CT_DonHangDTO ct_donHangDTO){
        return ct_donHangService.updateCT_DonHang(id,ct_donHangDTO);
    }

    @DeleteMapping("orders/{orderId}/order/detail_order/{idrem}")
    public ResponseEntity<?> deleteCT_DonHang(@PathVariable("orderId") Integer orderId,@PathVariable("idrem") Integer idrem){
        return ct_donHangService.deleteCT_DonHang(orderId,idrem);
    }
}
