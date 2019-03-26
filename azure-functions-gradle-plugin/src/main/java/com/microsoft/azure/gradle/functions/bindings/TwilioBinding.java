package com.microsoft.azure.gradle.functions.bindings;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.microsoft.azure.functions.annotation.TwilioSmsOutput;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TwilioBinding extends BaseBinding {
    public static final String TWILIO_SMS = "twilioSms";

    private String accountSid = "";

    private String authToken = "";

    private String to = "";

    private String from = "";

    private String body = "";

    public TwilioBinding(final TwilioSmsOutput smsOutput) {
        super(smsOutput.name(), TWILIO_SMS, Direction.OUT, smsOutput.dataType());

        accountSid = smsOutput.accountSid();
        authToken = smsOutput.authToken();
        to = smsOutput.to();
        from = smsOutput.from();
        body = smsOutput.body();
    }

    @JsonGetter
    public String getAccountSid() {
        return accountSid;
    }

    @JsonGetter
    public String getAuthToken() {
        return authToken;
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
    public String getBody() {
        return body;
    }
}
