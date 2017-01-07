package hienlt.app.musicplayer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import hienlt.app.musicplayer.R;
import hienlt.app.musicplayer.adapter.AlbumRecyclerViewAdapter;
import hienlt.app.musicplayer.adapter.LocalSongRecyclerViewAdapter;
import hienlt.app.musicplayer.core.HLBaseFragment;
import hienlt.app.musicplayer.models.Album;
import hienlt.app.musicplayer.provider.AlbumProvider;
import hienlt.app.musicplayer.provider.SongProvider;
import hienlt.app.musicplayer.utils.Common;
import hienlt.app.musicplayer.utils.FragmentUtils;
import hienlt.app.musicplayer.utils.ItemClickSupport;

/**
 * Created by hienl_000 on 4/24/2016.
 */
public class AlbumListFragment extends HLBaseFragment implements ItemClickSupport.OnItemClickListener {
    RecyclerView recyclerView;
    ArrayList<Album> listAlbum;
    AlbumRecyclerViewAdapter adapter;
    @Override
    protected int getLayout() {
        return R.layout.fragment_list_album;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        listAlbum = AlbumProvider.getInstance(getActivity()).getAlbums();
        adapter = new AlbumRecyclerViewAdapter(getActivity(), listAlbum);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(this);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        String albumName = listAlbum.get(position).getName();
//        FragmentUtils.addStackFragment(getActivity().getSupportFragmentManager(),AlbumDetailFragment.getInstance(albumName),true,true);

//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        Fragment fragment = manager.findFragmentById(R.id.container);
//        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
//        transaction.hide(fragment);
//        transaction.add(R.id.container, AlbumDetailFragment.getInstance(albumName),"detail");
//        transaction.addToBackStack(null);
//        transaction.commit();
    }
}
