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
        String getFoodsQuery ="select F.Idx,\n" +
                "       F.foodName,\n" +
                "       F.foodPhoto,\n" +
                "       F.amount,\n" +
                "       F.storageType, \n " +
                "       F.expirationDate,\n" +
                "       case when 31 > timestampdiff(DAY , current_timestamp, F.expirationDate)\n" +
                "            then concat(timestampdiff(Day , current_timestamp, F.expirationDate), '일 남음')\n" +
                "            when 12 > timestampdiff(MONTH , current_timestamp, F.expirationDate)\n" +
                "            then concat(timestampdiff(MONTH , current_timestamp, F.expirationDate), '달 남음')\n" +
                "            else concat(timestampdiff(YEAR , current_timestamp, F.expirationDate), '년 남음')\n" +
                "            end ED_Left\n" +
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
                        rs.getString("ED_Left")),
                GetUserIdx);
    }

}

