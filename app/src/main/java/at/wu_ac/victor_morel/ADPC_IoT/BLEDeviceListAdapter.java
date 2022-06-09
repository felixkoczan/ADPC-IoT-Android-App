package at.wu_ac.victor_morel.ADPC_IoT;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class BLEDeviceListAdapter extends
        RecyclerView.Adapter<BLEDeviceListAdapter.DeviceViewHolder> {
    private final LinkedList<String> deviceList;
    private LayoutInflater mInflater;
    private static ClickListener clickListener;


    public BLEDeviceListAdapter(Context context, LinkedList<String> deviceList) {
        mInflater = LayoutInflater.from(context);
        this.deviceList = deviceList;
    }


    @Override
    public void onBindViewHolder(@NonNull BLEDeviceListAdapter.DeviceViewHolder holder, int position) {
        String mCurrent = deviceList.get(position);
        holder.deviceView.setText(mCurrent);
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View mItemView = mInflater.inflate(R.layout.devices_list,
                parent, false);
        return new DeviceViewHolder(mItemView, this);
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        public final TextView deviceView;
        final BLEDeviceListAdapter mAdapter;

        public DeviceViewHolder(View itemView, BLEDeviceListAdapter adapter) {
            super(itemView);
            deviceView = itemView.findViewById(R.id.device);
            this.mAdapter = adapter;
            deviceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });

        }
    }

    public void setOnItemClickListener(BLEDeviceListAdapter.ClickListener clickListener) {
        BLEDeviceListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
