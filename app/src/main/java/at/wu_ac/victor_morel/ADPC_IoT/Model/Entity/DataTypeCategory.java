package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Converters.DataTypeConverter;


@Entity(tableName = "datatype_category_table")
public class DataTypeCategory {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idDataTypeCategory;

    @TypeConverters(DataTypeConverter.class)
    private List<DataType> dataTypes;

    @ColumnInfo(name = "datatype_category_name")
    private String name;

    //CONSTRUCTOR
    public DataTypeCategory(String name, List<DataType> dataTypes) {
        this.name = name;
        this.dataTypes = dataTypes;
        this.idDataTypeCategory = ID_GENERATOR.getAndIncrement();
    }

    //GETTERS AND SETTERS
    public int getIdDataTypeCategory() {
        return idDataTypeCategory;
    }

    public void setIdDataTypeCategory(int idCategory) {
        this.idDataTypeCategory = idCategory;
    }

    public List<DataType> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(List<DataType> dataTypes) {
        this.dataTypes = dataTypes;
    }

    public String getName (){
        return this.name;
    }

    public void addDataType (DataType dt){
        this.dataTypes.add(dt);
    }
}
