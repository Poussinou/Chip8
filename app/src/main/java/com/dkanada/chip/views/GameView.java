package com.dkanada.chip.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dkanada.chip.async.GameThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    GameThread gameThread;
    Paint paint;

    byte[][] display;

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setWillNotDraw(false);

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);

        gameThread = new GameThread(this);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    public void update(byte[][] array) {
        this.display = array;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if (display != null) {
            double stepX = width / display.length;
            double stepY = height / display[0].length;

            for (int x = 0; x < display.length; x++) {
                for (int y = 0; y < display[1].length; y++) {
                    if (display[x][y] == 1) {
                        float startX = (float) stepX * x;
                        float startY = (float) stepY * y;
                        float endX = (float) stepX * (x + 1);
                        float endY = (float) stepY * (y + 1);
                        canvas.drawRect(startX, startY, endX, endY, paint);
                    }
                }
            }
        } else {
            float startX = (width / 2) - 100;
            float startY = (height / 2) - 100;
            float endX = (width / 2) + 100;
            float endY = (height / 2) + 100;
            canvas.drawRect(startX, startY, endX, endY, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 2;
        int height = 1;

        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int calculatedHeight = originalWidth * height / width;

        int finalWidth;
        int finalHeight;

        if (calculatedHeight > originalHeight) {
            finalWidth = originalHeight * width / height;
            finalHeight = originalHeight;
        } else {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }

        int measureWidth = MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY);
        int measureHeight = MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY);

        super.onMeasure(measureWidth, measureHeight);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
