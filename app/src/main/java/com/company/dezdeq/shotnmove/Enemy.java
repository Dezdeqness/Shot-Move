package com.company.dezdeq.shotnmove;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

public class Enemy {

    private double x;
    private double y;
    private int r;
    private int color;

    private double dx;
    private double dy;
    private double rad;
    private double speed;

    private int health;
    private int type;
    private int rank;
    private boolean hit;
    private long hitTimer;

    private boolean ready;
    private boolean dead;

    public Enemy(int type, int rank) {
        this.type = type;
        this.rank = rank;

        if(type == 1) {
            if(rank == 1) {
                speed = 2;
                r = 32;
                health = 1;
                color = Color.rgb(205, 133, 63);
            }

            if(rank == 2) {
                speed = 2;
                r = 32;
                health = 2;
                color = Color.rgb(205, 133, 63);
            }

            if(rank == 3) {
                speed = 1.5;
                r = 75;
                health = 3;
                color = Color.rgb(205, 133, 63);
            }

            if(rank == 4) {
                speed = 1.5;
                r = 75;
                health = 4;
                color = Color.rgb(205, 133, 63);
            }
        }

        if(type == 2) {
            if(rank == 1) {
                speed = 3;
                r = 32;
                health = 2;
                color = Color.rgb(72, 61, 139);
            }
        }

        if(type == 3) {
            if(rank == 1) {
                speed = 3;
                r = 32;
                health = 2;
                color = Color.rgb(72, 61, 139);
            }
        }

        x = Math.random() * MainActivity.WIDTH / 2 + MainActivity.WIDTH /4;
        y = -r;

        double angle = Math.random() *140 +20;
        rad = Math.toRadians(angle);

        dx = Math.cos(rad) * speed;
        dy = Math.sin(rad) * speed;

        ready = false;
        dead = false;
        hit = false;
        hitTimer = 0;
    }

    public void explode() {
        if(rank > 1) {
            int amount = 0;
            if(type == 1) {
                amount = 3;
            }

            for(int i = 0; i < amount; i ++) {
                Enemy e = new Enemy(getType(),getRank()-1);
                e.x = this.x;
                e.y = this.y;
                double angle = 0;
                if(!ready) {
                    angle = Math.random()*140+20;
                } else {
                    angle = Math.random()*360;
                }
                e.rad = Math.toRadians(angle);
                GamePanel.enemies.add(e);
            }
        }
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

    public void hit() {
        health--;
        if(health <= 0) {
            dead = true;
        }
        hit = true;
        hitTimer = System.nanoTime();
    }

    public boolean isDead(){
        return dead;
    }

    public void update() {
        x +=dx;
        y +=dy;

        if(!ready) {
            if( x > r && x < MainActivity.WIDTH - r &&
                    y > r && y < MainActivity.HEIGHT - r) {
                ready = true;
            }
        }

        if(x < r && dx < 0) dx = -dx;
        if(y < r && dy < 0) dy = -dy;
        if(x > MainActivity.WIDTH - r && dx > 0) dx = -dx;
        //if(y > MainActivity.HEIGHT - r && dy > 0) dy = -dy;

        if(hit) {
            long elapsed = (System.nanoTime() - hitTimer)/1000000;
            if(elapsed > 50) {
                hit = false;
                hitTimer = 0;

            }
        }
    }

    public void draw(Canvas canvas){

        if (hit) {
            Paint paint = new Paint();
            Paint paint1 = new Paint();
            paint.setColor(Color.rgb(128, 0, 0));
            paint.setStyle(Paint.Style.FILL);

            paint1.setStyle(Paint.Style.STROKE);
            paint1.setColor(Color.BLACK);
            paint1.setStrokeWidth(8);
            paint1.setStrokeCap(Paint.Cap.ROUND);

            //paint.setStyle(Paint.Style.FILL);
            // paint.setAntiAlias(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //canvas.drawRect((float) (x-r),(float) (y-r),(float) (x+r),(float) (y+r),paint);
                canvas.drawCircle((float)(x),(float)(y),(float)r,paint);
                canvas.drawCircle((float)(x),(float)(y),(float)r,paint1);
            }
        } else {
            Paint paint = new Paint();
            Paint paint1 = new Paint();
            paint.setColor(Color.rgb(205, 92, 92));
            paint.setStyle(Paint.Style.FILL);

            paint1.setStyle(Paint.Style.STROKE);
            paint1.setColor(Color.BLACK);
            paint1.setStrokeWidth(8);
            paint1.setStrokeCap(Paint.Cap.ROUND);

            //paint.setStyle(Paint.Style.FILL);
            // paint.setAntiAlias(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //canvas.drawRect((float) (x-r),(float) (y-r),(float) (x+r),(float) (y+r),paint);
                canvas.drawCircle((float)(x),(float)(y),(float)r,paint);
                canvas.drawCircle((float)(x),(float)(y),(float)r,paint1);
            }
        }

    }

    public int getType() {
        return type;
    }

    public int getRank() {
        return rank;
    }
}
