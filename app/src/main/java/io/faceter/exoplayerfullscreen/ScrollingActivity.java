package io.faceter.exoplayerfullscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.exoplayer2.ui.PlayerView;

import io.faceter.util.ExoPlayerVideoHandler;

public class ScrollingActivity extends AppCompatActivity {

    private String videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/hls/TearsOfSteel.m3u8";
    private PlayerView exoPlayerView;
    private TextureView frame;
    private ImageButton fullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        exoPlayerView = findViewById(R.id.exo_player);
        frame = findViewById(R.id.media_frame);

        if (videoUrl != null && exoPlayerView != null) {
            ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUri(this, Uri.parse(videoUrl), exoPlayerView, frame);
            ExoPlayerVideoHandler.getInstance().goToForeground();
            exoPlayerView.requestFocus();
        }


        fullscreen = findViewById(R.id.exo_fullscreen_button);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFullscreen();

            }
        });
    }

    private void goToFullscreen(){
        Intent intent = new Intent(this,
                FullscreenVideoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }
}
