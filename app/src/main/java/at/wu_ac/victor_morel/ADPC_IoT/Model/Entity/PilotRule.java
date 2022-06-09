package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.concurrent.atomic.AtomicInteger;


@Entity(tableName = "pilotRule_table")
public class PilotRule implements Parcelable {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idRule;

    @ColumnInfo(name = "policyId")
    private int policyId;

    @Embedded(prefix = "rule_")
    private DataType dataType;

    @Embedded(prefix = "rule_")
    private DCR dcr;

    @Embedded(prefix = "rule_")
    private TR tr;


    //CONSTRUCTOR
    public PilotRule(DataType dataType, DCR dcr, TR tr) {
        this.dataType = dataType;
        this.dcr = dcr;
        this.tr = tr;
        this.idRule = ID_GENERATOR.getAndIncrement();
    }

    public PilotRule(DataType dataType, DCR dcr, TR tr, int id) {
        this.dataType = dataType;
        this.dcr = dcr;
        this.tr = tr;
        this.idRule = id;
    }

    @Ignore
    public PilotRule(Parcel in) {
        this.idRule = in.readInt();
        this.policyId = in.readInt();
        this.dataType = new DataType(in.readString());
        this.dcr = new DCR(new DataController(in.readString()), new DUR(new Purpose(in.readString()), in.readInt()));
        this.tr = null;
    }

    //OTHER METHODS
    public String getRule() {
        String ruleString = "";//"Rule n°" + this.idRule + "\n";
        ruleString += "Data authorized: "+dataType.getDataType();
        ruleString += "\n";
        ruleString += dcr.getDCR();
//        ruleString += tr.getTR();
        return ruleString;
    }


    //GETTERS AND SETTERS
    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public DCR getDcr() {
        return dcr;
    }

    public void setDcr(DCR dcr) {
        this.dcr = dcr;
    }

    public TR getTr() {
        return tr;
    }

    public void setTr(TR tr) {
        this.tr = tr;
    }

    public int getPolicyId() {
        return policyId;
    }

    public void setPolicyId(int policyId) {
        this.policyId = policyId;
    }

    public int getIdRule() {
        return idRule;
    }

    public void setIdRule(int idRule) {
        this.idRule = idRule;
    }


    //Parcelable-related
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idRule);
        dest.writeInt(policyId);
        dest.writeString(dataType.getDataType());
        dest.writeString(dcr.getDataController().getDCname());
        dest.writeString(dcr.getDur().getPurpose().getPurpose());
        dest.writeInt(dcr.getDur().getRetentionTime());
    }

    public static final Creator<PilotRule> CREATOR = new Creator<PilotRule>() {
        @Override
        public PilotRule createFromParcel(Parcel in) {
            return new PilotRule(in);
        }

        @Override
        public PilotRule[] newArray(int size) {
            return new PilotRule[size];
        }
    };

}
