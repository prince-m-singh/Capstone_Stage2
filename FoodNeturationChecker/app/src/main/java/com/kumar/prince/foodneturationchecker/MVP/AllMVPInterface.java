package com.kumar.prince.foodneturationchecker.MVP;

import com.kumar.prince.foodneturationchecker.communication.FoodCheckerSearch;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;

/**
 * Created by prince on 13/9/17.
 */

public  interface AllMVPInterface {

    interface IFoodCheckerView{
        void onError();
        void onResult();

    }

    interface IAllProductsView{
        void getListOfProducts(String category, FoodCheckerSearch foodChecker_search);
        void getProductDetails(FoodCheckerProduct foodChecker_product);
        void getError();
    }

    interface IFoodCheckerPresenter{
        void resultGetProducts(String category, String level);

    }

    interface IAllProductsPresenter{
        void requestGetAllProducts(String category, String level);
        void requestGetProductDetails(String barCode);
    }
}
