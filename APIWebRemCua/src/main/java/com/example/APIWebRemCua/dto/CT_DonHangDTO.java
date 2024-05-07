package com.example.APIWebRemCua.dto;

import com.example.APIWebRemCua.entity.DonHang;

public class CT_DonHangDTO {
    private int idrem;
    private int iddonhang;
    private float gia;
    private int soluong;
    private String temrem;
    public CT_DonHangDTO() {
    }

    public CT_DonHangDTO(int idrem, int iddonhang, float gia, int soluong) {
        this.idrem = idrem;
        this.iddonhang = iddonhang;
        this.gia = gia;
        this.soluong = soluong;
    }

    public int getIdrem() {
        return idrem;
    }

    public void setIdrem(int idrem) {
        this.idrem = idrem;
    }

    public void setTenRem(String tenrem){
        this.temrem = tenrem;
    }

    public String getTemrem(){
        return this.temrem;
    }
    public int getIddonhang() {
        return iddonhang;
    }

    public void setIddonhang(int iddonhang) {
        this.iddonhang = iddonhang;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    @Override
    public String toString() {
        return "CT_DonHangDTO{" +
                "idrem=" + idrem +
                ", iddonhang=" + iddonhang +
                ", gia=" + gia +
                ", soluong=" + soluong +
                '}';
    }
}
