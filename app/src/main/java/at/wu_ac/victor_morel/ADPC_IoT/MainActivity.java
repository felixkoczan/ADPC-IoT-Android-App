package at.wu_ac.victor_morel.ADPC_IoT;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotPolicy;
import at.wu_ac.victor_morel.ADPC_IoT.Model.PilotPolicyViewModel;
import at.wu_ac.victor_morel.ADPC_IoT.Tools.BluetoothLeDeviceStore;
import at.wu_ac.victor_morel.ADPC_IoT.Tools.BluetoothLeScanner;
import at.wu_ac.victor_morel.ADPC_IoT.Tools.BluetoothLeService;
import at.wu_ac.victor_morel.ADPC_IoT.Tools.BluetoothUtils;
import at.wu_ac.victor_morel.ADPC_IoT.Tools.PolicyEngine;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.adrecord.AdRecord;
import uk.co.alt236.bluetoothlelib.util.AdRecordUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected RecyclerView mList;

    public static final String EXTRA_DATA_UPDATE_POLICY = "extra_policy_to_be_updated";
    public static final String EXTRA_DATA_POLICY = "extra_policy_to_be_added";

    private PilotPolicyViewModel policyViewModel;
    private String additionalDevice;

    private String idCurrentDCG;

    private BluetoothUtils mBluetoothUtils;
    private BluetoothLeScanner mScanner;
    private BluetoothLeDeviceStore mDeviceStore;
    private BluetoothLeService mBluetoothLeService;

    private RecyclerView mRecyclerView;
    private BLEDeviceListAdapter mAdapter;
    private final LinkedList<String> deviceList = new LinkedList<>();
    private List<BluetoothGattService> BGS;
    Intent gattServiceIntent = null;

    private HashMap<Integer, Integer> testBLETable = new HashMap<>();
    private PilotPolicy receivedPolicy;
    private String receivedADPC;
    private PilotPolicy tmpPolicy;
    private String tmpADPC;
    private HashMap<String, String> purposes;
    public static HashMap<String, Boolean> consents;

    private PilotPolicy myPolicy = null;

    private BluetoothAdapter bluetoothAdapter;
    private android.bluetooth.le.BluetoothLeScanner bleScanner;
    private BluetoothGatt bleGatt;
    private final UUID[] mServiceUuids = {
            UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b")
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName componentName, final IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("oops", "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect("7C:DF:A1:DA:E4:3A");
            BGS = mBluetoothLeService.getSupportedGattServices();
        }

        @Override
        public void onServiceDisconnected(final ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("heure avant", String.valueOf(System.currentTimeMillis()));
                startScanPrepare();
                Snackbar.make(view, "Start scanning", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton sendConsent = (FloatingActionButton) findViewById(R.id.consent_button);
        sendConsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendConsent();
                Snackbar.make(v, "You communicated your consents", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //PolicyEngine.createExampleDCP();
        PolicyEngine.createExampleDCP();

        policyViewModel = ViewModelProviders.of(this).get(PilotPolicyViewModel.class);


        PolicyEngine.retrievedPolicies = new HashMap<>(); //utility?
        PolicyEngine.retrievedPolicy = new HashMap<>(); //utility?
        consents = new HashMap<>();

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
// Create an adapter and supply the data to be displayed.
        mAdapter = new BLEDeviceListAdapter(this, deviceList);
// Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.setOnItemClickListener(new BLEDeviceListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.i("test_click", "click to send consent");
                if (sendConsent()) {
                        Snackbar.make(v, "Consent sent", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(v, "Too far to send consent", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
            }
        });

        mDeviceStore = new BluetoothLeDeviceStore();
        mBluetoothUtils = new BluetoothUtils(this);
        mScanner = new BluetoothLeScanner(mLeScanCallback, mBluetoothUtils);
        gattServiceIntent = new Intent(this, BluetoothLeService.class);

    }

    private boolean sendConsent() {
        Log.i("test_demo", "before connection");
        mBluetoothLeService.connect("7C:DF:A1:DA:E4:3A");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                BGS = mBluetoothLeService.getSupportedGattServices();
                for (BluetoothGattService gattService : BGS) {
                    if (gattService.getUuid().toString().equals("4fafc201-1fb5-459e-8fcc-c5c9c331914b")) {
                        Log.i("test_demo", "when uuid found");
                        List<BluetoothGattCharacteristic> BGC = gattService.getCharacteristics();
                        for (BluetoothGattCharacteristic gattCharac : BGC) {
                            if (gattCharac.getUuid().toString().equals("beb5483e-36e1-4688-b7f5-ea07361b26a8")) { // ::Consent::{30:AE:A4:84:5F:0A},11a3e229084349bc25d97e29393ced1d\n
                                String s = "ADPC: consent=";
                                for (Map.Entry me : purposes.entrySet()) {
                                    if(consents.containsKey((String) me.getValue())){
                                        if(consents.get((String) me.getValue())){
                                            s+= me.getKey() + " ";
                                        }
                                    }
                                }
                                gattCharac.setValue(s.substring(0, s.length()-1));
                                boolean consent = mBluetoothLeService.mBluetoothGatt.writeCharacteristic(gattCharac);
                                Log.i("test_demo", String.valueOf(consent));
                            }
                        }
                    }
                }
                mBluetoothLeService.disconnect();
            }
        };

        Handler h = new Handler();
        try {
            h.postDelayed(r, 2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

            final BluetoothLeDevice deviceLe = new BluetoothLeDevice(device, rssi, scanRecord, System.currentTimeMillis());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (deviceLe.getAddress().equals("7C:DF:A1:DA:E4:3A")) {
                        //will have to be changed to compare UUID instead
                        AdRecord adr = AdRecordUtils.parseScanRecordAsSparseArray(deviceLe.getScanRecord()).get(255);
                        byte[] uuid = deviceLe.getScanRecord();
                        byte[] byt = adr.getData();
                        //retrieve the advertisement data containing the fragmented policy
                        try {
                            tmpADPC = PolicyEngine.reconstitutePolicies(byt, deviceLe.getAddress());
//                            tmpPolicy = PolicyEngine.reconstitutePolicies(byt, deviceLe.getAddress()); //try to reconstitute, return null if policy is uncomplete
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (tmpADPC != null) {
                            // if the policy is complete, store it and display it
                            // here we parse the notice received, to store it in a hashmap
                            purposes = PolicyEngine.parseADPCNotice(tmpADPC);

                            // SUIOEHDHQWOQK


                            //receivedADPC = tmpADPC;
                            idCurrentDCG = deviceLe.getAddress();
                            Log.i("dejkou", idCurrentDCG);

                            for (Map.Entry me : purposes.entrySet()) {
                                deviceList.addLast((String) me.getValue()); //use devicestore instead
                                //consents.put((String) me.getValue(), false);
                            }

                            //deviceList.addLast(receivedADPC); //use devicestore instead
                            Log.i("heure après", String.valueOf(System.currentTimeMillis()));
                            mDeviceStore.addDevice(deviceLe);
                            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);    //we also bind the gatt service MAYBE TO CHANGE


                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };





    private void startScanPrepare() {
        //
        // The COARSE_LOCATION permission is only needed after API 23 to do a BTLE scan
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionsResultAction() {

                        @Override
                        public void onGranted() {
                            startScan();
                        }

                        @Override
                        public void onDenied(String permission) {
                            Toast.makeText(MainActivity.this,
                                    "Permission missing",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        } else {
            startScan();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startScan() {
//        List<ScanFilter> filters = new ArrayList<ScanFilter>();
//        if (mServiceUuids != null && mServiceUuids.length > 0) { // this piece of code can retrieve UUIDs
//            for (UUID uuid : mServiceUuids) {
//                ScanFilter filter = new ScanFilter.Builder().setServiceUuid(
//                        new ParcelUuid(uuid)).build();
//                filters.add(filter);
//            }
//        }

//        if (bleScanner != null) {
//            ScanSettings settings = new ScanSettings.Builder().build();
//            bleScanner.startScan(scanCallback);
//        }
        final boolean isBluetoothOn = mBluetoothUtils.isBluetoothOn();
        final boolean isBluetoothLePresent = mBluetoothUtils.isBluetoothLeSupported();
        mDeviceStore.clear();

        mBluetoothUtils.askUserToEnableBluetoothIfNeeded();
        if (isBluetoothOn && isBluetoothLePresent) {
            mScanner.scanLeDevice(60000, true);
            //duration is in ms
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_rule) {
            Intent addRuleIntent = new Intent(this,
                    AddRuleActivity.class);
            startActivity(addRuleIntent);
            // Handle the camera action
        } else if (id == R.id.nav_visualize_policy) {
            Intent visualizeIntent = new Intent(this,
                    ViewPolicies.class);
            startActivity(visualizeIntent);

        } else if (id == R.id.nav_manage) {
            Intent settingsIntent = new Intent(this,
                    SettingsActivity.class);
            startActivity(settingsIntent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
