package com.example.shaju.letzchat.profile_page.presenter;

import com.example.shaju.letzchat.image_storage.ImageStorageService;
import com.example.shaju.letzchat.login_page.service.LoginPageService;
import com.example.shaju.letzchat.profile_page.service.ProfilePageService;
import com.example.shaju.letzchat.login_page.model.LoginAuthModel;
import com.example.shaju.letzchat.profile_page.view.ProfilePageDisplayer;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.service.UserPageService;
import com.example.shaju.letzchat.navigations.ProfileNavigator;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import android.widget.TextView;
import android.graphics.Bitmap;




public class ProfilePagePresenter {

    //Used to store logged in user details
    private User user;

    //Profile profileNavigator and displayer
    private final ProfileNavigator profileNavigator;
    private final ProfilePageDisplayer profilePageDisplayer;

    //All services that are required
    private final ProfilePageService profilePageService;
    private final UserPageService userPageService;
    private final LoginPageService loginPageService;
    private final ImageStorageService imageStorageService;

    //Subscriptions
    private Subscription loginSubscription;
    private Subscription userSubscription;

    public ProfilePagePresenter(LoginPageService loginPageServiceIN, UserPageService userPageServiceIN, ProfilePageService profilePageServiceIN, ImageStorageService imageStorageServiceIN,
                                ProfilePageDisplayer loginDisplayerIN, ProfileNavigator profileNavigatorIN)
    {
        userPageService = userPageServiceIN;
        profilePageService = profilePageServiceIN;
        profilePageDisplayer = loginDisplayerIN;
        profileNavigator = profileNavigatorIN;
        loginPageService = loginPageServiceIN;
        imageStorageService = imageStorageServiceIN;

    }

    //Start showing the profile
    public void startPresenting() {
        //listener for profile displayUserDetails
        profilePageDisplayer.attach(actionListener);

        //Attach listeners for dialog
        profileNavigator.attach(dialogListener);

        //Now we check to see if the user is registered ny calling on the login service
        loginSubscription = loginPageService.getAuthentication()
                .subscribe(new Action1<LoginAuthModel>() {
                    @Override
                    public void call(final LoginAuthModel loginAuthModel) {
                        //If user is logged in
                        if (loginAuthModel.isSuccess()) {
                            //get the userid
                            userPageService.getSpecificUser(loginAuthModel.getUser().getUid())
                                    .subscribe(new Subscriber<User>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(User userIN)
                                        {

                                            ProfilePagePresenter.this.user = userIN;
                                            //pass in user details to the profile displayer
                                            profilePageDisplayer.displayUserDetails(userIN);
                                        }
                                    });
                        } else
                        {
                            profileNavigator.toParent();
                        }
                    }
                });
    }

    public void stopPresenting() {
        //Remove the listeners
        profilePageDisplayer.detach(actionListener);
        profileNavigator.detach(dialogListener);
        //Login subscriprion unsubscribe
        loginSubscription.unsubscribe();

    }

    //Action listeners for button clicks or image clicks
    private ProfilePageDisplayer.ProfileOnClickActionListeners actionListener = new ProfilePageDisplayer.ProfileOnClickActionListeners() {

        @Override
        public void onImagePressed()
        {
            profileNavigator.showImagePicker();
        }

        @Override
        public void onOnlineIdPressed(String hintIN, TextView textViewIN) {
            if (user != null) {
                profileNavigator.showInputTextDialog("Enter new Online ID", textViewIN, user);
            }
        }

        @Override
        public void onChangePasswordPressed(String hintIN)
        {
            profileNavigator.showInputPasswordDialog("Enter New Password", user);
        }



        @Override
        public void onUpPressed()
        {
            profileNavigator.toParent();
        }
    };

    //These are the listeners for the dialog boxes that are opened when we change password, name etc
    private ProfileNavigator.ProfileDialogListener dialogListener = new ProfileNavigator.ProfileDialogListener() {


        @Override
        public void onImageSelected(final Bitmap bitmapIN) {
            imageStorageService.uploadImageToStorage(bitmapIN)
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String userProfileImageIN)
                        {
                            if (userProfileImageIN != null)
                            {
                                //change the image
                                userPageService.setUserProfilePicture(user,userProfileImageIN);
                                //update the profile image
                                profilePageDisplayer.updateUserCurrentProfileImage(bitmapIN);
                            }
                        }
                    });
        }

        //Change online id
        @Override
        public void onNameSelected(String textIN, User userIN)
        {
            userPageService.setUserOnlineId(userIN, textIN);
        }

        //change password
        @Override
        public void onPasswordSelected(String textIN)
        {
            profilePageService.setNewUserPassword(textIN);
        }


    };

}
