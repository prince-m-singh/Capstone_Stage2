package com.kumar.prince.foodneturationchecker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumar.prince.foodneturationchecker.MVP.AllMVPInterface;
import com.kumar.prince.foodneturationchecker.MVP.Presenter.AllProductsPresenter;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class FoodDetailsActivity extends AppCompatActivity implements AllMVPInterface.IAllProductsView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.expandedImage)
    ImageView imageView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tv_product_name_lebel)
            TextView tv_product_name_lebel;
    @BindView(R.id.tv_product_name_value)
            TextView tv_product_name_value;
    @BindView(R.id.tv_barcode_name_label)
            TextView tv_barcode_name_label;
    @BindView(R.id.tv_barcode_value)
            TextView tv_barcode_value;
    @BindView(R.id.tv_grade_label)
            TextView tv_grade_label;
    @BindView(R.id.tv_grade_value)
            TextView tv_grade_value;
    @BindView(R.id.tv_quantity_label)
            TextView tv_quantity_label;
    @BindView(R.id.tv_quantity_value)
            TextView tv_quantity_value;
    @BindView(R.id.tv_categories_label)
            TextView tv_categories_label;
    @BindView(R.id.tv_categories_value)
            TextView tv_categories_value;
    @BindView(R.id.tv_brand_label)
            TextView tv_brand_label;
    @BindView(R.id.tv_brand_value)
            TextView tv_brand_value;
    @BindView(R.id.product_recycler_view)
            RecyclerView recyclerView;


    AllProductsPresenter productsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        productsPresenter = new AllProductsPresenter(this);
        getDataFromIntent();
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
        getSupportActionBar().setTitle(fc_product.getmGenericName());
        Picasso.with(getApplicationContext()).load(fc_product.getmImageFrontSmallUrl()).into(imageView);
        productsPresenter.requestGetAllProducts(fc_product.getmParsableCategories().get(0),fc_product.getmNutritionGrades());
        setValueInView(fc_product);
        recylerViewOperation();
    }

    private void recylerViewOperation(){
        recyclerView.setHasFixedSize(true);
        Timber.d( "The application stopped after this");
        LinearLayoutManager llm = new LinearLayoutManager(FoodDetailsActivity.this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
    }

    private void setValueInView(FC_Product fc_product){
        tv_product_name_lebel.setText(getResources().getText(R.string.product_name_lebel));
        tv_product_name_value.setText(fc_product.getmGenericName());
        tv_barcode_name_label.setText(getResources().getText(R.string.barcode_name_label));
        tv_barcode_value.setText(fc_product.getmBarcode());
        tv_grade_label.setText(getResources().getText(R.string.grade_label));
        tv_grade_value.setText(fc_product.getmNutritionGrades().toUpperCase());
        tv_quantity_label.setText(getResources().getText(R.string.quantity_label));
        tv_quantity_value.setText(fc_product.getmQuantity());
        tv_categories_label.setText(getResources().getText(R.string.categories_label));
        tv_categories_value.setText("");
        int i=0;
        for (String categories:fc_product.getmParsableCategories()){
            if(i++ == fc_product.getmParsableCategories().size() - 1){
                tv_categories_value.append(categories);
            }else {
                tv_categories_value.append(categories + "\n");
            }
        }
        tv_brand_label.setText(getResources().getText(R.string.brand_label));
        tv_brand_value.setText(fc_product.getmBrands());
    }
    @Override
    public void getListOfProducts(String category, String level) {
        Timber.d(category+" "+level);

    }

    @Override
    public void getError() {
    Timber.d("Error");
    }


}
