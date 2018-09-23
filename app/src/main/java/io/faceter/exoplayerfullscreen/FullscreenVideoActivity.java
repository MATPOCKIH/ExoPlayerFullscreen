package io.faceter.exoplayerfullscreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.faceter.util.PlayerViewManager;
import io.faceter.view.PlayerHolderView;

// Fullscreen related code taken from Android Studio blueprint
public class FullscreenVideoActivity extends AppCompatActivity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of
            // API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private String mVideoUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_video);

        mContentView = findViewById(R.id.enclosing_layout);

        PlayerHolderView playerHolderView = findViewById(R.id.player_holder);
        playerHolderView.setResizeModeFill(false);

        mVideoUri = getIntent().getStringExtra(PlayerViewManager.EXTRA_VIDEO_URI);

        PlayerViewManager.getInstance(mVideoUri).preparePlayer(playerHolderView);
/*
        // Set the fullscreen button to "close fullscreen" icon
        View controlView = playerView.findViewById(R.id.exo_controller);
        ImageView fullscreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenIcon.setImageResource(R.drawable.exo_controls_fullscreen_exit);

        controlView.findViewById(R.id.exo_fullscreen_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        controlView.findViewById(R.id.exo_play)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlayerViewManager.getInstance(mVideoUri).playPlayer();
                    }
                });

        controlView.findViewById(R.id.exo_pause)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlayerViewManager.getInstance(mVideoUri).pausePlayer();
                    }
                });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        PlayerViewManager.getInstance(mVideoUri).goToForeground();
    }

    @Override
    public void onPause() {
        super.onPause();
        PlayerViewManager.getInstance(mVideoUri).goToBackground();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide();
    }

    private void hide() {
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide() {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 100);
    }
}