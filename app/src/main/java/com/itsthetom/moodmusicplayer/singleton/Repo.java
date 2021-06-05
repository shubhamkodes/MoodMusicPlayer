package com.itsthetom.moodmusicplayer.singleton;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.itsthetom.moodmusicplayer.model.Music;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repo {
    public static final String UNKNOWN_ARTIST="UN_ARTIST",UNKNOWN_ALBUM="UN_ALBUM";
    public static final int NO_THREADS=4;
    private static Executor executor;
    public static ArrayList<Music> musicList,artistTitleList,albumTitleList;
    public static HashMap<String,ArrayList<Music>> artistList,albumList;
    private Context context;
    private MutableLiveData<Integer> checkMetaData,checkListAvailable;


    private static Repo instance;
    public Repo(Context context){
        this.context=context;
        musicList=new ArrayList<>();
        executor= Executors.newFixedThreadPool(NO_THREADS);
        checkMetaData=new MutableLiveData<>();
        checkListAvailable=new MutableLiveData<>();
        artistList=new HashMap<>();
        albumList=new HashMap<>();
        artistTitleList=new ArrayList<>();
        albumTitleList=new ArrayList<>();
        loadAllMusic();

    }

    public static synchronized Repo getInstance(Context context){
        if(instance==null){
            instance=new Repo(context);
        }
        return instance;
    }

    private void loadAllMusic(){

        executor.execute(()->{
            checkMetaData.postValue(0);
            Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String projection[]={
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.TITLE
            };

            Cursor cursor= context.getContentResolver().query(uri,projection,null,null,MediaStore.Audio.Media.TITLE);
            if(cursor!=null){
                musicList.clear();
                while(cursor.moveToNext()){
                    String id=cursor.getString(0);
                    String data=cursor.getString(1);
                    String title=cursor.getString(2);


                    Music music=new Music(id,title,data);
                   // Log.d("TAG",music.toString());
                    musicList.add(music);
                }
                checkListAvailable.postValue(1);
            }
            loadMetaData();
        });

    }

    public void loadMetaData(){
        checkMetaData.postValue(0);
        executor.execute(()->{
                for(Music music:musicList){
                    music.setImage(getImage(music.getData()));
                    String[] metadata=getArtistAndAlbum(music.getData());
                    music.setArtist(metadata[0]);
                    music.setAlbum(metadata[1]);
                    if(artistList.containsKey(music.getArtist())){
                        artistList.get(music.getArtist()).add(music);
                    }else{
                        ArrayList<Music> temp=new ArrayList<>();
                        temp.add(music);
                        artistList.put(music.getArtist(),temp);

                        Music tempMusic=music.clone(music);
                        tempMusic.setTitle(tempMusic.getArtist());
                        tempMusic.setArtist("");
                        artistTitleList.add(tempMusic);

                    }

                    if(albumList.containsKey(music.getAlbum())){
                        albumList.get(music.getAlbum()).add(music);
                    }else{
                        ArrayList<Music> temp=new ArrayList<>();
                        temp.add(music);
                        albumList.put(music.getAlbum(),temp);

                        Music tempMusic=music.clone(music);
                        tempMusic.setArtist("");
                        tempMusic.setTitle(tempMusic.getAlbum());
                        albumTitleList.add(tempMusic);
                    }


                }
                Log.d("TAG",String.valueOf(artistTitleList.size()));
                checkMetaData.postValue(1);
                }
        );
    }


    private byte[] getImage(String data){
        byte[] image=null;
        try{
            MediaMetadataRetriever retriever=new MediaMetadataRetriever();
            File file =new File(data);
            FileInputStream fileInputStream=new FileInputStream(file);
            retriever.setDataSource(fileInputStream.getFD());
            image=retriever.getEmbeddedPicture();
            fileInputStream.close();
            retriever.release();
        }catch(Exception e){
           // Log.d("TAG",e.getMessage().toString());
        }
        return image;
    }

    private String[] getArtistAndAlbum(String data){
        String artist[]=new String[]{null,null};
        try{
            MediaMetadataRetriever retriever=new MediaMetadataRetriever();
            File file =new File(data);
            FileInputStream fileInputStream=new FileInputStream(file);
            retriever.setDataSource(fileInputStream.getFD());
            artist[0]=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            artist[1]=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            fileInputStream.close();
            retriever.release();
        }catch(Exception e){
               // Log.d("TAG",e.getMessage().toString());
        }
        if(artist[0]==null)
            artist[0]="Unknown Artist";
        if(artist[1]==null)
            artist[1]="Unknown Album";

        return artist;

    }

    public LiveData<Integer> isListAvailable(){
        return checkListAvailable;
    }
    public LiveData<Integer> isMetadataAvailable(){
        return checkMetaData;
    }
}
