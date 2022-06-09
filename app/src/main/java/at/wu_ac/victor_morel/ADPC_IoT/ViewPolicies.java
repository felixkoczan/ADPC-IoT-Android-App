package at.wu_ac.victor_morel.ADPC_IoT;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotRule;
import at.wu_ac.victor_morel.ADPC_IoT.Model.PilotPolicyViewModel;
import at.wu_ac.victor_morel.ADPC_IoT.Model.PilotRuleListAdapter;

public class ViewPolicies extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PilotPolicyViewModel policyViewModel;
    public static final int UPDATE_POLICY_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_DATA_UPDATE_POLICY = "extra_policy_to_be_updated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_policies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        recyclerView = findViewById(R.id.recyclerviewsimple);

        final PilotRuleListAdapter adapter = new PilotRuleListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        policyViewModel = ViewModelProviders.of(this).get(PilotPolicyViewModel.class);
        policyViewModel.getAllRulesFromPolicy().observe(this, new Observer<List<PilotRule>>() {
            @Override
            public void onChanged(@Nullable final List<PilotRule> pilotRules) {
                adapter.setRules(pilotRules);
            }
        });

        adapter.setOnItemClickListener(new PilotRuleListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                PilotRule rule = adapter.getRuleAtPosition(position);
                launchUpdatePolicyActivity(rule);
            }
        });

        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void launchUpdatePolicyActivity(PilotRule rule) {
        Intent intent = new Intent(this, AddRuleActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_POLICY, rule);
        startActivityForResult(intent, UPDATE_POLICY_ACTIVITY_REQUEST_CODE);
    }

}
