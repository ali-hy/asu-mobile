package com.dmm.ecommerceapp.services;

import android.annotation.SuppressLint;
import android.app.Application;

import com.dmm.ecommerceapp.db.AppDatabase;
import com.dmm.ecommerceapp.models.User;
import com.dmm.ecommerceapp.utils.IFunction;
import com.dmm.ecommerceapp.utils.IFunctionNoParam;

import org.mindrot.jbcrypt.BCrypt;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableMaybeObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserService {
    public static UserService instance;
    AppDatabase appDatabase;

    User currentUser;

    UserService(Application application) {
        appDatabase = AppDatabase.getInstance(application);
    }

    public static UserService getInstance(Application application) {
        if (instance == null) {
            instance = new UserService(application);
        }

        return instance;
    }

    // Updated addUser method with DOB
    public Completable addUser(String email, String name, String password, String securityQuestion, String securityAnswer, String dob) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setSecurityQuestion(securityQuestion);
        user.setSecurityAnswer(securityAnswer);
        user.setDob(dob); // Set the Date of Birth

        return appDatabase.userDao().insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void attemptLogin(String email, String password, DisposableMaybeObserver<User> observer, IFunctionNoParam<Void> invalidHandler) {
        MaybeObserver<User> obs = getUserByEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableMaybeObserver<User>() {
                            @Override
                            public void onSuccess(@NonNull User user) {
                                if (BCrypt.checkpw(password, user.getHashedPassword())) {
                                    currentUser = user;
                                    // TODO: Insert logic to handle persistence

                                    observer.onSuccess(user);
                                } else {
                                    invalidHandler.apply();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                observer.onError(e);
                            }

                            @Override
                            public void onComplete() {
                                invalidHandler.apply();
                            }
                        }
                );
    }

    public Maybe<User> getUserByEmail(String email) {
        return appDatabase.userDao().getUserByEmail(email)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<Boolean> checkEmailExists(String email) {
        return appDatabase.userDao().getUserByEmail(email).map(user -> user != null)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<Boolean> validateSecurityAnswer(String email, String securityAnswer) {
        return getUserByEmail(email)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(user -> user.getSecurityAnswer().equals(securityAnswer));
    }

    public void updatePassword(String email, String newPassword, IFunctionNoParam<Void> onSuccess, IFunction<Exception, Void> errorHandler) {
        MaybeObserver<User> subscription = getUserByEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                new DisposableMaybeObserver<User>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(@NonNull User user) {
                        user.setHashedPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                        appDatabase.userDao().update(user)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                                        new DisposableCompletableObserver() {
                                            @Override
                                            public void onComplete() {
                                                onSuccess.apply();
                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                errorHandler.apply((Exception) e);
                                            }
                                        }
                                );
                    }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                errorHandler.apply((Exception) e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}
