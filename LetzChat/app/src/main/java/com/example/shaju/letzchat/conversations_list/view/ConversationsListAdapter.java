package com.example.shaju.letzchat.conversations_list.view;


import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;


import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.conversations_list.model.ConversationModel;
import com.example.shaju.letzchat.conversations_list.model.ConversationsModel;


public class ConversationsListAdapter extends RecyclerView.Adapter<ConversationsListViewHolder> {

    private final LayoutInflater inflater;

    //Interaction listener
    private ConversationsListDisplayer.ConversationListInteractionListener conversationListInteractionListener;

    //A conversation list is a list of conversation between a particular user and the rest of the users
    private ConversationsModel conversationsModel = new ConversationsModel(new ArrayList<ConversationModel>());

    ConversationsListAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }


    //
    public void add(ConversationModel conversationModel) {
        this.conversationsModel.add(conversationModel);
        this.conversationsModel = this.conversationsModel.sortedByDate();
        notifyDataSetChanged();
    }

    //Update is called in order to show conversationsModel that are latest
    public void update(ConversationsModel conversationsModel){
        this.conversationsModel = conversationsModel.sortedByDate();
        notifyDataSetChanged();
    }

    @Override
    public ConversationsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConversationsListViewHolder((ConversationsView) inflater.inflate(R.layout.converssation_view_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(ConversationsListViewHolder holder, int position) {
        holder.bind(conversationsModel.getConversationAt(position), clickListener);
    }

    //Return the list size
    @Override
    public int getItemCount() {
        return conversationsModel.size();
    }

//    @Override
//    public long getItemId(int position) {
//        return conversationsModel.getConversationAt(position).hashCode();
//    }

    private final ConversationsListViewHolder.ConversationSelectionListener clickListener = new ConversationsListViewHolder.ConversationSelectionListener() {
        @Override
        public void onConversationSelected(ConversationModel conversationModel) {
            ConversationsListAdapter.this.conversationListInteractionListener.onConversationSelected(conversationModel);
        }
    };

    //Attach listener for when clicking on conversation list
    public void attach(ConversationsListDisplayer.ConversationListInteractionListener conversationListInteractionListener) {
        this.conversationListInteractionListener = conversationListInteractionListener;
    }

    //Detach listener is used when user is not in conversation list screen
    public void detach(ConversationsListDisplayer.ConversationListInteractionListener conversationListInteractionListener) {
        this.conversationListInteractionListener = null;
    }



}
