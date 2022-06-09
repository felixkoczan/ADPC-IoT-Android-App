package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;


@Entity(tableName = "dur_table")
public class DUR {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idDUR;

    @Embedded(prefix = "dur_")
    private Purpose purpose;

    @ColumnInfo(name = "retention_time")
    private int retentionTime;

    //CONSTRUCTOR
    public DUR(Purpose purpose, int retentionTime) {
        this.purpose = purpose;
        this.retentionTime = retentionTime;
        this.idDUR = ID_GENERATOR.getAndIncrement();
    }

    public String getDUR() {
        String durString = "\nFor purpose: ";
        durString += this.getPurpose().getPurpose();
        durString += " and retention time ";
        durString += String.valueOf(this.getRetentionTime()) + " days \n";
        return durString;
    }


    //GETTERS AND SETTERS
    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public int getRetentionTime() {
        return retentionTime;
    }

    public void setRetentionTime(int retentionTime) {
        this.retentionTime = retentionTime;
    }

    public int getIdDUR() {
        return idDUR;
    }

    public void setIdDUR(int idDUR) {
        this.idDUR = idDUR;
    }
}
