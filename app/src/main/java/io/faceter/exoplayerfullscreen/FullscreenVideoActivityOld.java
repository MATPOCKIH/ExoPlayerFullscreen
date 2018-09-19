package io.faceter.exoplayerfullscreen;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerView;

import io.faceter.util.ExoPlayerVideoHandler;

public class FullscreenVideoActivityOld extends AppCompatActivity {

    private String videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/TearsOfSteel.m3u8";
    private boolean destroyVideo = false;
    private PlayerView exoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
    }

    @Override
    protected void onResume(){
        super.onResume();
        exoPlayerView = findViewById(R.id.exoplayer_video);
        TextureView frame = findViewById(R.id.media_frame);
        ExoPlayerVideoHandler.getInstance()
                .prepareExoPlayerForUri(getApplicationContext(),
                        Uri.parse(videoUrl), exoPlayerView, frame);
        ExoPlayerVideoHandler.getInstance().goToForeground();

        findViewById(R.id.exo_fullscreen_button).setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        destroyVideo = false;
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed(){
        destroyVideo = false;
        super.onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(destroyVideo){
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
    }
}
