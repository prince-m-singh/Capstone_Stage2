package com.kumar.prince.foodneturationchecker.Error;

import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_SERVER_UNREACHABLE;

/**
 *  Created by prince on 29/8/17.
 * */

public class FoodCheckServerUnreachableException extends RuntimeException {

    public FoodCheckServerUnreachableException(){
        super(STATUS_SERVER_UNREACHABLE);

    }
}
