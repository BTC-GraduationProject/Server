package com.example.demo.src.recipe;
import com.example.demo.config.BaseException;
import com.example.demo.src.recipe.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Service Create, Update, Delete 의 로직 처리
@Service
public class RecipeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RecipeDao recipeDao;
    private final RecipeProvider recipeProvider;
    private final JwtService jwtService;

    @Autowired
    public RecipeService(RecipeDao recipeDao, RecipeProvider recipeProvider, JwtService jwtService) {
        this.recipeDao = recipeDao;
        this.recipeProvider = recipeProvider;
        this.jwtService = jwtService;
    }

// 레시피 등록
    public List<String> createRecipe(PostRecipeReq postRecipeReq, int userIdx) throws BaseException{
        try{
            PostCreateNewRecipe newRecipeRes = recipeDao.createNewRecipe(postRecipeReq, userIdx);
            int recipeIdx = newRecipeRes.getRecipeIdx();
            PostRecipeRes RecipeDetail = newRecipeRes.getRecipeDetail();

            List newRecipe = new ArrayList<PostRecipeRes>();
            newRecipe.add(RecipeDetail);

            List newRecipePictureList = recipeDao.createRecipePicture(postRecipeReq, recipeIdx);
            List newRecipeUrlList = recipeDao.createRecipeLink(postRecipeReq, recipeIdx);
            List newRecipeIgNameList = recipeDao.createRecipeIngredient(postRecipeReq, recipeIdx);

            List newRecipeDetail = new ArrayList<>(Arrays.asList(newRecipe, newRecipePictureList, newRecipeUrlList, newRecipeIgNameList));

            return newRecipeDetail;
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostRecomRes createRecommend(PostRecomReq postRecomReq, int userIdx) throws BaseException{
        try{
            System.out.println("서비스");
            int checkAlreadyExist = recipeDao.checkRecommend(postRecomReq, userIdx);
            if(checkAlreadyExist == 1){
                System.out.println("이미 추천한 레시피");
                throw new BaseException(DATABASE_ERROR);
            }
            else{
                int recommendIdx = recipeDao.createRecommend(postRecomReq, userIdx);
                return new PostRecomRes(recommendIdx, userIdx, postRecomReq.getRecipeIdx());
            }
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }




}
