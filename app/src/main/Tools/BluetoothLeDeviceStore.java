package at.wu_ac.victor_morel.ADPC_IoT.Tools;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.easycursor.objectcursor.EasyObjectCursor;

/**
 * Class to store and manage Bluetooth LE devices.
 * Provides functionality to add, clear, and retrieve BLE devices.
 */
public class BluetoothLeDeviceStore {
    // Default comparator for sorting BluetoothLeDevice objects
    private static final BluetoothLeDeviceComparator DEFAULT_COMPARATOR = new BluetoothLeDeviceComparator();
    // Map to store BluetoothLeDevice objects, keyed by device addresses
    private final Map<String, BluetoothLeDevice> mDeviceMap;

    // Constructor initializing the device map
    public BluetoothLeDeviceStore() {
        mDeviceMap = new HashMap<>();
    }

    /**
     * Adds a BluetoothLeDevice to the store.
     * If the device already exists, updates its RSSI reading. Otherwise, adds the new device.
     *
     * @param device The BluetoothLeDevice to be added.
     */
    public void addDevice(@NonNull final BluetoothLeDevice device) {
        if (mDeviceMap.containsKey(device.getAddress())) {
            mDeviceMap.get(device.getAddress()).updateRssiReading(device.getTimestamp(), device.getRssi());
        } else {
            mDeviceMap.put(device.getAddress(), device);
        }
    }

    // Clears all devices from the store
    public void clear() {
        mDeviceMap.clear();
    }

    // Returns the number of devices in the store
    public int getSize() {
        return mDeviceMap.size();
    }

    /**
     * Returns a cursor of BluetoothLeDevice objects sorted by the default comparator.
     * Useful for interfacing with components expecting cursor data.
     *
     * @return An EasyObjectCursor of BluetoothLeDevice objects.
     */
    @NonNull
    public EasyObjectCursor<BluetoothLeDevice> getDeviceCursor() {
        return getDeviceCursor(DEFAULT_COMPARATOR);
    }

    /**
     * Returns a cursor of BluetoothLeDevice objects sorted by the specified comparator.
     *
     * @param comparator Comparator to sort the BluetoothLeDevice objects.
     * @return An EasyObjectCursor of BluetoothLeDevice objects.
     */
    @NonNull
    public EasyObjectCursor<BluetoothLeDevice> getDeviceCursor(@NonNull Comparator<BluetoothLeDevice> comparator) {
        return new EasyObjectCursor<>(
                BluetoothLeDevice.class,
                getDeviceList(comparator),
                "address");
    }

    // Returns a list of BluetoothLeDevice objects sorted by the default comparator
    @NonNull
    public List<BluetoothLeDevice> getDeviceList() {
        return getDeviceList(DEFAULT_COMPARATOR);
    }

    /**
     * Returns a list of BluetoothLeDevice objects sorted by the specified comparator.
     *
     * @param comparator Comparator to sort the BluetoothLeDevice objects.
     * @return A List of sorted BluetoothLeDevice objects.
     */
    @NonNull
    public List<BluetoothLeDevice> getDeviceList(@NonNull Comparator<BluetoothLeDevice> comparator) {
        final List<BluetoothLeDevice> methodResult = new ArrayList<>(mDeviceMap.values());
        Collections.sort(methodResult, comparator);
        return methodResult;
    }

    /**
     * Comparator for BluetoothLeDevice objects.
     * Provides a comparison based on the Bluetooth device address.
     */
    private static class BluetoothLeDeviceComparator implements Comparator<BluetoothLeDevice> {

        @Override
        public int compare(final BluetoothLeDevice arg0, final BluetoothLeDevice arg1) {
            return arg0.getAddress().compareTo(arg1.getAddress());
        }
    }
}
