package com.javarxjavaroomexample.ex2;

import static com.javarxjavaroomexample.ex2.db.NameDatabase.DB_NAME;

import android.app.Application;

import androidx.room.Room;

import com.javarxjavaroomexample.ex2.db.Name;
import com.javarxjavaroomexample.ex2.db.NameDao;
import com.javarxjavaroomexample.ex2.db.NameDatabase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class MyRepositry{
    NameDao dao;
    public MyRepositry(Application app){
        dao = Room.databaseBuilder(app, NameDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build().nameDao();
    }

    public Flowable<List<String>> getAllName(){
        return dao.getAllName();
    }

    public Completable insertName(Name name){
        return dao.insert(name);
    }
}
