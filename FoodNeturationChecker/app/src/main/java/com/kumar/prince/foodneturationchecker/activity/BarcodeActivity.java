package com.kumar.prince.foodneturationchecker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.kumar.prince.foodneturationchecker.R;

import static com.kumar.prince.foodneturationchecker.activity.ScanBarcodeActivity.AUTOFOCUS_ENABLE;
import static com.kumar.prince.foodneturationchecker.activity.ScanBarcodeActivity.GETRESULT;

/**
 * Created by prince on 2/9/17.
 */

public class BarcodeActivity extends AppCompatActivity {

    TextView tvResult;
    Button btnScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        tvResult = (TextView) findViewById(R.id.tvResult);
        btnScanner = (Button) findViewById(R.id.btnScanner);

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanBarcode();
            }
        });
    }

    private void scanBarcode(){
        int barCodeRequestCode = 1000;
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        intent.putExtra(AUTOFOCUS_ENABLE,true);
        startActivityForResult(intent,barCodeRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1000){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra(GETRESULT);
                    tvResult.setText(barcode.displayValue);
                }
            }
        }



        super.onActivityResult(requestCode, resultCode, data);
    }
}
