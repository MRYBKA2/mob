package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.EOFException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoPlayer;
    private Spinner typeSpinner;
    private Spinner srcSpinner;
    private Spinner fileSpinner;
    private TextView fileTextView;
    private TextView uriTextView;
    private EditText uriInput;
    private AssetManager assetManager;
    private Button btnPlay;
    private Button btnPause;
    private Button btnResume;
    private Button btnStop;
    private Button btnSelect;
    private SeekBar volumeControl;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileSpinner = findViewById(R.id.spinner_file);
        fileSpinner.setEnabled(false);

        uriInput = findViewById(R.id.uri_input);

        fileTextView = findViewById(R.id.textView_file);
        uriTextView = findViewById(R.id.textView_uri);

        btnPlay = findViewById(R.id.button_play);
        btnPause = findViewById(R.id.button_pause);
        btnResume = findViewById(R.id.button_resume);
        btnStop = findViewById(R.id.button_stop);
        btnSelect = findViewById(R.id.button_select);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeControl = (SeekBar) findViewById(R.id.volumeControl);
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(curValue);
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final String[] types = getResources().getStringArray(R.array.types);
        final String[] sources = getResources().getStringArray(R.array.sources);
        typeSpinner = findViewById(R.id.spinner_type);
        srcSpinner = findViewById(R.id.spinner_src);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<String> srcAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sources);
        srcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srcSpinner.setAdapter(srcAdapter);

        videoPlayer = findViewById(R.id.videoView);

        MediaController mediaController = new MediaController(this);
        videoPlayer.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoPlayer);
    }

    public void select(View view) {
        String selectedType = typeSpinner.getSelectedItem().toString();
        String selectedSrc = srcSpinner.getSelectedItem().toString();
        uriInput.setText("");
        if (selectedSrc.equals("File")) {
            uriTextView.setEnabled(false);
            uriInput.setEnabled(false);

            if(selectedType.equals("Audio")) {
                videoPlayer.setVisibility(View.INVISIBLE);
                assetManager = getAssets();
                try {
                    final String[] audios = assetManager.list("audio");
                    ArrayAdapter<String> audioAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, audios);
                    audioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fileSpinner.setAdapter(audioAdapter);

                    fileTextView.setEnabled(true);
                    fileSpinner.setEnabled(true);
                    btnPlay.setEnabled(true);
                    btnPause.setEnabled(false);
                    btnResume.setEnabled(false);
                    btnStop.setEnabled(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (selectedType.equals("Video")) {
                assetManager = getAssets();
                try {
                    final String[] videos = assetManager.list("video");
                    ArrayAdapter<String> videoAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, videos);
                    videoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fileSpinner.setAdapter(videoAdapter);

                    fileTextView.setEnabled(true);
                    fileSpinner.setEnabled(true);
                    btnPlay.setEnabled(true);
                    btnPause.setEnabled(false);
                    btnResume.setEnabled(false);
                    btnStop.setEnabled(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (selectedSrc.equals("URI")) {
            if (selectedType.equals("Audio")) {
                uriInput.setText("http://dl2.mp3party.net/online/8427362.mp3");
                videoPlayer.setVisibility(View.INVISIBLE);
            } else if (selectedType.equals("Video")) {
                uriInput.setText("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4");
            }
            fileTextView.setEnabled(false);
            fileSpinner.setEnabled(false);
            uriTextView.setEnabled(true);
            uriInput.setEnabled(true);
            btnPlay.setEnabled(true);
            btnPause.setEnabled(false);
            btnResume.setEnabled(false);
            btnStop.setEnabled(false);
        }
    }

    public void play(View view) {
        String selectedType = typeSpinner.getSelectedItem().toString();
        String selectedSrc = srcSpinner.getSelectedItem().toString();
        if(selectedSrc.equals("File")) {
            if (selectedType.equals("Audio")) {
                String filename = fileSpinner.getSelectedItem().toString();
                try {
                    AssetFileDescriptor afd = assetManager.openFd("audio/" + filename);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (selectedType.equals("Video")) {
                String filename = fileSpinner.getSelectedItem().toString();

                String uri = "android.resource://"+getPackageName()+"/"+this.getResources().getIdentifier(filename.substring(0,filename.length()-4), "raw", this.getPackageName());

                videoPlayer.setVideoURI(Uri.parse(uri));
                videoPlayer.setVisibility(View.VISIBLE);
                videoPlayer.start();
            }
        } else if (selectedSrc.equals("URI")) {
            if (selectedType.equals("Audio")) {
                String contentAudio = uriInput.getText().toString();
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(contentAudio);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return;
                }
            }
        }
        btnPlay.setEnabled(false);
        btnPause.setEnabled(true);
        btnResume.setEnabled(false);
        btnStop.setEnabled(true);
        btnSelect.setEnabled(false);
    }

    public void pause(View view) {
        String selectedType = typeSpinner.getSelectedItem().toString();
        if (selectedType.equals("Video")) {
            videoPlayer.pause();
        } else {
            mediaPlayer.pause();
        }
        btnPlay.setEnabled(false);
        btnPause.setEnabled(false);
        btnResume.setEnabled(true);
        btnStop.setEnabled(true);
    }
    public void resume(View view) {
        String selectedType = typeSpinner.getSelectedItem().toString();
        if (selectedType.equals("Video")) {
            videoPlayer.start();
        } else {
            mediaPlayer.start();
        }
        btnPlay.setEnabled(false);
        btnPause.setEnabled(true);
        btnResume.setEnabled(false);
        btnStop.setEnabled(true);
    }
    public void stop(View view) {
        String selectedType = typeSpinner.getSelectedItem().toString();
        if (selectedType.equals("Video")) {
            videoPlayer.stopPlayback();
            videoPlayer.resume();
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        btnPlay.setEnabled(true);
        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnStop.setEnabled(false);
        btnSelect.setEnabled(true);
    }

}