package com.example.demo.src.recipe;
import com.example.demo.src.food.model.GetFoodRes;
import com.example.demo.src.recipe.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RecipeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
// 레시피 리스트 보기
    public List<GetRecipeRes> getRecommendRecipe(int userIdx){
        String getRecipeQuery = "select R.Idx, R.recipeName, R.makeTime, UFI.foodName foodHave, RP.photoUrl\n" +
                "from Recipe R\n" +
                "join\n" +
                "(select *\n" +
                "from Ingredient I,(select F.foodName\n" +
                "from Food F, User U\n" +
                "where U.Idx = F.userIdx and U.Idx = ?) UF\n" +
                "where UF.foodName like I.igName)UFI on R.Idx = UFI.recipeIdx\n" +
                "join RecipePhoto RP on R.Idx = RP.recipeIdx\n" +
                "group by R.Idx";

        return this.jdbcTemplate.query(getRecipeQuery,
                (rs,rowNum) -> new GetRecipeRes(
                        rs.getInt("Idx"),
                        rs.getString("recipeName"),
                        rs.getString("makeTime"),
                        rs.getString("foodHave"),
                        rs.getString("photoUrl")),
                userIdx);
    }

// 레시피 상세 설명
    public GetRecipeDetailRes getRecipeDetail(int recipeIdx){
        String getRecipeDetailQuery = "select R.recipeName, R.detail, R.makeTime\n" +
                "from Recipe R\n" +
                "where R.Idx = ?";
        return this.jdbcTemplate.queryForObject(getRecipeDetailQuery,
                (rs, rowNum) -> new GetRecipeDetailRes(
                        rs.getString("recipeName"),
                        rs.getString("detail"),
                        rs.getString("makeTime")),
                        recipeIdx);
    }
// 레시피 첨부사진
    public List<GetRecipeDetailPhotoRes> GetRecipeDetailPhoto(int recipeIdx){
        String getPhotoQuery = "select RP.photoUrl\n" +
                "from RecipePhoto RP\n" +
                "where RP.recipeIdx = ?";
        return this.jdbcTemplate.query(getPhotoQuery,
                (rs,rowNum) -> new GetRecipeDetailPhotoRes(
                        rs.getString("photoUrl")),
                recipeIdx);
    }

// 레시피 첨부 Url
    public List<GetRecipeDetailUrlRes> GetRecipeDetailUrl(int recipeIdx){
        String getUrlQuery = "select RU.recipeUrl\n" +
                "from RecipeUrl RU \n" +
                "where RU.recipeIdx = ?";
        return this.jdbcTemplate.query(getUrlQuery,
                (rs,rowNum) -> new GetRecipeDetailUrlRes(
                        rs.getString("recipeUrl")),
                recipeIdx);
    }

// 레시피 재료
    public List<GetRecipeDetailIngedientRes> GetRecipeDetailIngerdients(int recipeIdx){
        String getIngedientsQuery ="select I.igName\n" +
                "from Ingredient I\n" +
                "where I.recipeIdx = ?";
        return this.jdbcTemplate.query(getIngedientsQuery,
                (rs, rowNum)-> new GetRecipeDetailIngedientRes(
                        rs.getString("igName")),
                recipeIdx);
    }









// 레시피 추가
    public PostCreateNewRecipe createNewRecipe(PostRecipeReq postRecipeReq, int userIdx){
        String createRecipeQuery  = "insert into Recipe(recipeName, userIdx, detail, makeTime)\n" +
                "VALUES (?,?,?,?)";
        this.jdbcTemplate.update(createRecipeQuery, postRecipeReq.getRecipeName(), userIdx, postRecipeReq.getDetail(), postRecipeReq.getMakeTime());
        String lastInserIdQuery = "select last_insert_id()";

        PostRecipeRes postRecipeRes;
        postRecipeRes = new PostRecipeRes(postRecipeReq.getRecipeName(), userIdx, postRecipeReq.getDetail(), postRecipeReq.getMakeTime());

        PostCreateNewRecipe recipeIdxandRes;
        recipeIdxandRes = new PostCreateNewRecipe(this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class), postRecipeRes);

        return recipeIdxandRes;
    }
// 레시피 등록시 사진 첨부
    public List<String> createRecipePicture(PostRecipeReq postRecipeReq, int recipeIdx){
        List<String> pic = postRecipeReq.getPhoto();
        List newPictureList = new ArrayList<>();

        for(String newpic:pic){
            String createRecipePicture = "insert into RecipePhoto(photoUrl, recipeIdx) values(?,?)";
            this.jdbcTemplate.update(createRecipePicture,newpic, recipeIdx);
            newPictureList.add(newpic);
        }

        return newPictureList;
    }

// 레시피 등록시 링크 첨부
    public List<String> createRecipeLink(PostRecipeReq postRecipeReq, int recipeIdx){
        List<String> url = postRecipeReq.getRecipeUrl();
        List newLinkList = new ArrayList<>();
        if(url != null){
            for(String newurl:url) {
                String createRecipeUrl = "insert into RecipeUrl(recipeUrl, recipeIdx) values (?,?)";
                this.jdbcTemplate.update(createRecipeUrl,newurl, recipeIdx);
                newLinkList.add(newurl);
            }
        }
        return newLinkList;
    }

    // 레시피 등록시 재료첨부
    public List<String> createRecipeIngredient(PostRecipeReq postRecipeReq, int recipeIdx){
        List<String> igName = postRecipeReq.getIgName();
        List newLinkList = new ArrayList<>();

        for(String newigName:igName) {
            String createRecipeIngredient = "insert into Ingredient(igName, recipeIdx) values (?,?)";
            this.jdbcTemplate.update(createRecipeIngredient, newigName, recipeIdx);
            newLinkList.add(newigName);
        }

        return newLinkList;
    }

// 레시피 좋아요 누른 전적이 있는지
    public int checkRecommend(PostRecomReq postRecomReq, int userIdx){
        String checkRecommendRecipeByUserIdx = "select exists(select *\n" +
                "from RecipeRecomend RR\n" +
                "where RR.userIdx = ? and RR.recipeIdx = ?)";
        return this.jdbcTemplate.queryForObject(checkRecommendRecipeByUserIdx, int.class, userIdx, postRecomReq.getRecipeIdx());
    }


// 레시피 좋아요 생성
    public int createRecommend(PostRecomReq postRecomReq, int userIdx){
        System.out.println("다오");
        String createRecommendQuery = "insert into RecipeRecomend(userIdx, recipeIdx) values(?,?)";
        this.jdbcTemplate.update(createRecommendQuery,userIdx, postRecomReq.getRecipeIdx());
        String lastInsertIdxQuery ="select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }




}


