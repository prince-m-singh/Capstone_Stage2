package com.kumar.prince.foodneturationchecker.data.callbackinterface;

import android.support.annotation.NonNull;

import com.kumar.prince.foodneturationchecker.data.model.FC_Event;


/**
 * Main entry point for accessing event data.
 */
public interface FC_EventSourceInterface {

    interface Local {

        interface SaveEventCallback {

            void onEventSaved();

            void onError();

        }

        void saveEvent(@NonNull FC_Event FCEvent, @NonNull SaveEventCallback saveEventCallback);
    }



    interface SaveScanCallback {

        void onScanSaved();

        void onScanSavedWithError();

        void onError(Throwable throwable);

    }

    void saveScan(@NonNull String barcode, @NonNull SaveScanCallback saveScanCallback);

}
