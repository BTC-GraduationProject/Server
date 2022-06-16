package com.example.demo.src.food.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFoodRes {
    private int Idx;
    private String foodName;
    private String foodPhoto;
    private int amount;
    private int storageType;
    private String expirationDate;
    private int ED_Left;
}
