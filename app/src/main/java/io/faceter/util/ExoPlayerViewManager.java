package io.faceter.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.SurfaceView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.HashMap;
import java.util.Map;

public class ExoPlayerViewManager {

    private static final String TAG = "ExoPlayerViewManager";

    public static final String EXTRA_VIDEO_URI = "video_uri";

    private static Map<String, ExoPlayerViewManager> instances = new HashMap<>();
    private Uri videoUri;

    private DefaultDataSourceFactory dataSourceFactory;
    private SimpleExoPlayer player;
    private boolean isPlayerPlaying;


    public static ExoPlayerViewManager getInstance(String videoUri) {
        ExoPlayerViewManager instance = instances.get(videoUri);
        if (instance == null) {
            instance = new ExoPlayerViewManager(videoUri);
            instances.put(videoUri, instance);
        }
        return instance;
    }

    private ExoPlayerViewManager(String videoUri) {
        this.videoUri = Uri.parse(videoUri);
    }

    public void prepareExoPlayer(Context context, PlayerView exoPlayerView) {
        if (context == null || exoPlayerView == null) {
            return;
        }
        if (player == null) {

            player =
                    ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());

            dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "exoplayerfullscreen"));

            /*
            MediaSource videoSource = new HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);*/

            MediaSource videoSource = buildMediaSource(videoUri, null);

            // Prepare the player with the source.
            isPlayerPlaying = true;
            player.prepare(videoSource);
        }
        player.clearVideoSurface();
        player.setVideoSurfaceView((SurfaceView) exoPlayerView.getVideoSurfaceView());
       // player.seekTo(player.getCurrentPosition() + 1);
        exoPlayerView.setPlayer(player);
    }

    public void releaseVideoPlayer() {
        if (player != null) {
            player.release();
        }
        player = null;
    }

    public void goToBackground() {
        if (player != null) {
            isPlayerPlaying = player.getPlayWhenReady();
            player.setPlayWhenReady(false);
        }
    }

    public void goToForeground() {
        if (player != null) {
            player.setPlayWhenReady(isPlayerPlaying);
        }
    }

    @SuppressWarnings("unchecked")
    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            /*case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .setManifestParser(
                                new FilteringManifestParser<>(
                                        new DashManifestParser(), (List<RepresentationKey>) getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .setManifestParser(
                                new FilteringManifestParser<>(
                                        new SsManifestParser(), (List<StreamKey>) getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);*/
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        /*.setPlaylistParser(
                                new FilteringManifestParser<>(
                                        new HlsPlaylistParser(), (List<RenditionKey>) getOfflineStreamKeys(uri)))*/
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }
}