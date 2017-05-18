package com.example.shaju.letzchat.users_page.view;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.image_storage.FirebaseImageLoaderService;
import com.example.shaju.letzchat.users_page.model.User;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Shajun on 13/02/2017.
 */

public class UserView extends FrameLayout {

    //Image view of the other users profile picture
    private CircleImageView profileImageView;

    //Name text view
    private TextView onlineIDTextView;

    //Not used
    private int layoutResId;

    //Constructor
    public UserView(Context contextIN, AttributeSet attributeSetIN) {
        super(contextIN, attributeSetIN);
        init(contextIN, attributeSetIN);
    }

    //Initialise
    private void init(Context contextIN, AttributeSet attributeSetIN) {
        if (attributeSetIN != null)
        {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            //
            TypedArray typedArray = contextIN.obtainStyledAttributes(attributeSetIN, attrsArray);
            //
            layoutResId = typedArray.getResourceId(0, R.layout.merge_users_item_view);
            //
            typedArray.recycle();
        }
    }

    //Display user image and ID
    public void display(User userIN) {
        loadImageElseBlack(userIN.getImage(),profileImageView,getContext());
        onlineIDTextView.setText(userIN.getName());
    }

    //On xml load up
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);

        //Initialise variables
        onlineIDTextView = (TextView) this.findViewById(R.id.nameTextView);
        profileImageView = (CircleImageView) this.findViewById(R.id.profileImageView);
    }

    //Load black person image if no profile image
    public static void loadImageElseBlack(String image, CircleImageView circleImageViewIN, Context contextIN) {

        //If user has image the
        try {
            if (image != null && image.length() > 0) {
                StorageReference ref = ChatDependencies.INSTANCE.getStorageService().getProfileImageReferenceFromStorage(image);
                Glide.with(contextIN)
                        .using(new FirebaseImageLoaderService())
                        .load(ref)
                        .into(circleImageViewIN);
            } else {
                Glide.with(contextIN)
                        .load("")
                        .placeholder(R.drawable.person_circleblack)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(circleImageViewIN);
            }
        } catch (IllegalArgumentException e) {

        }
    }

}

