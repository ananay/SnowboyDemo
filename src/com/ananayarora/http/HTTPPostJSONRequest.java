package com.ananayarora.http;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

import javax.net.ssl.HttpsURLConnection;

import ai.kitt.snowboy.TrainActivity;
import ai.kitt.snowboy.demo.R;

import static android.R.id.input;

/**
 * Created by ananayarora on 6/26/16.
 */
public class HTTPPostJSONRequest extends AsyncTask <String, Void, String>
{
    private static String recPath = Environment.getExternalStorageDirectory() + "/snowboy";

    @Override
    public String doInBackground(String... url)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try
        {
            return downloadUrl(url[0],url[1],url[2]);
        } catch (IOException e) {
            Log.e("URL Error: ", e.toString());
            return "Error. URL maybe invalid";
        }
    }

    public String downloadUrl(String myurl, String query, String PMDLFileName) throws IOException {
        InputStream is = null;
        int len = 500;

        try {

            URL url = new URL(myurl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","*/*");
            conn.setRequestMethod("POST");
            conn.setReadTimeout(100000);

            Writer writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(query);
            writer.flush();
            writer.close();

            is = conn.getInputStream();

            File outputFile = new File(recPath, PMDLFileName);
            FileOutputStream fos = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];//Set buffer type
            int len1 = 0;//init length
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);//Write new file
            }

            fos.close();
            is.close();

            return "done";

//
//            File file = new File(recPath + "/lol.pmdl");
//            FileWriter fw = new FileWriter(file);
//            BufferedWriter bw = new BufferedWriter(fw);
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//            char[] buffer = new char[8192];
//            long count = 0;
//            int n;
//
//            while ((n = br.read(buffer)) != -1) {
//                bw.write(buffer, 0, n);
//                count += n;
//            }
//
//            return "done";

//            String line = "";
//            StringBuilder response = new StringBuilder();

//            while ((line = br.readLine()) != null)
//            {

//                response.append(line);
//            }

            //Log.e("Response Code", Integer.toString(conn.getResponseCode()));
            //Log.e("Response",response.toString());
//            return response.toString();

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