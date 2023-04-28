package com.javarxjavaroomexample;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.javarxjavaroomexample.persistence.User;
import com.javarxjavaroomexample.persistence.UsersDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test the implementation of {@link UserDao}
 */
@RunWith(AndroidJUnit4.class)
public class UserDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final User USER = new User("id", "username");

    private UsersDatabase mDatabase;

    @Before
    public void initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                        UsersDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void getUsersWhenNoUserInserted() {
        mDatabase.userDao().getUser()
                .test()
                .assertNoValues();
    }

    @Test
    public void insertAndGetUser() {
        // When inserting a new user in the data source
        mDatabase.userDao().insertUser(USER).blockingAwait();

        // When subscribing to the emissions of the user
        mDatabase.userDao().getUser()
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue(user -> {
                    // The emitted user is the expected one
                    return user != null && user.getId().equals(USER.getId()) &&
                            user.getUserName().equals(USER.getUserName());
                });
    }

    @Test
    public void updateAndGetUser() {
        // Given that we have a user in the data source
        mDatabase.userDao().insertUser(USER).blockingAwait();

        // When we are updating the name of the user
        User updatedUser = new User(USER.getId(), "new username");
        mDatabase.userDao().insertUser(updatedUser).blockingAwait();

        // When subscribing to the emissions of the user
        mDatabase.userDao().getUser()
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue(user -> {
                    // The emitted user is the expected one
                    return user != null && user.getId().equals(USER.getId()) &&
                            user.getUserName().equals("new username");
                });
    }

    @Test
    public void deleteAndGetUser() {
        // Given that we have a user in the data source
        mDatabase.userDao().insertUser(USER).blockingAwait();

        //When we are deleting all users
        mDatabase.userDao().deleteAllUsers();
        // When subscribing to the emissions of the user
        mDatabase.userDao().getUser()
                .test()
                // check that there's no user emitted
                .assertNoValues();
    }
}
