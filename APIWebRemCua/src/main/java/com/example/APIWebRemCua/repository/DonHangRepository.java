package com.example.APIWebRemCua.repository;

import com.example.APIWebRemCua.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface DonHangRepository extends JpaRepository<DonHang,Integer> {


    @Query("Update DonHang d set d.trangThai.id =:status where d.id =:id")
    void updateOrderStatus(@Param("id") int orderId, @Param("status") int status);

    @Query("SELECT month(ngaytao) as thang,sum(thanhtien) as tongtien FROM DonHang\n" +
            "where Year(ngaytao) = :yearTien\n" +
            "Group by month(ngaytao)")
    List<Object[]> getSum(@Param("yearTien") int yearTien);
}
