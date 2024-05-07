package com.example.APIWebRemCua.dto;

import com.example.APIWebRemCua.entity.CT_DonHang;
import com.example.APIWebRemCua.entity.HinhThucThanhToan;
import com.example.APIWebRemCua.entity.TrangThai;

import java.util.Date;
import java.util.List;

public class DonHangDTO {
    private int id;
    private String email;
    private String diachi;
    private String sdt;
    private Float thanhtien;
    private int trangThai;
    private int hinhThucThanhToan;

    private List<CT_DonHangDTO> ct_donHangList;
    private Date ngaytao;

    public DonHangDTO() {
    }

    public DonHangDTO(int id, String email, String diachi, String sdt, Float thanhtien, int trangThai, int hinhThucThanhToan, List<CT_DonHangDTO> ct_donHangList, Date ngaytao) {
        this.id = id;
        this.email = email;
        this.diachi = diachi;
        this.sdt = sdt;
        this.thanhtien = thanhtien;
        this.trangThai = trangThai;
        this.hinhThucThanhToan = hinhThucThanhToan;
        this.ct_donHangList = ct_donHangList;
        this.ngaytao = ngaytao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Float getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(Float thanhtien) {
        this.thanhtien = thanhtien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getHinhThucThanhToan() {
        return hinhThucThanhToan;
    }

    public void setHinhThucThanhToan(int hinhThucThanhToan) {
        this.hinhThucThanhToan = hinhThucThanhToan;
    }

    public List<CT_DonHangDTO> getCt_donHangList() {
        return ct_donHangList;
    }

    public void setCt_donHangList(List<CT_DonHangDTO> ct_donHangList) {
        this.ct_donHangList = ct_donHangList;
    }

    public Date getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(Date ngaytao) {
        this.ngaytao = ngaytao;
    }
}
