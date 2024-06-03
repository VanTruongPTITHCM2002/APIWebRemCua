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
    public ResponseEntity<?> insertCT_DonHang(DonHang donHang,List<CT_DonHangDTO> listct_donHangDTO) {

        if(donHang.getTrangThai().getId() == 2){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(), "Đơn hàng không tồn tại hoặc đã bị xóa. Không thể thêm chi tiết đơn hàng",""));
        }
        if(donHang.getTrangThai().getId() == 0 || donHang.getTrangThai().getId() == 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(),"Không thể thêm chi tiết đơn hàng trong đơn hàng này","" ));
        }


        for(CT_DonHangDTO ctDonHangDTO : listct_donHangDTO) {
            String flaskUrl = "http://127.0.0.1:7777/product_get_id_1/" + ctDonHangDTO.getIdrem();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> responseEntity = restTemplate.getForEntity(flaskUrl, Object.class);
            if(responseEntity.getBody().toString().contains("error")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(),"Không tìm thấy id này trong mặt hàng","" ));
            }
            CT_DonHang ct_donHang = new CT_DonHang();
            ct_donHang.setIdrem(ctDonHangDTO.getIdrem());
            ct_donHang.setDonHang(donHang);
            ct_donHang.setGia(ctDonHangDTO.getGia());
            ct_donHang.setSoluong(ctDonHangDTO.getSoluong());
            ct_donHangRepository.save(ct_donHang);
            donHang.setThanhtien(donHang.getThanhtien() + (ct_donHang.getGia() * ct_donHang.getSoluong()));
            flaskUrl = "http://127.0.0.1:7777/product_update_sl";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Tạo một Map để đại diện cho dữ liệu cần gửi
            Map<String, Object> requestData = new LinkedHashMap<>();
            requestData.put("id", ctDonHangDTO.getIdrem());
            requestData.put("method", "subtract");
            requestData.put("sl", ctDonHangDTO.getSoluong());

            // Gửi yêu cầu PUT với dữ liệu là một Map
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);
            restTemplate.put(flaskUrl, httpEntity, String.class);
            ctDonHangDTO.setIddonhang(donHang.getId());
        }
        donHangRepository.save(donHang);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponeObj(HttpStatus.CREATED.value(), "Thêm chi tiết thành công",null));

    }

    @Override
    public ResponseEntity<?> deleteCT_DonHang(int orderId, int id) {
        DonHang donHang = donHangRepository.findById(orderId).orElse(null);
        if(donHang.getTrangThai().getId() != 3){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(),"Không thể xóa chi tiết của đơn hàng này","" ));
        }
        for(CT_DonHang ct_donHang: donHang.getCt_donHangList()){
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
                return ResponseEntity.status(HttpStatus.OK).body(new ResponeObj(HttpStatus.OK.value(), "Xóa thành công",""));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(), "Không tìm thấy hàng",""));
    }

    @Override
    public ResponseEntity<?> updateCT_DonHang(int orderId,int id, CT_DonHangDTO ct_donHangDTO) {
        DonHang dh = donHangRepository.findById(orderId).orElse(null);
        if(dh.getTrangThai().getId() != 3){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(), "Không thể sửa chi tiết của đơn hàng này",""));
        }

        List<CT_DonHang> ct_donHangList = ct_donHangRepository.findAll().stream().filter(o->o.getDonHang().getId() == orderId).collect(Collectors.toList());
        CT_DonHang ct_donHang1 = ct_donHangList.stream().filter(o -> o.getIdrem() == id).findFirst().orElse(null);
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://127.0.0.1:7777/product_update_sl";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestData = new LinkedHashMap<>();
        if(ct_donHang1.getSoluong() > ct_donHangDTO.getSoluong()){
            // Tạo một Map để đại diện cho dữ liệu cần gửi

            requestData.put("id",id);
            requestData.put("method","add");
            requestData.put("sl", ct_donHang1.getSoluong() - ct_donHangDTO.getSoluong());
        }else{
            requestData.put("id",id);
            requestData.put("method","subtract");
            requestData.put("sl",ct_donHangDTO.getSoluong() - ct_donHang1.getSoluong());
        }
        float billdetail2 = ct_donHang1.getGia() * ct_donHang1.getSoluong();
        // Gửi yêu cầu PUT với dữ liệu là một Map
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);
        restTemplate.put(flaskUrl,httpEntity,String.class);
        ct_donHang1.setIdrem(id);
        ct_donHang1.setDonHang(dh);
        ct_donHang1.setGia(ct_donHangDTO.getGia());
        ct_donHang1.setSoluong(ct_donHangDTO.getSoluong());
        ct_donHangRepository.save(ct_donHang1);
        float billdetail = ct_donHangDTO.getGia() * ct_donHangDTO.getSoluong();

        dh.setThanhtien(billdetail);
        donHangRepository.save(dh);
        ct_donHangDTO.setIddonhang(orderId);
        ct_donHangDTO.setIdrem(id);
        return  ResponseEntity.ok().body(new ResponeObj(HttpStatus.OK.value(),"Sửa thành công chi tiết đơn hàng",ct_donHangDTO));
    }
}
