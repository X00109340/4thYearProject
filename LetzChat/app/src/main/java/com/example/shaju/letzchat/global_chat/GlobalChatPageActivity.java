package com.example.shaju.letzchat.global_chat;

import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.global_chat.presenter.GlobalChatPagePresenter;
import com.example.shaju.letzchat.global_chat.view.GlobalChatPageDisplayer;
import com.example.shaju.letzchat.navigations.AndroidNavigator;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;


public class GlobalChatPageActivity extends Fragment {

    //NAVIGATOR
    private AndroidNavigator androidNavigator;

    //Chat presenter
    private GlobalChatPagePresenter globalChatPagePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //set the layout
        View rootView = inflater.inflate(R.layout.global_frag, container, false);
        GlobalChatPageDisplayer globalChatPageDisplayer = (GlobalChatPageDisplayer) rootView.findViewById(R.id.globalView);
        //set the main_toolbar
        getActivity().setTitle(R.string.global_toolbar_title);

        androidNavigator = new AndroidNavigator(getActivity());
        globalChatPagePresenter = new GlobalChatPagePresenter(ChatDependencies.INSTANCE.getLoginService(), ChatDependencies.INSTANCE.getGlobalService(),
                globalChatPageDisplayer, ChatDependencies.INSTANCE.getUserService(), androidNavigator
        );

        //return rootView
        return rootView;
    }

    //On start start presenting
    @Override
    public void onStart() {
        super.onStart();
        globalChatPagePresenter.startPresenting();
    }

    //On destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        globalChatPagePresenter.stopPresenting();
    }

    @Override
    public void onStop() {
        super.onStop();
        globalChatPagePresenter.stopPresenting();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
