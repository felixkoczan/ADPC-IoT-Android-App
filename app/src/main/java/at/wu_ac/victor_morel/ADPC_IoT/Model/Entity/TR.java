package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Converters.DCRConverter;

@Entity(tableName = "tr_table")
public class TR {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idTR;

    @TypeConverters(DCRConverter.class)
    private List<DCR> DCR = new ArrayList<>();

    public String getTR() {
        String trString = "";
        for (DCR dcr : DCR) {
            trString += dcr.getDCR();
        }
        return trString;
    }


    //CONSTRUCTOR
    public TR() {
        this.idTR = ID_GENERATOR.getAndIncrement();
    }


    //GETTERS AND SETTERS
    public List<DCR> getDCR() {
        return DCR;
    }

    public void setDCR(List<DCR> dcr) {
        this.DCR = dcr;
    }

    public int getIdTR() {
        return idTR;
    }

    public void setIdTR(int idTR) {
        this.idTR = idTR;
    }
}
