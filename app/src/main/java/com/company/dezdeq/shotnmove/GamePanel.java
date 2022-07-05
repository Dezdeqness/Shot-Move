package com.company.dezdeq.shotnmove;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

import static com.company.dezdeq.shotnmove.MainActivity.HEIGHT;
import static com.company.dezdeq.shotnmove.MainActivity.WIDTH;


class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

    public MainThread mThread;

    public RectPlayer getPlayer() {
        return player;
    }

    private RectPlayer player;
    private Point playerPoint;
    public static boolean mGameOver;
    public static ArrayList<Enemy> enemies;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<PowerUp> sPowerUps;
    public static ArrayList<Explosion> sExplosions;

    private int waveAmount = 1;
    private long waveStartTimer;
    private long waveStartTimerDiff;
    private int waveNumber;
    private int waveDelay = 2000;
    private boolean waveStart;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);
        mThread = new MainThread(getHolder(),this);
        player = new RectPlayer(75,75, Color.rgb(255,0,0));
        playerPoint = new Point(WIDTH/2,HEIGHT-100);
        mGameOver = false;
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        sPowerUps = new ArrayList<>();
        sExplosions = new ArrayList<>();

        waveStartTimer = 0;
        waveStartTimerDiff = 0;
        waveStart = true;
        waveNumber = 0;

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mThread = new MainThread(getHolder(),this);

        mThread.setRunning(true);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        boolean retry = true;
       // while(true) {
            try {
                mThread.setRunning(false);
                mThread.join();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            retry = false;
       // }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int motionEvent = event.getAction();
        if(motionEvent == MotionEvent.ACTION_MOVE)
        {
            playerPoint.set((int)event.getX(), (int)event.getY()-75);
            //player.setFiring(true);
        }

        return true;
        //return super.onTouchEvent(event);
    }

    public void update() {

        for(int i =0; i < bullets.size(); i++) {
            boolean remove = bullets.get(i).update();
            if(remove){
                bullets.remove(i);
                i--;
            }
        }

        for(int i = 0;i<bullets.size();i++) {
            Bullet b= bullets.get(i);
            double bx = b.getX();
            double by = b.getY();
            double br = b.getR();

            for(int j = 0;j<enemies.size();j++) {
                Enemy e = enemies.get(j);
                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();

                double dx = bx - ex;
                double dy = by - ey;
                double dist = Math.sqrt(dx *dx + dy* dy);

                if(dist < br + er) {
                    e.hit();
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }

        for(int i = 0;i <enemies.size();i++) {
            if(enemies.get(i).isDead()){
                Enemy e = enemies.get(i);

                double rand = Math.random();
                if(rand < 0.001) sPowerUps.add(new PowerUp(1,e.getX(),e.getY()));
                else if(rand < 0.0023) sPowerUps.add(new PowerUp(3,e.getX(),e.getY()));
                else if(rand < 0.013) sPowerUps.add(new PowerUp(2,e.getX(),e.getY()));



                player.addScore(e.getType() + e.getRank());
                enemies.remove(i);
                i--;
                e.explode();
                sExplosions.add(new Explosion(e.getX(),e.getY(),(int)e.getR(), (int)e.getR() +40));
            }
        }

        if(waveStartTimer == 0 && enemies.size() ==0) {
            waveNumber++;
            waveStart = false;
            waveStartTimer = System.nanoTime();
        } else {
            waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
            if(waveStartTimerDiff > waveDelay) {
                waveStart = true;
                waveStartTimer = 0;
                waveStartTimerDiff = 0;
            }
        }

        if(waveStart && enemies.size() == 0) {
            createNewEnemies();
        }

        for(int i = 0;i<sPowerUps.size();i++) {
            boolean remove = sPowerUps.get(i).update();
            if(remove){
                sPowerUps.remove(i);
                i--;
            }
        }

        for(int i = 0;i<sExplosions.size();i++) {
            boolean remove = sExplosions.get(i).update();
            if(remove){
                sExplosions.remove(i);
                i--;
            }
        }

        for(int i = 0;i<enemies.size();i++) {
            enemies.get(i).update();
        }

        if(!player.isRecovering()) {
            int px = playerPoint.x;
            int py = playerPoint.y;
            int pr = player.getR();
            for(int j = 0; j < enemies.size(); j++) {
                Enemy e = enemies.get(j);
                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();

                double dx = px - ex;
                double dy = py - ey;
                double dist = Math.sqrt(dx *dx + dy* dy);

                if(dist < pr + er) {
                    player.loseLife();
                }
            }
        }

        int px = playerPoint.x;
        int py = playerPoint.y;
        int pr = player.getR();
        for(int j = 0; j < sPowerUps.size(); j++) {
            PowerUp p = sPowerUps.get(j);
            double bx = p.getX();
            double by = p.getY();
            double br = p.getR();
            double dx = px - bx;
            double dy = py - by;
            double dist = Math.sqrt(dx *dx + dy* dy);

            if(dist < pr +br) {

                int type = p.getType();

                if(type == 1) {
                    player.gainLive();
                }
                if(type == 2) {
                    player.increasePower(1);
                }
                if(type == 3) {
                    player.increasePower(2);
                }

                sPowerUps.remove(j);

            }
        }
        player.update();
        player.update(playerPoint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(waveNumber%10 == 1) {
            canvas.drawColor(Color.rgb(255, 64, 129));
        }
        if(waveNumber%10 == 2) {
            canvas.drawColor(Color.rgb(100, 0, 255));
        }
        if(waveNumber%10 == 3) {
            canvas.drawColor(Color.rgb(255, 0, 255));
        }
        if(waveNumber%10 == 4) {
            canvas.drawColor(Color.GRAY);
        }
        if(waveNumber%10 == 5) {
            canvas.drawColor(Color.BLUE);
        }
        if(waveNumber%10 == 6) {
            canvas.drawColor(Color.MAGENTA);
        }
        if(waveNumber%10 == 7) {
            canvas.drawColor(Color.rgb(255,192,203));
        }

        if(waveNumber%10 == 8) {
            canvas.drawColor(Color.rgb(255,192,203));
        }

        if(waveNumber%10 == 9) {
            canvas.drawColor(Color.rgb(255,192,203));
        }

        if(waveNumber%10 == 0) {
            canvas.drawColor(Color.rgb(255,192,203));
        }

        for(int i = 0;i<enemies.size();i++) {
            if(enemies.get(i).getY() > MainActivity.HEIGHT - enemies.get(i).getR()) {
                enemies.remove(i);
            }
            enemies.get(i).draw(canvas);
        }

        for(int i = 0;i<sExplosions.size();i++) {
            sExplosions.get(i).draw(canvas);
        }

        for(int i = 0;i<sPowerUps.size();i++) {
            sPowerUps.get(i).draw(canvas);
        }

        for(int i =0; i < bullets.size(); i++) {
            bullets.get(i).draw(canvas);
        }

        if(waveStartTimer!=0){
            String s = "- W A V E   " + waveNumber + "   -";
            int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff));
            if(alpha > 255 || alpha < 0) alpha = 255;
            Paint paint = new Paint();
            paint.setTextSize(75);
            paint.setColor(Color.argb(alpha,75,0,130));
            canvas.drawText(s,WIDTH/2 - 220 ,HEIGHT /2,paint);
        }
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(60);
        paint.setColor(Color.BLACK);
        canvas.drawText("Live(s): " + player.getLives(), 20, 60, paint);
        canvas.drawText("Score: " + player.getScore(), WIDTH - 325,60,paint);
        if(player.getLives() <= 0) {
            Paint gameIsOver = new Paint();
            mGameOver = true;
            gameIsOver.setTextSize(75);
            gameIsOver.setColor(Color.rgb(75,0,130));
            canvas.drawText("Game is over!",360,860,gameIsOver);
            mThread.setRunning(false);
        }
        player.draw(canvas);
    }

    private void createNewEnemies() {
        enemies.clear();

        if(waveNumber%10 == 1) {
            for(int i = 0; i < 4*waveAmount; i++) {
                enemies.add(new Enemy(1,1));
            }

        }
        if(waveNumber%10 == 2) {
            for(int i = 0; i < 5*waveAmount; i++) {
                enemies.add(new Enemy(1,2));
            }

        }
        if(waveNumber%10 == 3) {
            for(int i = 0; i < 6*waveAmount; i++) {
                enemies.add(new Enemy(1,3));
            }

        }
        if(waveNumber%10 == 4) {
            for(int i = 0; i < 7*waveAmount; i++) {
                enemies.add(new Enemy(1,4));
            }

        }
        if(waveNumber%10 == 5) {
            for(int i = 0; i < 8*waveAmount; i++) {
                enemies.add(new Enemy(1,1));
            }

        }
        if(waveNumber%10 == 6) {
            for(int i = 0; i < 9*waveAmount; i++) {
                enemies.add(new Enemy(1,1));
            }

        }
        if(waveNumber%10 == 7) {
            for(int i = 0; i < 10*waveAmount; i++) {
                enemies.add(new Enemy(1,1));
            }

        }

        if(waveNumber%10 == 8) {
            for(int i = 0; i < 11*waveAmount; i++) {
                enemies.add(new Enemy(1,1));
            }

        }

        if(waveNumber%10 == 9) {
            for(int i = 0; i < 12*waveAmount; i++) {
                enemies.add(new Enemy(1,1));
            }

        }

        if(waveNumber%10 == 0) {
            for(int i = 0; i < 1*waveAmount; i++) {
                enemies.add(new Enemy(1,1));
            }

        }
    }

}
