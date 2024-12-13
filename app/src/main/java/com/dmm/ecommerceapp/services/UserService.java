package com.dmm.ecommerceapp.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.dmm.ecommerceapp.db.DbClient;
import com.dmm.ecommerceapp.models.User;

import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    public static UserService instance;
    DbClient dbClient;

    User currentUser;

    UserService(Context context) {
        dbClient = DbClient.getInstance(context);
    }

    public static UserService getInstance(Context context) {
        if (instance == null) {
            return new UserService(context);
        }

        instance.dbClient = DbClient.getInstance(context);
        return instance;
    }

    public boolean addUser(String email, String name, String password, String securityQuestion, String securityAnswer) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);

        user.setHashedPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setSecurityQuestion(securityQuestion);
        user.setSecurityAnswer(securityAnswer);

        return dbClient.userDao().insert(user);
    }

    public User validateUser(String email, String password) {
        User user = dbClient.userDao().getUserByEmail(email);

        if (user == null) {
            return null;
        }

        if (BCrypt.checkpw(password, user.getHashedPassword())) {
            return user;
        } else {
            return null;
        }
    }

    public void login(String email, String password) {
        User user = validateUser(email, password);
        if (user != null) {
            currentUser = user;
        }

        // TODO: Insert logic to handle persistence
    }

    public boolean checkEmailExists(String email) {
        return dbClient.userDao().getUserByEmail(email) != null;
    }

    public boolean isLoggedIn(){
        return currentUser != null;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void logout(){
        currentUser = null;
    }
}
