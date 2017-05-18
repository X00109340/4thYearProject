package com.example.shaju.letzchat.users_page;

import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.presenter.UsersListPresenter;
import com.example.shaju.letzchat.users_page.view.UsersListDisplayer;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.navigations.AndroidConversationsNavigator;
import com.example.shaju.letzchat.navigations.AndroidNavigator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by Shajun on 13/02/2017.
 */

public class UsersPageActivity extends Fragment {

    //ConversationModel Navigator
    private AndroidConversationsNavigator androidConversationsNavigator;

    //Details about a particular user - name, id, image, messages
    private User user;

    //List of users in app
    private List<User> users;

    //Presenter
    private UsersListPresenter usersListPresenter;

    //On create
    @Override
    public View onCreateView(LayoutInflater layoutInflaterIN, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = layoutInflaterIN.inflate(R.layout.users_list_frag, container, false);
        //Set title
        getActivity().setTitle("Users List");

        androidConversationsNavigator = new AndroidConversationsNavigator((AppCompatActivity)getActivity(),new AndroidNavigator(getActivity()));
        usersListPresenter = new UsersListPresenter((UsersListDisplayer) rootView.findViewById(R.id.usersView), androidConversationsNavigator,
                ChatDependencies.INSTANCE.getLoginService(), ChatDependencies.INSTANCE.getUserService()
        );

        //return view
        return rootView;
    }

    //on start
    @Override
    public void onStart() {
        super.onStart();
        usersListPresenter.startPresenting();
    }

    //Pause activity
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(searchReceiver);
    }


    //on resume keep on searching
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("SEARCH");
        getActivity().registerReceiver(searchReceiver, filter);
    }

    //on stop
    @Override
    public void onStop() {
        super.onStop();
        usersListPresenter.stopPresenting();
    }


    private final BroadcastReceiver searchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("search");
            usersListPresenter.filterUsers(text);
        }
    };

    //Destroy on leave
    @Override
    public void onDestroy() {
        super.onDestroy();
        usersListPresenter.stopPresenting();
    }


}
