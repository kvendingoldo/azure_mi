package com.kvendingoldo.ami;

import com.microsoft.azure.management.cosmosdb.DatabaseAccountListKeysResult;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.cosmosdb.CosmosDBAccount;
import com.microsoft.azure.management.cosmosdb.DatabaseAccountListConnectionStringsResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Properties;

public class Cosmos {
    public void run(Azure azure, Properties prop) throws Exception {
        CosmosDBAccount cosmosDBAccount = azure.cosmosDBAccounts().getById(prop.getProperty("cosmos.id"));
        Utils.print(cosmosDBAccount);

        DatabaseAccountListConnectionStringsResult databaseAccountListConnectionStringsResult = cosmosDBAccount.listConnectionStrings();
        DatabaseAccountListKeysResult keys = cosmosDBAccount.listKeys();

        MongoClientURI uri = new MongoClientURI(databaseAccountListConnectionStringsResult.connectionStrings().get(0).connectionString());

        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(uri);

            // Get database
            MongoDatabase database = mongoClient.getDatabase("db");

            // Get collection (it can not exist)
            MongoCollection<Document> collection = database.getCollection("coll");

            // Insert example documents
            Document document1 = new Document("fruit", "apple");
            collection.insertOne(document1);

            Document document2 = new Document("fruit", "mango");
            collection.insertOne(document2);

            // Find fruits by name
            Document queryResult = collection.find(Filters.eq("fruit", "apple")).first();
            System.out.println(queryResult.toJson());

        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
            System.out.println("Completed successfully");
        }
    }
}