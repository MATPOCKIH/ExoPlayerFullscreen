package io.faceter.exoplayerfullscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ui.PlayerView;

import io.faceter.util.ExoPlayerViewManager;

public class DetailActivity extends AppCompatActivity {

    private PlayerView exoPlayerView;
    private FrameLayout frame;
    private NestedScrollView scrollView;
    private String videoUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        setupWindowAnimations();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        videoUrl = getIntent().getStringExtra(ExoPlayerViewManager.EXTRA_VIDEO_URI);

        frame = findViewById(R.id.player_frame);

        scrollView = findViewById(R.id.content_scroll);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                ViewGroup.LayoutParams params = frame.getLayoutParams();

                float persent = Math.min(Math.max(600f - scrollY, 400f), 600f) / 600f;

                params.width = Math.round(1080 * persent);
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                frame.setLayoutParams(params);

            }
        });

        exoPlayerView = findViewById(R.id.exo_player);
        exoPlayerView.findViewById(R.id.exo_play)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExoPlayerViewManager.getInstance(videoUrl).playPlayer();
                    }
                });

        exoPlayerView.findViewById(R.id.exo_pause)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExoPlayerViewManager.getInstance(videoUrl).pausePlayer();
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        setupPlayerView(exoPlayerView, videoUrl);
        exoPlayerView.hideController();
    }

    @Override
    public void onPause() {
        super.onPause();
        ExoPlayerViewManager.getInstance(videoUrl).goToBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ExoPlayerViewManager.getInstance(videoUrl).releaseVideoPlayer();
    }

    private void setupPlayerView(final PlayerView videoView, final String videoUrl) {
        ExoPlayerViewManager.getInstance(videoUrl).prepareExoPlayer(this, videoView);
        ExoPlayerViewManager.getInstance(videoUrl).goToForeground();

        View controlView = videoView.findViewById(R.id.exo_controller);
        controlView.findViewById(R.id.exo_fullscreen_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), FullscreenVideoActivity.class);
                        intent.putExtra(ExoPlayerViewManager.EXTRA_VIDEO_URI, videoUrl);
                        startActivity(intent);
                    }
                });
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }
}