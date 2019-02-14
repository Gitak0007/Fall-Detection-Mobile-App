package com.example.parvesh.myapplication;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private static final int REQUEST_PHONE_CALL =1 ;
    private Sensor mySensor;
    private SensorManager SM;
    private TextView Acc_X;
    private TextView Acc_Y;
    private TextView Acc_Z;
    private TextView Angle;
    private TextView Accelerometer_Magnitude;
    Double x = 0.0;
    Double y;
    float val1 = 0;
    float val2 = 0;
    float val3 = 0;
    String str;

    LineGraphSeries<DataPoint> series;

    GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        Acc_X = (TextView) findViewById(R.id.textView2);
        Acc_Y = (TextView) findViewById(R.id.textView3);
        Acc_Z = (TextView) findViewById(R.id.textView4);
        Angle = findViewById(R.id.textView5);


        Accelerometer_Magnitude = findViewById(R.id.textView6);

        graphView = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();

        Button Contact = (Button) findViewById(R.id.contact);
        Contact.setOnClickListener((View.OnClickListener) this);



//        for(int i=0 ; i<500; i++){
//            x=x+0.1;
//            y=Math.sin(x);
//            series.appendData(new DataPoint(x,y),true,500);
//            Log.d("values of x",""+x+""+y);
//        }
//        graphView.addSeries(series);

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.contact:
                intent = new Intent(this, Contact.class);
                startActivity(intent);
                return;

        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        Acc_X.setText("X:" + sensorEvent.values[0]);
        Acc_Y.setText("Y:" + sensorEvent.values[1]);
        Acc_Z.setText("Z:" + sensorEvent.values[2]);

        val1 = sensorEvent.values[0];
        val2 = sensorEvent.values[1];
        val3 = sensorEvent.values[2];
        float ATt = ((val1) * (val1)) + ((val2) * (val2)) + ((val3) * (val3));
        Double AccelerometerMagnitude = Math.sqrt(ATt)/9.8;
        Double angle = Math.acos(val3/AccelerometerMagnitude);
        Log.d("AccelerometerMagnitude", "" + AccelerometerMagnitude);
       // Log.d("Angle", "" + angle);
        Angle.setText("angle" + angle);
        Accelerometer_Magnitude.setText("AcceleromtereMagnitude" + AccelerometerMagnitude);

        SharedPreferences mPrefs = getSharedPreferences("IDvalue",0);
        final String str = mPrefs.getString("N", "");
        Log.d("Number",""+str);



        if (AccelerometerMagnitude > 2) {

            if (angle>2) {
                startService(new Intent(this, Alarm.class));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

                }

               else{

                Intent intent1 = new Intent(Intent.ACTION_CALL);
                intent1.setData(Uri.parse("tel:"+str));

                startActivity(intent1);}


        }
    }

}


        public  void stop(View view){
            stopService(new Intent(this, Alarm.class));
        }









    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}


