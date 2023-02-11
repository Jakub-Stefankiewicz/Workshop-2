package pl.coderslab.entity;

import com.mysql.cj.exceptions.NumberOutOfRange;
import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class UserDao {

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String RESET_AUTOINCREMENT_QUERY="ALTER TABLE users AUTO_INCREMENT=1";

    private static final String UPDATE_USER_QUERY=
            "UPDATE users SET email=?, username=?, password=? WHERE id=?";

    private static final String DELETE_USER_QUERY=
            "DELETE FROM users WHERE id=?";

    public String hashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }

    public User create(User user){
        try (Connection conn= DbUtil.getConnection()){

            //resetowanie ID żeby zliczał od ostatniego
            PreparedStatement stmtres= conn.prepareStatement(RESET_AUTOINCREMENT_QUERY);
            stmtres.executeUpdate();

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

    public void delete(int userId){
        try (Connection conn= DbUtil.getConnection()) {
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT MAX(id) FROM users");
            rs.next();
            int maxId=rs.getInt(1);
            if (userId<=maxId) {
                PreparedStatement prstmt = conn.prepareStatement(DELETE_USER_QUERY);
                prstmt.setString(1, Integer.toString(userId));
                prstmt.executeUpdate();
            } else {
                throw new IndexOutOfBoundsException("Szefie, za wysokie ID- ostatnie ID jakie istnieje to " + maxId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User[] findAll(){
        try (Connection conn= DbUtil.getConnection()) {
            User[] userArr=new User[0];
            User user=new User();
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM users");
            while (rs.next()){
                user.setId(rs.getInt(1));
                user.setEmail(rs.getString(2));
                user.setUserName(rs.getString(3));
                user.setPassword(rs.getString(4));
                userArr=addToArray(user,userArr);
            }
            return userArr;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }



}
