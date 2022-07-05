package com.company.dezdeq.shotnmove;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;

public class RectPlayer implements GameObject{

    private Rect mRect;
    private long firingDelay;
    private long firingTimer;
    private boolean recovering;
    private long recoveryTimer;
    private int score;
    private int lives;
    private int power;
    private int powerLevel;
    private int r;
    private int x;
    private int y;

    private boolean firing;

    public RectPlayer(int x1,int y1, int color) {
        mRect = new Rect();
        r = 25;
        x = x1;
        y = y1;
        mRect.set(x-r,y-r,x+r,y+r);
        power = 1;
        powerLevel = 1;
        firingTimer = System.nanoTime();
        firingDelay = 100;
        firing = true;
        recovering = false;
        recoveryTimer = 0;
        lives = 3;
        score = 0;
    }

    @Override
    public void draw(Canvas canvas) {

        if(recovering) {
            Paint paint = new Paint();
            Paint paint1 = new Paint();
            paint.setColor(Color.rgb(255, 0, 0));
            paint.setStyle(Paint.Style.FILL);

            paint1.setStyle(Paint.Style.STROKE);
            paint1.setColor(Color.BLACK);
            paint1.setStrokeWidth(6);
            paint1.setStrokeCap(Paint.Cap.ROUND);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawCircle((float)(x-r),(float)(y - r),2*r,paint);
            canvas.drawCircle((float)(x-r),(float)(y - r),2*r,paint1);
        }*/

            canvas.drawRect(mRect,paint);
            canvas.drawRect(mRect,paint1);
        } else {
            Paint paint = new Paint();
            Paint paint1 = new Paint();
            paint.setColor(Color.rgb(218, 165, 32));
            paint.setStyle(Paint.Style.FILL);

            paint1.setStyle(Paint.Style.STROKE);
            paint1.setColor(Color.BLACK);
            paint1.setStrokeWidth(6);
            paint1.setStrokeCap(Paint.Cap.ROUND);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawCircle((float)(x-r),(float)(y - r),2*r,paint);
            canvas.drawCircle((float)(x-r),(float)(y - r),2*r,paint1);
        }*/

            canvas.drawRect(mRect,paint);
            canvas.drawRect(mRect,paint1);
        }
    }

    @Override
    public void update() {

    }

    public void update(Point point){
        mRect.set(point.x - mRect.width()/2, point.y - mRect.height()/2, point.x + mRect.width()/2,
                point.y + mRect.height()/2);

        if(firing) {
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if(elapsed > firingDelay) {
                firingTimer = System.nanoTime();

                if(powerLevel == 1) {
                    GamePanel.bullets.add(new Bullet(270,point.x,point.y));
                }
                if(powerLevel == 2) {
                    GamePanel.bullets.add(new Bullet(270,point.x + 15,point.y));
                    GamePanel.bullets.add(new Bullet(270,point.x - 15,point.y));
                }
                if(powerLevel == 3) {
                    GamePanel.bullets.add(new Bullet(270,point.x + 15,point.y));
                    GamePanel.bullets.add(new Bullet(270,point.x,point.y));
                    GamePanel.bullets.add(new Bullet(270,point.x - 15,point.y));
                }
                if(powerLevel == 4) {
                    GamePanel.bullets.add(new Bullet(250,point.x + 25,point.y));
                    GamePanel.bullets.add(new Bullet(260,point.x + 15,point.y));
                    GamePanel.bullets.add(new Bullet(270,point.x,point.y));
                    GamePanel.bullets.add(new Bullet(280,point.x - 15,point.y));
                    GamePanel.bullets.add(new Bullet(290,point.x - 25,point.y));
                }
                if(powerLevel == 5) {
                    GamePanel.bullets.add(new Bullet(240,point.x + 35,point.y));
                    GamePanel.bullets.add(new Bullet(250,point.x + 25,point.y));
                    GamePanel.bullets.add(new Bullet(260,point.x + 15,point.y));
                    GamePanel.bullets.add(new Bullet(270,point.x,point.y));
                    GamePanel.bullets.add(new Bullet(280,point.x - 15,point.y));
                    GamePanel.bullets.add(new Bullet(290,point.x - 25,point.y));
                    GamePanel.bullets.add(new Bullet(300,point.x - 35,point.y));
                }
                if(powerLevel == 6) {
                    GamePanel.bullets.add(new Bullet(230,point.x + 45,point.y));
                    GamePanel.bullets.add(new Bullet(240,point.x + 35,point.y));
                    GamePanel.bullets.add(new Bullet(250,point.x + 25,point.y));
                    GamePanel.bullets.add(new Bullet(260,point.x + 15,point.y));
                    GamePanel.bullets.add(new Bullet(270,point.x,point.y));
                    GamePanel.bullets.add(new Bullet(280,point.x - 15,point.y));
                    GamePanel.bullets.add(new Bullet(290,point.x - 25,point.y));
                    GamePanel.bullets.add(new Bullet(300,point.x - 35,point.y));
                    GamePanel.bullets.add(new Bullet(310,point.x - 45,point.y));
                }

                firingTimer = System.nanoTime();
            }
        }

        long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
        if(elapsed > 2000) {
            recovering = false;
            recoveryTimer = 0;
        }
    }

    public int getScore() {
        return score;
    }

    public int addScore(int i) {
        return score+=i;
    }

    public void loseLife() {
        lives--;
        recovering = true;
        recoveryTimer = System.nanoTime();
    }

    public boolean isRecovering() {
        return recovering;
    }

    public int getLives() {
        return lives;
    }

    public void gainLive() {
        lives++;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public int getPower() { return power;}

    public void increasePower(int i) {
        power+=i;
        checkPower();

    }

    public void checkPower() {
        if(power == 10) {
            powerLevel = 2;
        }

        if(power == 100) {
            powerLevel = 3;
        }

        if(power == 1000) {
            powerLevel = 4;
        }

        if(power == 10000) {
            powerLevel = 5;
        }

        if(power == 100000) {
            powerLevel = 6;
        }
    }

    public int getR() {
        return r;
    }
}
