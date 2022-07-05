package com.company.dezdeq.shotnmove;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.fragment.app.Fragment;

public class GameActivity extends SingleFragmentActivity {

    private GameFragment mGameFragment;

    @Override
    protected Fragment createFragment() {


        getSupportActionBar().hide();
        MyTask mt = new MyTask();
        mt.execute();
        mGameFragment = new GameFragment();
        return mGameFragment;
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!GamePanel.mGameOver) {
                System.out.println("Game is not over!");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update();
            return null;
        }
    }


    public void update() {
        Intent intent = new Intent();
        intent.putExtra("score", mGameFragment.result());
        setResult(RESULT_OK, intent);
        finish();
    }

}
