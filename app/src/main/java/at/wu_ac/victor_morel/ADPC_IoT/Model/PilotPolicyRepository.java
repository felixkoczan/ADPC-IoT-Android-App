package at.wu_ac.victor_morel.ADPC_IoT.Model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DCCategoryDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DataControllersDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DataTypeCategoryDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DataTypeDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.PilotPolicyDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.PilotRuleDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.PurposeDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DCCategory;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataController;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataType;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataTypeCategory;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotPolicy;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotRule;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.Purpose;

public class PilotPolicyRepository {
    private PilotPolicyDao policyDao;
    private LiveData<List<PilotPolicy>> allPolicies;

    private PilotRuleDao ruleDao;
    private LiveData<List<PilotRule>> allRules;
    private List<PilotRule> allStaticRules;

    private PurposeDao purposeDao;
    private LiveData<List<Purpose>> allPurposes;

    private DataTypeDao dataTypeDao;
    private LiveData<List<DataType>> allDataTypes;

    private DataControllersDao dataControllersDao;
    private LiveData<List<DataController>> allDataControllers;

    private DCCategoryDao dcCategoryDao;
    private LiveData<List<DCCategory>> allDCCategories;

    private DataTypeCategoryDao datatypeCategoryDao;
    private LiveData<List<DataTypeCategory>> allDataTypeCategories;

    public PilotPolicyRepository(Application application) {
        PilotPolicyRoomDatabase db = PilotPolicyRoomDatabase.getDatabase(application);
        policyDao = db.policyDao();
        allPolicies = policyDao.getAllPolicies();

        purposeDao = db.purposeDao();
        allPurposes = purposeDao.getAllPurposes();

        dataTypeDao = db.dataTypeDao();
        allDataTypes = dataTypeDao.getAllDataTypes();

        dataControllersDao = db.dataControllersDao();
        allDataControllers = dataControllersDao.getAllDataControllers();

        dcCategoryDao = db.dcCategoryDao();
        allDCCategories = dcCategoryDao.getAllDCCategories();

        datatypeCategoryDao = db.datatypeCategoryDao();
        allDataTypeCategories = datatypeCategoryDao.getAllDataTypeCategories();

        ruleDao = db.ruleDao();
        allRules = ruleDao.getAllRulesFromPolicy();
    }

    public LiveData<List<PilotPolicy>> getAllPolicies() {
        return allPolicies;
    }

    public LiveData<List<DCCategory>> getAllDCCategories() {
        return allDCCategories;
    }

    public LiveData<List<DataTypeCategory>> getAllDataTypeCategories() {
        return allDataTypeCategories;
    }


    public PilotPolicy getActivePolicy() throws ExecutionException, InterruptedException {
        return new getActivePolicyAsyncTask(policyDao).execute().get();
    }

    public void updatePolicy(PilotPolicy policy) {
        new updatePolicyAsyncTask(policyDao).execute(policy);
    }

    public void insertRule(PilotRule rule) {
        new insertRuleAsyncTask(ruleDao).execute(rule);
    }

    public LiveData<List<PilotRule>> getAllRulesFromPolicy() {
        return allRules;
//        return new getAllRulesFromPolicyAsyncTask(ruleDao).execute(id).get();
    }

    public List<PilotRule> staticGetAllRulesFromPolicy(int id) throws ExecutionException, InterruptedException {
        allStaticRules = new staticGetAllRulesFromPolicyAsyncTask(ruleDao).execute(id).get();
        return allStaticRules;
    }

    public void updateRule(PilotRule rule) {
        new updateRuleAsyncTask(ruleDao).execute(rule);
    }

    public void deleteRule(PilotRule rule) {
        new deleteRuleAsyncTask(ruleDao).execute(rule);
    }

    public DataTypeCategory getDataCategoryByName(String name) throws ExecutionException, InterruptedException {
        DataTypeCategory dataTypeCategory = new getDataCategoryByNameAsyncTask(datatypeCategoryDao).execute(name).get();
        return dataTypeCategory;
    }

    private static class getDataCategoryByNameAsyncTask extends android.os.AsyncTask<String, Void, DataTypeCategory> {

        private DataTypeCategoryDao datatypeCategoryDao;

        getDataCategoryByNameAsyncTask(DataTypeCategoryDao dao) {
            datatypeCategoryDao = dao;
        }

        @Override
        protected DataTypeCategory doInBackground(String... params) {
            return datatypeCategoryDao.getDataCategoryByName(params[0]);
        }
    }

    private static class staticGetAllRulesFromPolicyAsyncTask extends android.os.AsyncTask<Integer, Void, List<PilotRule>> {

        private PilotRuleDao ruleDao;

        staticGetAllRulesFromPolicyAsyncTask(PilotRuleDao dao) {
            ruleDao = dao;
        }

        @Override
        protected List<PilotRule> doInBackground(Integer... params) {
            return ruleDao.staticGetAllRulesFromPolicy();
        }
    }


    private static class updatePolicyAsyncTask extends android.os.AsyncTask<PilotPolicy, Void, Void> {

        private PilotPolicyDao policyDao;

        updatePolicyAsyncTask(PilotPolicyDao dao) {
            policyDao = dao;
        }

        @Override
        protected Void doInBackground(PilotPolicy... params) {
            policyDao.update(params[0]);
            return null;
        }
    }

    private static class updateRuleAsyncTask extends android.os.AsyncTask<PilotRule, Void, Void> {

        private PilotRuleDao ruleDao;

        updateRuleAsyncTask(PilotRuleDao dao) {
            ruleDao = dao;
        }

        @Override
        protected Void doInBackground(PilotRule... params) {
            ruleDao.update(params[0]);
            return null;
        }
    }

    private static class deleteRuleAsyncTask extends AsyncTask<PilotRule, Void, Void> {

        private PilotRuleDao ruleDao;

        deleteRuleAsyncTask(PilotRuleDao dao) {
            ruleDao = dao;
        }

        @Override
        protected Void doInBackground(PilotRule... params) {
            ruleDao.delete(params[0]);
            return null;
        }
    }

    private static class getActivePolicyAsyncTask extends AsyncTask<Void, Void, PilotPolicy> {

        private PilotPolicyDao policyDao;

        getActivePolicyAsyncTask(PilotPolicyDao dao) {
            policyDao = dao;
        }

        @Override
        protected PilotPolicy doInBackground(Void... params) {
            return policyDao.getActivePolicy();
        }
    }

    private static class countPoliciesAsync extends AsyncTask<Void, Void, Integer> {

        private PilotPolicyDao policyDao;

        countPoliciesAsync(PilotPolicyDao dao) {
            policyDao = dao;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return policyDao.countPolicies();
        }
    }

    private static class insertRuleAsyncTask extends AsyncTask<PilotRule, Void, Void> {

        private PilotRuleDao mAsyncTaskDao;

        insertRuleAsyncTask(PilotRuleDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PilotRule... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public Integer countPolicies() throws ExecutionException, InterruptedException {
        return new countPoliciesAsync(policyDao).execute().get();
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
}
