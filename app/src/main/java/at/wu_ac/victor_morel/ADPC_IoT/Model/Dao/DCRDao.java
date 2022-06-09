package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DCR;

@Dao
public interface DCRDao {
    @Insert
    void insert(DCR dcr);

    @Delete
    void delete(DCR dcr);

    @Update
    void update(DCR dcr);

    @Query("Select * from dcr_table")
    LiveData<List<DCR>> getAllDCR();
}
