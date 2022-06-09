package at.wu_ac.victor_morel.ADPC_IoT.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotPolicy;
import at.wu_ac.victor_morel.ADPC_IoT.R;

public class PilotPolicyListAdapter extends RecyclerView.Adapter<PilotPolicyListAdapter.PilotPolicyViewHolder> {

    private final LayoutInflater inflater;
    private List<PilotPolicy> policies; // Cached copy of words
    //    private List<Purpose> purposes;
    private static ClickListener clickListener;

    public PilotPolicyListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PilotPolicyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PilotPolicyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PilotPolicyViewHolder holder, int position) {
        if (policies != null) {
            PilotPolicy current = policies.get(position);
            holder.policyItemView.setText(current.getPolicyAsString());
        } else {
            // Covers the case of data not being ready yet.
            holder.policyItemView.setText("No Policy");
        }
    }

    public void setPolicies(List<PilotPolicy> policies) {
        this.policies = policies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (policies != null)
            return policies.size();
        else return 0;
    }

    public PilotPolicy getPolicyAtPosition(int position) {
        return policies.get(position);
    }

    class PilotPolicyViewHolder extends RecyclerView.ViewHolder {
        private final TextView policyItemView;

        private PilotPolicyViewHolder(View itemView) {
            super(itemView);
            policyItemView = itemView.findViewById(R.id.textView);
            policyItemView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_menu_manage, 0, 0, 0);
            //icon pas approprié
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        PilotPolicyListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
