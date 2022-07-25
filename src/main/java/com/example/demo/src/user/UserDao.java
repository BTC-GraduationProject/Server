package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * userIdx로 User 정보 검색
     * @return GetUserRes
     */
    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select U.Idx, U.userId,U.userPw, U.userImage, U.status\n" +
                               "from User U\n" +
                               "where U.Idx = ?";
        int userIdxParm = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("Idx"),
                        rs.getString("userId"),
                        rs.getString("userPw"),
                        rs.getString("userName"),
                        rs.getString("userImage"),
                        rs.getInt("status")),
                userIdxParm);
    }

    /**
     * 입력받은 Id가 DB에 존재하는지 확인하는 쿼리
     * @return 0 or 1 -> 일치하는 아이디가 있으면 1, 없으면 0 리턴
     */
    public int checkUserId(String userId){
        String getcheckIdQuery ="select exists(select *\n" +
                                "from User U\n" +
                                "where U.userId = ?) checkId";
        String chkUserIdParm = userId;
        return this.jdbcTemplate.queryForObject(getcheckIdQuery, int.class, chkUserIdParm);
    }

    /**
     * 입력받은 Id에 해당하는 비밀번호가 맞는지 확인하는 쿼리
     * @return 0 or 1 ->  일치하면 1, 아니면 0 리턴
     */
    public int checkUserPw(String userId, String userPw){
        String getcheckPwQuery = "select exists(select *\n" +
                                "from User U\n" +
                                "where U.userId = ? and U.userPw = ?) checkPw";
        String chkUserIdParm = userId;
        String chkUserPwParm = userPw;
        return this.jdbcTemplate.queryForObject(getcheckPwQuery, int.class, chkUserIdParm, chkUserPwParm);
    }


    /**
     * 입력받은 Id와 Pw에 해당하는 User의 Idx 반환
     * @return PostLoginRes
     */
    public PostLoginRes loginUser(String userId, String userPw){
        String getUserQuery = "select U.Idx\n" +
                                "from User U\n" +
                                "where U.userId = ? and U.userPw = ?";
        String chkUserIdParm = userId;
        String chkUserPwParm = userPw;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new PostLoginRes(
                        rs.getInt("Idx")),
                chkUserIdParm, chkUserPwParm);
    }






    /**
    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from UserInfo";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from UserInfo where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getId(), postUserReq.getPassword(), postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from UserInfo where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
                );

    }
    **/

}
