package com.example.shaju.letzchat.profile_page.view;

import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.image_storage.FirebaseImageLoaderService;
import com.example.shaju.letzchat.users_page.model.User;
import com.google.firebase.storage.StorageReference;

import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Bitmap;


public class ProfilePageView extends LinearLayout implements ProfilePageDisplayer, View.OnClickListener {

    //Action listeners for profile
    private ProfileOnClickActionListeners profileOnClickActionListeners;

    //Circle image view for profile image
    private CircleImageView profileImageView;
    //Online id text view
    private TextView onlineIdTextView;
    //Current email address text view
    private TextView emailTextView;
    //Change password text view
    private TextView newPasswordTextView;

    //
    private TextView onlineTextView;
    //
    private TextView emailAddressTextView;

    //Profile main_toolbar
    private Toolbar toolbar;

    public ProfilePageView(Context contextIN, AttributeSet attributeSetIN)
    {
        super(contextIN, attributeSetIN);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_profile_view, this);

        //Set the main_toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);

        //Initialise all the field according to the id in XML file
        profileImageView = (CircleImageView) findViewById(R.id.profileImageView);
        onlineIdTextView = (TextView) findViewById(R.id.nameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        newPasswordTextView = (TextView) findViewById(R.id.passwordTextView);

        onlineTextView = (TextView) findViewById(R.id.onlineTextView);
        emailAddressTextView = (TextView) findViewById(R.id.emailAddressTextView);

    }

    //Change image
    @Override
    public void updateUserCurrentProfileImage(Bitmap imageBitmapIM)
    {
        profileImageView.setImageBitmap(imageBitmapIM);
        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();
    }

    //Display current user details
    @Override
    public void displayUserDetails(User userDetailsIN)
    {
        loadImageElseBlack(userDetailsIN.getImage(),profileImageView,getContext());
        onlineIdTextView.setText(userDetailsIN.getName());
        emailTextView.setText(userDetailsIN.getEmail());
    }

    @Override
    public void attach(ProfileOnClickActionListeners profileOnClickActionListenersIN) {
        this.profileOnClickActionListeners = profileOnClickActionListenersIN;

        toolbar.setNavigationOnClickListener(navigationClickListener);

        //Attach the listeners to the clicks
        profileImageView.setOnClickListener(this);
        onlineIdTextView.setOnClickListener(this);
        emailTextView.setOnClickListener(this);
        newPasswordTextView.setOnClickListener(this);
    }

    //Remove the listeners
    @Override
    public void detach(ProfileOnClickActionListeners profileOnClickActionListenersIN)
    {
        this.profileOnClickActionListeners = null;

        toolbar.setNavigationOnClickListener(null);

        //Remove the listeners
        profileImageView.setOnClickListener(null);
        onlineIdTextView.setOnClickListener(null);
        emailTextView.setOnClickListener(null);
        newPasswordTextView.setOnClickListener(null);

    }

    private final OnClickListener navigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View viewIN) {
            profileOnClickActionListeners.onUpPressed();
        }
    };

    @Override
    public void onClick(View viewIN) {
        switch (viewIN.getId()) {
            case R.id.profileImageView:
                profileOnClickActionListeners.onImagePressed();
                break;
            case R.id.nameTextView:
                profileOnClickActionListeners.onOnlineIdPressed("", onlineIdTextView);
                break;
            case R.id.passwordTextView:
                profileOnClickActionListeners.onChangePasswordPressed("");
                break;

        }
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
