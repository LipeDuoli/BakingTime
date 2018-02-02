package com.example.android.bakingtime.fragments;

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
import com.example.android.bakingtime.activities.StepDetailActivity;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        if (getArguments().containsKey(StepDetailActivity.EXTRA_STEP)) {
            mStep = getArguments().getParcelable(StepDetailActivity.EXTRA_STEP);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        if (mStepDescription != null) {
            mStepDescription.setText(mStep.getDescription());
        }
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
            mExoPlayer.setPlayWhenReady(true);
        }

    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        mUnbinder.unbind();
    }
}
