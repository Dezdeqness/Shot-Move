package com.company.dezdeq.shotnmove;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bullet {

    private double x;
    private double y;
    private int r;

    private double dx;
    private double dy;
    private double rad;
    private double speed;

    public Bullet(double angle, int x, int y) {
        this.x = x;
        this.y = y;
        r = 3;

        speed = 10;
        rad = Math.toRadians(angle);
        dx = Math.cos(rad) * speed;
        dy = Math.sin(rad) * speed;

    }

    public boolean update() {
        x += dx;
        y += dy;
        if(x < -r || x > MainActivity.WIDTH + r ||
                y < -r || y > MainActivity.HEIGHT +r) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {

        Paint paint = new Paint();
        Paint paint1 = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        paint1.setStyle(Paint.Style.STROKE);
        paint1.setColor(Color.BLACK);
        paint1.setStrokeWidth(3);
        paint1.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawCircle( (float)(x-r),(float)(y - r),2*r,paint);
        canvas.drawCircle( (float)(x-r),(float)(y - r),2*r,paint1);
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public double getR() {
        return r;
    }
}
