package com.kumar.prince.foodneturationchecker.MVP.Presenter;

import com.kumar.prince.foodneturationchecker.Error.FoodCheckServerUnreachableException;
import com.kumar.prince.foodneturationchecker.MVP.AllMVPInterface;
import com.kumar.prince.foodneturationchecker.MVP.model.AllProductRequest;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerSearch;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;

/**
 * Created by prince on 13/9/17.
 */

public class AllProductsPresenter implements AllMVPInterface.IAllProductsPresenter
        , AllProductRequest.IAllProductResponce {
    AllMVPInterface.IAllProductsView viewProducts;
    AllProductRequest allProductRequest;

    public AllProductsPresenter(AllMVPInterface.IAllProductsView viewProducts) {
        this.viewProducts = viewProducts;
        allProductRequest = new AllProductRequest(this);
    }

    @Override
    public void requestGetAllProducts(String category, String level) {
        allProductRequest.getAllProducts(category, level);

    }

    @Override
    public void requestGetProductDetails(String barCode) {
        allProductRequest.getProductDetailsBarCode(barCode);
    }

    @Override
    public void getResponce(FoodCheckerSearch foodChecker_search) {
        viewProducts.getListOfProducts(String.valueOf(foodChecker_search.getFCProducts().size()), foodChecker_search);
    }

    @Override
    public void getError(FoodCheckServerUnreachableException e) {
        viewProducts.getError();
    }

    @Override
    public void getProductDetails(FoodCheckerProduct foodChecker_product) {
        viewProducts.getProductDetails(foodChecker_product);
    }
}
