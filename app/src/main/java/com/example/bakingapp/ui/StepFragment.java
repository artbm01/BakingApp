package com.example.bakingapp.ui;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import com.google.android.exoplayer2.util.Util;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class StepFragment extends Fragment{

    Unbinder unbinder;
    private String descriptionText;
    private long playbackPosition;
    private int currentWindow = 0;
    private boolean playWhenReady;
    private String videoUrl;
    private SimpleExoPlayer player;
    Boolean resetPosition = true;
    @BindView(R.id.player_view) PlayerView playerView;
    @BindView(R.id.step_description_tv) TextView description;

    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            playWhenReady = savedInstanceState.getBoolean(getString(R.string.PLAY_WHEN_READY));
            playbackPosition = savedInstanceState.getLong(getString(R.string.PLAYBACK_POSITION));
            resetPosition = false;
        } else{
            playbackPosition = 0;
            playWhenReady = true;
        }

        getVideoUrl();

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        unbinder = ButterKnife.bind(this,rootView);

        description.setVisibility(View.VISIBLE);
        description.setText(descriptionText);

        if ((videoUrl == "") || videoUrl.trim().isEmpty()){
        } else {
            playerView.setVisibility(View.VISIBLE);

            if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                playerView.setLayoutParams(params);
            }

            startPlayer();
        }
        return rootView;
    }

    public void setStep(String descriptionText){
        this.descriptionText = descriptionText;
    }

    private void startPlayer(){
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, resetPosition, false);
    }
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            startPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            startPlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void getVideoUrl(){
        Bundle b = getArguments();
        videoUrl = b.getString(getString(R.string.VIDEO_URL));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(getString(R.string.PLAYBACK_POSITION),playbackPosition);
        outState.putBoolean(getString(R.string.PLAY_WHEN_READY),playWhenReady);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            playWhenReady = savedInstanceState.getBoolean(getString(R.string.PLAY_WHEN_READY));
            playbackPosition = savedInstanceState.getLong(getString(R.string.PLAYBACK_POSITION));
        }
        super.onViewStateRestored(savedInstanceState);
    }
}
