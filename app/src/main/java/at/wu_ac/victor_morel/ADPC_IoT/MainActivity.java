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
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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
            mBluetoothLeService.connect("30:AE:A4:84:5F:0A");
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

        FloatingActionButton addWatch = (FloatingActionButton) findViewById(R.id.watch);
        addWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalDevice = "C7:32:E9:C1:34:29";
                Snackbar.make(v, "Your smartwatch is now bonded", Snackbar.LENGTH_LONG)
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

                try {
                    myPolicy = policyViewModel.getActivePolicy();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (PolicyEngine.comparePolicies(receivedPolicy, myPolicy)) {
                    if (sendConsent()) {
                        Snackbar.make(v, "Consent sent", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(v, "Too far to send consent", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                } else {
                    if (PolicyEngine.intersectionPolicies(receivedPolicy, myPolicy) != null) {
                        if (sendPmin(myPolicy)) {
                            Snackbar.make(v, "Negotiation started", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } else {
                        Snackbar.make(v, "Policies do not match", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

        mDeviceStore = new BluetoothLeDeviceStore();
        mBluetoothUtils = new BluetoothUtils(this);
        mScanner = new BluetoothLeScanner(mLeScanCallback, mBluetoothUtils);
        gattServiceIntent = new Intent(this, BluetoothLeService.class);

//        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        bluetoothAdapter = bluetoothManager.getAdapter();
//        bluetoothAdapter.enable();
//        bleScanner = bluetoothAdapter.getBluetoothLeScanner();
    }


    private boolean sendConsent() {
        Log.i("test_demo", "before connection");
        mBluetoothLeService.connect("30:AE:A4:84:5F:0A");
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
                                String s = "::Consent::";
                                PilotPolicyProto.PilotPolicy.Builder pol = PolicyEngine.policyModelToBuilder(receivedPolicy);
                                if (additionalDevice != null) {
                                    try {
                                        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                                        messageDigest.update(JsonFormat.printer().print(pol).getBytes());
                                        byte[] digest = messageDigest.digest();
                                        String hash = PolicyEngine.byteArrayToHex(digest);
                                        Log.i("deijdjeid", hash);
                                        s += "{84:CF:BF:8A:99:21," + additionalDevice + "}," + hash + "\n";
                                        gattCharac.setValue(s);
//                                Log.i("deijdjeid", JsonFormat.printer().print(pol));
                                    } catch (InvalidProtocolBufferException e) {
                                        e.printStackTrace();
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                                        messageDigest.update(JsonFormat.printer().print(pol).getBytes());
                                        byte[] digest = messageDigest.digest();
                                        String hash = PolicyEngine.byteArrayToHex(digest);
                                        Log.i("deijdjeid", hash);
                                        s += "{84:CF:BF:8A:99:21}," + hash + "\n";
                                        gattCharac.setValue(s);
//                                Log.i("deijdjeid", JsonFormat.printer().print(pol));
                                    } catch (InvalidProtocolBufferException e) {
                                        e.printStackTrace();
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                }
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

/*    private String getMac() throws IOException {
        try{
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
            String stringMac = "";
            for(NetworkInterface networkInterface : networkInterfaceList)
            {
                if(networkInterface.getName().equalsIgnoreCase("wlon0"));
                {
                    for(int i = 0 ;i <networkInterface.getHardwareAddress().length; i++){
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i]& 0xFF);
                        if(stringMacByte.length() == 1)
                        {
                            stringMacByte = "0" +stringMacByte;
                        }
                        stringMac = stringMac + stringMacByte.toUpperCase() + ":";
                    }
                    break;
                }
            }
            return stringMac;
        }catch (SocketException e)
        {
            e.printStackTrace();
        }
        return  "0";
    }*/

    private boolean sendPmin(final PilotPolicy DSP) {
        mBluetoothLeService.connect("30:AE:A4:84:5F:0A");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                BGS = mBluetoothLeService.getSupportedGattServices();
                for (BluetoothGattService gattService : BGS) {
                    if (gattService.getUuid().toString().equals("4fafc201-1fb5-459e-8fcc-c5c9c331914b")) {
                        List<BluetoothGattCharacteristic> BGC = gattService.getCharacteristics();
                        for (BluetoothGattCharacteristic gattCharac : BGC) {
                            if (gattCharac.getUuid().toString().equals("beb5483e-36e1-4688-b7f5-ea07361b26a8")) {
                                //String s = PolicyEngine.policyModelToProto(intersection).toString();
                                PilotPolicyProto.PilotPolicy.Builder pol = PolicyEngine.policyModelToBuilder(DSP);
                                try {
                                    String s = JsonFormat.printer().print(pol);
//                                    s = s.substring(s.indexOf('\n')+1);
//                                    s = s+"{";
                                    Log.i("dede", s);
                                    gattCharac.setValue("&" + s);
                                    boolean consent = mBluetoothLeService.mBluetoothGatt.writeCharacteristic(gattCharac);
                                } catch (InvalidProtocolBufferException e) {
                                    e.printStackTrace();
                                }
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
                            //if the policy is complete, store it and display it
                            receivedADPC = tmpADPC;
                            idCurrentDCG = deviceLe.getAddress();
                            Log.i("dejkou", idCurrentDCG);
                            deviceList.addLast(receivedADPC); //use devicestore instead
                            Log.i("heure après", String.valueOf(System.currentTimeMillis()));
                            mDeviceStore.addDevice(deviceLe);
//                            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);    //we also bind the gatt service MAYBE TO CHANGE

//                            try {
//                                myPolicy = policyViewModel.getActivePolicy();
//                            } catch (ExecutionException e) {
//                                e.printStackTrace();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

//                            if (!PolicyEngine.comparePolicies(receivedPolicy, myPolicy)) {
//                                notifyNewPolicyNotMatching(PolicyEngine.outerJoinPolicies(receivedPolicy, myPolicy));
//                            }
                        }
//                        testBLETable.put((int) byt[2], 1);
//                        Log.i("BLE bytes2", String.valueOf(testBLETable));

                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel test";
            String description = "I have no idea why I have to set this field";
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel channel = new NotificationChannel("420", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static PendingIntent getDismissIntent(int notificationId, Context context) { //useless
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("1", notificationId);
        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return dismissIntent;
    }

    private void notifyNewPolicyNotMatching(PilotPolicy intersection) {
        createNotificationChannel();

        // Create an explicit intent for an Activity in your app
        Intent intentAddDynamic = new Intent(this, AddRuleActivity.class);
        intentAddDynamic.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentAddDynamic.putExtra(EXTRA_DATA_POLICY, intersection.getRules().get(0));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentAddDynamic, PendingIntent.FLAG_UPDATE_CURRENT);
        //int notificationId = new Random().nextInt();
        //PendingIntent dismissIntent = getDismissIntent(notificationId, this); //useless

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "420")
                .setSmallIcon(R.drawable.ic_more)
                .setContentTitle("New device")
                .setContentText("We detected a new device whose policy does not match yours")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("We detected a new device whose policy does not match yours. " +
                                "The DC claims the benefits for you would be X and Y, " +
                                "but in the end the decision is only yours to take."))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_more, "Add rule", pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, mBuilder.build());

    }

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
//
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

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private final ScanCallback scanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//            //printScanResult(result);
//            if (result.getDevice().getAddress().equals("7C:DF:A1:DA:E4:3A")) {
//                //will have to be changed to compare UUID instead
//                //AdRecord adr = result.getScanRecord().getBytes();
//                byte[] byt = result.getScanRecord().getBytes();
//                //retrieve the advertisement data containing the fragmented policy
//                try {
//                    tmpPolicy = PolicyEngine.reconstitutePolicies(byt, result.getDevice().getAddress()); //try to reconstitute, return null if policy is uncomplete
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (tmpPolicy != null) {
//                    //if the policy is complete, store it and display it
//                    receivedPolicy = tmpPolicy;
//                    idCurrentDCG = result.getDevice().getAddress();
//                    Log.i("dejkou", idCurrentDCG);
//                    deviceList.addLast(receivedPolicy.getPolicyAsString()); //use devicestore instead
//                    Log.i("heure après", String.valueOf(System.currentTimeMillis()));
//                    //mDeviceStore.addDevice(result.getDevice());
//                    bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);    //we also bind the gatt service MAYBE TO CHANGE
//
//                    try {
//                        myPolicy = policyViewModel.getActivePolicy();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (!PolicyEngine.comparePolicies(receivedPolicy, myPolicy)) {
//                        notifyNewPolicyNotMatching(PolicyEngine.outerJoinPolicies(receivedPolicy, myPolicy));
//                    }
//                }
////                        testBLETable.put((int) byt[2], 1);
////                        Log.i("BLE bytes2", String.valueOf(testBLETable));
//
//                mAdapter.notifyDataSetChanged();
//            }
//        }
//
//        @Override
//        public void onBatchScanResults(List<ScanResult> results) {
//            for (ScanResult r : results) {
//                printScanResult(r);
//            }
//        }
//
//        @Override
//        public void onScanFailed(int errorCode) {
//        }
//
//        private void printScanResult(ScanResult result) {
//            String id = result.getDevice() != null ? result.getDevice().getAddress() : "unknown";
//            int tx = result.getScanRecord() != null ? result.getScanRecord().getTxPowerLevel() : 0;
//        }
//    };

}
