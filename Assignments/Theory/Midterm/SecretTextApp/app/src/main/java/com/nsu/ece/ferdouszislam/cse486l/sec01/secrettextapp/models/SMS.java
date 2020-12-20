package com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp.models;

/**
 * Model class for an SMS
 * containing message as well as recipient info
 */
public class SMS {

    private String mRecipientPhoneNumber;
    private Message mMessage;

    public SMS() {
    }

    public SMS(String mRecipientPhoneNumber) {
        this.mRecipientPhoneNumber = mRecipientPhoneNumber;
    }

    public SMS(String mRecipientPhoneNumber, Message mMessage) {
        this.mRecipientPhoneNumber = mRecipientPhoneNumber;
        this.mMessage = mMessage;
    }

    public String getmRecipientPhoneNumber() {
        return mRecipientPhoneNumber;
    }

    public void setmRecipientPhoneNumber(String mRecipientPhoneNumber) {
        this.mRecipientPhoneNumber = mRecipientPhoneNumber;
    }

    public Message getmMessage() {
        return mMessage;
    }

    public void setmMessage(Message mMessage) {
        this.mMessage = mMessage;
    }

    public boolean isValid(){

        return mMessage.isValid()
                && mRecipientPhoneNumber.startsWith("+880")
                && mRecipientPhoneNumber.length() == 14;
    }

    @Override
    public String toString() {
        return "SMS{" +
                "mRecipientPhoneNumber='" + mRecipientPhoneNumber + '\'' +
                ", mMessage=" + mMessage +
                '}';
    }
}
