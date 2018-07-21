package com.example.bakingapp.ui;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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

import java.util.Objects;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class StepFragment extends Fragment{

    private String descriptionText;
    private long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean playWhenReady = true;
    private String videoUrl;
    private SimpleExoPlayer player;

    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getVideoUrl();

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        final PlayerView playerView = rootView.findViewById(R.id.player_view);
        final TextView description = rootView.findViewById(R.id.step_description_tv);

        description.setVisibility(View.VISIBLE);
        description.setText(descriptionText);

        if (Objects.equals(videoUrl, "") || videoUrl.trim().isEmpty()){
        } else {
            playerView.setVisibility(View.VISIBLE);

            if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                playerView.setLayoutParams(params);
            }

            startPlayer(playerView);
        }
        return rootView;
    }

    public void setStep(String descriptionText){
        this.descriptionText = descriptionText;
    }

    private void startPlayer(PlayerView playerView){
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
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
    public void onDestroyView() {
        Log.v("tester", "destroyer");
        super.onDestroyView();
        releasePlayer();
    }

    private void getVideoUrl(){
        Bundle b = getArguments();
        videoUrl = b.getString(getString(R.string.VIDEO_URL));
    }

}
