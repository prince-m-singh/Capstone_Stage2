package com.kumar.prince.foodneturationchecker.MVP;

import com.kumar.prince.foodneturationchecker.communication.FC_Search;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;

/**
 * Created by prince on 13/9/17.
 */

public  interface AllMVPInterface {

    interface IFoodCheckerView{
        void onError();
        void onResult();

    }

    interface IAllProductsView{
        void getListOfProducts(String category, FC_Search fc_search);
        void getProductDetails(FC_Product fc_product);
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
