package com.javarxjavaroomexample.ex2;

import static com.javarxjavaroomexample.ex2.db.NameDatabase.DB_NAME;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.javarxjavaroomexample.ex2.db.Name;
import com.javarxjavaroomexample.ex2.db.NameDao;
import com.javarxjavaroomexample.ex2.db.NameDatabase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyViewModel extends AndroidViewModel {
    MyRepositry repo;
    NameDao dao;
    public MyViewModel(Application application){
        super(application);
        repo = new MyRepositry(application);
    }

    public Flowable<List<String>> getAllNames(){
       return repo.getAllName().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Completable addName(String str){
        return repo.insertName(new Name(str));
    }

    public Flowable<List<String>> searchNames(String name){
        return repo.searchNames(name).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
