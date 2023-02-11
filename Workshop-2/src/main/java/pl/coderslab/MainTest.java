package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

public class MainTest {
    public static void main(String[] args) {
        UserDao userDao= new UserDao();
        User userread= userDao.read(5);
        System.out.println(userread.toString());
        userDao.update(userread);
        userread.setUserName("name2");
        userread.setEmail("email2@wp.pl");
        userread.setPassword("gggg2222");
        userDao.update(userread);
    }
}
