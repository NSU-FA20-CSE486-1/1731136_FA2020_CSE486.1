package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.imageUpload;

/**
 * Abstraction for file upload/download codes
 * @param <UploadFileType> file type that is to be directly uploaded
 * @param <DownloadLinkType> link type that can be directly downloaded from
 */
public abstract class FileUploader<UploadFileType, DownloadLinkType, UploadPathType> {

    public abstract void uploadFile(UploadFileType file, UploadPathType path);
    public abstract void downloadFile(DownloadLinkType downloadLink);

    public interface FileUploadCallbacks<UploadedImageLinkType>{

        void onUploadComplete(UploadedImageLinkType uploadedImageLink);
        void onUploadFailed(String message);
    }

    public interface FileDownloadCallbacks<DownloadedImageType>{

        void onDownloadComplete(DownloadedImageType downloadedImage);
        void onDownloadFailed(String message);
    }

}
