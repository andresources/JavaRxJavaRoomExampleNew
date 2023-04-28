package com.javarxjavaroomexample;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.javarxjavaroomexample.persistence.User;
import com.javarxjavaroomexample.ui.UserViewModel;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.Flowable;
public class UserViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserDataSource mDataSource;

    @Captor
    private ArgumentCaptor<User> mUserArgumentCaptor;

    private UserViewModel mViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mViewModel = new UserViewModel(mDataSource);
    }

    @Test
    public void getUserName_whenNoUserSaved() {
        // Given that the UserDataSource returns an empty list of users
        when(mDataSource.getUser()).thenReturn(Flowable.empty());

        //When getting the user name
        mViewModel.getUserName()
                .test()
                // The user name is empty
                .assertNoValues();
    }

    @Test
    public void getUserName_whenUserSaved() {
        // Given that the UserDataSource returns a user
        User user = new User("user name");
        when(mDataSource.getUser()).thenReturn(Flowable.just(user));

        //When getting the user name
        mViewModel.getUserName()
                .test()
                // The correct user name is emitted
                .assertValue("user name");
    }

    @Test
    public void updateUserName_updatesNameInDataSource() {
        // Given that a completable is returned when inserting a user
        when(mDataSource.insertOrUpdateUser(any())).thenReturn(Completable.complete());

        // When updating the user name
        mViewModel.updateUserName("new user name")
                .test()
                .assertComplete();

        // The user name is updated in the data source
        verify(mDataSource).insertOrUpdateUser(mUserArgumentCaptor.capture());
        assertThat(mUserArgumentCaptor.getValue().getUserName(), Matchers.is("new user name"));
    }

}
