package com.example.APIWebRemCua.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trangthai")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrangThai {
    @Id
    private int id;

    private String ten;
}
