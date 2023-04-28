package com.javarxjavaroomexample.ex2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.javarxjavaroomexample.R;
import com.javarxjavaroomexample.ex2.adapter.NameAdapter;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Ex2_MainActivityViewModel extends AppCompatActivity {
    MyViewModel vm;
    RecyclerView rv;
    NameAdapter adapter;
    EditText etName;
    Button btnAdd,btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex2_main);
        rv = findViewById(R.id.rv);
        etName = findViewById(R.id.etName);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbInsertCompleteObserver dd=new DbInsertCompleteObserver();
                vm.addName(etName.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(dd);
            }
        });
        btnDelete = findViewById(R.id.btnDelete);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NameAdapter();
        rv.setAdapter(adapter);
        vm= new ViewModelProvider(this).get(MyViewModel.class);
        DbUpdateObserver ob=new DbUpdateObserver(adapter);
        vm.getAllNames().subscribe(ob);
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
    private class DbInsertCompleteObserver implements CompletableObserver{

        @Override
        public void onSubscribe(Disposable d) {

        }
        @Override
        public void onComplete() {
            /*names.add(mName);
            adapter.setNames(names);*/
            Toast.makeText(Ex2_MainActivityViewModel.this, "Done", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(Ex2_MainActivityViewModel.this, "Duplicate Name", Toast.LENGTH_SHORT).show();
        }
    }
}