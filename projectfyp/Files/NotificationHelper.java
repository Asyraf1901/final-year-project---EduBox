package com.example.projectfyp.Files;

import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;

public class NotificationHelper {

    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/projectfyp-72d5b/messages:send";

    public static void sendNotificationToStudent(String message) {
        OkHttpClient client = new OkHttpClient();

        try {
            // Dapatkan Access Token dari Service Account JSON
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream("projectfyp-72d5b-firebase-adminsdk-o2qfm-6e9c0fe8a6.json"))
                    .createScoped("https://www.googleapis.com/auth/firebase.messaging");
            credentials.refreshIfExpired();
            String accessToken = credentials.getAccessToken().getTokenValue();

            // Mesej yang dihantar kepada pelajar
            JSONObject json = new JSONObject();
            JSONObject messageJson = new JSONObject();

            // Semua kod yang boleh menghasilkan JSONException adalah disekat disini
            messageJson.put("topic", "student"); // Topik untuk pelajar
            JSONObject notification = new JSONObject();
            notification.put("title", "New Announcement");  // Tajuk notifikasi
            notification.put("body", message);  // Kandungan mesej
            messageJson.put("notification", notification);  // Letakkan objek notifikasi dalam message
            json.put("message", messageJson);  // Letakkan objek message dalam json utama

            // Membuat request untuk menghantar notifikasi
            RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(FCM_URL)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            client.newCall(request).execute();
        } catch (IOException e) {
            System.err.println("Ralat semasa menghantar notifikasi: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {  // Menangkap pengecualian lain yang tidak dijangka
            System.err.println("Ralat semasa mencipta objek JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
