package io.faceter.exoplayerfullscreen;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ui.PlayerView;

import io.faceter.util.PlayerViewManager;
import io.faceter.view.PlayerHolderView;

public class DetailActivity extends AppCompatActivity {

    private PlayerHolderView playerHolderView;
    private PlayerView exoPlayerView;
    private FrameLayout frame;
    private NestedScrollView scrollView;
    private String videoUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        videoUrl = getIntent().getStringExtra(PlayerViewManager.EXTRA_VIDEO_URI);

        playerHolderView = findViewById(R.id.player_holder);

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

       // exoPlayerView = findViewById(R.id.exo_player);
        /*
        exoPlayerView.findViewById(R.id.exo_play)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlayerViewManager.getInstance(videoUrl).playPlayer();
                    }
                });

        exoPlayerView.findViewById(R.id.exo_pause)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlayerViewManager.getInstance(videoUrl).pausePlayer();
                    }
                });*/

    }

    @Override
    public void onResume() {
        super.onResume();
        playerHolderView.setupPlayerView(videoUrl);
    }

    @Override
    public void onPause() {
        super.onPause();
        PlayerViewManager.getInstance(videoUrl).goToBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ExoPlayerViewManager.getInstance(videoUrl).releaseVideoPlayer();
    }
/*
    private void setupPlayerView(final PlayerView videoView, final String videoUrl) {
        PlayerViewManager.getInstance(videoUrl).preparePlayer(this, videoView);
        PlayerViewManager.getInstance(videoUrl).goToForeground();

        View controlView = videoView.findViewById(R.id.exo_controller);
        controlView.findViewById(R.id.exo_fullscreen_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), FullscreenVideoActivity.class);
                        intent.putExtra(PlayerViewManager.EXTRA_VIDEO_URI, videoUrl);
                        startActivity(intent);
                    }
                });
    }
*/
}