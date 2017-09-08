package com.kumar.prince.fabfoodlibrary;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;

import timber.log.Timber;

/**
 * Created by prince on 6/9/17.
 */

public class FabFoodIntermediateLib {

    private Context context;
    private AppDataBase appDataBase;
    public FabFoodIntermediateLib(Context context){
        this.context=context;
    }

    public void dbInitialize(){
        appDataBase=AppDataBase.getAppDatabase(context);

    }


    public int insertData(FabFoodEntity fabFoodEntity){
        try{
            appDataBase.fabFoodDbDao().insertAll(fabFoodEntity);
            Timber.d("Inserted");
        }catch (SQLiteConstraintException ex){
            ex.printStackTrace();
            Timber.d(ex.getMessage());
            return 1;
        }catch (IllegalStateException ex){
            ex.printStackTrace();
            Timber.d(ex.getMessage());
            return 2;
        }
        return 0;
    }

    public List<FabFoodEntity> getAllData(){
        return appDataBase.fabFoodDbDao().getAllData();
    }
}
