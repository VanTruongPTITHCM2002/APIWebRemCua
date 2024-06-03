package com.example.APIWebRemCua.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ct_donhang")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CT_DonHang {
    @Id
    private int id;

    private int idrem;



    @ManyToOne()
    @JoinColumn(name = "iddonhang")
    private DonHang donHang;

    private float gia;
    private int soluong;
}
