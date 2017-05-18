package com.example.shaju.letzchat.image_storage;

import com.google.firebase.storage.StorageReference;
import android.graphics.Bitmap;

import rx.Observable;

/**
 * Created by Shajun on 02/04/2017.
 */

public interface ImageStorageService {

    Observable<String> uploadImageToStorage(Bitmap imageBitmapIN);

    StorageReference getProfileImageReferenceFromStorage(String imageIN);

}
