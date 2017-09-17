package com.kumar.prince.fabfoodlibrary;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by prince on 6/9/17.
 */
@Dao
public interface FabFoodDbDao {
    @Query("SELECT * FROM FabFood")
    List<FabFoodEntity> getAllData();

    @Query("SELECT COUNT(*) from FabFood")
    int countUsers();

    @Insert
    void insertAll(FabFoodEntity... fabFoodEntities);

    @Query("SELECT * FROM FabFood where mBarcode LIKE  :mBarcode")
    FabFoodEntity findByBarCode(String mBarcode);

    @Delete
    void delete(FabFoodEntity fabFoodEntity);
    @Query("DELETE FROM FabFood where mBarcode LIKE  :mBarcode")
    int deleteFabData(String mBarcode);
}
