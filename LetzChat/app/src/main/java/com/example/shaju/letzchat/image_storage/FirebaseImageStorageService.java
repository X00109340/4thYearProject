package com.example.shaju.letzchat.image_storage;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.shaju.letzchat.rx.FirebaseObservableListeners;

import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;

import rx.Observable;

/**
 * Created by Shajun on 02/04/2017.
 */

public class FirebaseImageStorageService implements ImageStorageService {

    //Observable listeners
    private final FirebaseObservableListeners firebaseObservableListeners;

    //Storage Reference Firebase
    private final StorageReference firebaseStorage;

    //Constructor
    public FirebaseImageStorageService(FirebaseStorage firebaseStorageIN, FirebaseObservableListeners firebaseObservableListenersIN) {
        this.firebaseObservableListeners = firebaseObservableListenersIN;
        this.firebaseStorage = firebaseStorageIN.getReference();
    }

    //Upload image to storage
    @Override
    public Observable<String> uploadImageToStorage(Bitmap imageBitmapIN) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmapIN.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] dataBytes = byteArrayOutputStream.toByteArray();
        final String imageReference = imageBitmapIN.hashCode() + System.currentTimeMillis() + ".jpg";
        //return
        return firebaseObservableListeners.uploadTask(dataBytes,firebaseStorage.child(imageReference),imageReference);
    }

    //Get the image reference for image from storage
    @Override
    public StorageReference getProfileImageReferenceFromStorage(String imageIN) {
        return firebaseStorage.child(imageIN);
    }

}
