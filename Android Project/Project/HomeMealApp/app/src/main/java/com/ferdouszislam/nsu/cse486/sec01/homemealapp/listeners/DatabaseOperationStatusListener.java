package com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners;

/**
 * Database operation status callback
 * @param <SuccessResponseType> successful operation response variable
 * @param <FailedResponseType> failed operation response variable
 */
public interface DatabaseOperationStatusListener<SuccessResponseType, FailedResponseType> {

    void onSuccess(SuccessResponseType successResponse);
    void onFailed(FailedResponseType failedResponse);
}
