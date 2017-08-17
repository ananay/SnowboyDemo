package ai.kitt.snowboy;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ai.kitt.snowboy.demo.R;


public class Demo extends Activity {

    private Button openTrain, btnService;
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

        btnService = (Button) this.findViewById(R.id.btnService);
        if(RecordingService.on){
            btnService.setText("Stop Service");
        } else {
            btnService.setText("Run Service");
        }
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnService.getText().equals("Run Service")){
                    startService(serviceIntent);
                    btnService.setText("Stop Service");
                } else {
                    stopService(serviceIntent);
                    btnService.setText("Run Service");
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
