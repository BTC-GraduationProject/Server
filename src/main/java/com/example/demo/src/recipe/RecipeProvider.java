package com.example.demo.src.recipe;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.food.FoodDao;
import com.example.demo.src.recipe.model.*;
import com.example.demo.src.recipe.RecipeDao;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class RecipeProvider {
    private final RecipeDao recipeDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RecipeProvider(RecipeDao recipeDao, JwtService jwtService) {
        this.recipeDao = recipeDao;
        this.jwtService = jwtService;
    }

    public List<GetRecipeRes> getRecommendRecipe(int userIdx) throws BaseException{
        try {
            List<GetRecipeRes> getRecipeRes = recipeDao.getRecommendRecipe(userIdx);
            return getRecipeRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<String> getRecipeDetail(int recipeIdx) throws BaseException{
        try{

            List newRecipeDetail = new ArrayList<GetRecipeDetailRes>();
            GetRecipeDetailRes getRecipeDetailRes = recipeDao.getRecipeDetail(recipeIdx);
            newRecipeDetail.add(getRecipeDetailRes);

            List newRecipeDetailPhoto = recipeDao.GetRecipeDetailPhoto(recipeIdx);
            List newRecipeDetailUrl = recipeDao.GetRecipeDetailUrl(recipeIdx);
            List newRecipeDetailIngredient = recipeDao.GetRecipeDetailIngerdients(recipeIdx);


            List newRecipeDetails = new ArrayList<>(Arrays.asList(newRecipeDetail, newRecipeDetailPhoto, newRecipeDetailUrl,newRecipeDetailIngredient));

            return newRecipeDetails;
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
