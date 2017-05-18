package com.example.shaju.letzchat.global_chat.presenter;
import com.example.shaju.letzchat.firebase_database.FirebaseDatabaseResult;
import com.example.shaju.letzchat.global_chat.service.GlobalChatPageService;
import com.example.shaju.letzchat.global_chat.view.GlobalChatPageDisplayer;
import com.example.shaju.letzchat.login_page.model.LoginAuthModel;
import com.example.shaju.letzchat.login_page.service.LoginPageService;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;
import com.example.shaju.letzchat.users_page.service.UserPageService;
import com.example.shaju.letzchat.navigations.Navigator;
import com.example.shaju.letzchat.global_chat.model.Chat;
import com.example.shaju.letzchat.global_chat.model.Message;

import rx.functions.Action1;
import rx.Subscriber;
import rx.Subscription;


public class GlobalChatPagePresenter {

    //User details
    private User userDetails;

    //Global chat displayer
    private final GlobalChatPageDisplayer globalChatPageDisplayer;

    //Different services that are required
    private final LoginPageService loginPageService;
    private final UserPageService userPageService;
    private final GlobalChatPageService globalChatPageService;

    //Navigator for navigation
    private final Navigator navigator;

    //Not used
    private Subscription subscription;

    //Constructor
    public GlobalChatPagePresenter(LoginPageService loginPageServiceIN, GlobalChatPageService globalChatPageServiceIN, GlobalChatPageDisplayer globalChatPageDisplayerIN,
                                   UserPageService userPageServiceIN, Navigator navigatorIN)
    {
        globalChatPageDisplayer = globalChatPageDisplayerIN;
        userPageService = userPageServiceIN;
        navigator = navigatorIN;
        loginPageService = loginPageServiceIN;
        globalChatPageService = globalChatPageServiceIN;

    }


    private final GlobalChatPageDisplayer.GlobalChatPageActionListener actionListener = new GlobalChatPageDisplayer.GlobalChatPageActionListener() {



        @Override
        public void onMessageLengthChanged(int messageLengthIN) {
            if (messageLengthIN > 0)
            {
                globalChatPageDisplayer.enableInteraction();
            } else {
                globalChatPageDisplayer.disableInteraction();
            }
        }

        @Override
        public void onMessageSend(String messageIN) {
            if (userDetails != null)
            {
                globalChatPageService.sendMessage(new Message(userDetails.getUid(), messageIN));
            }
        }

    };


    //Start showing
    public void startPresenting() {
        //Attach the listeners
        globalChatPageDisplayer.attach(actionListener);
        globalChatPageDisplayer.disableInteraction();

        final Subscriber messagesSubscriber = new Subscriber<Message>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final Message message) {
                userPageService.getSpecificUser(message.getUid())
                        .subscribe(new Subscriber<User>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(User sender) {
                                //if user is authenticated then send the message to global chat
                                if (sender != null)
                                {
                                    globalChatPageDisplayer.addToDisplay(message, sender, userDetails);
                                }
                            }
                        });
            }
        };

        final Subscriber chatSubscriber = new Subscriber<FirebaseDatabaseResult<Chat>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final FirebaseDatabaseResult<Chat> firebaseDatabaseResultIN) {
                final Chat chat = firebaseDatabaseResultIN.getData();
                userPageService.getAllUsers()
                        .subscribe(new Action1<Users>() {
                            @Override
                            public void call(Users users) {
                                globalChatPageDisplayer.display(chat,users, userDetails);
                                globalChatPageService.syncAllMessages()
                                    .subscribe(messagesSubscriber);
                            }
                        });
            }
        };

        loginPageService.getAuthentication()
                .subscribe(new Subscriber<LoginAuthModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LoginAuthModel loginAuthModel) {
                        if (loginAuthModel.isSuccess())
                        {
                            userDetails = loginAuthModel.getUser();
                            subscription = globalChatPageService.getChat()
                                        .subscribe(chatSubscriber);
                        }
                    }
                });

    }

    //Stop showing and detach the listeners
    public void stopPresenting() {
        globalChatPageDisplayer.detach(actionListener);
        subscription.unsubscribe();
    }


}
