package hienlt.app.musicplayer.ui.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import hienlt.app.musicplayer.R;
import hienlt.app.musicplayer.adapter.PlaybackPagerAdapter;
import hienlt.app.musicplayer.core.HLBaseActivity;
import hienlt.app.musicplayer.interfaces.IMusicServiceConnection;
import hienlt.app.musicplayer.media.MusicService;
import hienlt.app.musicplayer.models.Song;
import hienlt.app.musicplayer.ui.dialog.PlaylistDialog;
import hienlt.app.musicplayer.ui.fragments.LyricFragment;
import hienlt.app.musicplayer.utils.CacheManager;
import hienlt.app.musicplayer.utils.Common;
import hienlt.app.musicplayer.utils.DiskLruFileCache;
import hienlt.app.musicplayer.utils.DiskLruImageCache;
import hienlt.app.musicplayer.utils.GaussianBlur;
import hienlt.app.musicplayer.utils.Settings;

/**
 * Created by hienl_000 on 4/14/2016.
 */
public class PlaybackActivity extends HLBaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, IMusicServiceConnection {

    ImageView imgBackground;
    public MusicService mService;
    private TextView tvCurrentPlay, tvEndPlay;
    private SeekBar songProgressBar;
    private Handler handler;
    private ImageButton btnPlay, btnNext, btnPrevious, btnShuffle, btnRepeat, btnPlaylist;
    ViewPager pager;
    PlaybackPagerAdapter adapter;
    LyricFragment lyricFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Find view
        imgBackground = (ImageView) findViewById(R.id.imgBackground);
        tvCurrentPlay = (TextView) findViewById(R.id.tvCurrentPlay);
        tvEndPlay = (TextView) findViewById(R.id.tvEndPlay);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
        pager = (ViewPager) findViewById(R.id.pager);
        handler = new Handler();

        //set listener
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        btnPlaylist.setOnClickListener(this);
        addOnMusicServiceListener(this);
        songProgressBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
        if (mService == null || mService.getState() == MusicService.MusicState.Stop) return;
        int currPlay = Common.msCurrentSeekbar(seekBar.getProgress(), mService.getDuration());
        mService.seekTo(currPlay);
        updateTimeplay();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_playback;
    }

    public void setBackground(Bitmap bitmap, boolean blur) {
        if (blur) {
            bitmap = GaussianBlur.getInstance(this).setRadius(10).render(bitmap, true);
        }
        //Common.setBackground(this, imgBackground, bitmap);
        imgBackground.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        return super.onOptionsItemSelected(item);
    }

    

    

    private void updateTimeplay() {
        handler.removeCallbacks(mUpdateTimeTask);
        handler.post(mUpdateTimeTask);
        mUpdateTimeTask.run();
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (mService != null) {
                if (mService.getState() == MusicService.MusicState.Playing) {
                    tvCurrentPlay.setText(Common.miliSecondToString(mService.getCurrentPosition()));
                    songProgressBar.setProgress(Common.percentSeekbar(mService.getCurrentPosition(), mService.getDuration()));

                    if (lyricFragment == null) {
                        lyricFragment = (LyricFragment) adapter.getPagerFragment(pager, 0);
                    } else {
                        lyricFragment.seekLrcToTime(mService.getCurrentPosition());
                    }
                }
                songProgressBar.setSecondaryProgress(mService.getBufferingPercent());
                handler.postDelayed(mUpdateTimeTask, 200);
            }
        }
    };

    @Override
    public void onConnected(MusicService service) {
        mService = service;

        // Khởi tạo viewpager
        initViewPager();
        // Update thông tin
        updateTrackInfo();
        // Update trạng thái nút bấm
        updatePlayState();
        // Update thời gian phát nhạc
        updateTimeplay();
        updateShuffleRepeatState();
    }

    private void initViewPager() {
        adapter = new PlaybackPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicService.META_CHANGE);
        filter.addAction(MusicService.PLAY_STATE_CHANGE);
        filter.addAction(MusicService.EXIT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_playing, menu);
        return true;
    }

    public void onLrcSeeked(int ms) {
        if (mService == null) return;
        if (mService.getState() == MusicService.MusicState.Stop) return;
        mService.seekTo(ms);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.slide_out_down);
    }
}
