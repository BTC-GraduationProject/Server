package com.example.demo.src.recipe;

import com.example.demo.src.food.model.GetFoodRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.recipe.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/app/recipes")
public class RecipeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final RecipeProvider recipeProvider;
    @Autowired
    private final RecipeService recipeService;
    @Autowired
    private final JwtService jwtService;

    public RecipeController(RecipeProvider recipeProvider, RecipeService recipeService, JwtService jwtService){
        this.recipeProvider = recipeProvider;
        this.recipeService = recipeService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("{userIdx}")
    public BaseResponse<List<GetRecipeRes>> getRecommendRecipes(@PathVariable("userIdx") int userIdx){
        try{
            List<GetRecipeRes> getRecipeRes = recipeProvider.getRecommendRecipe(userIdx);
            return new BaseResponse<>(getRecipeRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("{userIdx}")
    public BaseResponse<List<String>> createRecipe(@RequestBody PostRecipeReq postRecipeReq, @PathVariable("userIdx") int userIdx){
        //레시피 사진을 넣지 않았을 때
        if (postRecipeReq.getPhoto() == null || postRecipeReq.getPhoto().size() == 0) {
            return new BaseResponse<>(RECIPE_EMPTY_PHOTO);
        }
        //레시피 재료를 넣지 않았을 때
        if (postRecipeReq.getIgName() == null || postRecipeReq.getIgName().size() == 0) {
            return new BaseResponse<>(RECIPE_EMPTY_INGREDIENT);
        }

        try{
            List<String> getRecipe = recipeService.createRecipe(postRecipeReq, userIdx);
            return new BaseResponse<>(getRecipe);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("{userIdx}/like")
    public BaseResponse<PostRecomRes> createRecommend(@RequestBody PostRecomReq postRecomReq, @PathVariable("userIdx") int userIdx){
        try{
            System.out.println("컨트롤러");
            PostRecomRes postRecomRes = recipeService.createRecommend(postRecomReq,userIdx);
            return new BaseResponse<>(postRecomRes);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
