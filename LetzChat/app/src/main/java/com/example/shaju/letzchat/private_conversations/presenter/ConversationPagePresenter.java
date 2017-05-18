package com.example.shaju.letzchat.private_conversations.presenter;

import rx.Subscriber;
import rx.Subscription;

import com.example.shaju.letzchat.login_page.service.LoginPageService;
import com.example.shaju.letzchat.private_conversations.service.ConversationPageService;
import com.example.shaju.letzchat.navigations.Navigator;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.private_conversations.view.ConversationPageDisplayer;
import com.example.shaju.letzchat.users_page.service.UserPageService;

import com.example.shaju.letzchat.private_conversations.model.Message;

public class ConversationPagePresenter {


    //Sender
    private final String sender;
    //Receiver
    private final String receiver;

    //ConversationModel service and displayer
    private final ConversationPageService conversationPageService;
    private final ConversationPageDisplayer conversationPageDisplayer;

    //Login service
    private final LoginPageService loginPageService;

    //User service
    private final UserPageService userPageService;




    //Subscription returns from Observable.subscribe(Subscriber) to allow unsubscribing.
    //See the utilities in Subscriptions and the implementations in the rx.subscriptions package.
    //This interface is the RxJava equivalent of IDisposable in Microsoft's Rx implementation.
    private Subscription subscription;
    private Subscription chatSubscription;
    private Subscription typingSubscription;

    //
    private final Navigator navigator;

    public ConversationPagePresenter(LoginPageService loginPageServiceIN, ConversationPageService conversationPageServiceIN, ConversationPageDisplayer conversationPageDisplayerIN, UserPageService userPageServiceIN, String senderIN, String receiverIN, Navigator navigatorIN)
    {
        loginPageService = loginPageServiceIN;
        conversationPageService = conversationPageServiceIN;
        conversationPageDisplayer = conversationPageDisplayerIN;
        userPageService = userPageServiceIN;
        sender = senderIN;
        receiver = receiverIN;
        navigator = navigatorIN;
    }

    private boolean userIsAuthenticated()
    {
        return sender != null;
    }

    private final ConversationPageDisplayer.ConversationListener actionListener = new ConversationPageDisplayer.ConversationListener() {

        @Override
        public void onUpPressed()
        {
            navigator.toParent();
        }

        @Override
        public void onMessageLengthChanged(int messageLength)
        {
            if (userIsAuthenticated() && messageLength > 0)
            {
                conversationPageDisplayer.enableInteraction();
                //Set the typing indicator to true
                conversationPageService.setTyping(sender, receiver,true);
            } else
            {
                conversationPageDisplayer.disableInteraction();
                //set the typing indicator to false
                conversationPageService.setTyping(sender, receiver,false);
            }
        }

        //Once the user presses the send button send the message
        @Override
        public void onSubmitMessage(String message) {
            conversationPageService.sendNewMessage(sender, new Message(sender, receiver, message, null));
        }

    };

    //is called when a user clicks to chat with a person
    public void startPresenting() {
        conversationPageDisplayer.attach(actionListener);
        conversationPageDisplayer.disableInteraction();

        Subscriber conversationSubscriber = new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            //To displayUserDetails receiver information on main_toolbar (last seen, image)
            @Override
            public void onNext(final User user) {
                conversationPageDisplayer.setupToolbar(user.getName(),user.getImage(),user.getLastSeen());
                chatSubscription = conversationPageService.syncMessages(sender, receiver)
                        .subscribe(new Subscriber<Message>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Message message) {
                                conversationPageDisplayer.addToDisplay(message, sender);
                            }
                        });
            }
        };

        subscription = userPageService.getSpecificUser(receiver)
                .subscribe(conversationSubscriber);

        //Typing indicator
        typingSubscription = conversationPageService.getTyping(sender, receiver)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        conversationPageService.setTyping(sender, receiver,false);
                    }

                    @Override
                    public void onNext(Boolean bool) {
                        if (bool)
                            conversationPageDisplayer.showTyping();
                        else
                            conversationPageDisplayer.hideTyping();
                    }
                });
    }


    //is called when the user stops the app or leaves the app
    public void stopPresenting() {
        conversationPageDisplayer.detach(actionListener);
        //on close set typing to false
        conversationPageService.setTyping(sender, receiver,false);
        subscription.unsubscribe();
        if (typingSubscription != null)
        {
            typingSubscription.unsubscribe();
        }
        if (chatSubscription != null)
        {
            chatSubscription.unsubscribe();
        }
    }



}
