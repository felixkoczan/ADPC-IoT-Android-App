package at.wu_ac.victor_morel.ADPC_IoT;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataController;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.Purpose;
import at.wu_ac.victor_morel.ADPC_IoT.Model.PilotPolicyViewModel;

public class AddTransferRuleActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private PilotPolicyViewModel policyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_done = (FloatingActionButton) findViewById(R.id.fab_done);
        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Rule (almost) added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab_more = (FloatingActionButton) findViewById(R.id.fab_more);
        fab_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTransferRuleActivity.this, AddTransferRuleActivity.class);
                startActivity(intent);
            }
        });


        setupActionBar();

//
//        final Bundle extras = getIntent().getExtras();
//
//        if (extras != null) {
//            String policy = extras.getString(EXTRA_DATA_UPDATE_POLICY, "");
//            if (!policy.isEmpty()) {
//                setupAutocomplete(policy);
//            }
//        } else {
//            setupAutocomplete();
//        }

        setupAutocomplete(); // to comment when intent properly managed


    }

    private void setupAutocomplete(Object... extraValue) {
        Resources res = getResources();
        String[] arrayPurposeChoice = res.getStringArray(R.array.labels_array_data_purpose);
        String[] arrayDCChoice = res.getStringArray(R.array.labels_array_controllers);

        policyViewModel = ViewModelProviders.of(this).get(PilotPolicyViewModel.class);


        //set up spinner for choice of purpose
        final ArrayAdapter<String> adapter_PURPOSE = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new ArrayList(Arrays.asList(arrayPurposeChoice)));
        AutoCompleteTextView textViewPurpose = (AutoCompleteTextView)
                findViewById(R.id.label_autocomplete_purpose);
        textViewPurpose.setAdapter(adapter_PURPOSE);

        policyViewModel.getAllPurposes().observe(this, new Observer<List<Purpose>>() {
            @Override
            public void onChanged(@Nullable final List<Purpose> purposes) {
                for (Purpose p : purposes) {
                    adapter_PURPOSE.add(p.getPurpose());
                }
                adapter_PURPOSE.notifyDataSetChanged();
            }
        });

        final ArrayAdapter<String> adapter_DC = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new ArrayList(Arrays.asList(arrayDCChoice)));
        AutoCompleteTextView textViewDC = (AutoCompleteTextView)
                findViewById(R.id.label_autocomplete_dc);
        textViewDC.setAdapter(adapter_DC);

        policyViewModel.getAllDataControllers().observe(this, new Observer<List<DataController>>() {
            @Override
            public void onChanged(@Nullable final List<DataController> dataControllers) {
                for (DataController dc : dataControllers) {
                    adapter_DC.add(dc.getDCname());
                }
                adapter_DC.notifyDataSetChanged();
            }
        });


    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
