package com.example.demo.src.main;

import com.example.demo.src.main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class MainDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetMainFoodRes> getMainFoodResList(int userIdx){
        System.out.println("foodDao");
        String getListQuery = "select F.Idx, F.foodName, F.foodPhoto, F.amount, F.storageType, F.expirationDate,\n" +
                "     case when 0 > timestampdiff(DAY , current_timestamp, F.expirationDate)\n" +
                "            then timestampdiff(DAY , current_timestamp, F.expirationDate)\n" +
                "        when 0 = timestampdiff(DAY , current_timestamp, F.expirationDate) && 0 > timestampdiff(SECOND , current_timestamp, F.expirationDate)\n" +
                "            then -1\n" +
                "        else timestampdiff(DAY , current_timestamp, F.expirationDate)\n" +
                "          end ED_Left\n" +
                "from Food F\n" +
                "where F.userIdx = ?\n" +
                "order by expirationDate";
        int GetUserIdx = userIdx;
        return this.jdbcTemplate.query(getListQuery,
                (rs,rowNum) -> new GetMainFoodRes(
                        rs.getInt("Idx"),
                        rs.getString("foodName"),
                        rs.getString("foodPhoto"),
                        rs.getInt("amount"),
                        rs.getInt("storageType"),
                        rs.getString("expirationDate"),
                        rs.getInt("ED_Left")),
                GetUserIdx);

    }

    public List<GetMainRecipeRes> getMainRecipeResList(int userIdx){
        System.out.println("RecipeDao");
        String getListQuery = "select R.Idx, R.recipeName, R.makeTime, UFI.foodName foodHave, RP.photoUrl\n" +
                "from Recipe R\n" +
                "join\n" +
                "(select *\n" +
                "from Ingredient I,(select F.foodName\n" +
                "from Food F, User U\n" +
                "where U.Idx = F.userIdx and U.Idx = ?) UF\n" +
                "where UF.foodName like I.igName)UFI on R.Idx = UFI.recipeIdx\n" +
                "join RecipePhoto RP on R.Idx = RP.recipeIdx\n" +
                "group by R.Idx";
        return this.jdbcTemplate.query(getListQuery,
                (rs,rowNum) -> new GetMainRecipeRes(
                        rs.getInt("Idx"),
                        rs.getString("recipeName"),
                        rs.getString("makeTime"),
                        rs.getString("foodHave"),
                        rs.getString("photoUrl")),
                userIdx);
    }



}
