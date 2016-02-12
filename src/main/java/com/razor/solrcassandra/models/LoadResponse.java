package com.razor.solrcassandra.models;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class LoadResponse {
    private String successMessage;
    private String errorMessage;
    private String loadStatistics;

    public String getSuccessMessage() {
        return successMessage;
    }

    public LoadResponse setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LoadResponse setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getLoadStatistics() {
        return loadStatistics;
    }

    public LoadResponse setLoadStatistics(String loadStatistics) {
        this.loadStatistics = loadStatistics;
        return this;
    }
}
