import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.util.Log;

/**
 * Class responsible for controlling Bluetooth Low Energy (BLE) scanning.
 */
public class BluetoothLeScanner {
    // Handler to manage asynchronous tasks, such as stopping the scan after a timeout
    private final Handler mHandler;
    // Callback to handle scan results
    private final BluetoothAdapter.LeScanCallback mLeScanCallback;
    // Utility class for Bluetooth operations
    private final BluetoothUtils mBluetoothUtils;
    // Flag to keep track of scanning state
    private boolean mScanning;

    /**
     * Constructor for BluetoothLeScanner.
     *
     * @param leScanCallback Callback to handle results of the BLE scan.
     * @param bluetoothUtils Utility class instance for Bluetooth operations.
     */
    public BluetoothLeScanner(final BluetoothAdapter.LeScanCallback leScanCallback, final BluetoothUtils bluetoothUtils) {
        mHandler = new Handler();
        mLeScanCallback = leScanCallback;
        mBluetoothUtils = bluetoothUtils;
    }

    /**
     * Checks if the scanner is currently scanning for BLE devices.
     *
     * @return True if scanning, false otherwise.
     */
    public boolean isScanning() {
        return mScanning;
    }

    /**
     * Starts or stops scanning for BLE devices.
     *
     * @param duration The duration in milliseconds for which to scan. A value of 0 scans indefinitely.
     * @param enable   True to start scanning, false to stop.
     */
    public void scanLeDevice(final int duration, final boolean enable) {
        if (enable) {
            if (mScanning) {
                // If already scanning, do not start again
                return;
            }
            Log.d("TAG", "~ Starting Scan");
            // Stops scanning after a pre-defined scan period, if duration is greater than 0
            if (duration > 0) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG", "~ Stopping Scan (timeout)");
                        mScanning = false;
                        mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);
                    }
                }, duration);
            }
            // Start scanning
            mScanning = true;
            mBluetoothUtils.getBluetoothAdapter().startLeScan(mLeScanCallback);
        } else {
            // Stop scanning
            Log.d("TAG", "~ Stopping Scan");
            mScanning = false;
            mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);
        }
    }
}
