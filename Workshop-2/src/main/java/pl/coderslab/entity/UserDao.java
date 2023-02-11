package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;

public class UserDao {

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    public String hashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }



    public User read(int userId){
        try (Connection conn= DbUtil.getConnection()) {

            try {
                User user = new User();
                PreparedStatement prstmt = conn.prepareStatement("SELECT * FROM users WHERE id=?");
                prstmt.setString(1, Integer.toString(userId));
                ResultSet rs = prstmt.executeQuery();
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setUserName(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                }
                return user;

            } catch (NullPointerException e) {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
