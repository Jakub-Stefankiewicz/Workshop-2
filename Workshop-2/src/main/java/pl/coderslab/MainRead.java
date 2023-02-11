package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

public class MainRead {
    public static void main(String[] args) {
        UserDao userDao= new UserDao();
        User userread= userDao.read(4);
        System.out.println(userread.toString());

    }
}
