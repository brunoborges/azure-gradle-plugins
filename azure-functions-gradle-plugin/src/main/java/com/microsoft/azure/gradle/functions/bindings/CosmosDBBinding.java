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
package com.microsoft.azure.gradle.functions.bindings;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.microsoft.azure.functions.annotation.CosmosDBInput;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.CosmosDBTrigger;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CosmosDBBinding extends BaseBinding {
    public static final String COSMOS_DB_TRIGGER = "cosmosDBTrigger";
    public static final String COSMOS_DB = "cosmosdb";

    private String databaseName = "";

    private String collectionName = "";

    private String leaseCollectionName = "";

    private String id = "";

    private String sqlQuery = "";

    private String connectionStringSetting = "";

    private boolean createIfNotExists = false;

    private boolean createLeaseCollectionIfNotExists = false;

    public CosmosDBBinding(final CosmosDBInput dbInput) {
        super(dbInput.name(), COSMOS_DB, Direction.IN, dbInput.dataType());

        databaseName = dbInput.databaseName();
        collectionName = dbInput.collectionName();
        id = dbInput.id();
        sqlQuery = dbInput.sqlQuery();
        connectionStringSetting = dbInput.connectionStringSetting();
    }

    public CosmosDBBinding(final CosmosDBOutput dbOutput) {
        super(dbOutput.name(), COSMOS_DB, Direction.OUT, dbOutput.dataType());

        databaseName = dbOutput.databaseName();
        collectionName = dbOutput.collectionName();
        connectionStringSetting = dbOutput.connectionStringSetting();
        createIfNotExists = dbOutput.createIfNotExists();
    }

    public CosmosDBBinding(final CosmosDBTrigger dbTrigger) {
        super(dbTrigger.name(), COSMOS_DB_TRIGGER, Direction.IN, dbTrigger.dataType());

        databaseName = dbTrigger.databaseName();
        collectionName = dbTrigger.collectionName();
        leaseCollectionName = dbTrigger.leaseCollectionName();
        connectionStringSetting = dbTrigger.connectionStringSetting();
        createLeaseCollectionIfNotExists = dbTrigger.createLeaseCollectionIfNotExists();
    }

    @JsonGetter
    public String getDatabaseName() {
        return databaseName;
    }

    @JsonGetter
    public String getCollectionName() {
        return collectionName;
    }

    @JsonGetter
    public String getId() {
        return id;
    }

    @JsonGetter
    public String getSqlQuery() {
        return sqlQuery;
    }

    @JsonGetter
    public String getConnectionStringSetting() {
        return connectionStringSetting;
    }

    @JsonGetter
    public boolean isCreateIfNotExists() {
        return createIfNotExists;
    }

    @JsonGetter
    public boolean isCreateLeaseCollectionIfNotExists() {
        return createLeaseCollectionIfNotExists;
    }

    @JsonGetter
    public String getLeaseCollectionName() {
        return leaseCollectionName;
    }
}
