package com.company.dezdeq.shotnmove;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GameFragment extends Fragment {

    private GamePanel mGamePanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Fragment newInstance() {
        return new GameFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mGamePanel = new GamePanel(getContext());

        return mGamePanel;
    }

    public int result() {
        return mGamePanel.getPlayer().getScore();
    }



    @Override
    public void onResume() {
        super.onResume();

        Log.d("Fragment 1", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("Fragment 1", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment 1", "onStop");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GamePanel.mGameOver = true;
        Log.d("Fragment 1", "onDestroyView");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("Fragment 1", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;

        Log.d("Fragment 1", "onDetach");
    }
}
