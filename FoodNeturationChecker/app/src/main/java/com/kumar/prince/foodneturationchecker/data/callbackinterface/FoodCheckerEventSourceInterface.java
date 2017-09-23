package com.kumar.prince.foodneturationchecker.data.callbackinterface;

import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerEvent;


/**
 * Main entry point for accessing event data.
 */
public interface FoodCheckerEventSourceInterface {

    void saveScan(@NonNull String barcode, @NonNull SaveScanCallback saveScanCallback);


    interface Local {

        void saveEvent(@NonNull FoodCheckerEvent FCEvent, @NonNull SaveEventCallback saveEventCallback);

        interface SaveEventCallback {

            void onEventSaved();

            void onError();

        }
    }

    interface SaveScanCallback {

        void onScanSaved();

        void onScanSavedWithError();

        void onError(Throwable throwable);

    }

}
