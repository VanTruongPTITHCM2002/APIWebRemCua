package com.example.APIWebRemCua.service.impl;

import com.example.APIWebRemCua.Enum.Payment_Class;
import com.example.APIWebRemCua.Enum.Status_Class;
import com.example.APIWebRemCua.dto.DonHangDTO;
import com.example.APIWebRemCua.entity.DonHang;
import com.example.APIWebRemCua.entity.HinhThucThanhToan;
import com.example.APIWebRemCua.entity.ResponeObj;
import com.example.APIWebRemCua.entity.TrangThai;
import com.example.APIWebRemCua.repository.DonHangRepository;
import com.example.APIWebRemCua.service.DonHangService;
import com.example.APIWebRemCua.utils.ConvertDonHang;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DonHangServiceImpl implements DonHangService {

    @Autowired
    DonHangRepository donHangRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ConvertDonHang convertDonHang;
    @Override
    public ResponseEntity<?> getListDonHang() {
        List<DonHangDTO> donHangDTOList = new ArrayList<>();
        List<DonHang> donHangList = donHangRepository.findAll().stream().filter(o->o.getTrangThai().getId() != 2).collect(Collectors.toList());
        for(DonHang dh:donHangList){
            DonHangDTO donHangDTO = new DonHangDTO();
            donHangDTO = convertDonHang.convertoDTO(dh,donHangDTO);
            donHangDTOList.add(donHangDTO);
        }
        return ResponseEntity.ok().body(new ResponeObj(HttpStatus.OK.value(),"Danh sách đơn hàng",donHangDTOList));
    }

    @Override
    public ResponseEntity<?> addDonHang(DonHangDTO donHangDTO) {
        DonHang donhang = modelMapper.map(donHangDTO,DonHang.class);
        Status_Class status_class = Status_Class.ORDERING;
        Payment_Class payment_class = Payment_Class.BANKING;
        if(donHangDTO.getTrangThai() != status_class.getValue()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(), "Vui lòng để trạng thái đang đặt hàng",""));
        }
        if(donHangDTO.getThanhtien() != 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(), "Vui lòng không nhập thành tiền",""));
        }
        if(donHangDTO.getCt_donHangList() != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(), "Không nhập chi tiết ở đây",""));
        }
        donhang.setTrangThai(new TrangThai(donHangDTO.getTrangThai(),status_class.getTitle(donHangDTO.getTrangThai())));
        donhang.setHinhThucThanhToan(new HinhThucThanhToan(donHangDTO.getHinhThucThanhToan(),payment_class.getTitle(donHangDTO.getHinhThucThanhToan())));
        donhang.setCt_donHangList(null);
        donhang =  donHangRepository.save(donhang);
        donHangDTO.setId(donhang.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponeObj(HttpStatus.CREATED.value(), "Thêm thành công đơn hàng",donHangDTO));
    }

    @Override
    public ResponseEntity<?> getDonHangById(int id) {
        DonHang dh = donHangRepository.findById(id).orElse(null);
        if(dh == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(),"Không tìm thấy đơn hàng số " + id,"" ));
        }
        if(dh.getTrangThai().getId() == 2){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(),"Không thể xem đơn hàng số " + id,"" ));
        }

        DonHangDTO donHangDTO = new DonHangDTO();
        donHangDTO = convertDonHang.convertoDTO(dh,donHangDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponeObj(HttpStatus.OK.value(),"Đơn hàng số " + id,donHangDTO ));
    }

    @Override
    public ResponseEntity<?> updateDonHang(int id, DonHangDTO donHangDTO) {
        DonHang dh = donHangRepository.findById(id).orElse(null);
        if (dh == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(), "Đơn hàng không tồn tại. Không thể sửa", ""));
        }
        Status_Class status_class = Status_Class.ORDERING;
        Payment_Class payment_class = Payment_Class.BANKING;
        if (dh.getTrangThai().getId() == 2) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(), "Đơn hàng không tồn tại hoặc đã bị xóa. Không thể sửa đổi", ""));
        } else if (dh.getTrangThai().getId() == 3) {
            dh.setDiachi(donHangDTO.getDiachi());
            dh.setNgaytao(donHangDTO.getNgaytao());
            dh.setSdt(donHangDTO.getSdt());
            dh.setEmail(donHangDTO.getEmail());
            dh.setThanhtien(donHangDTO.getThanhtien());
            if (donHangDTO.getTrangThai() == 1 || donHangDTO.getTrangThai() == 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(), "Vui lòng sửa đúng trạng thái", ""));
            }
            if (donHangDTO.getTrangThai() == 0 && dh.getCt_donHangList().size() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(), "Vui lòng thêm hàng trước khi sang trạng thái khác", ""));
            }
            dh.setTrangThai(new TrangThai(donHangDTO.getTrangThai(), status_class.getTitle(donHangDTO.getTrangThai())));
            dh.setHinhThucThanhToan(new HinhThucThanhToan(donHangDTO.getHinhThucThanhToan(), payment_class.getTitle(donHangDTO.getHinhThucThanhToan())));
            donHangRepository.save(dh);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponeObj(HttpStatus.OK.value(), "Đã sửa thành công đơn hàng", donHangDTO));
        } else if (dh.getTrangThai().getId() == 1) {
            if (donHangDTO.getTrangThai() != 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(), "Vui lòng sửa đúng trạng thái", ""));
            }
            dh.setTrangThai(new TrangThai(donHangDTO.getTrangThai(), status_class.getTitle(donHangDTO.getTrangThai())));
            donHangRepository.save(dh);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponeObj(HttpStatus.OK.value(), "Đã sửa thành công trạng thái đơn hàng",donHangDTO));
        }
        dh.setDiachi(donHangDTO.getDiachi());
        dh.setNgaytao(donHangDTO.getNgaytao());
        dh.setSdt(donHangDTO.getSdt());
        dh.setEmail(donHangDTO.getEmail());
        dh.setThanhtien(donHangDTO.getThanhtien());
        if(donHangDTO.getTrangThai() ==  2 || donHangDTO.getTrangThai() == 3){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(), "Vui lòng sửa đúng trạng thái",""));
        }
        dh.setTrangThai(new TrangThai(donHangDTO.getTrangThai(),status_class.getTitle(donHangDTO.getTrangThai())));
        dh.setHinhThucThanhToan(new HinhThucThanhToan(donHangDTO.getHinhThucThanhToan(),payment_class.getTitle(donHangDTO.getHinhThucThanhToan())));
        donHangRepository.save(dh);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponeObj(HttpStatus.OK.value(), "Đã sửa thành công đơn hàng",donHangDTO));
    }

    @Override
    public ResponseEntity<?> deleteDonHangById(int id) {
        Status_Class status_class = Status_Class.DELETED;

        DonHang dh = donHangRepository.findById(id).orElse(null);

        if(dh.getTrangThai().getId() == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponeObj(HttpStatus.BAD_REQUEST.value(), "Không thể xóa đơn hàng này",""));
        }
        if(dh.getTrangThai().getId() == 2){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponeObj(HttpStatus.NOT_FOUND.value(), "Không tìm thấy đơn hàng này",""));
        }
        dh.setTrangThai(new TrangThai(status_class.getValue(),status_class.getTitle(status_class.getValue())));
        donHangRepository.save(dh);
        return ResponseEntity.ok().body(new ResponeObj(HttpStatus.OK.value(),"Đã xóa thành công",""));
        }
}

