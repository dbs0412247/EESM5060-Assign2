package ece.course.eesm5060_assign2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class DrawingView extends SurfaceView {

    Paint mPaintRectRed;
    Paint mPaintRectYellow;
    Paint mPaintRectGreen;
    Paint mPaintLineBlue;
    Paint mPaintBall;

    RectF mRectRedTop; // Top Red Rectangle
    RectF mRectRedBottom; // Bottom Red Rectangle
    RectF mRectYellowTop; // Top Yellow Rectangle
    RectF mRectYellowBottom; // Bottom Yellow Rectangle
    RectF mRectGreen; // Center Green Rectangle

    Handler mHandler;

    float mBallHeight; // should be between -1 and 1. 0 - center, -1 at top, 1 is at bottom
    float mBallRadius;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        this.setBackgroundColor(Color.BLACK);

        mPaintRectRed = new Paint();
        mPaintRectRed.setColor(Color.RED);
        mPaintRectRed.setStyle(Paint.Style.FILL);

        mPaintRectYellow = new Paint();
        mPaintRectYellow.setColor(Color.YELLOW);
        mPaintRectYellow.setStyle(Paint.Style.FILL);

        mPaintRectGreen = new Paint();
        mPaintRectGreen.setColor(Color.GREEN);
        mPaintRectGreen.setStyle(Paint.Style.FILL);

        mPaintLineBlue = new Paint();
        mPaintLineBlue.setColor(Color.BLUE);
        mPaintLineBlue.setStrokeWidth(1f);

        mPaintBall = new Paint();
        mPaintBall.setColor(Color.GRAY);
        mPaintBall.setStyle(Paint.Style.FILL);

        mRectRedTop = new RectF();
        mRectRedBottom = new RectF();
        mRectYellowTop = new RectF();
        mRectYellowBottom = new RectF();
        mRectGreen = new RectF();

        mBallHeight = 0f;
        mBallRadius = 0f;
    }

    public void setHandler(Handler handler) { mHandler = handler; }

    public void setBallHeight(float newBallHeight) {
        mBallHeight =
                (newBallHeight > 1f? 1f :
                    newBallHeight < -1f? -1f : newBallHeight);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (canvas == null)
            return;

        drawStuff(canvas);
    }

    public void drawStuff(Canvas canvas) {
        if (canvas == null)
            return;

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        // Common for all rects
        float rectLeft = canvasWidth * 3f / 7f;
        float rectRight = canvasWidth * 4f / 7f;

        mRectRedTop.left = rectLeft;
        mRectRedTop.right = rectRight;
        mRectRedTop.top = 0f;
        mRectRedTop.bottom = canvasHeight * 2f / 11f;

        mRectYellowTop.left = rectLeft;
        mRectYellowTop.right = rectRight;
        mRectYellowTop.top = mRectRedTop.bottom;
        mRectYellowTop.bottom = canvasHeight * 4f / 11f;

        mRectGreen.left = rectLeft;
        mRectGreen.right = rectRight;
        mRectGreen.top = mRectYellowTop.bottom;
        mRectGreen.bottom = canvasHeight * 7f / 11f;

        mRectYellowBottom.left = rectLeft;
        mRectYellowBottom.right = rectRight;
        mRectYellowBottom.top = mRectGreen.bottom;
        mRectYellowBottom.bottom = canvasHeight * 9f / 11f;

        mRectRedBottom.left = rectLeft;
        mRectRedBottom.right = rectRight;
        mRectRedBottom.top = mRectYellowBottom.bottom;
        mRectRedBottom.bottom = canvasHeight;

        float lineXStart = canvasWidth / 3f;
        float lineXEnd = canvasWidth * 2f / 3f;
        float lineY = canvasHeight / 2f;

        mBallRadius = canvasHeight * 0.8f / 11f;
        float drawnBallHeight = (1f + mBallHeight) * canvasHeight / 2f;

        // Draw top red rectangle
        canvas.drawRect(mRectRedTop, mPaintRectRed);
        canvas.drawRect(mRectYellowTop, mPaintRectYellow);
        canvas.drawRect(mRectGreen, mPaintRectGreen);
        canvas.drawRect(mRectYellowBottom, mPaintRectYellow);
        canvas.drawRect(mRectRedBottom, mPaintRectRed);
        canvas.drawLine(lineXStart, lineY, lineXEnd, lineY, mPaintLineBlue);
        canvas.drawCircle(canvasWidth/2, drawnBallHeight, mBallRadius, mPaintBall);
    }
}
