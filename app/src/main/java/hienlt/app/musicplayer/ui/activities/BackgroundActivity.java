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

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_background;
    }
}
