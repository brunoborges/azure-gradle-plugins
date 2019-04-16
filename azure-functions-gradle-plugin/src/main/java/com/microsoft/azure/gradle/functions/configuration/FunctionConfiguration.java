/*
 * MIT License
 *
 * Copyright (c) 2017-2019 Elena Lakhno
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.microsoft.azure.gradle.functions.configuration;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.microsoft.azure.gradle.functions.bindings.BaseBinding;
import com.microsoft.azure.gradle.functions.bindings.StorageBaseBinding;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema of function.json is at https://github.com/Azure/azure-webjobs-sdk-script/blob/dev/schemas/json/function.json
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FunctionConfiguration {
    public static final String MULTIPLE_TRIGGER = "Only one trigger is allowed for each Azure Function. " +
            "Multiple triggers found on method: ";
    public static final String HTTP_OUTPUT_NOT_ALLOWED = "HttpOutput binding is only allowed to use with " +
            "HttpTrigger binding. HttpOutput binding found on method: ";
    public static final String STORAGE_CONNECTION_EMPTY = "Storage binding (blob/queue/table) must have non-empty " +
            "connection. Invalid storage binding found on method: ";

    private String scriptFile;

    private String entryPoint;

    private List<BaseBinding> bindings = new ArrayList<>();

    private boolean disabled = false;

    @JsonGetter("scriptFile")
    public String getScriptFile() {
        return scriptFile;
    }

    @JsonGetter("entryPoint")
    public String getEntryPoint() {
        return entryPoint;
    }

    @JsonGetter("bindings")
    public List<BaseBinding> getBindings() {
        return bindings;
    }

    @JsonGetter("disabled")
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public void validate() {
        checkMultipleTrigger();

        checkHttpOutputBinding();

        checkEmptyStorageConnection();
    }

    protected void checkMultipleTrigger() {
        if (getBindings().stream()
                .filter(b -> b.getType().endsWith("Trigger"))
                .count() > 1) {
            throw new RuntimeException(MULTIPLE_TRIGGER + getEntryPoint());
        }
    }

    protected void checkHttpOutputBinding() {
        if (getBindings().stream().noneMatch(b -> b.getType().equalsIgnoreCase("httpTrigger")) &&
                getBindings().stream().anyMatch(b -> b.getType().equalsIgnoreCase("http"))) {
            throw new RuntimeException(HTTP_OUTPUT_NOT_ALLOWED + getEntryPoint());
        }
    }

    protected void checkEmptyStorageConnection() {
        if (getBindings().stream()
                .filter(b -> b instanceof StorageBaseBinding)
                .map(b -> (StorageBaseBinding) b)
                .filter(sb -> StringUtils.isEmpty(sb.getConnection())).count() > 0) {
            throw new RuntimeException(STORAGE_CONNECTION_EMPTY + getEntryPoint());
        }
    }
}
