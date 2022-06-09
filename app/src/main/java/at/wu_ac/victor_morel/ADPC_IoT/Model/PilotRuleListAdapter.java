package at.wu_ac.victor_morel.ADPC_IoT.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotRule;
import at.wu_ac.victor_morel.ADPC_IoT.R;

public class PilotRuleListAdapter extends RecyclerView.Adapter<PilotRuleListAdapter.PilotRuleViewHolder> {

    private final LayoutInflater inflater;
    private List<PilotRule> rules; // Cached copy of words
    //    private List<Purpose> purposes;
    private static ClickListener clickListener;

    public PilotRuleListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PilotRuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PilotRuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PilotRuleViewHolder holder, int position) {
        if (rules != null) {
            PilotRule current = rules.get(position);
            holder.ruleItemView.setText(current.getRule());
        } else {
            // Covers the case of data not being ready yet.
            holder.ruleItemView.setText("No Rule");
        }
    }

    public void setRules(List<PilotRule> rules) {
        this.rules = rules;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (rules != null)
            return rules.size();
        else return 0;
    }

    public PilotRule getRuleAtPosition(int position) {
        return rules.get(position);
    }

    class PilotRuleViewHolder extends RecyclerView.ViewHolder {
        private final TextView ruleItemView;

        private PilotRuleViewHolder(View itemView) {
            super(itemView);
            ruleItemView = itemView.findViewById(R.id.textView);
            ruleItemView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rule, 0, 0, 0);
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
        PilotRuleListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
