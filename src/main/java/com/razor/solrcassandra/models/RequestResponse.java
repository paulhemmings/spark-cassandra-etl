package com.razor.solrcassandra.models;

import java.util.Objects;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class RequestResponse<T> {
    private String successMessage;
    private String errorMessage;
    private T responseContent;

    public String getSuccessMessage() {
        return successMessage;
    }

    public RequestResponse setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public RequestResponse setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public T getResponseContent() {
        return responseContent;
    }

    public RequestResponse<T> setResponseContent(T responseContent) {
        this.responseContent = responseContent;
        return this;
    }

    public boolean isSuccessful() {
        return Objects.isNull(this.errorMessage);
    }
}
