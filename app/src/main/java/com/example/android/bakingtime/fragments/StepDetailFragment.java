package com.example.android.bakingtime.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepDetailFragment extends Fragment {

    private static final String TAG = StepDetailFragment.class.getSimpleName();
    public static final String EXTRA_STEP = "extraStep";
    private static final String SAVED_PLAYER_POSITION = "savedPlayerPosition";

    @BindView(R.id.exo_step_video)
    SimpleExoPlayerView mStepVideo;
    @BindView(R.id.iv_step_image)
    ImageView mStepImage;
    @Nullable
    @BindView(R.id.tv_step_description)
    TextView mStepDescription;
    private Unbinder mUnbinder;

    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private long mVideoPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        if (getArguments().containsKey(EXTRA_STEP)) {
            mStep = getArguments().getParcelable(EXTRA_STEP);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_PLAYER_POSITION)){
            mVideoPosition = savedInstanceState.getLong(SAVED_PLAYER_POSITION);
        }

        if (!mStep.getVideoURL().isEmpty()) {
            initializePlayer(Uri.parse(mStep.getVideoURL()));
            hideImage();
        } else if (!mStep.getThumbnailURL().isEmpty()) {
            showImage();
            hideVideo();
        } else {
            hideVideo();
            hideImage();
        }

        mStepDescription.setText(mStep.getDescription());
        checkShowDescription();
    }

    private void checkShowDescription() {
        if (isLandscapeOrientation() && !isTabletMode()){
            if (mStepVideo.getVisibility() == View.VISIBLE ||
                    mStepImage.getVisibility() == View.VISIBLE){
                mStepDescription.setVisibility(View.INVISIBLE);
            }
        }
    }

    private boolean isLandscapeOrientation() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private boolean isTabletMode() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    private void hideVideo() {
        mStepVideo.setVisibility(View.GONE);
    }

    private void showImage() {
        Picasso.with(getContext()).load(mStep.getThumbnailURL()).into(mStepImage);
    }

    private void hideImage() {
        mStepImage.setVisibility(View.GONE);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            // Create an instance of the
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mStepVideo.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));

            DefaultDataSourceFactory defaultDataSourceFactory =
                    new DefaultDataSourceFactory(getContext(),
                            userAgent,
                            new DefaultBandwidthMeter());

            MediaSource mediaSource = new ExtractorMediaSource
                    .Factory(defaultDataSourceFactory).createMediaSource(mediaUri);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mVideoPosition);
            if (isTabletMode() && isLandscapeOrientation()) {
                mExoPlayer.setPlayWhenReady(true);
            } else {
                mExoPlayer.setPlayWhenReady(false);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer != null) {
            outState.putLong(SAVED_PLAYER_POSITION, mExoPlayer.getContentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public void pausePlayer() {
        if (mExoPlayer != null){
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    public void resumePlayer() {
        if (mExoPlayer != null){
            mExoPlayer.setPlayWhenReady(true);
        }
    }
}
