package com.ananayarora.http;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ananayarora on 6/25/16.
 */
public class HTTPGetRequest extends AsyncTask<String, Void, String>
{
    @Override
    public String doInBackground(String... urls)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // params comes from the execute() call: params[0] is the url.
        try
        {
            return (String) downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        int len = 500;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";

            StringBuilder response = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                response.append(line);
            }

            return response.toString();

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}