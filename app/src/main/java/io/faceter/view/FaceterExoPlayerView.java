package io.faceter.view;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.TextureView;

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

import io.faceter.exoplayerfullscreen.R;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;
import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT;

public class FaceterExoPlayerView extends UniversalPlayerView {

    private Uri videoUri;
    private DefaultDataSourceFactory dataSourceFactory;
    private SimpleExoPlayer player;
    private PlayerView exoPlayerView;
    private Context context;

    public FaceterExoPlayerView(Context context) {
        this.context = context;
    }

    @Override
    public void initialize(Uri videoUri, PlayerHolderView playerHolderView) {

        if (playerHolderView == null || videoUri == null)
            return;

        exoPlayerView = playerHolderView.findViewById(R.id.exo_player);


        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());

            dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "faceter"));

            MediaSource videoSource = buildMediaSource(videoUri, null);
            player.prepare(videoSource);
        }

        player.clearVideoSurface();
        player.setVideoTextureView((TextureView) exoPlayerView.getVideoSurfaceView());
        exoPlayerView.setPlayer(player);
        exoPlayerView.hideController();
        setResizeModeFill(playerHolderView.isResizeModeFill());
    }

    @Override
    public void play() {
        player.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        player.setPlayWhenReady(false);
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

    @Override
    public void release() {
        if (player != null) {
            player.release();
        }
        player = null;
    }

    @Override
    public void setResizeModeFill(boolean isResizeModeFill) {
        if (isResizeModeFill) {
            exoPlayerView.setResizeMode(RESIZE_MODE_FILL);
        } else {
            exoPlayerView.setResizeMode(RESIZE_MODE_FIT);
        }
    }
}
