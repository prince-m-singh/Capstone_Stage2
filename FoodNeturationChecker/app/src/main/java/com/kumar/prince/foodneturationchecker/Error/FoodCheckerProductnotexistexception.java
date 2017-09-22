package com.kumar.prince.foodneturationchecker.Error;

import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_NOT_IN_OFF_DATABASE;

/**
 *  Created by prince on 29/8/17.
 * */

public class FoodCheckerProductnotexistexception extends Exception {

    public FoodCheckerProductnotexistexception(){
        super(STATUS_NOT_IN_OFF_DATABASE);
    }

}
