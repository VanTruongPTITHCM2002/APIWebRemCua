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
        Map<String,Object> response = new LinkedHashMap<>();
        //DonHang dh = donHangService.getDonHangById(ct_donHangDTO.getIddonhang());
        DonHang dh = null;
        if(dh.getTrangThai().getId() != 3){
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Không thể sửa chi tiết của đơn hàng này");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        List<CT_DonHang> ct_donHangList = dh.getCt_donHangList();
        CT_DonHang ct_donHang1 = ct_donHangList.stream().filter(o -> o.getIdrem() == id).findFirst().orElse(null);
        ct_donHang1.setIdrem(ct_donHangDTO.getIdrem());
        ct_donHang1.setGia(ct_donHangDTO.getGia());
        ct_donHang1.setSoluong(ct_donHangDTO.getSoluong());
        ct_donHangService.updateCT_DonHang(ct_donHang1);
        float billdetail = ct_donHangDTO.getGia() * ct_donHangDTO.getSoluong();
        float billdetail2 = ct_donHang1.getGia() * ct_donHang1.getSoluong();
        dh.setThanhtien(billdetail < billdetail2 ? dh.getThanhtien() -(billdetail2 - billdetail):
                dh.getThanhtien() + (billdetail - billdetail2));
        //donHangService.updateDonHang(dh);
        response.put("status",HttpStatus.OK.value());
        response.put("message","Sửa thành công chi tiết đơn hàng");
        response.put("data",ct_donHangDTO);
        return  ResponseEntity.ok().body(response);
    }

    @DeleteMapping("orders/{orderId}/order/detail_order/{idrem}")
    public ResponseEntity<?> deleteCT_DonHang(@PathVariable("orderId") Integer orderId,@PathVariable("idrem") Integer idrem){
        Map<String,Object> response = new LinkedHashMap<>();
        //DonHang donHang = donHangService.getDonHangById(orderId);
        DonHang donHang = null;
        if(donHang.getTrangThai().getId() != 3){
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Không thể xóa chi tiết của đơn hàng này");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ct_donHangService.deleteCT_DonHang(donHang.getCt_donHangList(),idrem,donHang);
        return ResponseEntity.status(HttpStatus.OK).body("Xóa thành công");
    }
}
