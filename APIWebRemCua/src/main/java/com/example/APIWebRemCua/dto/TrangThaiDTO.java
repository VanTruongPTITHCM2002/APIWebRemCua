package com.example.APIWebRemCua.dto;

public class TrangThaiDTO {
    private int id;
    private String ten;

    public TrangThaiDTO() {
    }

    public TrangThaiDTO(int id, String ten) {
        this.id = id;
        this.ten = ten;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    @Override
    public String toString() {
        return "TrangThaiDTO{" +
                "id=" + id +
                ", ten='" + ten + '\'' +
                '}';
    }
}
