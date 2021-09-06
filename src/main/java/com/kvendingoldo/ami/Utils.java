package com.kvendingoldo.ami;

import com.microsoft.azure.management.msi.Identity;
import com.microsoft.azure.management.cosmosdb.CosmosDBAccount;
import com.microsoft.azure.management.cosmosdb.DatabaseAccountListKeysResult;
import com.microsoft.azure.management.cosmosdb.DatabaseAccountListReadOnlyKeysResult;

public final class Utils {
    /**
     * Print User Assigned MSI info.
     *
     * @param resource a User Assigned MSI
     */
    public static void print(Identity resource) {
        StringBuilder info = new StringBuilder();
        info.append("Resource Group: ").append(resource.id())
                .append("\n\tName: ").append(resource.name())
                .append("\n\tRegion: ").append(resource.region())
                .append("\n\tTags: ").append(resource.tags())
                .append("\n\tService Principal Id: ").append(resource.principalId())
                .append("\n\tClient Id: ").append(resource.clientId())
                .append("\n\tTenant Id: ").append(resource.tenantId())
                .append("\n\tClient Secret Url: ").append(resource.clientSecretUrl());
        System.out.println(info.toString());
    }

    /**
     * Print CosmosDB info.
     *
     * @param cosmosDBAccount a CosmosDB
     */
    public static void print(CosmosDBAccount cosmosDBAccount) {
        StringBuilder builder = new StringBuilder()
                .append("CosmosDB: ").append(cosmosDBAccount.id())
                .append("\n\tName: ").append(cosmosDBAccount.name())
                .append("\n\tResourceGroupName: ").append(cosmosDBAccount.resourceGroupName())
                .append("\n\tKind: ").append(cosmosDBAccount.kind().toString())
                .append("\n\tDefault consistency level: ").append(cosmosDBAccount.consistencyPolicy().defaultConsistencyLevel())
                .append("\n\tIP range filter: ").append(cosmosDBAccount.ipRangeFilter());

        DatabaseAccountListKeysResult keys = cosmosDBAccount.listKeys();
        DatabaseAccountListReadOnlyKeysResult readOnlyKeys = cosmosDBAccount.listReadOnlyKeys();
        builder
                .append("\n\tPrimary Master Key: ").append(keys.primaryMasterKey())
                .append("\n\tSecondary Master Key: ").append(keys.secondaryMasterKey())
                .append("\n\tPrimary Read-Only Key: ").append(readOnlyKeys.primaryReadonlyMasterKey())
                .append("\n\tSecondary Read-Only Key: ").append(readOnlyKeys.secondaryReadonlyMasterKey());

        for (com.microsoft.azure.management.cosmosdb.Location writeReplica : cosmosDBAccount.writableReplications()) {
            builder.append("\n\t\tWrite replication: ")
                    .append("\n\t\t\tName :").append(writeReplica.locationName());
        }

        builder.append("\n\tNumber of read replications: ").append(cosmosDBAccount.readableReplications().size());
        for (com.microsoft.azure.management.cosmosdb.Location readReplica : cosmosDBAccount.readableReplications()) {
            builder.append("\n\t\tRead replication: ")
                    .append("\n\t\t\tName :").append(readReplica.locationName());
        }
    }
}