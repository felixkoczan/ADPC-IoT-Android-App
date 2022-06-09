package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataTypeCategory;

@Dao
public interface DataTypeCategoryDao {

    @Insert
    void insert(DataTypeCategory dataTypeCategory);

    @Delete
    void delete(DataTypeCategory dataTypeCategory);

    @Update
    void update(DataTypeCategory dataTypeCategory);

    @Query("Select * from datatype_category_table")
    LiveData<List<DataTypeCategory>> getAllDataTypeCategories();

    @Query("Select * from datatype_category_table where datatype_category_name like :name")
    DataTypeCategory getDataCategoryByName(String name);
}
