package comsorry.sosorry.youtubedataactivities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import comsorry.sosorry.R;
import comsorry.sosorry.SoSorryApplication;

/**
 * Created by Surya on 5/17/2017.
 */
public class YouTubePlayer_Activity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    String videoId;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;
    LinearLayout banner;
    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().setFactory(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        banner=(LinearLayout)findViewById(R.id.banner);
        mInterstitialAd = new InterstitialAd(this);
        orientation=getResources().getConfiguration().orientation;
        if(orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            banner.setVisibility(View.GONE);

        }
        if (SoSorryApplication.counter == 3) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

                    AdRequest adRequest = new AdRequest.Builder()

                            .build();

                    // Load ads into Interstitial Ads
                    mInterstitialAd.loadAd(adRequest);
                    mInterstitialAd.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            showInterstitial();
                        }
                    });

                }
            }, 4000);


            SoSorryApplication.counter = 0;
        } else {
            SoSorryApplication.counter++;
        }
        youTubeView = (YouTubePlayerView) findViewById(R.id.player_view);

        // Initializing video player with developer key
        youTubeView.initialize(Config.DEVELOPER_KEY, this);
        videoId=getIntent().getStringExtra("VideoId");

        mAdView = (AdView)findViewById(R.id.startAppBanner);
        AdRequest adRequest = new AdRequest.Builder().
                build();

        mAdView.loadAd(adRequest);

    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
//            String errorMessage = String.format(
//                    getString(R.string.error_player), errorReason.toString());
//            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            //  player.loadVideo(Config.YOUTUBE_VIDEO_CODE);
            player.loadVideo(videoId);
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.player_view);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AdRequest adRequest = new AdRequest.Builder().
                build();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) //To fullscreen
        {
            banner.setVisibility(View.GONE);
            mAdView.destroy();

        } else {
            banner.setVisibility(View.VISIBLE);
            mAdView.loadAd(adRequest);

        }

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();


    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();

    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
