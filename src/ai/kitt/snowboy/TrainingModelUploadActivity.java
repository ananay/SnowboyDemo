package ai.kitt.snowboy;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ai.kitt.snowboy.demo.R;
import com.ananayarora.http.HTTPPostJSONRequest;

public class TrainingModelUploadActivity extends AppCompatActivity {

    private static String mRcordFilePath = Environment.getExternalStorageDirectory() + "/SnowboyDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_model_upload);

    }

    private String getWavBase64(File file) {
        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            Log.e("Error", "File was not found");
        }
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}
