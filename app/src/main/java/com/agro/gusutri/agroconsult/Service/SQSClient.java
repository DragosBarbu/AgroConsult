package com.agro.gusutri.agroconsult.Service;

import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;


/**
 * Created by dragos on 5/6/15.
 */
public class SQSClient {

    private static SQSClient instance;

    private BasicAWSCredentials credentials;
    public static final String FIELD_QUEUE = "fieldProblemQueue";
    private static final String accessKey = "AKIAIFT2GT232CD4RINQ";
    private static final String secretKey = "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";

    private AmazonSQS sqs;

    private SQSClient() {
        credentials = new BasicAWSCredentials(accessKey, secretKey);
        sqs = new AmazonSQSClient(credentials);
        sqs.setEndpoint("https://sqs.eu-central-1.amazonaws.com");

    }

    public static SQSClient getInstance() {
        if (instance == null)
            instance = new SQSClient();
        return instance;
    }

    public String getQueueUrl(String queueName) {
        GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
        return sqs.getQueueUrl(getQueueUrlRequest).getQueueUrl();
    }


    public boolean sendMessageToQueue(String queueUrl, String message) {
        SendMessageRequest messageRequest = new SendMessageRequest(queueUrl, message);
        try {
            SendMessageResult messageResult = sqs.sendMessage(messageRequest);
            Log.i("SQS message result", messageResult.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
