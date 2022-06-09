package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataController;

@Dao
public interface DataControllersDao {

    @Insert
    void insert(DataController dc);

    @Delete
    void delete(DataController dc);

    @Update
    void update(DataController dc);

    @Query("Select * from dc_table")
    LiveData<List<DataController>> getAllDataControllers();

}
