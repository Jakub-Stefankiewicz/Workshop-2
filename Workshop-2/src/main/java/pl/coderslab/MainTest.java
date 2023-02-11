package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

public class MainTest {
    public static void main(String[] args) {
        UserDao userDao= new UserDao();
        User uzytkownik = new User();
        uzytkownik.setUserName("dddd22");
        uzytkownik.setEmail("jdj222d@dd.pl");
        uzytkownik.setPassword("dsdsd2222fsd");
        userDao.create(uzytkownik);

    }
}
