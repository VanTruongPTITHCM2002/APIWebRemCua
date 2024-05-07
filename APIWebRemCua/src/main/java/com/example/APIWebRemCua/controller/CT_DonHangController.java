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
        Map<String,Object> response = new LinkedHashMap<>();
        if(ct_donHangService.getCT_DonHang(id) == null){
            response.put("status",HttpStatus.NOT_FOUND.value());
            response.put("message","Đơn hàng và chi tiết của đơn hàng không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        List<CT_DonHangDTO> donHangDTOList = new ArrayList<>();
        for(CT_DonHang ct_donHang: ct_donHangService.getCT_DonHang(id)){
            CT_DonHangDTO ct_donHangDTO = new CT_DonHangDTO();
            ct_donHangDTO.setIdrem(ct_donHang.getIdrem());
            ct_donHangDTO.setIddonhang(ct_donHang.getDonHang().getId());
            ct_donHangDTO.setGia(ct_donHang.getGia());
            ct_donHangDTO.setSoluong(ct_donHang.getSoluong());
            donHangDTOList.add(ct_donHangDTO);
        }
        response.put("status",HttpStatus.OK.value());
        response.put("message","Chi tiết đơn hàng của đơn hàng số " + id);
        response.put("data",donHangDTOList);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("orders/{id}/order/detail_order")
    public ResponseEntity<?> addCT_DonHang(@PathVariable int id,@RequestBody CT_DonHangDTO ct_donHangDTO) throws JsonProcessingException {
        Map<String,Object> response = new LinkedHashMap<>();
        DonHang donHang = donHangService.getDonHangById(id);
        if(donHang.getTrangThai().getId() == 2){
            response.put("status",HttpStatus.NOT_FOUND.value());
            response.put("message","Đơn hàng không tồn tại hoặc đã bị xóa. Không thể thêm chi tiết đơn hàng");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(donHang.getTrangThai().getId() == 0 || donHang.getTrangThai().getId() == 1){
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Không thể thêm chi tiết đơn hàng trong đơn hàng này");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        String flaskUrl = "http://127.0.0.1:7777/product_get_id_1/" + ct_donHangDTO.getIdrem();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(flaskUrl, Object.class);
        if(responseEntity.getBody().toString().contains("error")){
            response.put("status",HttpStatus.NOT_FOUND.value());
            response.put("message","Không tìm thấy id này trong mặt hàng");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        CT_DonHang ct_donHang = new CT_DonHang();
        ct_donHang.setIdrem(ct_donHangDTO.getIdrem());
        ct_donHang.setDonHang(donHangService.getDonHangById(id));
        ct_donHang.setGia(ct_donHangDTO.getGia());
        ct_donHang.setSoluong(ct_donHangDTO.getSoluong());
        CT_DonHang ct_donHang1 = ct_donHangService.insertCT_DonHang(ct_donHang);
        donHang.setThanhtien(donHang.getThanhtien() + (ct_donHang.getGia() * ct_donHang.getSoluong()));
        donHangService.updateDonHang(donHang);
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

        response.put("status",HttpStatus.CREATED.value());
        response.put("message","Thêm chi tiết thành công");
        response.put("data",ct_donHangDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("orders/{orderId}/order/detail_order/{id}")
    public ResponseEntity<?> updateCT_DonHang(@PathVariable("id") Integer id, @RequestBody CT_DonHangDTO ct_donHangDTO){
        Map<String,Object> response = new LinkedHashMap<>();
        DonHang dh = donHangService.getDonHangById(ct_donHangDTO.getIddonhang());
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
        donHangService.updateDonHang(dh);
        response.put("status",HttpStatus.OK.value());
        response.put("message","Sửa thành công chi tiết đơn hàng");
        response.put("data",ct_donHangDTO);
        return  ResponseEntity.ok().body(response);
    }

    @DeleteMapping("orders/{orderId}/order/detail_order/{idrem}")
    public ResponseEntity<?> deleteCT_DonHang(@PathVariable("orderId") Integer orderId,@PathVariable("idrem") Integer idrem){
        Map<String,Object> response = new LinkedHashMap<>();
        DonHang donHang = donHangService.getDonHangById(orderId);
        if(donHang.getTrangThai().getId() != 3){
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Không thể xóa chi tiết của đơn hàng này");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ct_donHangService.deleteCT_DonHang(donHang.getCt_donHangList(),idrem,donHang);
        return ResponseEntity.status(HttpStatus.OK).body("Xóa thành công");
    }
}
