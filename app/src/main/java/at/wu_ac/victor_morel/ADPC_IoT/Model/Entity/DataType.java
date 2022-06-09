package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;

@Entity(tableName = "datatype_table")
public class DataType {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idDataType;

    @ColumnInfo(name = "datatype")
    private String dataType;

    //CONSTRUCTOR
    public DataType(String dataType) {
        this.dataType = dataType;
        this.idDataType = ID_GENERATOR.getAndIncrement();
    }


    //GETTERS AND SETTERS
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getIdDataType() {
        return idDataType;
    }

    public void setIdDataType(int idDataType) {
        this.idDataType = idDataType;
    }
}
