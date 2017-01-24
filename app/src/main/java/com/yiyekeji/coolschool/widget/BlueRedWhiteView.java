package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.yiyekeji.coolschool.R;

/**
 * Created by lxl on 2017/1/24.
 */
public class BlueRedWhiteView extends View {
    private Paint paint;
    private Context context;
    public BlueRedWhiteView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public BlueRedWhiteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
    }
    int blue,red;
    private void init() {
        red=ContextCompat.getColor(context, R.color.theme_red);
        blue=ContextCompat.getColor(context, R.color.theme_blue);
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.theme_red));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }
    final int PIECE=15;
    private void drawLine(Canvas canvas) {
        float x=0,y=0,x1=0,y1=0,scanX;
        scanX=getWidth()/PIECE;
        int count=0;
        for (int j=0;j<getHeight();j++){
            count=0;
            for (int i=0;i<PIECE;i++){
                if (count==0){
                    paint.setColor(blue);
                }
                if (count==1){
                    paint.setColor(Color.WHITE);
                }
                if (count==2){
                    paint.setColor(red);
                }
                if (count==3){
                    paint.setColor(Color.WHITE);
                    count=-1;
                }
                x=scanX*i-j;
                x1=scanX*(i+1)-j;
                canvas.drawLine(x,j,x1,j,paint);
                count++;
            }
        }
    }
}
