package ai.kitt.snowboy;

import ai.kitt.snowboy.audio.RecordingThread;
import ai.kitt.snowboy.audio.PlaybackThread;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import ai.kitt.snowboy.audio.AudioDataSaver;
import ai.kitt.snowboy.demo.R;


public class Demo extends Activity {

    private Button openTrain, btnStart, btnStop;
    private Intent intent, serviceIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(this, TrainActivity.class);
        serviceIntent = new Intent(this, RecordingService.class);
        setContentView(R.layout.main);
        AppResCopy.copyResFromAssetsToSD(this);
        openTrain = (Button) this.findViewById(R.id.btnTrain);
        openTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(serviceIntent);
            }
        });

        btnStop = (Button) this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(serviceIntent);
            }
        });

        Intent transfer = getIntent();

        if (transfer != null) {
            String recordingAction = transfer.getStringExtra("recording");
            if (recordingAction != null) {
                if (recordingAction.equals("start")) {
                    startService(serviceIntent);
                    Log.e("Recording", "Started");
                } else {
                    Log.e("Recording", "Not Started");
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
