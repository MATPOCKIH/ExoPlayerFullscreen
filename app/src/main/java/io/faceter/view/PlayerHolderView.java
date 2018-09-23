package io.faceter.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import io.faceter.exoplayerfullscreen.FullscreenVideoActivity;
import io.faceter.exoplayerfullscreen.R;
import io.faceter.util.PlayerViewManager;

public class PlayerHolderView extends FrameLayout {

    private String videoUrl;

    private boolean isResizeModeFill = true;

    private OnUserInteractionListener onUserInteractionListener;

    public PlayerHolderView(@NonNull Context context) {
        super(context);
        init();
    }

    public PlayerHolderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerHolderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_player, this, true);

        View controlView = this.findViewById(R.id.exo_controller);

        controlView.findViewById(R.id.exo_play)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlayerViewManager.getInstance(videoUrl).playPlayer();
                    }
                });

        controlView.findViewById(R.id.exo_pause)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlayerViewManager.getInstance(videoUrl).pausePlayer();
                    }
                });

        controlView.findViewById(R.id.exo_fullscreen_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), FullscreenVideoActivity.class);
                        intent.putExtra(PlayerViewManager.EXTRA_VIDEO_URI, videoUrl);
                        getContext().startActivity(intent);
                    }
                });
/*
        CardView card = this.findViewById(R.id.video_card_view);

        card.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
//                intent.putExtra(ExoPlayerViewManager.EXTRA_VIDEO_URI, videoUrl);
//                startActivity(intent);
                onUserInteractionListener.onVideoTitleClicked(videoUrl);
            }
        });*/
    }

    public void setOnUserInteractionListener(OnUserInteractionListener onUserInteractionListener) {
        this.onUserInteractionListener = onUserInteractionListener;
    }

    public void setupPlayerView(String videoUrl) {
        this.videoUrl = videoUrl;
        PlayerViewManager.getInstance(videoUrl).preparePlayer(this);
        PlayerViewManager.getInstance(videoUrl).goToForeground();
    }

    public void setResizeModeFill(boolean isResizeModeFill){
        this.isResizeModeFill = isResizeModeFill;
    }

    public boolean isResizeModeFill() {
        return isResizeModeFill;
    }

    public interface OnUserInteractionListener {
        void onPauseClicked();

        void onPlayClicked();

        void onFullscreenClicked();

        void onVideoTitleClicked(String videoUrl);
    }

}
