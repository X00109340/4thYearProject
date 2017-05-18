package com.example.shaju.letzchat.users_page.view;

import com.example.shaju.letzchat.users_page.model.User;
import android.view.View;

import android.support.v7.widget.RecyclerView;


/**
 * Created by Shajun on 12/02/2017.
 */

public class UserListViewHolder extends RecyclerView.ViewHolder {

    //A user view
    private final UserView userView;

    //Constructor
    public UserListViewHolder(UserView userViewIN) {
        super(userViewIN);
        this.userView = userViewIN;
    }

    public void bind(final User user, final UserSelectionListener listener) {
        userView.display(user);
        //when user is selected
        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserSelected(user);
            }
        });
    }

    //User selection
    public interface UserSelectionListener {
        void onUserSelected(User userIN);
    }

}