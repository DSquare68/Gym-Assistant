package com.example.daniel.extraview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.daniel.procedures.Units;

/**
 * Created by Daniel on 2017-08-18.
 */

public class Scale extends LinearLayout {
    double startValue;
    double step;
    double maxValue;
    TextPaint mTextPaint;
    public Scale(Context context, double poczWartość, double coIleWzrasta, double maxWartość) {
        super(context);
        this.startValue =poczWartość;
        this.step =coIleWzrasta;
        this.maxValue =maxWartość;
        setBackgroundColor(Color.WHITE);
        init();
    }
    StaticLayout mStaticLayout;
    private void init() {
        setLayoutParams(new LinearLayout.LayoutParams(Units.dpToPx(getContext(),30), ViewGroup.LayoutParams.MATCH_PARENT));
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(Units.dpToPx(getContext(),20));
        mTextPaint.setColor(0xFF000000);



    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int height = getHeight();
        int scale = (int) (((height - Diagram.bottomMargin) / maxValue)* step);
        Paint paint = new Paint();

        //canvas.drawLine(0, 0, getWidth(), getHeight(), paint);
        //canvas.drawLine(100,100,getWidth(),getHeight(),paint);
        paint.setColor(0x00FF00FF00); // zielony
        paint.setStrokeWidth(Units.dpToPx(getContext(), 5));
        paint.setTextSize(Units.dpToPx(getContext(), 5));
        paint.setStyle(Paint.Style.FILL);
        int i = 1;
        // default to a single line of text

        while (step * i <= maxValue) {//
            canvas.save();
            canvas.translate(0, height - i * scale - Diagram.bottomMargin);
            int width = (int) mTextPaint.measureText(String.valueOf((int) (step * i + startValue - 1)));
            mStaticLayout = new StaticLayout(String.valueOf((int) (step * i + startValue - 1)), mTextPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
            mStaticLayout.draw(canvas);
            i++;
            canvas.restore();
        }
    }
}
