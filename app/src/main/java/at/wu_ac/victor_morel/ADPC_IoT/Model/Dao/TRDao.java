package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.TR;

@Dao
public interface TRDao {
    @Insert
    void insert(TR tr);

    @Delete
    void delete(TR tr);

    @Update
    void update(TR tr);

    @Query("Select * from tr_table")
    LiveData<List<TR>> getAllTR();
}
