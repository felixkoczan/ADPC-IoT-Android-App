package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotPolicy;

@Dao
public interface PilotPolicyDao {

    @Insert
    void insert(PilotPolicy policy);

    @Delete
    void delete(PilotPolicy policy);

    @Update
    void update(PilotPolicy policy);

    @Query("SELECT * from pilotPolicy_table LIMIT 1")
    PilotPolicy[] getAnyPolicy();

    @Query("Select * from pilotPolicy_table")
    LiveData<List<PilotPolicy>> getAllPolicies();

    @Query("select Count(*) from pilotPolicy_table")
    int countPolicies();

    @Query("select * from pilotPolicy_table where active = 1")
    PilotPolicy getActivePolicy();

}
