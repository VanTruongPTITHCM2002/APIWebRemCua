package com.example.APIWebRemCua.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "donhang")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String diachi;
    private String sdt;
    private Float thanhtien;

    @ManyToOne
    @JoinColumn(name = "IDTRANGTHAI")
    private TrangThai trangThai;

    @ManyToOne
    @JoinColumn(name = "IDHINHTHUCTHANHTOAN")
    private  HinhThucThanhToan hinhThucThanhToan;

    @OneToMany(mappedBy = "donHang",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CT_DonHang> ct_donHangList;
    private Date ngaytao;

    @Override
    public String toString() {
        return "DonHang{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", diachi='" + diachi + '\'' +
                ", sdt='" + sdt + '\'' +
                ", thanhtien=" + thanhtien +
                ", trangThai=" + trangThai +
                ", hinhThucThanhToan=" + hinhThucThanhToan +
                ", ngaytao=" + ngaytao +
                '}';
    }
}
