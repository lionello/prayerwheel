package com.lunesu.prayerwheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ColorView extends View {
    private Paint mCenterPaint;
    
    public void setColor(int color) {
    	mCenterPaint.setColor(color);
    }
    
    public ColorView(Context context, AttributeSet attrs) {
    	super(context, attrs);
    	
    	//int color = attrs.getAttributeIntValue("", "color", -1);
    	
        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mCenterPaint.setColor(color);
        mCenterPaint.setStrokeWidth(5);        	
    }
    
    @Override 
    protected void onDraw(Canvas canvas) {
        canvas.translate(CENTER_RADIUS, CENTER_RADIUS);
        canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(CENTER_RADIUS*2, CENTER_RADIUS*2);
    }

    private static final int CENTER_RADIUS = 16;
}
