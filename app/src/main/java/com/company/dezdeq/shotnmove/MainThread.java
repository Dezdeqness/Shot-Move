package com.company.dezdeq.shotnmove;

import android.graphics.Canvas;
import android.os.Looper;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    public static final int MAX_FPS = 60;
    private double averageFps;
    private final SurfaceHolder mSurfaceHolder;
    private GamePanel mGamePanel;

    private boolean running;
    public static Canvas sCanvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        mSurfaceHolder = surfaceHolder;
        mGamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMills;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(running) {
            startTime = System.nanoTime();
            sCanvas = null;

            try {
                sCanvas = this.mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    this.mGamePanel.update();
                    this.mGamePanel.draw(sCanvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(sCanvas != null) {
                    try {
                       mSurfaceHolder.unlockCanvasAndPost(sCanvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMills = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMills;
            try {
                if(waitTime > 0) {
                    this.sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS) {
                averageFps = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFps);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }
}
