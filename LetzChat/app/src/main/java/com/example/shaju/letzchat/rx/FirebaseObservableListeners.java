package com.example.shaju.letzchat.rx;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import rx.functions.Func1;
import rx.Observable;

/**
 * The rx file was retrieved from GitHub:
 * https://github.com/novoda/bonfire-firebase-sample/tree/master/android/app/src/main/java/com/novoda/bonfire/rx
 */

public class FirebaseObservableListeners {

    public <T> Observable<T> listenToAddChildEvents(Query queryIN, Func1<DataSnapshot, T> marshallerIN) {
        return Observable.create(new ListenToAddChildEventsOnSubscribe<T>(queryIN, marshallerIN));
    }

    public <T> Observable<T> listenToSingleValueEvents(Query queryIN, Func1<DataSnapshot, T> marshallerIN) {
        return Observable.create(new ListenToSingleValueOnSubscribe<>(queryIN, marshallerIN));
    }

    public <T> Observable<T> listenToValueEvents(Query queryIN, Func1<DataSnapshot, T> marshallerIN) {
        return Observable.create(new ListenToValueEventsOnSubscribe<>(queryIN, marshallerIN));
    }

    public <T, U> Observable<U> uploadTask(T valueIN, StorageReference storageReferenceIN, U returnValueIN) {
        return Observable.create(new UploadTaskOnSubscribe<>(valueIN, storageReferenceIN, returnValueIN));
    }

}
