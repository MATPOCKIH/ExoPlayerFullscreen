package io.faceter.exoplayerfullscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.faceter.util.PlayerViewManager;
import io.faceter.view.PlayerHolderView;

public class MainActivity extends AppCompatActivity {

    private List<PlayerHolderView> playerHolders = new ArrayList<>();
    private List<TextView> links = new ArrayList<>();

    private List<String> mVideoUrls = new ArrayList<>(
            Arrays.asList(
                    //"http://10.110.3.30/api/Playlists/6a3ecad7-e744-446f-9341-0e0ba834de63?from=2018-09-20&to=2018-09-21"
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/TearsOfSteel.m3u8",
                    "http://redirector.c.youtube.com/videoplayback?id=604ed5ce52eda7ee&itag=22&source=youtube&sparams=ip,ipbits,expire,source,id&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=513F28C7FDCBEC60A66C86C9A393556C99DC47FB.04C88036EEE12565A1ED864A875A58F15D8B5300&key=ik0",
                    "https://html5demos.com/assets/dizzy.mp4"
                    //"https://cdn.faceter.io/hls/ab196789-8876-4854-82f3-087e5682d013",
                      //"https://cdn.faceter.io/hls/65d1c673-6a63-44c8-836b-132449c9462a"
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerHolders.add((PlayerHolderView) findViewById(R.id.holder1));
        playerHolders.add((PlayerHolderView) findViewById(R.id.holder2));
        playerHolders.add((PlayerHolderView) findViewById(R.id.holder3));

        links.add((TextView) findViewById(R.id.title1));
        links.add((TextView) findViewById(R.id.title2));
        links.add((TextView) findViewById(R.id.title3));
    }


    @Override
    public void onResume() {
        super.onResume();
        int i = 0;
        for (final String videoUrl : mVideoUrls) {
            playerHolders.get(i).setupPlayerView(videoUrl);
            playerHolders.get(i).setOnUserInteractionListener(this);

            links.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onVideoTitleClicked(videoUrl);
                }
            });

            i++;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (String videoUrl : mVideoUrls) {
            PlayerViewManager.getInstance(videoUrl).goToBackground();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (String videoUrl : mVideoUrls) {
            PlayerViewManager.getInstance(videoUrl).releaseVideoPlayer();
        }
    }

    public void onVideoTitleClicked(String videoUrl) {
        Intent intent = new Intent(getBaseContext(), DetailActivity.class);
        intent.putExtra(PlayerViewManager.EXTRA_VIDEO_URI, videoUrl);
        startActivity(intent);
    }
}
