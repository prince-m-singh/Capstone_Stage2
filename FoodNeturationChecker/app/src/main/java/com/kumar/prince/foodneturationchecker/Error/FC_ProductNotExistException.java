package com.kumar.prince.foodneturationchecker.Error;

/**
 *  Created by prince on 29/8/17.
 * */

public class FC_ProductNotExistException extends Exception {

    public FC_ProductNotExistException(){
        super("This product cannot be find in OpenFoodFacts database");
    }

}
