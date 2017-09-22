package com.kumar.prince.foodneturationchecker.Barcode;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.kumar.prince.foodneturationchecker.R;

import java.io.IOException;

/**
 * Created by prince on 2/9/17.
 */

public class ScanBarcodeActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    Context contextScanBarcode;

    Toolbar toolbar;
    ActionBar actionBar;

    public final static String AUTOFOCUS_ENABLE="AUTOFOCUS_ENABLE";
    public final static String GETRESULT="barcode";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanbarcode);
        contextScanBarcode = getApplicationContext();
        /*Note : Getting from User whether to use AutoFocus in Camera*/
        boolean blnAutoFocusEnable = this.getIntent().getBooleanExtra(AUTOFOCUS_ENABLE,false);
        /*Note : UI Function for this class*/
        setUIScanBarcode();
        /*Note : call Barcode Detector function*/
        createCameraSource(blnAutoFocusEnable);
    }

    private void setUIScanBarcode(){

        /*Note : Setting custom toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
           // setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        /*Note : Creating Back Arrow for back button*/
        final Drawable upArrow = ContextCompat.getDrawable(contextScanBarcode, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(contextScanBarcode, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        /*Note : Setting Custom ActionBar with Title and Back arrow button*/
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(upArrow);
        //String strLogin = getResources().getString(R.string.Login);
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + "Scanner" + "</font>"));
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*Note : Reference of SurfaceView*/
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
    }

    /*Note : Function for Barcode Detector*/
    private void createCameraSource(Boolean AutoFocusEnable) {
        /*Note : Reference of Barcode Detector*/
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        /*Note : Reference of CameraSource for detecting barcode through camera.*/
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(AutoFocusEnable)
                /*.setRequestedPreviewSize(1600, 1024)*/
                .build();

        /*Note : SurfaceView holder creating after that start detecting through camera*/
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (getPermissionCarmera()) {
                    try {
                        if (ActivityCompat.checkSelfPermission(ScanBarcodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        /*NOte : Started detecting through camera*/
                        cameraSource.start(surfaceView.getHolder());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        /*Note : BarcodeDetector*/
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                /*Note : After Barcode Detector. It can detect more than one barcode,
                             in that taking first bacode value.
                         Setting the result through Intent putExtra.    */
                final SparseArray<Barcode> sparseArray = detections.getDetectedItems();
                if(sparseArray.size()>0){
                    Intent intent = new Intent();
                    intent.putExtra(GETRESULT,sparseArray.valueAt(0));
                    setResult(CommonStatusCodes.SUCCESS,intent);
                    finish();
                }
            }
        });
    }

    /*NOte : Permission for Camera*/
    private Boolean getPermissionCarmera(){
        if (ContextCompat.checkSelfPermission(contextScanBarcode, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.CAMERA))
            {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(contextScanBarcode);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(getResources().getString(R.string.permission_title));
                alertBuilder.setMessage(getResources().getString(R.string.permission_message));
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) contextScanBarcode,
                                new String[]{Manifest.permission.CAMERA}, 0);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                /*Note : Asking permission to the Device User through Auto Android Dialog box*/
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CAMERA}, 0);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        /*Note : If Back button pressed*/
        if(itemId == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
