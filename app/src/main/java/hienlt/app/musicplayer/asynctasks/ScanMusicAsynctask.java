package hienlt.app.musicplayer.asynctasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hienlt.app.musicplayer.interfaces.IScanMedia;
import hienlt.app.musicplayer.models.Song;
import hienlt.app.musicplayer.provider.SongProvider;
import hienlt.app.musicplayer.ui.activities.ScanActivity;
import hienlt.app.musicplayer.utils.Common;
import hienlt.app.musicplayer.utils.DiskLruImageCache;
import hienlt.app.musicplayer.utils.FileUtils;
import hienlt.app.musicplayer.utils.NumberUtils;
import hienlt.app.musicplayer.utils.StringUtils;

/**
 * Created by hienl_000 on 4/4/2016.
 */
public class ScanMusicAsynctask extends AsyncTask<Void, File, ArrayList<Song>> {
    private static String TAG = "hienlt0610";
    private boolean isCancelled = false;
    private ArrayList<Song> listSong;
    private int numMusicFind = 0;
    private Handler myHandler;
    private ArrayList<String> exceptionFolter;
    private IScanMedia iScanMedia;
    private Context context;

    public ScanMusicAsynctask(Context context, IScanMedia iScanMedia) {

        this.iScanMedia = iScanMedia;
        this.context = context;
    }

    private DiskLruImageCache cache;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listSong = new ArrayList<>();
        myHandler = new Handler();

        cache = DiskLruImageCache.newInstance(Common.ALBUM_CACHE_FOLDER);

    }

    public void cancelTask() {
        isCancelled = true;
    }

    @Override
    protected ArrayList<Song> doInBackground(Void... params) {
        if (isCancelled) return null;

        if (FileUtils.isExternalStorageReadable()) {
            scanDirectory(Environment.getExternalStorageDirectory());
        }
        scanDirectory(Environment.getDataDirectory());

        return listSong;
    }

    @Override
    protected void onPostExecute(final ArrayList<Song> songs) {
        super.onPostExecute(songs);
        
    }

    @Override
    protected void onProgressUpdate(final File... values) {
        super.onProgressUpdate(values);
//        myHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                ((ScanActivity) context).tvFile.setText(values[0].getName());
//            }
//        });
        

    }
}
