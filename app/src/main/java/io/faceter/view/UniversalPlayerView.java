package io.faceter.view;

import android.net.Uri;

public abstract class UniversalPlayerView {

    abstract public void initialize(Uri videoUri, PlayerHolderView playerView);

    abstract public void play();

    abstract public void pause();

    abstract public void release();

    public abstract void setResizeModeFill(boolean isResizeModeFill);
}
