package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DUR;

@Dao
public interface DURDao {
    @Insert
    void insert(DUR dur);

    @Delete
    void delete(DUR dur);

    @Update
    void update(DUR dur);

    @Query("Select * from dur_table")
    LiveData<List<DUR>> getAllDUR();
}
