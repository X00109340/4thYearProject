package com.example.shaju.letzchat.image_storage;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.InputStream;

/**
 * Created by Shajun on 02/04/2017.
 */

/**
 * Based on https://github.com/firebase/FirebaseUI-Android/blob/master/storage/src/main/java/com/firebase/ui/storage/images/FirebaseImageLoader.java#L57
 * From firebase
 */

public class FirebaseImageLoaderService implements StreamModelLoader<StorageReference> {

    private static final String TAG = "FirebaseImageLoaderService";

    @Override
    public DataFetcher<InputStream> getResourceFetcher(StorageReference modelIN, int widthIN, int heightIN) {
        return new FirebaseStorageFetcher(modelIN);
    }

    private class FirebaseStorageFetcher implements DataFetcher<InputStream> {

        //Stream download
        private StreamDownloadTask mStreamTask;
        //input stream
        private InputStream mInputStream;
        //Storage reference
        private StorageReference storageReference;

        FirebaseStorageFetcher(StorageReference refIN) {
            storageReference = refIN;
        }

        @Override
        public InputStream loadData(Priority priority) throws Exception {
            mStreamTask = storageReference.getStream();
            mInputStream = Tasks.await(mStreamTask).getStream();

            return mInputStream;        }

        @Override
        public String getId() {
            return storageReference.getPath();
        }


        @Override
        public void cleanup() {
            // No cleanup possible
        }



        @Override
        public void cancel() {
            // No cancellation possible, Task does not expose cancellation
        }
    }
}