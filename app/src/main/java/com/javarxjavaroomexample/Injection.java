package com.javarxjavaroomexample;

import android.content.Context;

import com.javarxjavaroomexample.persistence.LocalUserDataSource;
import com.javarxjavaroomexample.persistence.UsersDatabase;
import com.javarxjavaroomexample.ui.ViewModelFactory;

/**
 * Enables injection of data sources.
 */
public class Injection {

    public static UserDataSource provideUserDataSource(Context context) {
        UsersDatabase database = UsersDatabase.getInstance(context);
        return new LocalUserDataSource(database.userDao());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        UserDataSource dataSource = provideUserDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}
