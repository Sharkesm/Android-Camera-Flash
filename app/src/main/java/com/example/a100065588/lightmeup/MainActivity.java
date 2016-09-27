package com.example.a100065588.lightmeup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private CameraManager mCameraManager;
    private String mCameraId;
    private Boolean isFlashOn = false;
    private Boolean imageState = false;
    private ImageView bulbImageView;
    private String appCameraFlashMissing, appCameraFlashFound;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI setup
        initializeUI(savedInstanceState);

        // Return boolean if system supports camera flash feature
        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        // Check if system doesn't support camera flash feature
        if (!isFlashAvailable) {
            AlertDialog infoOnFlash = new AlertDialog.Builder(MainActivity.this)
                    .create();
            infoOnFlash.setTitle("Permission denied");
            infoOnFlash.setMessage(appCameraFlashFound);
            infoOnFlash.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            infoOnFlash.show();
        } else {
            // Get current camera connected
            appGetCamera();
        }

        // Fire on click listener once bulbImageView is clicked
        bulbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!imageState) {
                    appTurnFlashOn(); // Turn camera flash on
                } else {
                    appTurnFlashOff(); // Turn camera flash off
                }
            }
        });
    }




    /**
     * private initializeUI method
     *
     * Initialize application user interface
     *
     * @param savedInstanceState
     */
    private void initializeUI(Bundle savedInstanceState) {
        appCameraFlashMissing = "Your device doesn't support camera flash light.";
        appCameraFlashFound = "Application has permission to camera flash feature";
        bulbImageView = (ImageView) findViewById(R.id.bulbImageViews);
        // Get system service manager
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        restoreState(savedInstanceState);
    }



    /**
     * private appGetCamera() method
     *
     * Will request the camera manager to fetch attached system cameras
     * then get a non-removable camera identity to be stored into mCameraId variable
     *
     */
    private void appGetCamera(){
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * private appTurnFlashOn method
     *
     * Determine if built SDK value is greater than Marshmallow since applications supports API level 21
     * then turn flash mode on and set flash on image on bulbImageView variable
     * and set image state to true
     *
     */
    private void appTurnFlashOn() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
                bulbImageView.setImageDrawable(getResources().getDrawable(R.drawable.on));
                imageState = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * private appTurnFlashOn method
     *
     * Determine if built SDK value is greater than Marshmallow since applications supports API level 21
     * then turn flash mode on and set flash off image on bulbImageView variable
     * and set image state to false
     *
     */
    private void appTurnFlashOff() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
                bulbImageView.setImageDrawable(getResources().getDrawable(R.drawable.off));
                imageState = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("BUILD_STATE", imageState); // Store image current state
        super.onSaveInstanceState(outState);
    }




    private void restoreState(Bundle state) {
        if (state == null) return; // no state to restore

        // Maintain current flash image state
        if (state.getBoolean("BUILD_STATE")){
            bulbImageView.setImageDrawable(getResources().getDrawable(R.drawable.on));
            imageState = true;
        } else {
            bulbImageView.setImageDrawable(getResources().getDrawable(R.drawable.off));
            imageState = false;
        }
    }


}
