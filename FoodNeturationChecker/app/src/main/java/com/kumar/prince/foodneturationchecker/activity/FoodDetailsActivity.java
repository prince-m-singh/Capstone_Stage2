package com.kumar.prince.foodneturationchecker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class FoodDetailsActivity extends AppCompatActivity {
    ImageView imageView;
    Context context;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView=(ImageView) findViewById(R.id.expandedImage);

        getDataFromIntent();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void getDataFromIntent(){
        Intent i = getIntent();
        FC_Product fc_product = (FC_Product) i.getSerializableExtra("sampleObject");
        Timber.d(fc_product.toString());
        Timber.d(fc_product.getmImageFrontUrl());
        getSupportActionBar().setTitle(fc_product.getmProductName());
        Picasso.with(getApplicationContext()).load(fc_product.getmImageFrontSmallUrl()).into(imageView);
    }
}
