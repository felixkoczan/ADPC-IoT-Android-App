package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "dc_table")
public class DataController {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idDC;

    @ColumnInfo(name = "dc_name")
    private String DCname;


    //CONSTRUCTOR
    public DataController(String DCname) {
        this.DCname = DCname;
        this.idDC = ID_GENERATOR.getAndIncrement();
    }


    //GETTERS AND SETTERS
    public String getDCname() {
        return DCname;
    }

    public void setDCname(String DCname) {
        this.DCname = DCname;
    }

    public int getIdDC() {
        return idDC;
    }

    public void setIdDC(int idDC) {
        this.idDC = idDC;
    }
}
