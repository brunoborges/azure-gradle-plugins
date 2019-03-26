package com.microsoft.azure.gradle.functions.bindings;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.microsoft.azure.functions.annotation.SendGridOutput;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SendGridBinding extends BaseBinding {
    public static final String SEND_GRID = "sendGrid";

    private String apiKey = "";

    private String to = "";

    private String from = "";

    private String subject = "";

    private String text = "";

    public SendGridBinding(final SendGridOutput sendGridOutput) {
        super(sendGridOutput.name(), SEND_GRID, Direction.OUT, sendGridOutput.dataType());

        apiKey = sendGridOutput.apiKey();
        to = sendGridOutput.to();
        from = sendGridOutput.from();
        subject = sendGridOutput.subject();
        text = sendGridOutput.text();
    }

    @JsonGetter
    public String getApiKey() {
        return apiKey;
    }

    @JsonGetter
    public String getTo() {
        return to;
    }

    @JsonGetter
    public String getFrom() {
        return from;
    }

    @JsonGetter
    public String getSubject() {
        return subject;
    }

    @JsonGetter
    public String getText() {
        return text;
    }
}
