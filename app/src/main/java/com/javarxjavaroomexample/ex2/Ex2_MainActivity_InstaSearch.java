package com.javarxjavaroomexample.ex2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.javarxjavaroomexample.R;
import com.javarxjavaroomexample.ex2.adapter.NameAdapter;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Ex2_MainActivity_InstaSearch extends AppCompatActivity {
    EditText etName;
    TextView tv;
    private CompositeDisposable disposable = new CompositeDisposable();
    MyViewModel vm;
    RecyclerView rv;
    NameAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex2_main_insta_search);

        etName = findViewById(R.id.etName);
        rv = findViewById(R.id.rv);
        tv = findViewById(R.id.tv);
        disposable.add(RxTextView.textChangeEvents(etName)
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(searchMoodsTextWatcher()));
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NameAdapter();
        rv.setAdapter(adapter);
        vm= new ViewModelProvider(this).get(MyViewModel.class);
        DbUpdateObserver ob=new DbUpdateObserver(adapter);
        vm.getAllNames().subscribe(ob);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.isDisposed();
    }

    private DisposableObserver<TextViewTextChangeEvent> searchMoodsTextWatcher() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                filter(textViewTextChangeEvent.text().toString().toLowerCase());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
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
            tv.setText("Size : "+names.size());
            adapter.setNames(names);
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {
        }
    }

    private void filter(String query){
        DbUpdateObserver ob=new DbUpdateObserver(adapter);
        vm.searchNames(query).subscribe(ob);
    }
}