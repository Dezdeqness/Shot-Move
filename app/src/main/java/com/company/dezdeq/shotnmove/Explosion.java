package com.company.dezdeq.shotnmove;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

public class Explosion {

    private double x;
    private double y;
    private int r;
    private int maxRadius;

    public Explosion(double x, double y, int r, int maxRadius) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.maxRadius = maxRadius;
    }

    public boolean update() {
        r++;
        if (r >= maxRadius) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawCircle((float)x-r+20,(float)y-r+25,2*r,paint);
        }
    }
}
