package at.wu_ac.victor_morel.ADPC_IoT.Model.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotRule;

@Dao
public interface PilotRuleDao {

    @Insert
    void insert(PilotRule rule);

    @Delete
    void delete(PilotRule rule);

    @Update
    void update(PilotRule rule);

    @Query("select * from pilotRule_table")
    LiveData<List<PilotRule>> getAllRulesFromPolicy();

    @Query("select * from pilotRule_table")
    List<PilotRule> staticGetAllRulesFromPolicy();

//    @Query("Select * from rule_table where data_controllers like :datacontroller")
//    LiveData<List<PilotRule>> getRulesMatchingDC(DataController datacontroller); //example of query retrieving livedata
}
