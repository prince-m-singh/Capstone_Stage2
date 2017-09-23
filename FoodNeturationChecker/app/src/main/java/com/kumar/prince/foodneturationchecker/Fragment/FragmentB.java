package com.kumar.prince.foodneturationchecker.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kumar.prince.fabfoodlibrary.FabFoodEntity;
import com.kumar.prince.fabfoodlibrary.FabFoodIntermediateLib;
import com.kumar.prince.foodneturationchecker.Adapter.FoodCheckerFavouriteFoodAdapter;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.activity.FoodDetailsActivity;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;

import java.util.List;

import timber.log.Timber;

/**
 * Created by prince on 14/9/17.
 */

public class FragmentB extends Fragment implements
        FoodCheckerFavouriteFoodAdapter.FavouriteFoodOnClickHandler,
        LoaderManager.LoaderCallbacks<List<FabFoodEntity>> {


    private static final int TASK_LOADER_ID = 1;
    ListView list;
    FoodCheckerFavouriteFoodAdapter fc_favouriteFoodAdapter;
    FabFoodIntermediateLib fabFoodIntermediateLib;
    private RecyclerView recyclerView;
    private List<FabFoodEntity> fabFoodEntityList;


    public FragmentB() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fabFoodIntermediateLib = new FabFoodIntermediateLib(getActivity());
        fc_favouriteFoodAdapter = new FoodCheckerFavouriteFoodAdapter(this);
        getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        Timber.d("The application stopped after this");
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        /*list.setLayoutManager(llm);
        list.setAdapter( adapter );*/
        // ListView
        recyclerView.setLayoutManager(llm);
        Timber.d("Set Layout");
        Timber.d("Adapter instance");
        recyclerView.setAdapter(fc_favouriteFoodAdapter);
//        fabFoodEntityList=fabFoodIntermediateLib.getAllData();
        Timber.d("Adapter");


        return view;
    }

    @Override
    public void onClick(FabFoodEntity fabFoodEntity) {
        foodDetailsActivity(convertData(fabFoodEntity));

    }

    private void foodDetailsActivity(FoodCheckerProduct foodChecker_product) {
        Intent i = new Intent(getActivity(), FoodDetailsActivity.class);
        i.putExtra("sampleObject", foodChecker_product);
        startActivity(i);
    }

    private FoodCheckerProduct convertData(FabFoodEntity fabFoodEntity) {
        FoodCheckerProduct foodChecker_product = new FoodCheckerProduct();
        foodChecker_product.setmBarcode(fabFoodEntity.getMBarcode());
        foodChecker_product.setmBrands(fabFoodEntity.getMBrands());
        foodChecker_product.setmGenericName(fabFoodEntity.getMGenericName());
        foodChecker_product.setmImageFrontSmallUrl(fabFoodEntity.getMImageFrontSmallUrl());
        foodChecker_product.setmParsableCategories(fabFoodEntity.getMParsableCategories());
        foodChecker_product.setmProductName(fabFoodEntity.getMProductName());
        foodChecker_product.setmQuantity(fabFoodEntity.getMQuantity());
        foodChecker_product.setmImageFrontUrl(fabFoodEntity.getMImageFrontUrl());
        foodChecker_product.setmNutritionGrades(fabFoodEntity.getMNutritionGrades());
        return foodChecker_product;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<FabFoodEntity>>(this.getActivity()) {
            List<FabFoodEntity> fabFoodEntityList = null;

            @Override
            protected void onStartLoading() {
                if (fabFoodEntityList != null) {
                    deliverResult(fabFoodEntityList);
                } else {
                    forceLoad();
                }
                //   super.onStartLoading();
            }

            @Override
            public List<FabFoodEntity> loadInBackground() {
                fabFoodIntermediateLib.dbInitialize();

                try {
                    List<FabFoodEntity> fabFoodEntityList1 = fabFoodIntermediateLib.getAllData();
                    return fabFoodEntityList1;

                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;

                }

            }

            @Override
            public void deliverResult(List<FabFoodEntity> data) {
                fabFoodEntityList = data;
                super.deliverResult(data);
            }
        };


    }

    @Override
    public void onLoadFinished(Loader<List<FabFoodEntity>> loader, List<FabFoodEntity> data) {
        fabFoodEntityList = data;
        fc_favouriteFoodAdapter.setFavouriteFoodData(fabFoodEntityList);

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public void restartLoader() {
        getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

}
