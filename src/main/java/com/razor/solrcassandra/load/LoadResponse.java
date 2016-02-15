package com.razor.solrcassandra.load;

import java.util.Objects;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class LoadResponse {
    private String successMessage;
    private String errorMessage;
    private Object loadStatistics;

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

    public Object getLoadStatistics() {
        return loadStatistics;
    }

    public LoadResponse setLoadStatistics(Object loadStatistics) {
        this.loadStatistics = loadStatistics;
        return this;
    }

    public boolean isSuccessful() {
        return Objects.isNull(this.errorMessage);
    }
}
