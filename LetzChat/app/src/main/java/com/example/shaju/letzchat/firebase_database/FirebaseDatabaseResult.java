package com.example.shaju.letzchat.firebase_database;

import rx.functions.Func1;

public class FirebaseDatabaseResult<T> {


    private final T data;


    private final Throwable failure;




    public FirebaseDatabaseResult(Throwable failure) {
        this.failure = failure;
        data = null;
    }

    public static  <T> Func1<Throwable, FirebaseDatabaseResult<T>> errorAsDatabaseResult() {
        return new Func1<Throwable, FirebaseDatabaseResult<T>>() {
            @Override
            public FirebaseDatabaseResult<T> call(Throwable throwable) {
                return new FirebaseDatabaseResult<>(throwable == null ? new Throwable("Database error") : throwable);
            }
        };
    }

    public FirebaseDatabaseResult(T data) {
        this.failure = null;
        this.data = data;
    }



    public T getData() {
        if (data == null) {
            throw new IllegalStateException("Database write is not successful please check with isSuccess first");
        }
        return data;
    }


}
