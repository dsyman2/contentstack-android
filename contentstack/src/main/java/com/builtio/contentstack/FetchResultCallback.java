package com.builtio.contentstack;

/**
 * Created by chinmay on 10/10/16.
 */

public abstract class FetchResultCallback extends ResultCallBack{

    /**
     * Triggered after call execution complete.
     *
     * @param responseType
     *                     call response from cache or network.
     * @param error
     *                     {@link Error} instance if call failed else null.
     */
    public abstract void onCompletion(ResponseType responseType, Error error);

    void onRequestFinish(ResponseType responseType){
        onCompletion(responseType, null);
    }

    @Override
    void onRequestFail(ResponseType responseType, Error error) {
        onCompletion(responseType, error);
    }

    @Override
    void always() {
    }

}
