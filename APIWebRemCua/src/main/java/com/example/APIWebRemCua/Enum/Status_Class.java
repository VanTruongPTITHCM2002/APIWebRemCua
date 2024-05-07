package com.example.APIWebRemCua.Enum;

public enum Status_Class {
    NOT_RECEIVED(0),
    RECEIVED(1),
    DELETED(2),
    ORDERING(3);
    private  int value;

    Status_Class(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public String getTitle(int value){
        if(value == 0){
            return "Đang vận chuyển";
        } else if (value == 1) {
            return "Đã nhận hàng";
        }else if(value == 2) {
            return "Đã xóa";
        }else{
            return "Đang đặt hàng";
        }
    }
    public int getValueString(String title){
        if(title.equals("Đang vận chuyển")){
            return 0;
        }else if(title.equals("Đã nhận hàng")){
            return 1;
        }else if(title.equals("Đã xóa")){
            return 2;
        }
        return 3;
    }
}
