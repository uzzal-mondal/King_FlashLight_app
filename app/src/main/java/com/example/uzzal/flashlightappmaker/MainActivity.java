package com.example.uzzal.flashlightappmaker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private CameraManager mCameraManager;
    private String mCameraId;
    private ImageView mTorchOnOffButton;
    private Boolean isTorchOn;
    private MediaPlayer mp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTorchOnOffButton = findViewById(R.id.button_on_off);
        isTorchOn = false;


        if (isFlashLight()) {

            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    assert mCameraManager != null;
                    mCameraId = mCameraManager.getCameraIdList()[0];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mTorchOnOffButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if (isTorchOn) {
                           turnOffFlashLight ();
                            isTorchOn = false;
                        } else {
                            turnOnFlashLight();
                            isTorchOn = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


     public boolean isFlashLight(){

            Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                    .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

            if(!isFlashAvailable){
                AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                        .create();
                alert.setTitle("Eror !!");
                alert.setMessage("Your Device does not Support FlashLight !");
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                finish();
                                System.exit(0);
                            }
                        });
                alert.show();
                return  false;
        } else{
                return true;
            }
    }

        public void turnOnFlashLight(){

            try{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    mCameraManager.setTorchMode(mCameraId,true);
                    playOnOffSound();
                    mTorchOnOffButton.setImageResource(R.drawable.on_off);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        public void turnOffFlashLight(){

            try{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    mCameraManager.setTorchMode(mCameraId, false);
                    playOnOffSound();
                    mTorchOnOffButton.setImageResource(R.drawable.on_off);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        private void playOnOffSound(){

            mp = MediaPlayer.create(MainActivity.this,R.raw.flash_sound);

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    mp.release();
                }
            });

            mp.start();
        }



//        private void playOffOnSound(){
//
//            mp = MediaPlayer.create(MainActivity.this, R.raw.flash_sound_two);
//
//            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    mp.release();
//                }
//            });
//            mp.start();
//        }


        @Override
        protected void onStop(){
            super.onStop();

            if(isTorchOn){
                turnOffFlashLight();
            }
        }


        @Override
        protected void onPause(){
            super.onPause();
            if(isTorchOn){
                turnOffFlashLight();
            }
        }

        @Override
        protected void onResume(){
            super.onResume();
            if(isTorchOn){
                turnOnFlashLight();
            }
        }


    }


