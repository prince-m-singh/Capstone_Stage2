package com.kumar.prince.foodneturationchecker.MVP.Presenter;

import com.kumar.prince.foodneturationchecker.Error.FC_ServerUnreachableException;
import com.kumar.prince.foodneturationchecker.MVP.AllMVPInterface;
import com.kumar.prince.foodneturationchecker.MVP.model.AllProductRequest;
import com.kumar.prince.foodneturationchecker.communication.FC_Search;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;

/**
 * Created by prince on 13/9/17.
 */

public class AllProductsPresenter implements AllMVPInterface.IAllProductsPresenter
        ,AllProductRequest.IAllProductResponce {
    AllMVPInterface.IAllProductsView viewProducts;
    AllProductRequest allProductRequest;

    public AllProductsPresenter(AllMVPInterface.IAllProductsView viewProducts) {
        this.viewProducts=viewProducts;
        allProductRequest=new AllProductRequest(this);
    }

    @Override
    public void requestGetAllProducts(String category, String level) {
        allProductRequest.getAllProducts(category,level);

    }

    @Override
    public void requestGetProductDetails(String barCode) {
        allProductRequest.getProductDetailsBarCode(barCode);
    }

    @Override
    public void getResponce(FC_Search fc_search) {
        viewProducts.getListOfProducts(String.valueOf(fc_search.getFCProducts().size()),fc_search);
    }

    @Override
    public void getError(FC_ServerUnreachableException e) {
        viewProducts.getError();
    }

    @Override
    public void getProductDetails(FC_Product fc_product) {
        viewProducts.getProductDetails(fc_product);
    }
}
