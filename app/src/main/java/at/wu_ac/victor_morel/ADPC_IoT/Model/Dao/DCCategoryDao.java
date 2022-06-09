package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DCCategory;

@Dao
public interface DCCategoryDao {

    @Insert
    void insert(DCCategory dcCategory);

    @Delete
    void delete(DCCategory dcCategory);

    @Update
    void update(DCCategory dcCategory);

    @Query("Select * from dc_category_table")
    LiveData<List<DCCategory>> getAllDCCategories();

}
