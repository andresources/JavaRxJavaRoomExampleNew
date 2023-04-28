package com.javarxjavaroomexample.ex2;

import static com.javarxjavaroomexample.ex2.db.NameDatabase.DB_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.javarxjavaroomexample.R;
import com.javarxjavaroomexample.ex2.adapter.NameAdapter;
import com.javarxjavaroomexample.ex2.db.Name;
import com.javarxjavaroomexample.ex2.db.NameDao;
import com.javarxjavaroomexample.ex2.db.NameDatabase;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableObserver;
import io.reactivex.FlowableSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Ex2_MainActivity extends AppCompatActivity {
    EditText etName;
    Button btnAdd,btnDelete;
    RecyclerView rv;
    private NameAdapter adapter;
    private ExecutorService executorService;
    private List<String> names;
    private NameDatabase db;
    private NameDao dao;
    private DbUpdateObserver updateObserver;
    private DbDeleteObserver deleteObserver;
    List<Name> str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex2_main);
        etName = findViewById(R.id.etName);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        names = new ArrayList<>();
        db = Room.databaseBuilder(this, NameDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
        dao = db.nameDao();

        adapter = new NameAdapter();
        rv.setAdapter(adapter);
        btnAdd.setOnClickListener(v ->
                dao.insert(new Name(etName.getText().toString()))
                //.subscribeOn(Schedulers.from(executorService))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DbInsertCompleteObserver(etName.getText().toString())));

        btnDelete.setOnClickListener(v -> {
            dao.deleteName(etName.getText().toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(deleteObserver);
        });

        updateObserver = new DbUpdateObserver(adapter);
        deleteObserver = new DbDeleteObserver();
        //executorService = new ThreadPoolExecutor(4, 5, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    }
    private class DbInsertCompleteObserver implements CompletableObserver{
        String mName;
        public DbInsertCompleteObserver(String name){
            mName = name;
        }

        @Override
        public void onSubscribe(Disposable d) {

        }
        @Override
        public void onComplete() {
            /*names.add(mName);
            adapter.setNames(names);*/
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(Ex2_MainActivity.this, "Duplicate Name", Toast.LENGTH_SHORT).show();
        }
    }
    private class DbDeleteObserver implements SingleObserver<Integer>{

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onSuccess(Integer aLong) {
            Toast.makeText(getApplicationContext(),""+aLong.toString(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable e) {

        }
    }
    private class DbUpdateObserver implements FlowableSubscriber<List<String>> {

        private NameAdapter adapter;

        DbUpdateObserver(NameAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(List<String> names) {
            adapter.setNames(names);
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //executorService.shutdown();
    }
    @Override
    protected void onResume() {
        super.onResume();

        dao.getAllName()
                //.subscribeOn(Schedulers.from(executorService))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateObserver);
    }
}