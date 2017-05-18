package com.example.shaju.letzchat.users_page.presenter;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import android.os.Bundle;

import com.example.shaju.letzchat.login_page.model.LoginAuthModel;
import com.example.shaju.letzchat.login_page.service.LoginPageService;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;
import com.example.shaju.letzchat.users_page.service.UserPageService;
import com.example.shaju.letzchat.users_page.view.UsersListDisplayer;
import com.example.shaju.letzchat.navigations.AndroidConversationsNavigator;


/**
 * Created by Shajun on 12/02/2017.
 */

/**
 * Based on: https://github.com/novoda/bonfire-firebase-sample/blob/master/android/core/src/main/java/com/novoda/bonfire/user/presenter/UsersPresenter.java
 */

public class UsersListPresenter {

    //User details
    private User user;

    //Login Service
    private LoginPageService loginPageService;

    //User service
    private UserPageService userPageService;

    //Receiver
    private static final String DESTINATION = "destination";

    //Sender
    private static final String SENDER = "sender";


    //Display all users
    private UsersListDisplayer usersListDisplayer;

    //Navigate to the conversation
    private AndroidConversationsNavigator androidConversationsNavigator;

    //Subscriptions
    private Subscription userSubscription;
    private Subscription loginSubscription;

    //Constructor
    public UsersListPresenter(UsersListDisplayer conversationListDisplayerIN, AndroidConversationsNavigator androidConversationsNavigatorIN,
                              LoginPageService loginPageServiceIN, UserPageService userPageServiceIN) {
        usersListDisplayer = conversationListDisplayerIN;
        androidConversationsNavigator = androidConversationsNavigatorIN;
        loginPageService = loginPageServiceIN;
        userPageService = userPageServiceIN;
    }

    //Start presenting users
    public void startPresenting() {
        usersListDisplayer.attach(conversationInteractionListener);

        final Subscriber usersSubscriber = new Subscriber<Users>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onNext(Users usersIN)
            {
                usersListDisplayer.displayAllUsers(usersIN);
            }
        };

        Subscriber userSubscriber = new Subscriber<LoginAuthModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LoginAuthModel authentication) {
                userSubscription = userPageService.syncUsers()
                        .subscribe(usersSubscriber);
            }
        };

        loginSubscription = loginPageService.getAuthentication()
                .filter(successfullyAuthenticated())
                .doOnNext(new Action1<LoginAuthModel>() {
                    @Override
                    public void call(LoginAuthModel loginAuthModelIN) {
                        user = loginAuthModelIN.getUser();
                    }
                })
                .subscribe(userSubscriber);
    }

    //Stop showing
    public void stopPresenting() {
        usersListDisplayer.detach(conversationInteractionListener);
        loginSubscription.unsubscribe();

    }

    //FIlter users
    public void filterUsers(String idIN)
    {
        usersListDisplayer.filteredSearch(idIN);
    }


    private Func1<LoginAuthModel, Boolean> successfullyAuthenticated() {
        return new Func1<LoginAuthModel, Boolean>() {
            @Override
            public Boolean call(LoginAuthModel loginAuthModel) {
                return loginAuthModel.isSuccess();
            }
        };
    }

    private final UsersListDisplayer.OnUserClickInteractionListener conversationInteractionListener = new UsersListDisplayer.OnUserClickInteractionListener() {

        @Override
        public void onSpecificUserSelection(User user) {
            Bundle bundle = new Bundle();
            bundle.putString(SENDER, UsersListPresenter.this.user.getUid());
            bundle.putString(DESTINATION,user.getUid());
            androidConversationsNavigator.toSelectedConversation(bundle);
        }
    };


}
