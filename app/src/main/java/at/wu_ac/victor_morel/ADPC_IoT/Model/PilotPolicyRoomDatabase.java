package at.wu_ac.victor_morel.ADPC_IoT.Model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DCCategoryDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DCRDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DURDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DataControllersDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DataTypeCategoryDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.DataTypeDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.PilotPolicyDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.PilotRuleDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.PurposeDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Dao.TRDao;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DCCategory;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DCR;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DUR;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataController;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataType;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataTypeCategory;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotPolicy;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotRule;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.Purpose;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.TR;


@Database(entities = {DataController.class, Purpose.class, DataType.class, PilotPolicy.class, PilotRule.class, DUR.class, DCR.class, TR.class, DCCategory.class, DataTypeCategory.class}, version = 2, exportSchema = false)
public abstract class PilotPolicyRoomDatabase extends RoomDatabase {
    public abstract DataControllersDao dataControllersDao();

    public abstract DataTypeDao dataTypeDao();

    public abstract PilotPolicyDao policyDao();

    public abstract PurposeDao purposeDao();

    public abstract PilotRuleDao ruleDao();

    public abstract DURDao durDao();

    public abstract DCRDao dcrDao();

    public abstract TRDao trDao();

    public abstract DCCategoryDao dcCategoryDao();

    public abstract DataTypeCategoryDao datatypeCategoryDao();

    private static PilotPolicyRoomDatabase INSTANCE;

    public static PilotPolicyRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PilotPolicyRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PilotPolicyRoomDatabase.class, "policy_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback =
            new Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }


            };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PilotPolicyDao polDao;
        private final PurposeDao purDao;
        private final DataControllersDao dcDao;
        private final DataTypeDao dtDao;
        private final PilotRuleDao rulDao;
        private final DCRDao dcrDao;
        private final DURDao durDao;
        private final DCCategoryDao dccDAO;
        private final DataTypeCategoryDao dtcDao;

        PopulateDbAsync(PilotPolicyRoomDatabase db) {
            dcrDao = db.dcrDao();
            durDao = db.durDao();
            polDao = db.policyDao();
            purDao = db.purposeDao();
            dcDao = db.dataControllersDao();
            dtDao = db.dataTypeDao();
            rulDao = db.ruleDao();
            dccDAO = db.dcCategoryDao();
            dtcDao = db.datatypeCategoryDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Purpose purpose1 = new Purpose("Marketing");
            Purpose purpose2 = new Purpose("Analytics");

            DataType dataType1 = new DataType("Wi-Fi MAC Address");
            DataType dataType2 = new DataType("Bluetooth MAC Address");

            List<DataType> dataTypes = new ArrayList<>();
            dataTypes.add(dataType1);
            dataTypes.add(dataType2);
            DataTypeCategory dataTypeCategory1 = new DataTypeCategory("Identifiers", dataTypes);

            DataController dataController1 = new DataController("Google");
            DataController dataController2 = new DataController("Amazon");
            DataController dataController3 = new DataController("Facebook");
            DataController dataController4 = new DataController("Carrefour");
            DataController dataController5 = new DataController("FNAC");
            DataController dataController6 = new DataController("Decathlon");


            List<DataController> dataControllers1 = new ArrayList<>();
            dataControllers1.add(dataController1);
            dataControllers1.add(dataController2);
            dataControllers1.add(dataController3);
            DCCategory dcCategory1 = new DCCategory("GAFAM", dataControllers1);

            List<DataController> dataControllers2 = new ArrayList<>();
            dataControllers1.add(dataController4);
            dataControllers1.add(dataController5);
            dataControllers1.add(dataController6);
            DCCategory dcCategory2 = new DCCategory("Malls", dataControllers2);


            DUR dur1 = new DUR(purpose1, 30);
            DUR dur2 = new DUR(purpose2, 100);

            DCR dcr1 = new DCR(dataController1, dur1);
            DCR dcr2 = new DCR(dataController2, dur2);

            PilotRule pilotRule1 = new PilotRule(dataType1, dcr1, null);
            PilotRule pilotRule2 = new PilotRule(dataType2, dcr2, null);

            List<PilotRule> rules = new ArrayList<>();
            rules.add(pilotRule1);
            rules.add(pilotRule2);

            PilotPolicy pilotPolicy = new PilotPolicy(rules, "Example", true);
            pilotRule1.setPolicyId(1);
            pilotRule2.setPolicyId(1);

            // If we have no policy, then create one
            if (polDao.getAnyPolicy().length < 1) {
                purDao.insert(purpose1);
                purDao.insert(purpose2);

                dcDao.insert(dataController1);
                dcDao.insert(dataController2);
                dcDao.insert(dataController3);
                dcDao.insert(dataController4);
                dcDao.insert(dataController5);
                dcDao.insert(dataController6);

                dccDAO.insert(dcCategory1);
                dccDAO.insert(dcCategory2);

                dtDao.insert(dataType1);
                dtDao.insert(dataType2);

                dtcDao.insert(dataTypeCategory1);

                durDao.insert(dur1);
                durDao.insert(dur2);

                dcrDao.insert(dcr1);
                dcrDao.insert(dcr2);

                rulDao.insert(pilotRule1);
                rulDao.insert(pilotRule2);

                polDao.insert(pilotPolicy);
            }
            return null;
        }
    }
}
