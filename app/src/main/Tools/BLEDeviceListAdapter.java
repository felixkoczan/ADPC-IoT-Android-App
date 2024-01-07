import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Map;

/**
 * Adapter for RecyclerView to display a list of BLE devices.
 * Each device in the list is represented by a TextView and a Switch for user interaction.
 */
public class BLEDeviceListAdapter extends RecyclerView.Adapter<BLEDeviceListAdapter.DeviceViewHolder> {
    // LinkedList to store the list of BLE device names
    private final LinkedList<String> deviceList;
    // LayoutInflater to inflate the layout for each item in the RecyclerView
    private LayoutInflater mInflater;
    // Click listener to handle item clicks
    private static ClickListener clickListener;

    /**
     * Constructor for the BLEDeviceListAdapter.
     * @param context Context from which the LayoutInflater is obtained.
     * @param deviceList LinkedList containing the list of BLE devices.
     */
    public BLEDeviceListAdapter(Context context, LinkedList<String> deviceList) {
        mInflater = LayoutInflater.from(context);
        this.deviceList = deviceList;
    }

    @Override
    public void onBindViewHolder(@NonNull BLEDeviceListAdapter.DeviceViewHolder holder, int position) {
        // Bind the data to the ViewHolder for each item in the RecyclerView
        String mCurrent = deviceList.get(position);
        holder.deviceView.setText(mCurrent);
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new ViewHolder for each item in the RecyclerView
        View mItemView = mInflater.inflate(R.layout.devices_list, parent, false);
        return new DeviceViewHolder(mItemView, this);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return deviceList.size();
    }

    /**
     * ViewHolder class for the RecyclerView items.
     * Each item has a TextView to display the device name and a Switch for user interaction.
     */
    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        public final TextView deviceView;
        final BLEDeviceListAdapter mAdapter;

        public DeviceViewHolder(View itemView, BLEDeviceListAdapter adapter) {
            super(itemView);
            deviceView = itemView.findViewById(R.id.device);
            this.mAdapter = adapter;

            // Set a click listener for the device name TextView
            deviceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });

            // Set a click listener for the Switch associated with each device
            itemView.findViewById(R.id.switch1).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    // Toggle the consent state for the corresponding device
                    String deviceName = (String) deviceView.getText();
                    boolean currentState = MainActivity.consents.getOrDefault(deviceName, false);
                    MainActivity.consents.put(deviceName, !currentState);
                    System.out.println("Not crashed");
                }
            });
        }
    }

    /**
     * Set a click listener for item clicks in the RecyclerView.
     * @param clickListener The listener to handle item clicks.
     */
    public void setOnItemClickListener(BLEDeviceListAdapter.ClickListener clickListener) {
        BLEDeviceListAdapter.clickListener = clickListener;
    }

    /**
     * Interface to handle item clicks in the RecyclerView.
     */
    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
