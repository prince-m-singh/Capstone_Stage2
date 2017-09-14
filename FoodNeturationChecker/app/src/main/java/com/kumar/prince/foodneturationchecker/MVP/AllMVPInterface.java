package com.kumar.prince.foodneturationchecker.MVP;

/**
 * Created by prince on 13/9/17.
 */

public  interface AllMVPInterface {

    interface IFoodCheckerView{
        void onError();
        void onResult();

    }

    interface IAllProductsView{
        void getListOfProducts(String category, String level);
        void getError();
    }

    interface IFoodCheckerPresenter{
        void resultGetProducts(String category, String level);

    }

    interface IAllProductsPresenter{
        void requestGetAllProducts(String category, String level);
    }
}
