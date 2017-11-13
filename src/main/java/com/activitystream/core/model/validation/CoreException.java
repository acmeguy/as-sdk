package com.activitystream.core.model.validation;

import java.util.LinkedHashMap;
import java.util.Map;

public class CoreException extends RuntimeException {

    private Map<String, Object> params = new LinkedHashMap<>();

    public CoreException(String error, Map<? extends String, ?> params) {
        super(error + ": " + params);

        this.params.put("_error", error);
        this.params.putAll(params);
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
