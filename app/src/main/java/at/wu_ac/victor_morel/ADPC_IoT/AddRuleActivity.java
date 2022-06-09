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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DCR;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DUR;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataController;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataType;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataTypeCategory;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotPolicy;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotRule;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.Purpose;
import at.wu_ac.victor_morel.ADPC_IoT.Model.PilotPolicyViewModel;

public class AddRuleActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private PilotPolicyViewModel policyViewModel;
    public static final String EXTRA_DATA_UPDATE_POLICY = "extra_policy_to_be_updated";
    public static final String EXTRA_DATA_POLICY = "extra_policy_to_be_added";
    private boolean update = false;


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
                addRule();

                if (update) {
                    Snackbar.make(view, "Rule updated", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Rule added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                Intent intent = new Intent(AddRuleActivity.this, ViewPolicies.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab_more = (FloatingActionButton) findViewById(R.id.fab_more);
        fab_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddRuleActivity.this, AddTransferRuleActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deleteRule();
                Snackbar.make(view, "Rule deleted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupActionBar();

        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_DATA_UPDATE_POLICY)) {
            PilotRule myRule = intent.getParcelableExtra(EXTRA_DATA_UPDATE_POLICY);
            update = true;
            setupAutocomplete(myRule);
        } else {
            setupAutocomplete();
        }

        if (intent.hasExtra(EXTRA_DATA_POLICY)) {
            PilotRule myRule = intent.getParcelableExtra(EXTRA_DATA_POLICY);
            setupAutocomplete(myRule);
        } else {
            setupAutocomplete();
        }

        setupSwitch();
    }

    private void setupSwitch() {
        Switch switchDataCategories = findViewById(R.id.id_switch_generic_datatype);
        switchDataCategories.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AutoCompleteTextView textViewDataTypeCategory = (AutoCompleteTextView)
                        findViewById(R.id.label_autocomplete_datatype_category);
                AutoCompleteTextView textViewDataType = (AutoCompleteTextView)
                        findViewById(R.id.label_autocomplete_datatype);

                if (isChecked) {
                    textViewDataTypeCategory.setVisibility(View.VISIBLE);
                    textViewDataTypeCategory.requestFocus();
                    textViewDataType.setVisibility(View.INVISIBLE);
                } else {
                    textViewDataTypeCategory.setVisibility(View.INVISIBLE);
                    textViewDataType.setVisibility(View.VISIBLE);
                    textViewDataType.requestFocus();
                }
            }
        });

    }

    private void deleteRule() {
        final Intent intent = getIntent();
        PilotRule myRule = intent.getParcelableExtra(EXTRA_DATA_UPDATE_POLICY);
        PilotPolicy currentPolicy = null;
        try {
            currentPolicy = policyViewModel.getActivePolicy();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        policyViewModel.deleteRule(myRule);
        currentPolicy.deleteRule(myRule);
        policyViewModel.updatePolicy(currentPolicy);
    }

    private void addRule() {
        String strDatatype;
        String strDataCategory;
        String strPurpose;
        String strRetention;
        String strDC;

        AutoCompleteTextView acDatatype = findViewById(R.id.label_autocomplete_datatype);
        AutoCompleteTextView acDataCategory = findViewById(R.id.label_autocomplete_datatype_category);
        AutoCompleteTextView acPurpose = findViewById(R.id.label_autocomplete_purpose);
        EditText acRetention = findViewById(R.id.label_retention);
        AutoCompleteTextView acDC = findViewById(R.id.label_autocomplete_dc);
        Switch switchDataCategories = findViewById(R.id.id_switch_generic_datatype);

        strDatatype = String.valueOf(acDatatype.getText());
        strDataCategory = String.valueOf(acDataCategory.getText());
        strPurpose = String.valueOf(acPurpose.getText());
        strRetention = String.valueOf(acRetention.getText());
        strDC = String.valueOf(acDC.getText());

        DataType dataType = new DataType(strDatatype);
        Purpose purpose = new Purpose(strPurpose);
        int retention = Integer.parseInt(strRetention);
        DataController DC = new DataController(strDC);

        DUR newDUR = new DUR(purpose, retention);
        DCR newDCR = new DCR(DC, newDUR);


        if (update) {
            final Intent intent = getIntent();
            PilotRule myRule = intent.getParcelableExtra(EXTRA_DATA_UPDATE_POLICY);

            PilotRule newRule = new PilotRule(dataType, newDCR, null, myRule.getIdRule());
            PilotPolicy currentPolicy = null;
            try {
                currentPolicy = policyViewModel.getActivePolicy();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("policy_before", currentPolicy.getPolicyAsString());
            currentPolicy.updateRule(newRule);
            policyViewModel.updatePolicy(currentPolicy);
            policyViewModel.updateRule(newRule);
            Log.d("policy_after", currentPolicy.getPolicyAsString());

        } else {
            if (switchDataCategories.isChecked()) {
                DataTypeCategory dataTypeCategory = null;
                List<DataType> dataTypes = null;
                PilotPolicy currentPolicy = null;
                try {
                    dataTypeCategory = policyViewModel.getDataCategoryByName(strDataCategory);
                    dataTypes = dataTypeCategory.getDataTypes();
                    currentPolicy = policyViewModel.getActivePolicy();
                    for (DataType dt : dataTypes) {
                        PilotRule newRule = new PilotRule(dt, newDCR, null);
                        currentPolicy.addNewRule(newRule);
                        policyViewModel.updatePolicy(currentPolicy);
                        policyViewModel.insertRule(newRule);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                PilotRule newRule = new PilotRule(dataType, newDCR, null);
                PilotPolicy currentPolicy = null;
                try {
                    currentPolicy = policyViewModel.getActivePolicy();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("policy_before", currentPolicy.getPolicyAsString());
                currentPolicy.addNewRule(newRule); //if not exists
                policyViewModel.updatePolicy(currentPolicy);
                Log.d("policy_after", currentPolicy.getPolicyAsString());
                policyViewModel.insertRule(newRule); //it inserts the rule as a standalone object in the DB
            }
        }
    }

    private void setupAutocomplete(PilotRule... ruleToUpdate) {
        Resources res = getResources();
        String[] arrayDataChoice = res.getStringArray(R.array.labels_array_data_choice);
        String[] arrayPurposeChoice = res.getStringArray(R.array.labels_array_data_purpose);
        String[] arrayDCChoice = res.getStringArray(R.array.labels_array_controllers);
        String[] arrayDataCategoryChoice = res.getStringArray(R.array.labels_array_datatype_categories);
        ;

        policyViewModel = ViewModelProviders.of(this).get(PilotPolicyViewModel.class);

        //set up autocomplete for datatype
        final ArrayAdapter<String> adapter_DATA = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new ArrayList(Arrays.asList(arrayDataChoice)));

        AutoCompleteTextView textViewDataType = (AutoCompleteTextView)
                findViewById(R.id.label_autocomplete_datatype);
        textViewDataType.setAdapter(adapter_DATA);

        policyViewModel.getAllDataTypes().observe(this, new Observer<List<DataType>>() {
            @Override
            public void onChanged(@Nullable final List<DataType> dataTypes) {
                for (DataType d : dataTypes) {
                    adapter_DATA.add(d.getDataType());
                }
                adapter_DATA.notifyDataSetChanged();
            }
        });


        //set up autocomplete for datatype categories
        final ArrayAdapter<String> adapter_DataCategory = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new ArrayList(Arrays.asList(arrayDataCategoryChoice)));

        AutoCompleteTextView textViewDataTypeCategory = (AutoCompleteTextView)
                findViewById(R.id.label_autocomplete_datatype_category);
        textViewDataTypeCategory.setAdapter(adapter_DataCategory);

        policyViewModel.getAllDataTypeCategories().observe(this, new Observer<List<DataTypeCategory>>() {
            @Override
            public void onChanged(@Nullable final List<DataTypeCategory> dataTypeCategories) {
                for (DataTypeCategory dtc : dataTypeCategories) {
                    adapter_DataCategory.add(dtc.getName());
                }
                adapter_DataCategory.notifyDataSetChanged();
            }
        });


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
        AutoCompleteTextView textViewDC = findViewById(R.id.label_autocomplete_dc);
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


        if (ruleToUpdate.length > 0) {
            textViewDataType.setText(ruleToUpdate[0].getDataType().getDataType());
            textViewPurpose.setText(ruleToUpdate[0].getDcr().getDur().getPurpose().getPurpose());
            textViewDC.setText(ruleToUpdate[0].getDcr().getDataController().getDCname());
            EditText editTextRetention = findViewById(R.id.label_retention);
            editTextRetention.setText(String.valueOf(ruleToUpdate[0].getDcr().getDur().getRetentionTime()));
        }

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
