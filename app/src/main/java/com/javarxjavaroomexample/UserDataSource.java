package com.javarxjavaroomexample;

import com.javarxjavaroomexample.persistence.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;
public interface UserDataSource {
    Flowable<User> getUser();
    Completable insertOrUpdateUser(User user);
    void deleteAllUsers();
}
