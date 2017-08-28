package com.kumar.prince.foodneturationchecker.Error;

/**
 *  Created by prince on 29/8/17.
 * */

public class FC_CountryCategoryNotExistException extends Exception {

    public FC_CountryCategoryNotExistException(){
        super("This category(country) cannot be find in OpenFoodFacts database");
    }
}
