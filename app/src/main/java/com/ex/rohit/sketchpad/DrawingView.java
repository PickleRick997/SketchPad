package com.ex.rohit.sketchpad;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint,canvasPaint;
    private int paintColor=0xFF6600;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private float brushSize,lastBrushSize;
    private boolean erase = false;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {

        //get drawing area for interaction

        brushSize= getResources().getInteger(R.integer.medium_size);
        lastBrushSize=brushSize;

        drawPath= new Path();
        drawPaint=new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint=new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh)
    {
        super.onSizeChanged( w,h,oldw,oldh);
        canvasBitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        drawCanvas= new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(canvasBitmap,0,0, canvasPaint);
        canvas.drawPath(drawPath,drawPaint);

        //Adding  Border to the custom view
        Paint paint= new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);
        canvas.drawRect(0,0,getWidth(),1225,paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent  event)
    {
        //detect user touch
        float touchX=event.getX();
        float touchY=event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath,drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setColor(String newColor)
    {
        invalidate();
        paintColor= Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void startNew() {
        drawCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void setBrushSize(float newSize)
    {
        float pixelSize= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize,getResources().getDisplayMetrics());
        brushSize=pixelSize;
        drawPaint.setStrokeWidth(brushSize);

    }

    public void setLastBrushSize(float lastSize)
    {
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize()
    {
        return lastBrushSize;
    }

    public void setErase(boolean isErase)
    {
        //set erase true or false
        erase=isErase;
        if(erase)
        {
            drawPaint.setColor(Color.WHITE);
        }
        else drawPaint.setXfermode(null);
    }

}