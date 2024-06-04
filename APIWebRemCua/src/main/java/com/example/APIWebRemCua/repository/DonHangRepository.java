package com.example.APIWebRemCua.repository;

import com.example.APIWebRemCua.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang,Integer> {

    @Query("Update donhang d set d.trangthai =:status where d.id =:id")
    void updateOrderStatus(@Param("id") int orderId, @Param("status") int status);
}
