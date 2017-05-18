package com.example.shaju.letzchat.profile_page.service;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Shajun on 22/03/2017.
 */

public class FirebaseProfilePageService implements ProfilePageService {

    //Firebase user
    private final FirebaseUser firebaseUser;

    //Firebase authentication
    private final FirebaseAuth firebaseAuth;

    //Firebase database reference
    //private final DatabaseReference databaseReference;

    public FirebaseProfilePageService(FirebaseAuth firebaseAuthIN) {
        firebaseAuth = firebaseAuthIN;
        firebaseUser = firebaseAuthIN.getCurrentUser();

    }

    //Change password the user
    @Override
    public void setNewUserPassword(final String newPasswordIN) {
        try {
            firebaseUser.updatePassword(newPasswordIN.toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //If successful then
                            //Toast.makeText(, "Password has been updated", Toast.LENGTH_LONG).show();
                            Log.d("PASSWORD:", newPasswordIN);
                            Log.d("NO","I SHOULD HAVE NEW PASSWORD");

                        }
                    });
        } catch (NullPointerException e) {

        }
    }


}
