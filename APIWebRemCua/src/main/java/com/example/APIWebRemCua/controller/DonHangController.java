package com.example.APIWebRemCua.controller;
import com.example.APIWebRemCua.Enum.Payment_Class;
import com.example.APIWebRemCua.Enum.Status_Class;
import com.example.APIWebRemCua.dto.CT_DonHangDTO;
import com.example.APIWebRemCua.dto.DonHangDTO;
import com.example.APIWebRemCua.entity.CT_DonHang;
import com.example.APIWebRemCua.entity.DonHang;
import com.example.APIWebRemCua.entity.HinhThucThanhToan;
import com.example.APIWebRemCua.entity.TrangThai;
import com.example.APIWebRemCua.function.getCT_DonHang;
import com.example.APIWebRemCua.service.DonHangService;
import com.example.APIWebRemCua.utils.ConvertDonHang;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
        List<DonHangDTO> donHangDTOList = new ArrayList<>();
        List<DonHang> donHangList = donHangService.getListDonHang();
        for(DonHang dh:donHangList){
            DonHangDTO donHangDTO = new DonHangDTO();
            donHangDTO = convertDonHang.convertoDTO(dh,donHangDTO);
            donHangDTOList.add(donHangDTO);
        }
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status",HttpStatus.OK.value());
        response.put("message","Danh sách đơn hàng");
        response.put("data", donHangDTOList);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<?> searchDonHang(@PathVariable int id){
        Map<String, Object> response = new LinkedHashMap<>();
        DonHang dh = donHangService.getDonHangById(id);
        if(dh == null){
            response.put("status",HttpStatus.NOT_FOUND.value());
            response.put("message","Không tìm thấy đơn hàng số " + id);
            response.put("data", new HashMap<>());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(dh.getTrangThai().getId() == 2){
            response.put("status",HttpStatus.NOT_FOUND.value());
            response.put("message","Không thể xem đơn hàng số " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        DonHangDTO donHangDTO = new DonHangDTO();
        donHangDTO = convertDonHang.convertoDTO(dh,donHangDTO);
        response.put("status",HttpStatus.OK.value());
        response.put("message","Đơn hàng số " + id);
        response.put("data", donHangDTO);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("orders")
    public ResponseEntity<?> addDonHang(@RequestBody DonHangDTO donHangDTO){
        Map<String,Object> response = new LinkedHashMap<>();
        DonHang donHang = modelMapper.map(donHangDTO,DonHang.class);
        Status_Class status_class = Status_Class.ORDERING;
        Payment_Class payment_class = Payment_Class.BANKING;
        if(donHangDTO.getTrangThai() != status_class.getValue()){
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Vui lòng để trạng thái đang đặt hàng");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if(donHangDTO.getThanhtien() != 0){
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Vui lòng không nhập thành tiền");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if(donHangDTO.getCt_donHangList() != null){
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Không nhập chi tiết ở đây");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        donHang.setTrangThai(new TrangThai(donHangDTO.getTrangThai(),status_class.getTitle(donHangDTO.getTrangThai())));
        donHang.setHinhThucThanhToan(new HinhThucThanhToan(donHangDTO.getHinhThucThanhToan(),payment_class.getTitle(donHangDTO.getHinhThucThanhToan())));
        donHang = donHangService.addDonHang(donHang);
        donHangDTO.setId(donHang.getId());
        response.put("status",HttpStatus.CREATED.value());
        response.put("message","Thêm thành công đơn hàng");
        response.put("data",donHangDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("orders/{id}")
    public ResponseEntity<?> updateDonHang(@PathVariable("id") int id, @RequestBody DonHangDTO donHangDTO){
        Map<String,Object> response = new LinkedHashMap<>();
        DonHang dh = donHangService.getDonHangById(id);
        if(dh == null){
            response.put("status",HttpStatus.NOT_FOUND.value());
            response.put("message","Đơn hàng không tồn tại. Không thể sửa");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Status_Class status_class = Status_Class.ORDERING;
        Payment_Class payment_class = Payment_Class.BANKING;

        if(dh.getTrangThai().getId() == 2){
            response.put("status",HttpStatus.NOT_FOUND.value());
            response.put("message","Đơn hàng không tồn tại hoặc đã bị xóa. Không thể sửa đổi");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }else if(dh.getTrangThai().getId() == 3){
            dh.setDiachi(donHangDTO.getDiachi());
            dh.setNgaytao(donHangDTO.getNgaytao());
            dh.setSdt(donHangDTO.getSdt());
            dh.setEmail(donHangDTO.getEmail());
            dh.setThanhtien(donHangDTO.getThanhtien());
            if(donHangDTO.getTrangThai() == 1){
                response.put("status",HttpStatus.BAD_REQUEST.value());
                response.put("message","Vui lòng sửa đúng trạng thái");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if(donHangDTO.getTrangThai() == 0 && dh.getCt_donHangList().size() == 0){
                response.put("status",HttpStatus.BAD_REQUEST.value());
                response.put("message","Vui lòng thêm hàng trước khi sang trạng thái khác");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            dh.setTrangThai(new TrangThai(donHangDTO.getTrangThai(),status_class.getTitle(donHangDTO.getTrangThai())));
            dh.setHinhThucThanhToan(new HinhThucThanhToan(donHangDTO.getHinhThucThanhToan(),payment_class.getTitle(donHangDTO.getHinhThucThanhToan())));
            donHangService.updateDonHang(dh);
            response.put("status",HttpStatus.OK.value());
            response.put("message","Đã sửa thành công đơn hàng");
            response.put("data",donHangDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else if(dh.getTrangThai().getId() == 1){
            if(donHangDTO.getTrangThai() != 2){
                response.put("status",HttpStatus.BAD_REQUEST.value());
                response.put("message","Vui lòng sửa đúng trạng thái");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            dh.setTrangThai(new TrangThai(donHangDTO.getTrangThai(),status_class.getTitle(donHangDTO.getTrangThai())));
            response.put("status",HttpStatus.OK.value());
            response.put("message","Đã sửa thành công trạng thái đơn hàng");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        dh.setDiachi(donHangDTO.getDiachi());
        dh.setNgaytao(donHangDTO.getNgaytao());
        dh.setSdt(donHangDTO.getSdt());
        dh.setEmail(donHangDTO.getEmail());
        dh.setThanhtien(donHangDTO.getThanhtien());
        if(donHangDTO.getTrangThai() != 1 || donHangDTO.getTrangThai() != 0){
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Vui lòng sửa đúng trạng thái");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        dh.setTrangThai(new TrangThai(donHangDTO.getTrangThai(),status_class.getTitle(donHangDTO.getTrangThai())));
        dh.setHinhThucThanhToan(new HinhThucThanhToan(donHangDTO.getHinhThucThanhToan(),payment_class.getTitle(donHangDTO.getHinhThucThanhToan())));
        donHangService.updateDonHang(dh);
        response.put("status",HttpStatus.OK.value());
        response.put("message","Đã sửa thành công đơn hàng");
        response.put("data",donHangDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("orders/{id}")
    public ResponseEntity<?> deleteDonHang(@PathVariable("id") int id){
        Map<String,Object> response = new LinkedHashMap<>();
        DonHang dh = donHangService.getDonHangById(id);
        if(dh.getTrangThai().getId() == 0){
            response.put("status",HttpStatus.BAD_REQUEST.value());
            response.put("message","Không thể xóa đơn hàng này");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if(dh.getTrangThai().getId() == 2){
            response.put("status",HttpStatus.NOT_FOUND.value());
            response.put("message","Không tìm thấy đơn hàng này");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        donHangService.deleteDonHangById(id);
        response.put("status",HttpStatus.OK.value());
        response.put("message","Đã xóa thành công");
        return ResponseEntity.ok().body(response);
    }
}
