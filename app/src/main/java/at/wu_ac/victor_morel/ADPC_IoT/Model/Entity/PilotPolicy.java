package at.wu_ac.victor_morel.ADPC_IoT.Model.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Converters.RuleConverter;

@Entity(tableName = "pilotPolicy_table")
public class PilotPolicy {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    //ATTRIBUTES
    @PrimaryKey
    private int idPolicy;

    @TypeConverters(RuleConverter.class)
    private List<PilotRule> rules;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "active")
    private boolean active;


    //CONSTRUCTOR
    public PilotPolicy(List<PilotRule> rules, String name, boolean active) {
        this.name = name;
        this.rules = rules;
        this.active = active;
        this.idPolicy = ID_GENERATOR.getAndIncrement();
    }


    //OTHER METHODS
    public String getPolicyAsString() {
        String myPolicy = "";
        myPolicy += this.getName();
        myPolicy += ": \n";
        for (PilotRule r : rules) {
            myPolicy += r.getRule();
            myPolicy += "\n"; //\r perhaps
        }
        return myPolicy;
    }

    public void addNewRule(PilotRule newRule) {
        this.rules.add(newRule);
    }

    public void deleteRule(PilotRule oldRule){
        for (PilotRule rule : this.rules) {
            if (rule.getIdRule()==oldRule.getIdRule()){
                int index = this.rules.indexOf(rule);
                this.rules.remove(index);
            }
        }
    }

    public void updateRule(PilotRule ruleToUpdate) {
        for (PilotRule rule : this.rules) {
            if (rule.getIdRule()==ruleToUpdate.getIdRule()){
                int index = this.rules.indexOf(rule);
                this.rules.set(index, ruleToUpdate) ;
            }
        }
    }


    //GETTERS AND SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PilotRule> getRules() {
        return rules;
    }

    public void setRules(List<PilotRule> rules) {
        this.rules = rules;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getIdPolicy() {
        return idPolicy;
    }

    public void setIdPolicy(int idPolicy) {
        this.idPolicy = idPolicy;
    }
}
