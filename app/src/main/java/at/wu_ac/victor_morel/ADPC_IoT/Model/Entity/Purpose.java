package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "purpose_table")
public class Purpose {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idPurpose;

    @ColumnInfo(name = "purpose")
    private String purpose;

    //CONSTRUCTOR
    public Purpose(String purpose) {
        this.purpose = purpose;
        this.idPurpose = ID_GENERATOR.getAndIncrement();
    }


    //GETTERS AND SETTERS
    public String getPurpose() {
        return this.purpose;
    }

    public void setPurpose(String purpose){
        this.purpose = purpose;
    }

    public int getIdPurpose() {
        return idPurpose;
    }

    public void setIdPurpose(int idPurpose) {
        this.idPurpose = idPurpose;
    }
}
