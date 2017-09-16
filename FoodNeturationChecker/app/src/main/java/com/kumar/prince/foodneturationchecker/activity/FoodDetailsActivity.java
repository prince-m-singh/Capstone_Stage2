package com.kumar.prince.foodneturationchecker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.kumar.prince.foodneturationchecker.Adapter.FC_FoodListAdapter;
import com.kumar.prince.foodneturationchecker.MVP.AllMVPInterface;
import com.kumar.prince.foodneturationchecker.MVP.Presenter.AllProductsPresenter;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.communication.FC_Search;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class FoodDetailsActivity extends AppCompatActivity implements AllMVPInterface.IAllProductsView,FC_FoodListAdapter.FC_FoodOnClickHandler {
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
    @BindView(R.id.title_lebel)
            TextView title_lebel;
   /* @BindView(R.id.tv_categories_title_label)
            TextView tv_categories_title_label;
    @BindView(R.id.tv_categories_value_title_label)
            TextView tv_categories_value_title_label;
    @BindView(R.id.tv_grade_title_label)
            TextView tv_grade_title_label;
    @BindView(R.id.tv_grade_title_value)
            TextView tv_grade_title_value;*/
    //@BindView(R.id.product_recycler_view)
            RecyclerView recyclerView;


    AllProductsPresenter productsPresenter;
    FC_FoodListAdapter fc_foodListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        recyclerView = (RecyclerView)findViewById(R.id.product_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(FoodDetailsActivity.this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodDetailsActivity.this));

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        productsPresenter = new AllProductsPresenter(this);
        getDataFromIntent();
        fc_foodListAdapter=new FC_FoodListAdapter(this);
        recyclerView.setAdapter(fc_foodListAdapter);
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
        //recylerViewOperation();
    }


    private void setValueInView(FC_Product fc_product){
        tv_product_name_lebel.setText(getResources().getText(R.string.product_name_lebel));
        if (fc_product.getmGenericName().length()>fc_product.getmProductName().length()){
            if (fc_product.getmProductName().length()>20)
                tv_product_name_value.setText(fc_product.getmProductName().substring(1,20));
            else
                tv_product_name_value.setText(fc_product.getmProductName());
        }else {
            if (fc_product.getmGenericName().length()>20)
                tv_product_name_value.setText(fc_product.getmGenericName().substring(1,20));
            else
                tv_product_name_value.setText(fc_product.getmGenericName());
        }

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
        title_lebel.setText(getResources().getText(R.string.title_message));
       /* tv_categories_title_label.setText(getResources().getText(R.string.categories_label));
        tv_categories_value_title_label.setText(fc_product.getmParsableCategories().get(0));
        tv_grade_title_label.setText(getResources().getText(R.string.grade_label));
        tv_grade_title_value.setText(fc_product.getmNutritionGrades().toUpperCase());*/

    }
    @Override
    public void getListOfProducts(String category, FC_Search fc_search) {
        Timber.d(category+" "+fc_search.toString());
        fc_search.getFCProducts();
        fc_foodListAdapter.setEventData(fc_search.getFCProducts());

    }

    @Override
    public void getProductDetails(FC_Product fc_product) {
        newActivityStart(fc_product);
    }

    @Override
    public void getError() {
    Timber.d("Error");
    }


    @Override
    public void onClick(FC_Product fc_product) {
        Timber.d(fc_product.toString());
        productsPresenter.requestGetProductDetails(fc_product.getmBarcode());
    }

    private void newActivityStart(FC_Product fc_product){
        Intent i = new Intent(this, FoodDetailsActivity.class);
        i.putExtra("sampleObject", fc_product);
        startActivity(i);
    }
}
