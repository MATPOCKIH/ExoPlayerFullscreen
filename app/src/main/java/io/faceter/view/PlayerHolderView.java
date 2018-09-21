package io.faceter.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ui.PlayerView;

import io.faceter.exoplayerfullscreen.R;

public class PlayerHolderView extends FrameLayout {

    private PlayerView exoPlayerView;

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
                        //ExoPlayerViewManager.getInstance(videoUrl).playPlayer();
                        onUserInteractionListener.onPlayClicked();
                    }
                });

        controlView.findViewById(R.id.exo_pause)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ExoPlayerViewManager.getInstance(videoUrl).pausePlayer();
                        onUserInteractionListener.onPauseClicked();
                    }
                });

        controlView.findViewById(R.id.exo_fullscreen_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent intent = new Intent(getBaseContext(), FullscreenVideoActivity.class);
                        intent.putExtra(ExoPlayerViewManager.EXTRA_VIDEO_URI, videoUrl);
                        startActivity(intent);*/
                        onUserInteractionListener.onFullscreenClicked();
                    }
                });

        CardView card = this.findViewById(R.id.video_card_view);

        card.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra(ExoPlayerViewManager.EXTRA_VIDEO_URI, videoUrl);
                startActivity(intent);*/
                onUserInteractionListener.onVideoTitleClicked();
            }
        });
    }

    public void setOnUserInteractionListener(OnUserInteractionListener onUserInteractionListener) {
        this.onUserInteractionListener = onUserInteractionListener;
    }

    interface OnUserInteractionListener {
        void onPauseClicked();

        void onPlayClicked();

        void onFullscreenClicked();

        void onVideoTitleClicked();
    }

}
