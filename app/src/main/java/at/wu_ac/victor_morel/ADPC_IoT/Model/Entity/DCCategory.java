package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Converters.DCConverter;


@Entity(tableName = "dc_category_table")
public class DCCategory {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idDCCategory;

    @TypeConverters(DCConverter.class)
    private List<DataController> dataControllers;

    @ColumnInfo(name = "dc_category_name")
    private String name;

    //CONSTRUCTOR
    public DCCategory(String name, List<DataController> dataControllers) {
        this.name = name;
        this.dataControllers = dataControllers;
        this.idDCCategory = ID_GENERATOR.getAndIncrement();
    }


    //GETTERS AND SETTERS
    public int getIdDCCategory() {
        return idDCCategory;
    }

    public void setIdDCCategory(int idCategory) {
        this.idDCCategory = idCategory;
    }

    public List<DataController> getDataControllers() {
        return dataControllers;
    }

    public void setDataControllers(List<DataController> dataControllers) {
        this.dataControllers = dataControllers;
    }

    public void addDC (DataController dc){
        this.dataControllers.add(dc);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
