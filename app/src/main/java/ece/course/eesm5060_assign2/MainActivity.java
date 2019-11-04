package ece.course.eesm5060_assign2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    DrawingView mDrawingView;
    AccelerometerSensor mAccSensor;

    final float GRAVITY = 9.81f;
    final float GRAVITY_SCALE = 5f; // i.e. gravity min = 7.81, max = 11.81
    final float GRAVITY_MULTI = 1f/GRAVITY_SCALE;

    int mStepCount = 0;
    float mPrevForce = 9.81f;
    final float STEP_THRESHOLD = 11.2f;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawingView = (DrawingView)findViewById(R.id.drawing);
        mDrawingView.setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                mDrawingView.invalidate();
            }
        });

        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepCount = 0;
            }
        });

        mAccSensor = new AccelerometerSensor(this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                float tmpX = msg.getData().getFloat(AccelerometerSensor.TAG_VALUE_DX);
                float tmpY = msg.getData().getFloat(AccelerometerSensor.TAG_VALUE_DY);
                float tmpZ = msg.getData().getFloat(AccelerometerSensor.TAG_VALUE_DZ);
                float force = (float)Math.sqrt(tmpX*tmpX + tmpY*tmpY + tmpZ*tmpZ);
                float ballHeight = (force - GRAVITY) * GRAVITY_MULTI;
                mDrawingView.setBallHeight(ballHeight);
                // Calculate step count based on force
                if (mPrevForce < STEP_THRESHOLD && force > STEP_THRESHOLD)
                    mStepCount += 1;
                mPrevForce = force;
                TextView tvValueX = (TextView) findViewById(R.id.tvAccX);
                TextView tvValueY = (TextView) findViewById(R.id.tvAccY);
                TextView tvValueZ = (TextView) findViewById(R.id.tvAccZ);
                TextView tvTotal = findViewById(R.id.tvTotalForce);
                TextView tvCount = findViewById(R.id.tvCount);
                tvValueX.setText(String.valueOf(tmpX));
                tvValueY.setText(String.valueOf(tmpY));
                tvValueZ.setText(String.valueOf(tmpZ));
                tvTotal.setText(String.valueOf(force));
                tvCount.setText(String.valueOf(mStepCount));
            }
        });
    }

    public synchronized void onResume() {
        super.onResume();
        if (mAccSensor != null)
            mAccSensor.startListening();
    }

    public synchronized void onPause() {
        if (mAccSensor != null)
            mAccSensor.stopListening();
        super.onPause();
    }
}
