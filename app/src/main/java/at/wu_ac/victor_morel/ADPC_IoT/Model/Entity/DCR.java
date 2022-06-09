package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;


@Entity(tableName = "dcr_table")
public class DCR {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idDCR;

    @Embedded(prefix = "dcr_")
    private DataController dataController;

    @Embedded(prefix = "dcr_")
    private DUR dur;


    //CONSTRUCTOR
    public DCR(DataController dataController, DUR dur) {
        this.dur = dur;
        this.dataController = dataController;
        this.idDCR = ID_GENERATOR.getAndIncrement();
    }


    //OTHER METHODS
    public String getDCR() {
        String dcrString = "Data controller: ";
        dcrString += dataController.getDCname();
        dcrString += dur.getDUR();
        return dcrString;
    }


    //GETTERS AND SETTERS
    public DataController getDataController() {
        return dataController;
    }

    public void setDataController(DataController dataController) {
        this.dataController = dataController;
    }

    public DUR getDur() {
        return this.dur;
    }

    public void setDur(DUR dur) {
        this.dur = dur;
    }

    public int getIdDCR() {
        return idDCR;
    }

    public void setIdDCR(int idDCR) {
        this.idDCR = idDCR;
    }


//    @TypeConverter
//    public static List<DCR> stringToRule(String data) {
//        if (data == null) {
//            return Collections.emptyList();
//        }
//
//        Type listType = new TypeToken<List<DCR>>() {
//        }.getType();
//
//        return gson.fromJson(data, listType);
//    }
//
//    @TypeConverter
//    public static String DCRListToString(List<DCR> someObjects) {
//        return gson.toJson(someObjects);
//    }
}
