package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;

public class UserDao {

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String UPDATE_USER_QUERY=
            "UPDATE users SET email=?, username=?, password=? WHERE id=?";

    public String hashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }

    public User create(User user){
        try (Connection conn= DbUtil.getConnection()){
            PreparedStatement statement=conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet=statement.getGeneratedKeys();
            if (resultSet.next()){
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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

    public void update(User user){
        int id= user.getId();
        try (Connection conn= DbUtil.getConnection()){
            PreparedStatement statement=conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setString(4, Integer.toString(id));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
