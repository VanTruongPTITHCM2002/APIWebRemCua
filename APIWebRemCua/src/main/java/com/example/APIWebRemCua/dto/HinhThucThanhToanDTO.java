package com.example.APIWebRemCua.dto;

public class HinhThucThanhToanDTO {
    private int id;
    private String tenhinhthuc;

    public HinhThucThanhToanDTO() {
    }

    public HinhThucThanhToanDTO(int id, String tenhinhthuc) {
        this.id = id;
        this.tenhinhthuc = tenhinhthuc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenhinhthuc() {
        return tenhinhthuc;
    }

    public void setTenhinhthuc(String tenhinhthuc) {
        this.tenhinhthuc = tenhinhthuc;
    }

    @Override
    public String toString() {
        return "HinhThucThanhToanDTO{" +
                "id=" + id +
                ", tenhinhthuc='" + tenhinhthuc + '\'' +
                '}';
    }
}
