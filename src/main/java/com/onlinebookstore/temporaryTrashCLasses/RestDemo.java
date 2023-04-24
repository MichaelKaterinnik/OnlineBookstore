package com.onlinebookstore.temporaryTrashCLasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestDemo {

    public void sendRequest(String url, String method, String body) {
        try {
            URL urlNet = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlNet.openConnection();

            connection.setRequestMethod(method);

            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method))    {
                connection.setDoOutput(true);
                try(OutputStream os = connection.getOutputStream()) {
                    os.write(body.getBytes());
                }
            }

            // connect to server
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)  {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) !=null)  {
                    sb.append(line).append("\n");
                }
                br.close();
            }

            connection.disconnect();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
