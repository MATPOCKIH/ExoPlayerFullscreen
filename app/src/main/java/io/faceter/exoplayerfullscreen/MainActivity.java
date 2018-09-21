package io.faceter.exoplayerfullscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.faceter.util.ExoPlayerViewManager;

public class MainActivity extends AppCompatActivity {

    private List<PlayerView> players = new ArrayList<>();

    private List<String> mVideoUrls = new ArrayList<>(
            Arrays.asList(
                    //"http://10.110.3.30/api/Playlists/6a3ecad7-e744-446f-9341-0e0ba834de63?from=2018-09-20&to=2018-09-21"
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/TearsOfSteel.m3u8",
                    "http://redirector.c.youtube.com/videoplayback?id=604ed5ce52eda7ee&itag=22&source=youtube&sparams=ip,ipbits,expire,source,id&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=513F28C7FDCBEC60A66C86C9A393556C99DC47FB.04C88036EEE12565A1ED864A875A58F15D8B5300&key=ik0",
                    "https://html5demos.com/assets/dizzy.mp4"
                    //"https://cdn.faceter.io/hls/ab196789-8876-4854-82f3-087e5682d013",
                    //  "https://cdn.faceter.io/hls/65d1c673-6a63-44c8-836b-132449c9462a"
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        players.add((PlayerView) findViewById(R.id.player1));
        players.add((PlayerView) findViewById(R.id.player2));
        players.add((PlayerView) findViewById(R.id.player3));

    }


    @Override
    public void onResume() {
        super.onResume();
        int i = 0;
        for (String videoUrl : mVideoUrls) {
            ExoPlayerViewManager.getInstance(videoUrl).goToBackground();
            setupPlayerView(players.get(i), videoUrl);
            players.get(i).hideController();
            i++;
        }


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

        controlView.findViewById(R.id.exo_play)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExoPlayerViewManager.getInstance(videoUrl).playPlayer();
                    }
                });

        controlView.findViewById(R.id.exo_pause)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExoPlayerViewManager.getInstance(videoUrl).pausePlayer();
                    }
                });

        CardView card = (CardView) videoView.getParent();

        card.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                        intent.putExtra(ExoPlayerViewManager.EXTRA_VIDEO_URI, videoUrl);
                        startActivity(intent);
                    }
                });
    }
}
