package ece.course.eesm5060_assign2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class AccelerometerSensor implements SensorEventListener {
    public final static String TAG_VALUE_DX = "tagValueDx";
    public final static String TAG_VALUE_DY = "tagValueDy";
    public final static String TAG_VALUE_DZ = "tagValueDz";

    private boolean mIsStarted = false;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Handler mHandler;
    private float prevX = 0f, prevY = 0f, prevZ = 0f;
    private float FILTER_ALPHA = 0.25f;

    public AccelerometerSensor(Context context, Handler handler) {
        mHandler = handler;
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        float dx = sensorEvent.values[0];
        float dy = sensorEvent.values[1];
        float dz = sensorEvent.values[2];
        float fx = filter(dx, prevX, FILTER_ALPHA);
        float fy = filter(dy, prevY, FILTER_ALPHA);
        float fz = filter(dz, prevZ, FILTER_ALPHA);
        if (mHandler != null) {
            Message message = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putFloat(TAG_VALUE_DX, fx);
            bundle.putFloat(TAG_VALUE_DY, fy);
            bundle.putFloat(TAG_VALUE_DZ, fz);
            message.setData(bundle);
            mHandler.sendMessage(message);
        }
        prevX = fx;
        prevY = fy;
        prevZ = fz;
    }

    private static float filter(float input, float prev, float alpha) {
        return prev + alpha * (input - prev);
    }

    public void startListening() {
        if (mIsStarted)
            return;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mIsStarted = true;
    }
    public void stopListening() {
        if (!mIsStarted)
            return;
        mSensorManager.unregisterListener(this);
        mIsStarted = false;
    }
}
