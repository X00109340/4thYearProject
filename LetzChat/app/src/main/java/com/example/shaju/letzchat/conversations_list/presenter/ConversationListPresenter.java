package com.example.shaju.letzchat.conversations_list.presenter;


import com.example.shaju.letzchat.login_page.model.LoginAuthModel;
import com.example.shaju.letzchat.login_page.service.LoginPageService;
import com.example.shaju.letzchat.private_conversations.model.Message;
import com.example.shaju.letzchat.conversations_list.model.ConversationModel;
import com.example.shaju.letzchat.conversations_list.service.ConversationListService;
import com.example.shaju.letzchat.conversations_list.view.ConversationsListDisplayer;
import com.example.shaju.letzchat.navigations.AndroidConversationsNavigator;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.service.UserPageService;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import android.os.Bundle;

import rx.Observable;



public class ConversationListPresenter {

    //Login service
    private LoginPageService loginPageService;
    //user service
    private UserPageService userPageService;

    private Subscription loginSubscription;
    private Subscription userSubscription;
    private Subscription messageSubscription;

    private ConversationsListDisplayer conversationsListDisplayer;
    private ConversationListService conversationListService;

    private static final String SENDER = "sender";
    private static final String DESTINATION = "destination";


    private AndroidConversationsNavigator navigator;

    //List of user IDS
    private List<String> userID;
    //Store logged in user online id
    private User self;

    //Constructor
    public ConversationListPresenter(ConversationsListDisplayer conversationsListDisplayerIN, ConversationListService conversationListServiceIN, AndroidConversationsNavigator navigatorIN, LoginPageService loginPageServiceIN, UserPageService userPageServiceIN) {
        conversationsListDisplayer = conversationsListDisplayerIN;
        conversationListService = conversationListServiceIN;
        navigator = navigatorIN;
        loginPageService = loginPageServiceIN;
        userPageService = userPageServiceIN;
    }

    //On load up of previous chats
    public void startPresenting() {
        conversationsListDisplayer.attach(conversationListInteractionListener);

        final Subscriber userSubscriber = new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            //Load everything
            @Override
            public void onNext(final User userIN) {
                messageSubscription = conversationListService.getLastMessageFor(self,userIN)
                        .subscribe(new Action1<Message>() {
                            @Override
                            public void call(Message message) {
                                conversationsListDisplayer.addToDisplay(
                                        new ConversationModel(userIN.getUid(),userIN.getName(),userIN.getImage(),message.getMessage(),message.getTimestamp()));
                            }
                        });
            }

        };
        Subscriber usersSubscriber = new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                userID = new ArrayList<>(strings);
                for (String uid: userID) {
                    userSubscription = userPageService.getSpecificUser(uid)
                            .subscribe(userSubscriber);
                }
            }

        };

        loginSubscription = loginPageService.getAuthentication()
                .filter(successfullyAuthenticated())
                .doOnNext(new Action1<LoginAuthModel>() {
                    @Override
                    public void call(LoginAuthModel loginAuthModel) {
                        //get the current logged in user name (online id)
                        self = loginAuthModel.getUser();
                    }
                })
                .flatMap(conversationsForUser())
                .subscribe(usersSubscriber);
    }



    // Solution code from http://stackoverflow.com/questions/29672705/convert-observablelistcar-to-a-sequence-of-observablecar-in-rxjava
    private Func1<LoginAuthModel, Observable<List<String>>> conversationsForUser() {
        return new Func1<LoginAuthModel, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(LoginAuthModel loginAuthModel) {
                return conversationListService.getConversationsFor(self);
            }
        };
    }

    // Solution code from http://stackoverflow.com/questions/29672705/convert-observablelistcar-to-a-sequence-of-observablecar-in-rxjava
    private Func1<LoginAuthModel, Boolean> successfullyAuthenticated() {
        return new Func1<LoginAuthModel, Boolean>() {
            @Override
            public Boolean call(LoginAuthModel loginAuthModel) {
                return loginAuthModel.isSuccess();
            }
        };
    }

    private final ConversationsListDisplayer.ConversationListInteractionListener conversationListInteractionListener = new ConversationsListDisplayer.ConversationListInteractionListener() {

        @Override
        public void onConversationSelected(ConversationModel conversationModel) {
            Bundle bundle = new Bundle();
            bundle.putString(SENDER, self.getUid());
            bundle.putString(DESTINATION, conversationModel.getUid());
            navigator.toSelectedConversation(bundle);
        }

    };

    //On close of application or when called
    public void stopPresenting() {
        conversationsListDisplayer.detach(conversationListInteractionListener);
        loginSubscription.unsubscribe();
        if (userSubscription != null) {
            userSubscription.unsubscribe();
        }
        if (messageSubscription != null)
        {
            messageSubscription.unsubscribe();
        }
    }

}
