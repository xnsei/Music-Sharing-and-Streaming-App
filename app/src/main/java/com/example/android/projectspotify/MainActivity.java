package com.example.android.projectspotify;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean checkPermission = false;
    ProgressDialog progressDialog;
    ListView listView;
    List<String> songsNameList;
    List<String> songsUrlList;
    List<String> songsArtistList;
    List<String> songsDurationList;
    SongAdapter adapter;
    JcPlayerView jcPlayerView;
    List<JcAudio> jcAudios;
    List<String> thumbnail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            startMainActivity();
        }

    }

    private void startMainActivity(){
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Please Wait...");
        listView = findViewById(R.id.songList);
        songsNameList = new ArrayList<>();
        songsUrlList = new ArrayList<>();
        songsArtistList = new ArrayList<>();
        songsDurationList = new ArrayList<>();
        jcAudios = new ArrayList<>();
        thumbnail = new ArrayList<>();
        jcPlayerView = findViewById(R.id.jcplayer);
//        jcPlayerView.createNotification(R.drawable.app_icon);
        retrieveSongs();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jcPlayerView.playAudio(jcAudios.get(i));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check if user is already signed in?
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            UpdateUI();
        }
    }

    private void UpdateUI() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    // RETRIEVING THE SONGS FROM THE SERVER
    public void retrieveSongs() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Songs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                songsNameList.clear();
                songsUrlList.clear();
                songsArtistList.clear();
                thumbnail.clear();
                songsDurationList.clear();
                jcAudios.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Song mSong = dataSnapshot.getValue(Song.class);
                    songsNameList.add(mSong.getSongName());
                    songsUrlList.add(mSong.getSongURL());
                    songsArtistList.add(mSong.getSongArtist());
                    thumbnail.add(mSong.getImageURL());
                    songsDurationList.add(mSong.getSongDuration());

                    jcAudios.add(JcAudio.createFromURL(mSong.getSongName(),mSong.getSongURL()));
                }

                adapter = new SongAdapter(MainActivity.this, songsNameList, thumbnail, songsArtistList, songsDurationList);
                jcPlayerView.initPlaylist(jcAudios,null);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "FAILED!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.uploadItem){
            if (validatePermissions()){
                Intent intent = new Intent(this,UploadSongsActivity.class);
                startActivity(intent);
            }
        }

        else if (item.getItemId() == R.id.signOut){
            FirebaseAuth.getInstance().signOut();
            UpdateUI();
        }

//        listView.setAdapter(null);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Please Wait...");
        retrieveSongs();
        return true;
    }

    // METHOD TO HANDEL RUNTIME PERMISSIONS
    private boolean validatePermissions(){
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        checkPermission = true;
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        checkPermission = false;
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
        return checkPermission;

    }
}