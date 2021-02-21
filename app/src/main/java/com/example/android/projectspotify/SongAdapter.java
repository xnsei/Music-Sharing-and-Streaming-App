package com.example.android.projectspotify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class SongAdapter extends BaseAdapter {

    List<String> songNameList;
    List<String> imageURLList;
    List<String> songArtistList;
    List<String> songDurationList;
    Context context;

    public SongAdapter(Context context, List<String> songNameList, List<String> imageURLList,
                       List<String> songArtistList, List<String> songDurationList){
        this.context = context;
        this.songNameList = songNameList;
        this.imageURLList = imageURLList;
        this.songArtistList = songArtistList;
        this.songDurationList = songDurationList;
    }


    @Override
    public int getCount() {
        return songNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SongHolder"})

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final SongHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.song_list, null);
            viewHolder = new SongHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (SongHolder) view.getTag();
        }

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(15)
                .build();

        Picasso.get().load(imageURLList.get(i)).transform(transformation).into(viewHolder.thumbnailImage);
        viewHolder.songName.setText(songNameList.get(i));
        viewHolder.songArtist.setText(songArtistList.get(i));
        viewHolder.songDuration.setText(songDurationList.get(i));
        return view;
    }

    private class SongHolder {
        TextView songName;
        TextView songArtist;
        TextView songDuration;
        ImageView thumbnailImage;
        CardView cardView;
        ImageView currentlyPlaying;

        SongHolder(View view){
            songName = view.findViewById(R.id.songName);
            songArtist = view.findViewById(R.id.songArtist);
            songDuration = view.findViewById(R.id.songDuration);
            thumbnailImage = view.findViewById(R.id.thumbnailImage);
            cardView = view.findViewById(R.id.cardView);
            currentlyPlaying = view.findViewById(R.id.currentlyPlaying);
        }
    }
}
