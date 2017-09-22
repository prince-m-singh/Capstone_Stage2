package com.kumar.prince.foodneturationchecker.activity;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.kumar.prince.fabfoodlibrary.FabFoodEntity;
import com.kumar.prince.fabfoodlibrary.FabFoodIntermediateLib;
import com.kumar.prince.foodneturationchecker.Adapter.FoodCheckerFoodListAdapter;
import com.kumar.prince.foodneturationchecker.MVP.AllMVPInterface;
import com.kumar.prince.foodneturationchecker.MVP.Presenter.AllProductsPresenter;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerSearch;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class FoodDetailsActivity extends AppCompatActivity implements AllMVPInterface.IAllProductsView,FoodCheckerFoodListAdapter.FC_FoodOnClickHandler {
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
    FabFoodIntermediateLib fabFoodIntermediateLib;
    RecyclerView recyclerView;


    AllProductsPresenter productsPresenter;
    FoodCheckerFoodListAdapter foodChecker_foodListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        recyclerView = (RecyclerView)findViewById(R.id.product_recycler_view);
        recyclerView.setHasFixedSize(true);
        fabFoodIntermediateLib=new FabFoodIntermediateLib(this);
        fabFoodIntermediateLib.dbInitialize();
        LinearLayoutManager llm = new LinearLayoutManager(FoodDetailsActivity.this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodDetailsActivity.this));

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        productsPresenter = new AllProductsPresenter(this);
        getDataFromIntent();
        foodChecker_foodListAdapter =new FoodCheckerFoodListAdapter(this);
        recyclerView.setAdapter(foodChecker_foodListAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544/1033173712");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void getDataFromIntent(){
        Intent i = getIntent();
        FoodCheckerProduct foodChecker_product = (FoodCheckerProduct) i.getSerializableExtra("sampleObject");
        Timber.d(foodChecker_product.toString());
        Timber.d(foodChecker_product.getmImageFrontUrl());
        getSupportActionBar().setTitle(foodChecker_product.getmGenericName());
        Picasso.with(getApplicationContext()).load(foodChecker_product.getmImageFrontSmallUrl()).into(imageView);
        if (foodChecker_product.getmParsableCategories().size()>0)
            productsPresenter.requestGetAllProducts(foodChecker_product.getmParsableCategories().get(0), foodChecker_product.getmNutritionGrades());
        setValueInView(foodChecker_product);
        favouriteButtoFunction(foodChecker_product);
        //recylerViewOperation();
    }


    private void setValueInView(FoodCheckerProduct foodChecker_product){
        tv_product_name_lebel.setText(getResources().getText(R.string.product_name_lebel));
        if (foodChecker_product.getmGenericName().length()> foodChecker_product.getmProductName().length()){
            if (foodChecker_product.getmProductName().length()>30)
                tv_product_name_value.setText(foodChecker_product.getmProductName().substring(1,20));
            else
                tv_product_name_value.setText(foodChecker_product.getmProductName());
        }else {
            if (foodChecker_product.getmGenericName().length()>30)
                tv_product_name_value.setText(foodChecker_product.getmGenericName().substring(1,20));
            else
                tv_product_name_value.setText(foodChecker_product.getmGenericName());
        }

        tv_barcode_name_label.setText(getResources().getText(R.string.barcode_name_label));
        tv_barcode_value.setText(foodChecker_product.getmBarcode());
        tv_grade_label.setText(getResources().getText(R.string.grade_label));
        tv_grade_value.setText(foodChecker_product.getmNutritionGrades().toUpperCase());
        tv_quantity_label.setText(getResources().getText(R.string.quantity_label));
        tv_quantity_value.setText(foodChecker_product.getmQuantity());
        tv_categories_label.setText(getResources().getText(R.string.categories_label));
        tv_categories_value.setText("");
        int i=0;
        for (String categories: foodChecker_product.getmParsableCategories()){
            if(i++ == foodChecker_product.getmParsableCategories().size() - 1){
                tv_categories_value.append(categories);
            }else {
                tv_categories_value.append(categories + "\n");
            }
        }
        tv_brand_label.setText(getResources().getText(R.string.brand_label));
        tv_brand_value.setText(foodChecker_product.getmBrands());
        title_lebel.setText(getResources().getText(R.string.title_message));
       /* tv_categories_title_label.setText(getResources().getText(R.string.categories_label));
        tv_categories_value_title_label.setText(foodChecker_product.getmParsableCategories().get(0));
        tv_grade_title_label.setText(getResources().getText(R.string.grade_label));
        tv_grade_title_value.setText(foodChecker_product.getmNutritionGrades().toUpperCase());*/

    }
    @Override
    public void getListOfProducts(String category, FoodCheckerSearch foodChecker_search) {
        Timber.d(category+" "+ foodChecker_search.toString());
        foodChecker_search.getFCProducts();
        foodChecker_foodListAdapter.setEventData(foodChecker_search.getFCProducts());

    }

    @Override
    public void getProductDetails(FoodCheckerProduct foodChecker_product) {
        newActivityStart(foodChecker_product);
    }

    @Override
    public void getError() {
    Timber.d("Error");
    }


    @Override
    public void onClick(FoodCheckerProduct foodChecker_product) {
        Timber.d(foodChecker_product.toString());
        productsPresenter.requestGetProductDetails(foodChecker_product.getmBarcode());
    }

    private void newActivityStart(FoodCheckerProduct foodChecker_product){
        Intent i = new Intent(this, FoodDetailsActivity.class);
        i.putExtra("sampleObject", foodChecker_product);
        startActivity(i);
    }

    private void favouriteButtoFunction(final FoodCheckerProduct foodChecker_product){
        if (dataStatus(foodChecker_product))
            fab.setImageResource(R.drawable.ic_favorite_red_24dp);
        else
            fab.setImageResource(R.drawable.ic_favorite_border_red_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataStatus(foodChecker_product)){
                    removeDataFromDB(foodChecker_product);
                    fab.setImageResource(R.drawable.ic_favorite_border_red_24dp);
                    Snackbar.make(view, getResources().getString(R.string.add_in_fab), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else {
                    insertDataInDB(foodChecker_product);
                    fab.setImageResource(R.drawable.ic_favorite_red_24dp);
                    Snackbar.make(view, getResources().getString(R.string.remove_from_fab), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }

    private boolean dataStatus(FoodCheckerProduct foodChecker_product){
        List<FabFoodEntity> listData=fabFoodIntermediateLib.getAllData();
        for (FabFoodEntity fabFoodEntity:listData){
            Timber.d(fabFoodEntity.toString());
            if (fabFoodEntity.getMBarcode().equals(foodChecker_product.getmBarcode())){
                return true;
            }
        }
        return false;
    }

    private boolean insertDataInDB(FoodCheckerProduct foodChecker_product){
        FabFoodEntity fabFoodEntity=new FabFoodEntity();
        fabFoodEntity.setMBarcode(foodChecker_product.getmBarcode());
        fabFoodEntity.setMBrands(foodChecker_product.getmBrands());
        fabFoodEntity.setMGenericName(foodChecker_product.getmGenericName());
        fabFoodEntity.setMImageFrontSmallUrl(foodChecker_product.getmImageFrontSmallUrl());
        fabFoodEntity.setMImageFrontUrl(foodChecker_product.getmImageFrontUrl());
        fabFoodEntity.setMNutritionGrades(foodChecker_product.getmNutritionGrades());
        fabFoodEntity.setMQuantity(foodChecker_product.getmQuantity());
        fabFoodEntity.setMProductName(foodChecker_product.getmProductName());
        fabFoodEntity.setMParsableCategories(foodChecker_product.getmParsableCategories());
        int responce=fabFoodIntermediateLib.insertData(fabFoodEntity);
        Timber.d("Data Insert" +responce);
        if (responce==0)
            return true;
        else
            return false;
    }

    private boolean removeDataFromDB(FoodCheckerProduct foodChecker_product){
        Timber.d(""+fabFoodIntermediateLib.deleteData(foodChecker_product.getmBarcode()));
        return true;
    }
}
