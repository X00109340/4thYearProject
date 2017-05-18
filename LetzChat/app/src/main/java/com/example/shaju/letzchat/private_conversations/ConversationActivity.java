package com.example.shaju.letzchat.private_conversations;



import com.example.shaju.letzchat.BaseActivity;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.private_conversations.presenter.ConversationPagePresenter;
import com.example.shaju.letzchat.private_conversations.view.ConversationPageDisplayer;
import com.example.shaju.letzchat.private_conversations.view.ConversationPageView;
import com.example.shaju.letzchat.navigations.AndroidNavigator;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class ConversationActivity extends BaseActivity {


    //ConversationModel presenter
    private ConversationPagePresenter conversationPagePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);
        //Set main_toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ConversationPageDisplayer conversationPageDisplayer = (ConversationPageView) findViewById(R.id.conversationView);
        conversationPagePresenter = new ConversationPagePresenter(ChatDependencies.INSTANCE.getLoginService(), ChatDependencies.INSTANCE.getConversationService(), conversationPageDisplayer,
                ChatDependencies.INSTANCE.getUserService(), getIntent().getStringExtra("sender"), getIntent().getStringExtra("destination"), new AndroidNavigator(this)
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Start presenting
    @Override
    protected void onStart() {
        super.onStart();
        conversationPagePresenter.startPresenting();
    }

    //Stop presenting
    @Override
    protected void onStop() {
        super.onStop();
        conversationPagePresenter.stopPresenting();
    }




}
