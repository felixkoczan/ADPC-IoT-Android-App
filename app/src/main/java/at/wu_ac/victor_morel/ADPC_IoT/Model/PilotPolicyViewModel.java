package at.wu_ac.victor_morel.ADPC_IoT.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DCCategory;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataController;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataType;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataTypeCategory;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotPolicy;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotRule;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.Purpose;

public class PilotPolicyViewModel extends AndroidViewModel {
    private PilotPolicyRepository repository;
    private LiveData<List<PilotPolicy>> allPolicies;
    private LiveData<List<Purpose>> allPurposes;
    private LiveData<List<DataType>> allDataTypes;
    private LiveData<List<DataController>> allDataControllers;
    private LiveData<List<PilotRule>> allRules;
    private List<PilotRule> allStaticRules;
    private LiveData<List<DataTypeCategory>> allDataTypeCategories;
    private LiveData<List<DCCategory>> allDCCategories;

    private int numPolicies;

    public PilotPolicyViewModel(@NonNull Application application) throws ExecutionException, InterruptedException {
        super(application);
        repository = new PilotPolicyRepository(application);
        allPolicies = repository.getAllPolicies();
        allPurposes = repository.getAllPurposes();
        allDataTypes = repository.getAllDataTypes();
        allDataControllers = repository.getAllDataControllers();
        numPolicies = repository.countPolicies();
        allRules = repository.getAllRulesFromPolicy();
        allStaticRules = repository.staticGetAllRulesFromPolicy(1);
        allDataTypeCategories = repository.getAllDataTypeCategories();
        allDCCategories = repository.getAllDCCategories();
    }

    public LiveData<List<DCCategory>> getAllDCCategories(){return allDCCategories;}

    public LiveData<List<DataTypeCategory>> getAllDataTypeCategories(){return allDataTypeCategories;}

    public LiveData<List<PilotPolicy>> getAllPolicies() {
        return allPolicies;
    }

    public int countPolicies() {
        return numPolicies;
    }

    public PilotPolicy getActivePolicy() throws ExecutionException, InterruptedException {
        return repository.getActivePolicy();
    }

    public DataTypeCategory getDataCategoryByName(String name) throws ExecutionException, InterruptedException {
        return repository.getDataCategoryByName(name);
    }

    public LiveData<List<PilotRule>> getAllRulesFromPolicy()  {
        return allRules;
//        return repository.getAllRulesFromPolicy(id);
    }

    public List<PilotRule> staticGetAllRulesFromPolicy(int id) throws ExecutionException, InterruptedException {
        return allStaticRules;
//        return repository.staticGetAllRulesFromPolicy(id);
    }

    public void updatePolicy(PilotPolicy policy){
        repository.updatePolicy(policy);
    }

    public void updateRule(PilotRule rule){
        repository.updateRule(rule);
    }

    public LiveData<List<Purpose>> getAllPurposes() {
        return allPurposes;
    }

    public LiveData<List<DataType>> getAllDataTypes() {
        return allDataTypes;
    }

    public LiveData<List<DataController>> getAllDataControllers() {
        return allDataControllers;
    }

    public void insertRule(PilotRule rule){
        repository.insertRule(rule);
    }

    public void deleteRule(PilotRule rule){
        repository.deleteRule(rule);
    }


}
