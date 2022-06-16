package com.example.demo.src.main.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMainRecipeRes {
    private int Idx;
    private String recipeName;
    private String makeTime;
    private String foodHave;
    private String photoUrl;
}
