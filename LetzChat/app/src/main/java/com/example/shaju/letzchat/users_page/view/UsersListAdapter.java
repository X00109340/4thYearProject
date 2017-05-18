package com.example.shaju.letzchat.users_page.view;

import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import java.util.ArrayList;
import java.util.Iterator;
import android.view.ViewGroup;

/**
 * Created by Shajun on 12/02/2017.
 */

public class UsersListAdapter extends RecyclerView.Adapter<UserListViewHolder> {

    //Layout
    private final LayoutInflater layoutInflater;

    //interaction listener
    private UsersListDisplayer.OnUserClickInteractionListener usersInteractionListener;

    //List of users
    private Users users = new Users(new ArrayList<User>());

    //Constructor
    UsersListAdapter(LayoutInflater layoutInflaterIN)
    {
        this.layoutInflater = layoutInflaterIN;
    }

    //Filter user base on idIN
    public void filter(String onlineID)
    {
        Iterator<User> it = users.getUsers().iterator();
        while (it.hasNext()) {
            if (!it.next().getName().toLowerCase().contains(onlineID.toLowerCase())) {
                it.remove();
            }
        }
        notifyDataSetChanged();
    }

    //Update the user
    public void update(Users usersIN){
        this.users = usersIN;
        //sort alphabetically
        this.users = usersIN.sortedByName();
        notifyDataSetChanged();
    }


    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewTypeIN) {
        return new UserListViewHolder((UserView) layoutInflater.inflate(R.layout.users_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        holder.bind(users.getUserAt(position), clickListener);
    }

    //Return users
    public Users getUsers()
    {
        return users;
    }

    //Size of users list
    @Override
    public int getItemCount()
    {
        return users.size();
    }

    //Get user at position clicked
    @Override
    public long getItemId(int positionIN)
    {
        return users.getUserAt(positionIN).hashCode();
    }

    private final UserListViewHolder.UserSelectionListener clickListener = new UserListViewHolder.UserSelectionListener() {
        @Override
        public void onUserSelected(User userIN) {
            usersInteractionListener.onSpecificUserSelection(userIN);
        }
    };

    //Attach listeners
    public void attach(UsersListDisplayer.OnUserClickInteractionListener onUserClickInteractionListener)
    {
        this.usersInteractionListener = onUserClickInteractionListener;
    }

    //Remove listeners
    public void detach(UsersListDisplayer.OnUserClickInteractionListener onUserClickInteractionListener) {
        this.usersInteractionListener = null;
    }


}
