package com.example.APIWebRemCua.Enum;

public enum Payment_Class {
    BANKING(1),
    MOMO(2),
    CASH(3),
    VNPAY(4),
    ZALOPAY(5);

    private final int value;

    Payment_Class(int value) {
        this.value = value;
    }

    public String getTitle(int value){
        if(value == 1){
            return "Chuyển ngân hàng";
        }else if(value == 2){
            return "Ví MoMo";
        }else if(value == 3){
            return "Tiền mặt";
        }else if(value == 4){
            return "VNPay";
        }
        return "ZaloPay";
    }
}
