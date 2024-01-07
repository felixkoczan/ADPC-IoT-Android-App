import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Utility class for handling common Bluetooth operations within an Activity.
 */
public final class BluetoothUtils {
    // Request code for enabling Bluetooth. Used in onActivityResult callback.
    public final static int REQUEST_ENABLE_BT = 2001;

    // Reference to the activity using this utility class.
    private final Activity mActivity;
    // BluetoothAdapter for Bluetooth operations.
    private final BluetoothAdapter mBluetoothAdapter;

    /**
     * Constructor initializing the BluetoothUtils class.
     *
     * @param activity The Activity context using this utility class.
     */
    public BluetoothUtils(final Activity activity) {
        mActivity = activity;
        // Get the BluetoothManager and obtain the BluetoothAdapter from it.
        final BluetoothManager btManager = (BluetoothManager) mActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = btManager.getAdapter();
    }

    /**
     * Prompts the user to enable Bluetooth if it's supported but not currently enabled.
     */
    public void askUserToEnableBluetoothIfNeeded() {
        // Check if Bluetooth LE is supported and if Bluetooth is not enabled.
        if (isBluetoothLeSupported() && (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())) {
            // Create an intent to request enabling Bluetooth.
            final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // Start the activity with the intent, requesting a result.
            mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * Getter for the BluetoothAdapter.
     *
     * @return The BluetoothAdapter instance.
     */
    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    /**
     * Checks if Bluetooth Low Energy (LE) is supported on the device.
     *
     * @return True if Bluetooth LE is supported, false otherwise.
     */
    public boolean isBluetoothLeSupported() {
        // Check if the device's PackageManager reports Bluetooth LE support.
        return mActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * Checks if Bluetooth is turned on.
     *
     * @return True if Bluetooth is on, false otherwise.
     */
    public boolean isBluetoothOn() {
        // Return false if BluetoothAdapter is null, otherwise return its enabled state.
        if (mBluetoothAdapter == null) {
            return false;
        } else {
            return mBluetoothAdapter.isEnabled();
        }
    }
}
