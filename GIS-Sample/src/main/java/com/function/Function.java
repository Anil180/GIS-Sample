package com.function;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Optional;
// import java.util.UUID;


import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
//import com.azure.storage.blob.models.BlobDownloadResponse;
//import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;
// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.nio.charset.StandardCharsets;
// import java.text.SimpleDateFormat;
import java.util.Date;

// import java.util.Optional;


// import java.util.Properties;
// import java.util.UUID;

// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeBodyPart;
// import javax.mail.internet.MimeMessage;
// import javax.mail.internet.MimeMultipart;
// import javax.net.ssl.TrustManager;
// import javax.net.ssl.X509TrustManager;
// import javax.mail.*;

// import java.io.*;
// import java.sql.Connection;
// import java.sql.DatabaseMetaData;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.Statement;
// import java.util.ArrayList;
// import java.util.List;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }

    
    @FunctionName("uploadfile")
    public void fileuploading(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET, HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
                  String connectionString = "DefaultEndpointsProtocol=https;AccountName=enzenpocsalesforceb1de;AccountKey=tGsh4EaxYKRL584agN0ee0fGMLCgNJr9lcW7Ezw172K+5juyu/DQdn42P8GI6sf+J4AlDbcKTGbc+AStzs7o3g==;EndpointSuffix=core.windows.net";
                    String containerName = "psraudit";
  BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
    BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

    for (int i = 0; i < 10; i++) {
        context.getLogger().info("fileuploading....");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentTime = dateFormat.format(new Date());
        String fileName = "file_" + currentTime + "_" + i + ".txt";
        String content = "This is the content of file " + fileName;

        // Create a BlobClient for the new file
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        // Upload the file content as a blob
        blobClient.upload(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), content.length(), true);
       
    }
}
}