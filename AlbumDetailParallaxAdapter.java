package hienlt.app.musicplayer.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hienlt.app.musicplayer.R;
import hienlt.app.musicplayer.models.Song;
import hienlt.app.musicplayer.utils.App;

/**
 * Created by hienl_000 on 4/26/2016.
 */
public class AlbumDetailParallaxAdapter extends ParallaxRecyclerAdapter<Song>{
    List<Song> list;
    public AlbumDetailParallaxAdapter(List<Song> data) {
        super(data);
        this.list = data;
    }

    @Override
    public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<Song> parallaxRecyclerAdapter, int i) {
        AlbumDetailViewHolder holder = (AlbumDetailViewHolder) viewHolder;
        Song song = list.get(i);
        File file = new File(song.getLocalDataSource());
        if(!file.exists()){
            holder.tvSong.setText("Lỗi không tìm thấy bài hát");
            holder.tvArtist.setText("");
        }else{
            holder.view.setClickable(true);
            holder.tvSong.setText(song.getTitle());
            holder.tvArtist.setText(song.getArtist());
        }
        if (song.getBitRate() == 320) {
            holder.tvBitrate.setText("320");
            holder.tvBitrate.setVisibility(View.VISIBLE);
        } else
            holder.tvBitrate.setVisibility(View.GONE);

        holder.btnAction.setTag(i);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<Song> parallaxRecyclerAdapter, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_local_song,viewGroup,false);
        return new AlbumDetailViewHolder(v);
    }

    public class AlbumDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tvSong, tvArtist, tvBitrate;
        LinearLayout btnAction;
        View view;

        public AlbumDetailViewHolder(View itemView) {
            super(itemView);
            tvSong = (TextView) itemView.findViewById(R.id.tvSong);
            tvArtist = (TextView) itemView.findViewById(R.id.tvArtist);
            tvBitrate = (TextView) itemView.findViewById(R.id.tvBitrate);
            tvSong.setTextColor(ContextCompat.getColor(App.getAppContext(), R.color.text_while));
            tvArtist.setTextColor(ContextCompat.getColor(App.getAppContext(),R.color.text_while));
        }
    }

    @Override
    public int getItemCountImpl(ParallaxRecyclerAdapter<Song> parallaxRecyclerAdapter) {
        return list.size();
    }
}
