package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.firebaseImageUpload;

import android.net.Uri;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.CapturedImage;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload.FileUploader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Class for firestore upload image implementation
 */
public class FirebaseStorageFileUploader extends FileUploader<CapturedImage, String, String> {

    private FileUploadCallbacks<Uri> mFileUploadCallbacks;
    private FileDownloadCallbacks<Uri> mFileDownloadCallbacks;

    private StorageReference mFirebaseStorageRef;

    public FirebaseStorageFileUploader(FileUploadCallbacks<Uri> mFileUploadCallbacks) {
        this.mFileUploadCallbacks = mFileUploadCallbacks;
        mFirebaseStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public FirebaseStorageFileUploader(FileDownloadCallbacks<Uri> mFileDownloadCallbacks) {
        this.mFileDownloadCallbacks = mFileDownloadCallbacks;
        mFirebaseStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void uploadFile(CapturedImage image, String path) {

        StorageReference ref = mFirebaseStorageRef.child(path);

        ref.putFile(image.getmPhotoUri()).continueWithTask(task -> {
            if (!task.isSuccessful()) {

                mFileUploadCallbacks.onUploadFailed("failed to upload -> "+task.getException().getMessage());
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return ref.getDownloadUrl();

        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Uri downloadUri = task.getResult();
                mFileUploadCallbacks.onUploadComplete(downloadUri);
            }

            else{

                mFileUploadCallbacks.onUploadFailed("failed to fetch uploaded image url -> "
                        + task.getException().getMessage());
            }
        });
    }

    @Override
    public void downloadFile(String downloadPath) {

        mFirebaseStorageRef.child(downloadPath).getDownloadUrl()

                .addOnSuccessListener(uri -> mFileDownloadCallbacks.onDownloadComplete(uri))

                .addOnFailureListener(e -> mFileDownloadCallbacks.onDownloadFailed(e.getMessage()));
    }
}
