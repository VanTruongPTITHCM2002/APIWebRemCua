package com.example.APIWebRemCua.controller;

import com.example.APIWebRemCua.dto.CT_DonHangDTO;
import com.example.APIWebRemCua.dto.DonHangDTO;

import com.example.APIWebRemCua.service.DonHangService;
import com.example.APIWebRemCua.utils.ConvertDonHang;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class DonHangController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    ConvertDonHang convertDonHang;
    @Autowired
    private DonHangService donHangService;

    @GetMapping("orders")
    public ResponseEntity<?> listDonHang(){
        return donHangService.getListDonHang();
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<?> searchDonHang(@PathVariable int id){
        return donHangService.getDonHangById(id);
    }

    @PostMapping("orders")
    public ResponseEntity<?> addDonHang(@RequestBody DonHangDTO donHangDTO, List<CT_DonHangDTO> ct_donHangDTOList){
       return donHangService.addDonHang(donHangDTO,ct_donHangDTOList);
    }
    @PutMapping("orders/{id}")
    public ResponseEntity<?> updateDonHang(@PathVariable("id") int id, @RequestBody DonHangDTO donHangDTO){
        return donHangService.updateDonHang(id,donHangDTO);
    }
    @DeleteMapping("orders/{id}")
    public ResponseEntity<?> deleteDonHang(@PathVariable("id") int id){
        return donHangService.deleteDonHangById(id);
    }
}
