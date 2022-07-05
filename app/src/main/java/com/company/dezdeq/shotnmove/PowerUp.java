package com.company.dezdeq.shotnmove;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PowerUp {

    private double x;
    private double y;
    private int r = 3;
    private Paint paint;

    private int type;

    public PowerUp(int type,double x, double y){
        this.x = x;
        this.y = y;
        this.type = type;
        paint = new Paint();
        if(type == 1) {
            r = 5;
            paint.setColor(Color.rgb(0,0,0));
        }
        if(type == 2 ){
            r = 7;
            paint.setColor(Color.YELLOW);
        }
        if(type == 3){
            r = 8;
            paint.setColor(Color.WHITE);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public boolean update() {
        y+=2;

        if(y > MainActivity.HEIGHT + r) {
            return true;
        }

        return false;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect((float)(x-r),(float)(y-r),(float)(x+r),(float)(y+r),paint);
    }

    public int getType() {
        return type;
    }
}
