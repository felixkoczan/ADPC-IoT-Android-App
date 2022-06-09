package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.Purpose;

@Dao
public interface PurposeDao {

    @Insert
    void insert(Purpose purpose);

    @Delete
    void delete(Purpose purpose);

    @Update
    void update(Purpose purpose);

    @Query("Select * from purpose_table")
    LiveData<List<Purpose>> getAllPurposes();
}
