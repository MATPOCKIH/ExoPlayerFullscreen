package io.faceter.exoplayerfullscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.TextureView;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.faceter.util.ExoPlayerViewManager;

public class MyActivity extends AppCompatActivity {

    private PlayerView exoPlayerView, exoPlayerView2;
    private TextureView frame;

    private List<String> mVideoUrls = new ArrayList<>(
            Arrays.asList(
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/TearsOfSteel.m3u8",
                    "https://storage.googleapis.com/exoplayer-test-media-1/gen-3/screens/dash-vod-single-segment/video-avc-baseline-480.mp4"
                    //"https://cdn.faceter.io/hls/ab196789-8876-4854-82f3-087e5682d013",
                  //  "https://cdn.faceter.io/hls/65d1c673-6a63-44c8-836b-132449c9462a"
            )
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        exoPlayerView = findViewById(R.id.exo_player);
        exoPlayerView2 = findViewById(R.id.exo_player2);
        frame = findViewById(R.id.media_frame);


        //setupPlayerView(exoPlayerView2, mVideoUrls.get(1));

        /*for (String videoUrl : mVideoUrls) {
            setupPlayerView(videoView, videoUrl);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        setupPlayerView(exoPlayerView, mVideoUrls.get(0));
        setupPlayerView(exoPlayerView2, mVideoUrls.get(1));
/*
        for (String videoUrl : mVideoUrls) {
            setupPlayerView(exoPlayerView, videoUrl);
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        for (String videoUrl : mVideoUrls) {
            ExoPlayerViewManager.getInstance(videoUrl).goToBackground();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (String videoUrl : mVideoUrls) {
            ExoPlayerViewManager.getInstance(videoUrl).releaseVideoPlayer();
        }
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

}