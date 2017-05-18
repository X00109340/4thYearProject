package com.example.shaju.letzchat.first_time_login;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.shaju.letzchat.R;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.ByteArrayOutputStream;
import java.util.List;


/**
 * Created by Shajun on 12/03/2017.
 */

public class FirstTimeUserLoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "3TFXaSzBxPz2lHTIdIxmRK3Vh";
    private static final String TWITTER_SECRET = "U1Bazabg4txxtViOiTUWYNZfoUwHJeFarJi7keqGllQQ3D1Wc4";

    //Toolbar
    private Toolbar topToolbar;
    //Online Id edit text
    private EditText nameEditText;
    private CircleImageView profileImage;
    private Button buttonStart;

    //Firebase authentication
    private FirebaseAuth firebaseAuth;
    //Firebase user details
    private FirebaseUser firebaseUser;
    //Firebase storage for photo storage
    private FirebaseStorage firebaseStorage;
    //storage url
    private StorageReference storageReference;
    //firebase database
    private DatabaseReference databaseReference;

    private static List<String> users = new ArrayList<String>();

    private boolean found = false;


    private boolean hasImageChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userfirstlogin_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();



        topToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (topToolbar != null)
        {
            setSupportActionBar(topToolbar);
            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setTitle("Profile");
            }
        }



        nameEditText = (EditText) findViewById(R.id.nameEditText);
        if (firebaseUser.getDisplayName() != null && firebaseUser.getDisplayName().length() > 0)
        {
            nameEditText.setText(firebaseUser.getDisplayName());
        }
        buttonStart = (Button) findViewById(R.id.startButton);
        profileImage = (CircleImageView) findViewById(R.id.profileImageView);

        //Set listners for profile image and start button
        buttonStart.setOnClickListener(this);
        profileImage.setOnClickListener(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();


            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCodeIN, int resultCodeIN, Intent dataIN) {
        super.onActivityResult(requestCodeIN, resultCodeIN, dataIN);

        switch(requestCodeIN) {
            case 1:
                if(resultCodeIN == RESULT_OK){
                    try {
                        //image URI
                        final Uri imageUri = dataIN.getData();
                        //REad in image
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        //Convert image
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        //Change profile image (image View)
                        profileImage.setImageBitmap(selectedImage);
                        //Boolean to true - image has been changed
                        hasImageChanged = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    //Some parts of the code are retrieved from
    //http://stackoverflow.com/questions/39704291/how-can-i-add-name-profile-pic-address-of-a-user-to-firebase-database
    @Override
    public void onClick(View view) {

        switch(view.getId()) {


            case R.id.profileImageView:
                //Photopicker intent
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                //Start photo picker
                startActivityForResult(photoPicker, 1);
                break;

            case R.id.startButton:
                if (nameEditText.getText() == null || nameEditText.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(this,"Online ID cannot be left blank",Toast.LENGTH_SHORT).show();
                    return;
                }

                //if user is authenticated
                if (firebaseUser != null)
                {
                    //System.out.println(checkOnlineID());
                    //System.out.println("test3: " + found);
                    //Create a map of values
                    final HashMap<String,String> hashMapValues = new HashMap<>();
                    //email
                    hashMapValues.put("email",firebaseUser.getEmail());
                    //user name
                    hashMapValues.put("name",nameEditText.getText().toString());
                    //user id
                    hashMapValues.put("uid",firebaseUser.getUid());

                    //if new profile image was selected
                    if (hasImageChanged)
                    {
                        //Retrieved from http://stackoverflow.com/questions/20700181/convert-imageview-in-bytes-android
                        profileImage.setDrawingCacheEnabled(true);
                        profileImage.buildDrawingCache();
                        Bitmap bitmap = profileImage.getDrawingCache();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] imageData = byteArrayOutputStream.toByteArray();

                        //create an image reference
                        final String imageReference = profileImage.getDrawingCache().hashCode() + System.currentTimeMillis() + ".jpg";
                        //Create storage reference
                        StorageReference storageReference1 = storageReference.child(imageReference);
                        UploadTask uploadTask = storageReference1.putBytes(imageData);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                hashMapValues.put("image",imageReference);
                                databaseReference.child("users")
                                        .child(firebaseUser.getUid()).setValue(hashMapValues);
                                setToken();

                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    } else {
                        databaseReference.child("users")
                                .child(firebaseUser.getUid()).setValue(hashMapValues);
                        setToken();

//                        Intent myIntent = new Intent(FirstTimeUserLoginActivity.this, SampleActivity.class);
//                        FirstTimeUserLoginActivity.this.startActivity(myIntent);
                        //checkOnlineID(nameEditText.getText().toString());


                        finish();
                    }
                }
                break;

        }
    }

    //Token's can be used to send notifications to users about offers or other updates
    private void setToken() {
        DatabaseReference databaseTokenReference = FirebaseDatabase.getInstance().getReference("fcm");
        databaseTokenReference.child(firebaseUser.getUid() + "/" + "token").setValue(FirebaseInstanceId.getInstance().getToken());
        databaseTokenReference.child(firebaseUser.getUid() + "/" + "enabled").setValue(Boolean.TRUE.toString());
    }

    private Integer checkOnlineID()
    {
        final Integer[] result = new Integer[1];
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot imageSnapshot: dataSnapshot.getChildren()) {
                    //Log.e("name", imageSnapshot.child("name").getValue().toString());
                    users.add(imageSnapshot.child("name").getValue().toString().trim());
                }

                for (String user: users){
                    //Log.d("name", user);
                    if (user.toString().equals(nameEditText.getText().toString().trim())){
                        //Toast.makeText(FirstTimeUserLoginActivity.this, "found user:" + user, Toast.LENGTH_SHORT).show();
                        result[0] = 1;
                    }

                }
                System.out.println("test1: " + result[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println("test2: " + result[0]);
        return result[0];
    }

}
