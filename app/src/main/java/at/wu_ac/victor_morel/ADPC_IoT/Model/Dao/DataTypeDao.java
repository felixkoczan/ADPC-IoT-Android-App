package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataType;

@Dao
public interface DataTypeDao {

    @Insert
    void insert(DataType dt);

    @Delete
    void delete(DataType dt);

    @Update
    void update(DataType dt);

    @Query("Select * from datatype_table")
    LiveData<List<DataType>> getAllDataTypes();
}
