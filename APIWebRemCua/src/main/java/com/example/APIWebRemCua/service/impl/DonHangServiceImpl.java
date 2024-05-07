package com.example.APIWebRemCua.service.impl;

import com.example.APIWebRemCua.Enum.Status_Class;
import com.example.APIWebRemCua.entity.DonHang;
import com.example.APIWebRemCua.entity.TrangThai;
import com.example.APIWebRemCua.repository.DonHangRepository;
import com.example.APIWebRemCua.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DonHangServiceImpl implements DonHangService {

    @Autowired
    DonHangRepository donHangRepository;


    @Override
    public List<DonHang> getListDonHang() {
        return donHangRepository.findAll().stream().filter(o->o.getTrangThai().getId() != 2).collect(Collectors.toList());
    }

    @Override
    public DonHang addDonHang(DonHang donhang) {
        donhang.setCt_donHangList(null);
        return donHangRepository.save(donhang);
    }

    @Override
    public DonHang getDonHangById(int id) {
        Optional<DonHang> dh = donHangRepository.findById(id);
        DonHang donHang = null;
        if(dh.isPresent()){
            donHang = dh.get();
        }
        return donHang;
    }

    @Override
    public DonHang updateDonHang(DonHang donhang) {
        return donHangRepository.save(donhang);
    }

    @Override
    public void deleteDonHangById(int id) {
        Status_Class status_class = Status_Class.DELETED;
        DonHang dh = getDonHangById(id);
        dh.setTrangThai(new TrangThai(status_class.getValue(),status_class.getTitle(status_class.getValue())));
        donHangRepository.save(dh);
        }
}

