package com.example.demo.src.food;

import com.example.demo.src.food.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class FoodDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 식재료 리스트 쿼리
    public List<GetFoodRes> getFoods(int userIdx){
        //System.out.println("다오");
        //System.out.println(userIdx);
        String getFoodsQuery ="select F.Idx, F.foodName, F.foodPhoto, F.amount, F.storageType, F.expirationDate,\n" +
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
        return this.jdbcTemplate.query(getFoodsQuery,
                (rs,rowNum) -> new GetFoodRes(
                        rs.getInt("Idx"),
                        rs.getString("foodName"),
                        rs.getString("foodPhoto"),
                        rs.getInt("amount"),
                        rs.getInt("storageType"),
                        rs.getString("expirationDate"),
                        rs.getInt("ED_Left")),
                GetUserIdx);
    }

    // 식재료 추가 쿼리
    public int postFoods(PostFoodReq postFoodReq, int userIdx){
        String postFoodQuery = "insert into Food(userIdx, foodName, foodPhoto, categoryIdx, amount, storageType, expirationDate)\n" +
                "VALUES (?,?,?,?,?,?,?)";
        this.jdbcTemplate.update(postFoodQuery, userIdx, postFoodReq.getFoodName(), postFoodReq.getFoodPhoto(), postFoodReq.getCategoryIdx(), postFoodReq.getAmount(), postFoodReq.getStorageType(),postFoodReq.getExpirationDate());
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }



}

