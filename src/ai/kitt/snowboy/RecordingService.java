package ai.kitt.snowboy;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import ai.kitt.snowboy.audio.AudioDataSaver;
import ai.kitt.snowboy.audio.RecordingThread;

import static android.content.ContentValues.TAG;

public class RecordingService extends Service {

    private RecordingThread recordingThread = null;
    private int activeTimes = 0;
    private Vibrator vibrator = null;
    public static boolean on = false;

    @Override
    public void onCreate() {
        recordingThread = new RecordingThread(handle, new AudioDataSaver());
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        on = true;
        startRecording();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startRecording() {
        recordingThread.startRecording();
        Log.i(TAG, "Recording started.");
    }

    private void stopRecording() {
        recordingThread.stopRecording();
        Log.i(TAG, "Recording stopped.");
    }

    @SuppressLint("HandlerLeak")
    public Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MsgEnum message = MsgEnum.getMsgEnum(msg.what);
            switch(message) {
                case MSG_ACTIVE:
                    activeTimes++;
                    vibrator.vibrate(1000);
                    Log.i(TAG, msg.toString());
                    break;
                case MSG_INFO:
                    Log.i(TAG, msg.toString());
                    break;
                case MSG_VAD_SPEECH:
                    Log.i(TAG, msg.toString());
                    break;
                case MSG_VAD_NOSPEECH:
                    Log.i(TAG, msg.toString());
                    break;
                case MSG_ERROR:
                    Log.e(TAG, msg.toString());
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        stopRecording();
        on = false;
        super.onDestroy();
    }
}
