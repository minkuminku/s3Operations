package com.mayank.s3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        String accessKey = "XXXXXX";
        String secretKey = "XXXXXX";
        String bucketName = "mayankvideos22";
        String objectKey = "tabc.txt";
        String downloadFilePath = "ddd.txt"; // Specify the local file path where you want to save the downloaded object

        // Create AWS credentials using your access key and secret key
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        // Create an S3Client with credentials and the desired AWS region
        S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1) // Specify your desired AWS region
                .credentialsProvider(() -> awsCredentials)
                .build();

        try {
            // Create a GetObjectRequest to specify the bucket name and object key
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            // Download the object and save it to the local file
            ResponseInputStream responseInputStream = s3.getObject(getObjectRequest);
            FileOutputStream outputStream = new FileOutputStream(downloadFilePath);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = responseInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            responseInputStream.close();

            System.out.println("Object downloaded successfully to: " + downloadFilePath);
        } catch (S3Exception e) {
            System.err.println("Error downloading object: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        } finally {
            // Close the S3 client
            s3.close();
        }
    }
}
