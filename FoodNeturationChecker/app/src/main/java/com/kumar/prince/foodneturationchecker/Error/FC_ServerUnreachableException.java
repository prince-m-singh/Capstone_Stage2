package com.kumar.prince.foodneturationchecker.Error;

/**
 *  Created by prince on 29/8/17.
 * */

public class FC_ServerUnreachableException extends RuntimeException {

    public FC_ServerUnreachableException(){
        super("OpenFoodFacts database is unreachable");

    }
}
