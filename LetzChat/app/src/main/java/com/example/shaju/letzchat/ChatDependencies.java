package com.example.shaju.letzchat;

import android.content.Context;

import com.example.shaju.letzchat.global_chat.service.ContinuedGlobalChatPageService;
import com.example.shaju.letzchat.image_storage.ImageStorageService;
import com.example.shaju.letzchat.login_page.service.FirebaseLoginPageService;
import com.example.shaju.letzchat.login_page.service.LoginPageService;
import com.example.shaju.letzchat.main_page.service.CloudMessagingTokenService;
import com.example.shaju.letzchat.main_page.service.ContinuedMainPageService;
import com.example.shaju.letzchat.main_page.service.FirebaseCloudMessagingTokenService;
import com.example.shaju.letzchat.main_page.service.MainPageService;
import com.example.shaju.letzchat.private_conversations.database.FirebaseConversationPageDatabase;
import com.example.shaju.letzchat.private_conversations.service.ContinuedConversationPageService;
import com.example.shaju.letzchat.private_conversations.service.ConversationPageService;
import com.example.shaju.letzchat.conversations_list.database.FirebaseConversationListDb;
import com.example.shaju.letzchat.conversations_list.service.ContinuedConversationListService;
import com.example.shaju.letzchat.login_page.database.FirebaseLoginPageAuthDatabase;
import com.example.shaju.letzchat.profile_page.service.FirebaseProfilePageService;
import com.example.shaju.letzchat.profile_page.service.ProfilePageService;
import com.example.shaju.letzchat.registration_page.service.FirebaseRegistrationPageService;
import com.example.shaju.letzchat.registration_page.service.RegistrationPageService;
import com.example.shaju.letzchat.users_page.database.FBUserDatabase;
import com.example.shaju.letzchat.users_page.service.UserPageService;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.example.shaju.letzchat.conversations_list.service.ConversationListService;
import com.example.shaju.letzchat.global_chat.database.FirebaseGlobalChatPageDatabase;
import com.example.shaju.letzchat.global_chat.service.GlobalChatPageService;
import com.example.shaju.letzchat.main_page.database.FirebaseCloudMessagingTokenDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.example.shaju.letzchat.analytics.Analytics;
import com.example.shaju.letzchat.analytics.FirebaseAnalyticsAnalytics;
import com.example.shaju.letzchat.rx.FirebaseObservableListeners;
import com.example.shaju.letzchat.image_storage.FirebaseImageStorageService;
import com.example.shaju.letzchat.users_page.service.ContinuedUserPageService;

public enum ChatDependencies {
    INSTANCE;

    //Token for notifications
    private String firebaseToken;

    //Analytics
    private Analytics analytics;

    //Services
    private ImageStorageService imageStorageService;
    private CloudMessagingTokenService messagingService;
    private RegistrationPageService registrationPageService;
    private LoginPageService loginPageService;
    private MainPageService mainPageService;
    private ConversationListService conversationListService;
    private ConversationPageService conversationPageService;
    private GlobalChatPageService globalChatPageService;
    private UserPageService userPageService;
    private ProfilePageService profilePageService;

    private boolean setPersistence = false;

    public void init(Context context)
    {
        if (needsInitialisation())
        {
            Context appContext = context.getApplicationContext();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseToken = FirebaseInstanceId.getInstance().getToken();
            if (!setPersistence)
            {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                setPersistence = true;
            }

            //Storage
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            //Database
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            //Listeners FireBase
            FirebaseObservableListeners firebaseObservableListeners = new FirebaseObservableListeners();
            //User Database FireBase
            FBUserDatabase FBUserDatabase = new FBUserDatabase(firebaseDatabase, firebaseObservableListeners);
            //Notification
            FirebaseCloudMessagingTokenDatabase firebaseCloudMessagingDatabase = new FirebaseCloudMessagingTokenDatabase(firebaseDatabase, firebaseObservableListeners, firebaseToken);

            //ConversationModel
            FirebaseConversationPageDatabase firebaseConversationDatabase = new FirebaseConversationPageDatabase(firebaseDatabase, firebaseObservableListeners);
            FirebaseConversationListDb firebaseConversationListDb = new FirebaseConversationListDb(firebaseDatabase,firebaseObservableListeners);


            messagingService = new FirebaseCloudMessagingTokenService(firebaseCloudMessagingDatabase);
            imageStorageService = new FirebaseImageStorageService(firebaseStorage,firebaseObservableListeners);
            analytics = new FirebaseAnalyticsAnalytics(FirebaseAnalytics.getInstance(appContext));
            registrationPageService = new FirebaseRegistrationPageService(firebaseAuth);
            loginPageService = new FirebaseLoginPageService(new FirebaseLoginPageAuthDatabase(firebaseAuth),firebaseCloudMessagingDatabase);
            mainPageService = new ContinuedMainPageService(firebaseAuth, FBUserDatabase, firebaseCloudMessagingDatabase);
            conversationListService = new ContinuedConversationListService(firebaseConversationListDb,firebaseConversationDatabase, FBUserDatabase);
            conversationPageService = new ContinuedConversationPageService(firebaseConversationDatabase);
            globalChatPageService = new ContinuedGlobalChatPageService(new FirebaseGlobalChatPageDatabase(firebaseDatabase, firebaseObservableListeners));
            userPageService = new ContinuedUserPageService(FBUserDatabase);
            profilePageService = new FirebaseProfilePageService(firebaseAuth);

        }
    }


    private boolean needsInitialisation()
    {
        return loginPageService == null || conversationListService == null || conversationPageService == null || registrationPageService == null
                || userPageService == null;// || analytics == null || errorLogger == null;
    }

    //Clear all dependencies - Not Used
    public void clearDependecies() {
        loginPageService = null;
        conversationListService = null;
        conversationPageService = null;
        userPageService = null;
    }


    //Return token
    public String getFirebaseToken() {
        return firebaseToken;
    }

    //Get Storage
    public ImageStorageService getStorageService() {
        return imageStorageService;
    }

    //Get Cloud service
    public CloudMessagingTokenService getMessagingService() {
        return messagingService;
    }

    //Get Registration service
    public RegistrationPageService getRegistrationService() {
        return registrationPageService;
    }

    //Get login
    public LoginPageService getLoginService() {
        return loginPageService;
    }

    //Get Main
    public MainPageService getMainService() {
        return mainPageService;
    }

    //Get ConversationModel
    public ConversationPageService getConversationService() {
        return conversationPageService;
    }
    public ConversationListService getConversationListService() {
        return conversationListService;
    }

    //Get Global service
    public GlobalChatPageService getGlobalService() {
        return globalChatPageService;
    }

    //Return user service
    public UserPageService getUserService() {
        return userPageService;
    }

    public ProfilePageService getProfileService() {
        return profilePageService;
    }



}
