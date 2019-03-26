package com.microsoft.azure.gradle.functions.bindings;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.microsoft.azure.functions.annotation.MobileTableInput;
import com.microsoft.azure.functions.annotation.MobileTableOutput;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MobileTableBinding extends BaseBinding {
    public static final String MOBILE_TABLE = "mobileTable";

    private String tableName = "";

    private String id = "";

    private String connection = "";

    private String apiKey = "";

    public MobileTableBinding(final MobileTableInput tableInput) {
        super(tableInput.name(), MOBILE_TABLE, Direction.IN, tableInput.dataType());

        tableName = tableInput.tableName();
        id = tableInput.id();
        connection = tableInput.connection();
        apiKey = tableInput.apiKey();
    }

    public MobileTableBinding(final MobileTableOutput tableOutput) {
        super(tableOutput.name(), MOBILE_TABLE, Direction.OUT, tableOutput.dataType());

        tableName = tableOutput.tableName();
        connection = tableOutput.connection();
        apiKey = tableOutput.apiKey();
    }

    @JsonGetter
    public String getTableName() {
        return tableName;
    }

    @JsonGetter
    public String getId() {
        return id;
    }

    @JsonGetter
    public String getConnection() {
        return connection;
    }

    @JsonGetter
    public String getApiKey() {
        return apiKey;
    }
}
