package com.kumar.prince.foodneturationchecker.Error;

/**
 * Created by prince on 29/8/17.
 */

public class FoodCheckCountryCategoryNotExistException extends Exception {

    public FoodCheckCountryCategoryNotExistException() {
        super("This category(country) cannot be find in OpenFoodFacts database");
    }
}
