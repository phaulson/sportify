package com.spogss.sportifycommunity.data.Connection;

import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ControllerSync {
    private URL url;
    //   private static String token;

    ControllerSync(URL url) {
        this.url = url;
    }

    protected String getJSONString(String... params) {
        String result;
        // extract request params for clarity
        HttpMethod httpMethod = HttpMethod.valueOf(params[0]);
        String route = params[1];
        String payload = (params.length >= 3) ? params[2] : null;

        try {
            // set payload or querystring
            if (payload != null && httpMethod == HttpMethod.GET) {
                route += "?" + payload;
            }

            url = new URL(url + "/" + route);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // set token header if logged in
            //if (token != null) {
            //    connection.setRequestProperty("Token", token);
            //}

            if (payload != null && httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT || httpMethod == HttpMethod.DELETE) {
                write(connection, httpMethod, payload);
            }

            result = read(connection);

            connection.disconnect();

            return result;

        } catch (Exception ex) {
            return null;
        }
    }

    private void write(HttpURLConnection connection, HttpMethod httpMethod, String json) throws IOException {
        connection.setRequestMethod(httpMethod.toString());
        connection.setRequestProperty("Content-Type", "application/json");

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        writer.write(json);
        writer.flush();
        writer.close();

        connection.getResponseCode();
    }

    private String read(HttpURLConnection connection) throws IOException {
        String result = null;
        // read responseText
        BufferedReader reader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        result = sb.toString();
        reader.close();

        return result;
    }
}
