package com.example.shaju.letzchat.rx;

import com.google.firebase.storage.StorageReference;
import android.support.annotation.NonNull;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import rx.Observable;
import rx.Subscriber;

/**
 * The rx file was retrieved from GitHub:
 * https://github.com/novoda/bonfire-firebase-sample/tree/master/android/app/src/main/java/com/novoda/bonfire/rx
 */


public class UploadTaskOnSubscribe<T,U> implements Observable.OnSubscribe<U> {

    //Return value
    private final U returnValue;
    //Storage reference
    private final StorageReference storageReference;
    //Value
    private final T value;

    UploadTaskOnSubscribe(T valueIN, StorageReference storageReferenceIN, U returnValueIN) {
        this.value = valueIN;
        this.returnValue = returnValueIN;
        this.storageReference = storageReferenceIN;
    }

    @Override
    public void call(Subscriber<? super U> subscriber) {
        UploadTask uploadTask = storageReference.putBytes((byte[])value);
        uploadTask.addOnFailureListener(new RxFailureListener<>(subscriber))
                .addOnSuccessListener(new RxSuccessListener<>(subscriber,returnValue));
    }

    //Rx Failure
    private static class RxFailureListener<T> implements OnFailureListener {

        private final Subscriber<? super T> subscriber;

        RxFailureListener(Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            subscriber.onError(e);
        }
    }

    //Rx Success
    private static class RxSuccessListener<T> implements OnSuccessListener {

        private final Subscriber<? super T> subscriber;
        private final T successValue;

        RxSuccessListener(Subscriber<? super T> subscriberIN, T successValueIN) {
            this.subscriber = subscriberIN;
            this.successValue = successValueIN;
        }

        @Override
        public void onSuccess(Object object) {
            subscriber.onNext(successValue);
            subscriber.onCompleted();
        }
    }


}
