package com.kvendingoldo.ami;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.rest.LogLevel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties prop = new Properties();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Properties are ready");
        System.out.println("Start to build MSI credentials");

        /* MSI Auth */
        MSICredentials credentials = new MSICredentials(AzureEnvironment.AZURE);
        Azure azure = null;

        try {
            System.out.println("Start to build Azure credentials");
            azure = Azure
                    .configure()
                    .withLogLevel(LogLevel.BODY_AND_HEADERS)
                    .authenticate(credentials)
                    .withDefaultSubscription();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        Cosmos cosmos = new Cosmos();

        try {
            System.out.println("Starting CosmosDB");
            cosmos.run(azure, prop);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(String.format("Cosmos getStarted failed with %s", e));
        }
    }
}