package com.example.bakingapp.ui;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class StepFragment extends Fragment{

    String descriptionText;
    long playbackPosition = 0;
    int currentWindow = 0;
    boolean playWhenReady = true;

    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        final TextView description = rootView.findViewById(R.id.step_description_tv);
        description.setText(descriptionText);
        final PlayerView playerView = rootView.findViewById(R.id.player_view);
        startPlayer(playerView);

        return rootView;
    }

    public void setStep(String descriptionText){
        this.descriptionText = descriptionText;
    }

    public void startPlayer(PlayerView playerView){
        SimpleExoPlayer player;
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        Uri uri = Uri.parse("http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4");
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }
}
