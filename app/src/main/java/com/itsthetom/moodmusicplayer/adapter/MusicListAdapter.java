package com.itsthetom.moodmusicplayer.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itsthetom.moodmusicplayer.activities.ArtistAlbumActivity;
import com.itsthetom.moodmusicplayer.activities.MusicPlayerActivity;
import com.itsthetom.moodmusicplayer.R;
import com.itsthetom.moodmusicplayer.databinding.MusicItemBinding;
import com.itsthetom.moodmusicplayer.fragments.SearchFragment;
import com.itsthetom.moodmusicplayer.listeners.AdapterListener;
import com.itsthetom.moodmusicplayer.model.Music;
import com.itsthetom.moodmusicplayer.singleton.Repo;

import java.util.ArrayList;
import java.util.List;

import static com.itsthetom.moodmusicplayer.singleton.Repo.musicList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicViewHolder> {
    Context context;
    AdapterListener listener;
    String adapterListenerType;
    List<Music> musicAdapterList;
    public MusicListAdapter(Context ctx, AdapterListener listen, String listenType){
        context=ctx;
        listener=listen;
        adapterListenerType=listenType;

        switch (adapterListenerType){
            case MusicPlayerActivity.FROM_HOME:
               musicAdapterList=musicList;
               break;
            case MusicPlayerActivity.FROM_SEARCH:
                musicAdapterList=SearchFragment.searchArrayList;
                break;
            case MusicPlayerActivity.FROM_LIBRARY_ARTIST:
                musicAdapterList= Repo.artistTitleList;
                break;
            case MusicPlayerActivity.FROM_LIBRARY_ALBUM:
                musicAdapterList=Repo.albumTitleList;
                break;
            case MusicPlayerActivity.FROM_ARTIST:
            case MusicPlayerActivity.FROM_ALBUM:
                musicAdapterList=ArtistAlbumActivity.artistAlbumList;
                break;
            default:
                musicAdapterList=new ArrayList<>();

        }
    }

    @Override
    public int getItemCount() {
       return musicAdapterList.size();
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.music_item,parent,false);
            Log.d("TAG1","view created");
            return new MusicViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MusicListAdapter.MusicViewHolder holder, int position) {
            Music music= musicAdapterList.get(position);
            holder.binding.tvMusicTitle.setText(music.getTitle());
            holder.binding.tvMusicArtist.setText(music.getArtist());

            if(adapterListenerType.equals(MusicPlayerActivity.FROM_LIBRARY_ARTIST) || adapterListenerType.equals(MusicPlayerActivity.FROM_LIBRARY_ALBUM)){
                holder.binding.tvMusicTitle.setSingleLine(false);
                holder.binding.btnMore.setVisibility(View.GONE);
            }


            if(music.getImage()!=null)
                Glide.with(context).asBitmap()
                        .load(music.getImage())
                        .into(holder.binding.ivMusicImage);
            else holder.binding.ivMusicImage.setImageResource(R.drawable.ic_music);

            holder.binding.musicItemLayout.setOnClickListener((view)->{
                if(adapterListenerType==MusicPlayerActivity.FROM_LIBRARY_ARTIST || adapterListenerType== MusicPlayerActivity.FROM_LIBRARY_ALBUM)
                    listener.startArtistAlbumActivity(music.getTitle(),adapterListenerType);
                else
                     listener.startPlayerActivity(position, adapterListenerType);

            });

    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{
        MusicItemBinding binding;
        public MusicViewHolder(View view){
            super(view);
            binding=MusicItemBinding.bind(view);
        }
    }

    public void updateFilteredList(ArrayList<Music> music,String intentType){
        musicAdapterList=music;
        notifyDataSetChanged();
        this.adapterListenerType=intentType;
    }


}
