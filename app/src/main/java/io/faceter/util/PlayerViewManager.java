package io.faceter.util;

import android.content.Context;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import io.faceter.view.FaceterExoPlayerView;
import io.faceter.view.PlayerHolderView;
import io.faceter.view.UniversalPlayerView;

public class PlayerViewManager {

    private static final String TAG = "ExoPlayerViewManager";

    public static final String EXTRA_VIDEO_URI = "video_uri";

    private static Map<String, PlayerViewManager> instances = new HashMap<>();
    private Uri videoUri;

    public boolean isPlayerPlaying;
    private boolean isMustPlaying;

    private UniversalPlayerView universalPlayer;


    public static PlayerViewManager getInstance(String videoUri) {
        PlayerViewManager instance = instances.get(videoUri);
        if (instance == null) {
            instance = new PlayerViewManager(videoUri);
            instances.put(videoUri, instance);
        }
        return instance;
    }

    private PlayerViewManager(String videoUri) {
        this.videoUri = Uri.parse(videoUri);
    }

    public void preparePlayer(PlayerHolderView playerHolderView) {
        if (playerHolderView == null) {
            return;
        }

        if (universalPlayer == null) {
            universalPlayer = createPlayer(playerHolderView.getContext());
            isPlayerPlaying = true;
            isMustPlaying = true;
        }

        universalPlayer.initialize(videoUri, playerHolderView);
    }

    public void releaseVideoPlayer() {
        if (universalPlayer != null) {
            universalPlayer.release();
        }
        universalPlayer = null;
    }

    public void goToBackground() {
        if (universalPlayer != null /*&& !isMustPlaying*/) {
            //isPlayerPlaying = player.getPlayWhenReady();
            universalPlayer.pause();
        }
    }

    public void goToForeground() {
        if (universalPlayer != null && isMustPlaying) {
            universalPlayer.play();
        }
    }

    public void pausePlayer(){
        if (universalPlayer != null) {
            universalPlayer.pause();
            isPlayerPlaying = false;
            isMustPlaying = false;
        }
    }

    public void playPlayer(){
        if (universalPlayer != null) {
            universalPlayer.play();
            isPlayerPlaying = true;
            isMustPlaying = true;
        }
    }

    private UniversalPlayerView createPlayer(Context context){
        if (videoUri.getScheme().startsWith("http")){
            return new FaceterExoPlayerView(context);
        }
        return new FaceterExoPlayerView(context);
    }
}