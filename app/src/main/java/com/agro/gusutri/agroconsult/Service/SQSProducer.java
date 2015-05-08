package com.agro.gusutri.agroconsult.Service;

import android.graphics.Bitmap;
import android.util.Base64;

import com.agro.gusutri.agroconsult.model.Field;
import com.agro.gusutri.agroconsult.model.ProblemEvent;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by dragos on 5/6/15.
 */
public class SQSProducer {
    private static SQSProducer instance;
    private Gson gson = new Gson();
    private SQSClient sqsClient = SQSClient.getInstance();

    private SQSProducer() {
    }

    public static SQSProducer getInstance() {
        if (instance == null)
            instance = new SQSProducer();
        return instance;
    }

    public boolean sendProblemEventMessage(ProblemEvent problemEvent) {


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Bitmap bitmap = problemEvent.getImage();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] b = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        ProblemEventSQSMessage message = new ProblemEventSQSMessage(encodedImage, problemEvent.getDate(), problemEvent.getDetails(), problemEvent.getCategoryName(), problemEvent.getField().getId(), problemEvent.getLocation().latitude, problemEvent.getLocation().longitude);

        String json = gson.toJson(message);

        String queueUrl = sqsClient.getQueueUrl(SQSClient.FIELD_QUEUE);
        return sqsClient.sendMessageToQueue(queueUrl, json);
    }

    private class ProblemEventSQSMessage {


        private Date date;
        private String details, categoryName;
        private long fieldId;
        private String encodedImage;
        double latitude;
        double longitude;

        private ProblemEventSQSMessage(String encodedImage, Date date, String details, String categoryName, long fieldId, double longitude, double latitude) {
            this.date = date;
            this.details = details;
            this.categoryName = categoryName;
            this.fieldId = fieldId;
            this.longitude = longitude;
            this.latitude = latitude;
            this.encodedImage = encodedImage;
        }
    }
}
