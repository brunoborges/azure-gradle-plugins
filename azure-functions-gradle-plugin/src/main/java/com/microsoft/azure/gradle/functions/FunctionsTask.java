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
package com.microsoft.azure.gradle.functions;

import com.microsoft.azure.gradle.functions.auth.AuthConfiguration;
import com.microsoft.azure.gradle.functions.auth.AzureAuthFailureException;
import com.microsoft.azure.gradle.functions.auth.AzureAuthHelper;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.appservice.FunctionApp;
import org.gradle.api.DefaultTask;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.microsoft.azure.gradle.functions.AzureFunctionsPlugin.AZURE_FUNCTIONS;

public abstract class FunctionsTask extends DefaultTask implements AuthConfiguration {
    private static final String AZURE_INIT_FAIL = "Failed to authenticate with Azure. Please check your configuration.";

    protected AzureFunctionsExtension azureFunctionsExtension;
    protected AzureAuthHelper azureAuthHelper;
    private Azure azure;

    protected Object settings;

    /**
     * Resource group of Function App. It will be created if it doesn't exist.
     */
    private String resourceGroup;

    /**
     * Function App name. It will be created if it doesn't exist.
     */
    private String appName;

    /**
     * Function App region, which will only be used to create Function App at the first time.
     */
    private String region= "westus2";

    private String outputDirectory;

    private String finalName;

    private Map<String, String> appSettings;

    public String getFinalName() {
        if (finalName == null) {
            finalName = getProject().getTasks().getByPath("jar").property("archiveName").toString().replace(".jar", "");
        }
        return finalName;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public String getResourceGroup() {
        return resourceGroup;
    }

    public String getAppName() {
        return appName;
    }

    public void setResourceGroup(String resourceGroup) {
        this.resourceGroup = resourceGroup;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public Map getAppSettings() {
        if (appSettings == null) {
            appSettings = new HashMap<>();
            appSettings.put("FUNCTIONS_EXTENSION_VERSION", "beta");
        }
        return appSettings;
    }

    public String getOutputDirectory() {
        if (outputDirectory == null) {
            outputDirectory = getProject().getTasks().getByPath("compileJava").getOutputs().getFiles().getAsPath();
        }
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    FunctionsTask() {
        azureAuthHelper = new AzureAuthHelper(this);
        azureFunctionsExtension = (AzureFunctionsExtension) getProject().getExtensions().getByName(AZURE_FUNCTIONS);
    }

    @Override
    public String getUserAgent() {
        return getName() + " " + getGroup();
//        return String.format("%s/%s %s:%s %s:%s", this.getName(), this.getGroup()
//                getPluginName(), getPluginVersion(),
//                INSTALLATION_ID_KEY, getInstallationId(),
//                SESSION_ID_KEY, getSessionId());
    }

    @Override
    public String getSubscriptionId() {
        return (String) getProject().getProperties().get("subscriptionId");
    }

    @Override
    public boolean hasAuthenticationSettings() {
        return getProject().getProperties().containsKey(AzureAuthHelper.CLIENT_ID) || azureFunctionsExtension.getAuthFile() != null;
    }

    @Override
    public String getAuthenticationSetting(String key) {
        return (String) getProject().getProperties().get(key);
    }

    @Override
    public String getAuthFile() {
        return azureFunctionsExtension.getAuthFile();
    }

    public Azure getAzureClient() throws AzureAuthFailureException {
        if (azure == null) {
            azure = azureAuthHelper.getAzureClient();
            if (azure == null) {
//                getTelemetryProxy().trackEvent(INIT_FAILURE);
                throw new AzureAuthFailureException(AZURE_INIT_FAIL);
            } else {
                // Repopulate subscriptionId in case it is not configured.
//                getTelemetryProxy().addDefaultProperty(SUBSCRIPTION_ID_KEY, azure.subscriptionId());
            }
        }
        return azure;
    }

    public String getBuildDirectoryAbsolutePath() {
        return getProject().getBuildDir().getAbsolutePath();
    }

    public String getDeploymentStageDirectory() {
        return Paths.get(getBuildDirectoryAbsolutePath(),
                AZURE_FUNCTIONS,
                getAppName()).toString();
    }

    public FunctionApp getFunctionApp() throws AzureAuthFailureException {
        try {
            return getAzureClient().appServices().functionApps().getByResourceGroup(getResourceGroup(), getAppName());
        } catch (AzureAuthFailureException authEx) {
            throw authEx;
        } catch (Exception ex) {
            // Swallow exception for non-existing function app
        }
        return null;
    }
}
