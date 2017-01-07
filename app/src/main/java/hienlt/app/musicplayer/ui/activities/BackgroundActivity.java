package hienlt.app.musicplayer.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import hienlt.app.musicplayer.R;
import hienlt.app.musicplayer.adapter.BackgroundGridAdapter;
import hienlt.app.musicplayer.core.HLBaseActivity;
import hienlt.app.musicplayer.models.BackgroundImage;
import hienlt.app.musicplayer.utils.Common;
import hienlt.app.musicplayer.utils.GaussianBlur;
import hienlt.app.musicplayer.utils.ImageUtils;
import hienlt.app.musicplayer.utils.ItemClickSupport;
import hienlt.app.musicplayer.utils.Settings;
import hienlt.app.musicplayer.utils.SystemUtils;

public class BackgroundActivity extends HLBaseActivity implements ItemClickSupport.OnItemClickListener {

    RecyclerView recyclerView;
    BackgroundGridAdapter adapter;
    ArrayList<BackgroundImage> images;
    ImageView imgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgBackground = (ImageView) findViewById(R.id.imgBackground);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        setBackground(false);
        initListBackground();

        //Get current background
        boolean isDefaultBkg = Settings.getInstance().get(Common.DEFAULT_BACKGROUND, false);
        BackgroundImage currBackground = new BackgroundImage();
        if(isDefaultBkg){
            int bkgID = Settings.getInstance().get(Common.BACKGROUND_ID,0);
            currBackground.setId(bkgID);
        }else{
            String path = Settings.getInstance().get(Common.MY_BACKGROUND, null);
            currBackground.setPath(path);
        }

        adapter = new BackgroundGridAdapter(this, images);
        adapter.setSelectedImage(currBackground);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_background;
    }
}
