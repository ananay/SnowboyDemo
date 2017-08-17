package ai.kitt.snowboy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ananayarora.http.HTTPPostJSONRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ai.kitt.snowboy.demo.R;

public class TrainActivity extends Activity {

    private Button btnControl, btnNext;
    private TextView textDisplay;
    private WavAudioRecorder mRecorder;
    private static String mRcordFilePath = Environment.getExternalStorageDirectory() + "/SnowboyDemo";
    private static String mRcordFilePathtwo = Environment.getExternalStorageDirectory() + "/snowboy";
    private int fileNumber = 1;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        context = getApplicationContext();
        textDisplay = (TextView) this.findViewById(R.id.Textdisplay);
        File directory = new File(mRcordFilePath);
        directory.mkdirs();
        btnControl = (Button) this.findViewById(R.id.btnControl);
        btnNext = (Button) this.findViewById(R.id.btnNext);
        btnControl.setText("Start");
        mRecorder = WavAudioRecorder.getInstance();
        textDisplay.setText("File " + fileNumber);
        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
                    mRecorder.setOutputFile(mRcordFilePath + "/wav" + fileNumber + ".wav");
                    mRecorder.prepare();
                    mRecorder.start();
                    btnControl.setText("Stop");
                } else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
                    mRecorder.release();
                    mRecorder = WavAudioRecorder.getInstance();
                    mRecorder.setOutputFile(mRcordFilePath + "/wav" + fileNumber + ".wav");
                    btnControl.setText("Start");
                    textDisplay.setText(textDisplay.getText() + " - Try Again!");
                } else {
                    mRecorder.stop();
                    mRecorder.reset();
                    btnControl.setText("Start");
                    File recording = new File(mRcordFilePath + "/wav" + fileNumber + ".wav");
                    if(recording.exists()) {
                        btnNext.setEnabled(true);
                    } else {
                        textDisplay.setText(textDisplay.getText() + " - Try Again!");
                    }
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileNumber < 3) {
                    fileNumber++;
                    textDisplay.setText("File " + fileNumber);
                } else {

                    // Get the Base64 of the WAV Files

                    String PMDLFileName = Constants.ACTIVE_UMDL;

                    File wav1 = new File(mRcordFilePath + "/wav1.wav");
                    File wav2 = new File(mRcordFilePath + "/wav2.wav");
                    File wav3 = new File(mRcordFilePath + "/wav3.wav");

                    String base64wav1 = getWavBase64(wav1);
                    String base64wav2 = getWavBase64(wav2);
                    String base64wav3 = getWavBase64(wav3);

                    String endpoint = "https://snowboy.kitt.ai/api/v1/train/";
                    String hotword_name = "Computer";
                    String token = "8931d603f214519e32f7d85dcbb5c2d786fe5e2b";
                    String language = "en";
                    String age_group = "20_29";
                    String gender = "M";
                    String microphone = "phone microphone";


                    Log.e("wav1length", Integer.toString(base64wav1.toString().length()));
                    Log.e("wav2length", Integer.toString(base64wav2.toString().length()));
                    Log.e("wav3length", Integer.toString(base64wav3.toString().length()));

                    String JSONData = "{\n" +
                            "        \"name\": \""+hotword_name+"\",\n" +
                            "        \"language\": \""+language+"\",\n" +
                            "        \"age_group\": \""+age_group+"\",\n" +
                            "        \"gender\": \""+gender+"\",\n" +
                            "        \"microphone\": \""+microphone+"\",\n" +
                            "        \"token\": \""+token+"\",\n" +
                            "        \"voice_samples\": [\n"+
                            "            {\"wave\": \""+base64wav1.toString()+"\"},\n" +
                            "            {\"wave\": \""+base64wav2.toString()+"\"},\n" +
                            "            {\"wave\": \""+base64wav3.toString()+"\"}\n" +
                            "        ]\n" +
                            "    }";


                    HTTPPostJSONRequest request = new HTTPPostJSONRequest();
                    String response = request.doInBackground(endpoint, JSONData, PMDLFileName);

                    textDisplay.setText("Model Processing Complete. Launching Main Activity...");

                    if (response == "done") {
                        Intent intent = new Intent(context, Demo.class);
                        intent.putExtra("recording","start");
                        startActivity(intent);
                    }

                }
                btnNext.setEnabled(false);
            }
        });
    }

    private String getWavBase64(File file) {
        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            Log.e("Error", "File was not found");
        }
        byte[] buffer = new byte[104857600];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream output64 = new Base64OutputStream(output, Base64.NO_WRAP);
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("WAV: ", Integer.toString(output.toString().length()));
        return output.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mRecorder) {
            mRecorder.release();
        }
    }
}