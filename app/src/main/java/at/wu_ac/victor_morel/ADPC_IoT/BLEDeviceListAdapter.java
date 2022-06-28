package at.wu_ac.victor_morel.ADPC_IoT;

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
        int position;

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

            itemView.findViewById(R.id.switch1).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    if(MainActivity.consents.containsKey((String) deviceView.getText())){
                        if(MainActivity.consents.get((String) deviceView.getText())==true){
                            MainActivity.consents.replace((String) deviceView.getText(), false);
                        } else{
                            MainActivity.consents.replace((String) deviceView.getText(), true);
                        }
                    } else{
                        MainActivity.consents.put((String) deviceView.getText(), true);
                    }
                    System.out.println("Not crashed");
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
