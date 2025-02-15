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

    private StorageReference mFirebaseStorageRef;

    public FirebaseStorageFileUploader() {

        mFirebaseStorageRef = FirebaseStorage.getInstance().getReference();
    }


    @Override
    public void uploadFile(CapturedImage image, String path, FileUploadCallbacks fileUploadCallbacks) {

        StorageReference ref = mFirebaseStorageRef.child(path);

        ref.putFile(image.getmPhotoUri()).continueWithTask(task -> {
            if (!task.isSuccessful()) {

                fileUploadCallbacks.onUploadFailed("failed to upload -> "+task.getException().getMessage());
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return ref.getDownloadUrl();

        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Uri downloadUri = task.getResult();
                fileUploadCallbacks.onUploadComplete(downloadUri);
            }

            else{

                fileUploadCallbacks.onUploadFailed("failed to fetch uploaded image url -> "
                        + task.getException().getMessage());
            }
        });
    }

    @Override
    public void downloadFile(String downloadPath, FileDownloadCallbacks fileDownloadCallbacks) {

        mFirebaseStorageRef.child(downloadPath).getDownloadUrl()

                .addOnSuccessListener(fileDownloadCallbacks::onDownloadComplete)

                .addOnFailureListener(e -> fileDownloadCallbacks.onDownloadFailed(e.getMessage()));
    }
}
