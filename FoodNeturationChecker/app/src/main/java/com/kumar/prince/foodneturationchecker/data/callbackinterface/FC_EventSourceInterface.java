/*
 *     Food Inspector - Choose well to eat better
 *     Copyright (C) 2016  Frédéric Letellier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
