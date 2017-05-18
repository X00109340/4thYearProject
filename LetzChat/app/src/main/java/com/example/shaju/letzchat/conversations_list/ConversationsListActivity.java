package com.example.shaju.letzchat.conversations_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.conversations_list.presenter.ConversationListPresenter;
import com.example.shaju.letzchat.conversations_list.view.ConversationsListDisplayer;
import com.example.shaju.letzchat.navigations.AndroidConversationsNavigator;
import com.example.shaju.letzchat.navigations.AndroidNavigator;

/**
 * Created by Shajun on 12/03/2017.
 */

public class ConversationsListActivity extends Fragment {

    //Navigator
    private AndroidConversationsNavigator androidConversationsNavigator;

    //ConversationModel LIST Presenter
    private ConversationListPresenter conversationListPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conv_frag, container, false);
        //Title
        getActivity().setTitle("Chats");

        androidConversationsNavigator = new AndroidConversationsNavigator((AppCompatActivity)getActivity(),new AndroidNavigator(getActivity()));
        conversationListPresenter = new ConversationListPresenter(
                (ConversationsListDisplayer) rootView.findViewById(R.id.conversationsView),
                ChatDependencies.INSTANCE.getConversationListService(),
                androidConversationsNavigator,
                ChatDependencies.INSTANCE.getLoginService(),
                ChatDependencies.INSTANCE.getUserService()
                );

        //return view
        return rootView;
    }

    //On startup
    @Override
    public void onStart() {
        super.onStart();
        conversationListPresenter.startPresenting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    //On application close or application stop
    @Override
    public void onStop() {
        super.onStop();
        conversationListPresenter.stopPresenting();
    }

}
