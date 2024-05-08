package com.example.APIWebRemCua.service.impl;

import com.example.APIWebRemCua.dto.CT_DonHangDTO;
import com.example.APIWebRemCua.entity.CT_DonHang;
import com.example.APIWebRemCua.entity.DonHang;
import com.example.APIWebRemCua.entity.ResponeObj;
import com.example.APIWebRemCua.repository.CT_DonHangRepository;
import com.example.APIWebRemCua.repository.DonHangRepository;
import com.example.APIWebRemCua.service.CT_DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
    public ResponseEntity<?> getCT_DonHang(int id) {
        DonHang dh = donHangRepository.findById(id).orElse(null);
        if(dh == null || dh.getTrangThai().getId() == 2){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(),"Đơn hàng và chi tiết của đơn hàng không tồn tại","" ));
        }
        List <CT_DonHang> listCTDonHang=  ct_donHangRepository.findAll().stream().filter(o->o.getDonHang().getId() == id).collect(Collectors.toList());
        List<CT_DonHangDTO> donHangDTOList = new ArrayList<>();
        for(CT_DonHang ct_donHang: listCTDonHang){
            CT_DonHangDTO ct_donHangDTO = new CT_DonHangDTO();
            ct_donHangDTO.setIdrem(ct_donHang.getIdrem());
            ct_donHangDTO.setIddonhang(ct_donHang.getDonHang().getId());
            ct_donHangDTO.setGia(ct_donHang.getGia());
            ct_donHangDTO.setSoluong(ct_donHang.getSoluong());
            donHangDTOList.add(ct_donHangDTO);
        }
        return ResponseEntity.ok().body(new ResponeObj(HttpStatus.OK.value(),"Chi tiết đơn hàng của đơn hàng số " + id,donHangDTOList ));
    }

    @Override
    public ResponseEntity<?> insertCT_DonHang(int id,CT_DonHangDTO ct_donHangDTO) {
        DonHang donHang = donHangRepository.findById(id).orElse(null);
        if(donHang.getTrangThai().getId() == 2){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(), "Đơn hàng không tồn tại hoặc đã bị xóa. Không thể thêm chi tiết đơn hàng",""));
        }
        if(donHang.getTrangThai().getId() == 0 || donHang.getTrangThai().getId() == 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(),"Không thể thêm chi tiết đơn hàng trong đơn hàng này","" ));
        }
        String flaskUrl = "http://127.0.0.1:7777/product_get_id_1/" + ct_donHangDTO.getIdrem();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(flaskUrl, Object.class);
        if(responseEntity.getBody().toString().contains("error")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(),"Không tìm thấy id này trong mặt hàng","" ));
        }
        CT_DonHang ct_donHang = new CT_DonHang();
        ct_donHang.setIdrem(ct_donHangDTO.getIdrem());
        ct_donHang.setDonHang(donHang);//donHangService.getDonHangById(id));
        ct_donHang.setGia(ct_donHangDTO.getGia());
        ct_donHang.setSoluong(ct_donHangDTO.getSoluong());
        ct_donHangRepository.save(ct_donHang);
        donHang.setThanhtien(donHang.getThanhtien() + (ct_donHang.getGia() * ct_donHang.getSoluong()));
        donHangRepository.save(donHang);
        flaskUrl = "http://127.0.0.1:7777/product_update_sl";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Tạo một Map để đại diện cho dữ liệu cần gửi
        Map<String, Object> requestData = new LinkedHashMap<>();
        requestData.put("id", ct_donHangDTO.getIdrem());
        requestData.put("method","subtract");
        requestData.put("sl", ct_donHangDTO.getSoluong());

        // Gửi yêu cầu PUT với dữ liệu là một Map
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);
        restTemplate.put(flaskUrl,httpEntity,String.class);
        ct_donHangDTO.setIddonhang(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponeObj(HttpStatus.CREATED.value(), "Thêm chi tiết thành công",ct_donHangDTO));

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
