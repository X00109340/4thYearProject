package com.example.shaju.letzchat.main_page.presenter;

import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.login_page.model.LoginAuthModel;
import com.example.shaju.letzchat.login_page.service.LoginPageService;
import com.example.shaju.letzchat.main_page.service.MainPageService;
import com.example.shaju.letzchat.main_page.view.MainPageDisplayer;
import com.example.shaju.letzchat.navigations.AndroidMainNavigator;
import com.example.shaju.letzchat.main_page.service.CloudMessagingTokenService;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.service.UserPageService;

import rx.Subscriber;
import rx.Subscription;

import android.support.v7.app.AppCompatActivity;
import rx.functions.Action1;
import android.content.Intent;
import android.view.MenuItem;


public class MainPresenter {

    //Login subscription
    private Subscription loginSubscription;
    //message subscription
    private Subscription messageSubscription;
    //user subscription
    private Subscription userSubscription;


    //Login service
    private final LoginPageService loginPageService;
    //user service
    private final UserPageService userPageService;
    //Cloud messaging service
    private final CloudMessagingTokenService messagingService;
    //App compact activity
    private AppCompatActivity activity;
    //Token for user
    private final String token;
    //Main displayer
    private final MainPageDisplayer mainPageDisplayer;
    //main service
    private final MainPageService mainPageService;
    //Android navigator
    private final AndroidMainNavigator navigator;




    public MainPresenter(LoginPageService loginPageServiceIN, UserPageService userPageServiceIN, MainPageDisplayer mainPageDisplayerIN, MainPageService mainPageServiceIN, CloudMessagingTokenService cloudMessagingTokenServiceIN, AndroidMainNavigator androidMainNavigatorIN, String tokenIN, AppCompatActivity appCompatActivityIN) {
        mainPageDisplayer = mainPageDisplayerIN;
        loginPageService = loginPageServiceIN;
        userPageService = userPageServiceIN;
        mainPageService = mainPageServiceIN;
        token = tokenIN;
        activity = appCompatActivityIN;
        messagingService = cloudMessagingTokenServiceIN;
        navigator = androidMainNavigatorIN;

    }

    //Start showing
    public void startPresenting() {
        navigator.init();
        mainPageDisplayer.attach(drawerClickActionListeners,navigationActionListener, searchUserActionListener);

        final Subscriber userSubscriber = new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final User userIN) {
                //if user is not logged in then
                if (userIN == null)
                {
                    //Go to login
                    navigator.toFirstLogin();
                } else {
                    messageSubscription = messagingService.readUserToken(userIN)
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(String s) {
                                    if (s == null || !s.equals(token)) {
                                        messageSubscription.unsubscribe();
                                        messagingService.setUserToken(userIN);
                                    }
                                }
                            });
                    mainPageService.initialiseLastSeenDisplay(userIN);
                    mainPageDisplayer.setUserDetails(userIN);
                }
            }

        };

        loginSubscription = loginPageService.getAuthentication()
                .first().subscribe(new Action1<LoginAuthModel>() {
                    @Override
                    public void call(LoginAuthModel loginAuthModel) {
                        if (loginAuthModel.isSuccess())
                        {
                            userSubscription = userPageService.getSpecificUser(loginAuthModel.getUser().getUid())
                                    .first().subscribe(userSubscriber);

                        }
                        else
                        {
                            //go to login
                            navigator.toLogin();
                        }
                    }
                });
    }



    //This is the main drawer in the application
    private final MainPageDisplayer.DrawerClickActionListeners drawerClickActionListeners = new MainPageDisplayer.DrawerClickActionListeners() {

        @Override
        public void headerProfileSelected() {
            //go to profile setting page
            navigator.toProfile();
        }

        @Override
        public void navigationMenuItemSelected(MenuItem menuItemIN) {
            switch (menuItemIN.getItemId()) {
                case R.id.nav_conversations:
                    navigator.toConversations();
                    mainPageDisplayer.clearMenu();
                    break;

                case R.id.nav_global:
                    navigator.toGlobalRoom();
                    mainPageDisplayer.clearMenu();
                    break;

                case R.id.nav_users:
                    navigator.toUserList();
                    mainPageDisplayer.inflateMenu();
                    break;

                case R.id.nav_share:
                    navigator.toInvite();
                    break;

                case R.id.profile:
                    navigator.toProfile();
                    break;

                case R.id.logout:
                    try
                    {
                        if (mainPageService.getLoginProvider().equals("google.com"))
                        {
                            navigator.toGoogleSignOut(AndroidMainNavigator.LOGOUT_GOOGLE);
                        }

                        mainPageService.applicationLogout();
                        navigator.toLogin();

                    } catch (Exception e) {

                    }
            }
            mainPageDisplayer.closeDrawer();
        }

    };

    //Open navigation drawer on three line press
    private final MainPageDisplayer.NavigationActionListener navigationActionListener = new MainPageDisplayer.NavigationActionListener() {

        @Override
        public void onHamburgerPressed() {
            mainPageDisplayer.openDrawer();
        }

    };

    public boolean onBackPressed() {
        return mainPageDisplayer.onBackPressed();
    }


    private final MainPageDisplayer.SearchUserActionListener searchUserActionListener = new MainPageDisplayer.SearchUserActionListener() {

        @Override
        public void displayFilteredUserList(String textIN) {
            Intent intent = new Intent("SEARCH");
            intent.putExtra("search", textIN);
            activity.sendBroadcast(intent);
        }

    };


    //Stop showing
    public void stopPresenting() {
        mainPageDisplayer.detach(drawerClickActionListeners,navigationActionListener, searchUserActionListener);

        if (userSubscription != null) {
            userSubscription.unsubscribe();
        }

        if (messageSubscription != null) {
            messageSubscription.unsubscribe();
        }

        if (loginSubscription != null) {
            loginSubscription.unsubscribe();
        }

    }

}
