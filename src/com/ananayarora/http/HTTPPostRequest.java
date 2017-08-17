package com.ananayarora.http;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ananayarora on 6/26/16.
 */
public class HTTPPostRequest extends AsyncTask <String, Void, String>
{
    public String doInBackground(String... url)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try
        {
            return (String) downloadUrl(url[0],url[1]);
        } catch (IOException e) {
            return "Unable to Retrieve Webpage. URL maybe invalid";
        }
    }

    public String downloadUrl(String myurl, String query) throws IOException {
        InputStream is = null;
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            Writer writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(query);
            writer.flush();
            writer.close();

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

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}