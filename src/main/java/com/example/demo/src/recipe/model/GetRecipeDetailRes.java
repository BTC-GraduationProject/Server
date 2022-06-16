package com.example.demo.src.recipe.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRecipeDetailRes {
    private String recipeName;
    private String detail;
    private String makeTime;
}
