package com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp.models;

/**
 * Model class for a message
 */
public class Message {

    private String mPlainMessage;
    private String mEncryptedMessage;
    private String mEncryptionKey;

    public Message() {
    }

    public String getmPlainMessage() {
        return mPlainMessage;
    }

    public void setmPlainMessage(String mPlainMessage) {
        this.mPlainMessage = mPlainMessage;
    }

    public String getmEncryptedMessage() {
        return mEncryptedMessage;
    }

    public void setmEncryptedMessage(String mEncryptedMessage) {
        this.mEncryptedMessage = mEncryptedMessage;
    }

    public String getmEncryptionKey() {
        return mEncryptionKey;
    }

    public void setmEncryptionKey(String mEncryptionKey) {
        this.mEncryptionKey = mEncryptionKey;
    }

    public boolean isValid(){

        boolean isEncryptionKeyAlphaNumeric = true;
        for(int i=0;i<mEncryptionKey.length();i++) {

            if ((mEncryptionKey.charAt(i) < 48 || mEncryptionKey.charAt(i) > 57)
                    && (mEncryptionKey.charAt(i) < 97 || mEncryptionKey.charAt(i) > 122)) {

                isEncryptionKeyAlphaNumeric = false;
                break;
            }
        }

        return mPlainMessage.length()>0 && mPlainMessage.length()<=150
                && mEncryptionKey.length()==3 && isEncryptionKeyAlphaNumeric;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mPlainMessage='" + mPlainMessage + '\'' +
                ", mEncryptedMessage='" + mEncryptedMessage + '\'' +
                ", mEncryptionKey='" + mEncryptionKey + '\'' +
                '}';
    }
}
