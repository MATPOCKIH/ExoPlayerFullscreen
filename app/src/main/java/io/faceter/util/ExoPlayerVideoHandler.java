package io.faceter.util;

import android.content.Context;
import android.net.Uri;
import android.view.TextureView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerVideoHandler {

    private static ExoPlayerVideoHandler instance;

    public static ExoPlayerVideoHandler getInstance(){
        if (instance == null){
            instance = new ExoPlayerVideoHandler();
        }
        return instance;
    }

    private SimpleExoPlayer player;
    private Uri playerUri;
    private boolean isPlayerPlaying;
    private long currentPosition;

    public ExoPlayerVideoHandler() {
    }

    public void prepareExoPlayerForUri(Context context, Uri uri, PlayerView exoPlayerView, TextureView frame){
        if(uri != null && !uri.equals(playerUri)){
            currentPosition = 0;
        }

        if(context != null && uri != null && exoPlayerView != null){

            if(!uri.equals(playerUri) || player == null){
                // Create a new player if the player is null or
                // we want to play a new video
                playerUri = uri;

                player =
                        ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());

                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, "exoplayerfullscreen"));

                MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
                /*MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);*/
                isPlayerPlaying = true;
                player.prepare(mediaSource);
            }
            //player.setVideoTextureView(frame);
            //player.seekTo(player.getCurrentPosition() + 1);
            exoPlayerView.setPlayer(player);
        }
    }

    public void releaseVideoPlayer(){
        if (player != null){
            player.release();
        }
        player = null;
    }

    public void goToBackground(){
        if(player != null){
            isPlayerPlaying = player.getPlayWhenReady();
            player.setPlayWhenReady(false);
            currentPosition = player.getCurrentPosition();
        }
    }

    public void goToForeground(){
        if(player != null){
            player.setPlayWhenReady(isPlayerPlaying);
        }
    }
}
