package com.example.demo.src.recipe.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostRecipeReq {

    private List<String> recipeUrl;
    private List<String> Photo;
    private List<String> igName;


    private String recipeName;
    private String detail;
    private String makeTime;
}
