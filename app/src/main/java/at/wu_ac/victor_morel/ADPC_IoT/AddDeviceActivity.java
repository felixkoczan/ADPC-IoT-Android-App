package at.wu_ac.victor_morel.ADPC_IoT;

import static at.wu_ac.victor_morel.ADPC_IoT.MainActivity.devices;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

// simple activity to manually add a controller device
public class AddDeviceActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_more = (FloatingActionButton) findViewById(R.id.fab_more);
        fab_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addDevice();
            }
        });


        setupActionBar();
    }

    private void addDevice() {
        String strMAC;
        String strName;

        AutoCompleteTextView acMAC = findViewById(R.id.label_autocomplete_mac);
        AutoCompleteTextView acname = findViewById(R.id.label_autocomplete_name);

        strMAC = String.valueOf(acMAC.getText());
        strName = String.valueOf(acname.getText());

        updateDevices(strMAC, strName);

        Toast.makeText(AddDeviceActivity.this,
                "Device added!",
                Toast.LENGTH_SHORT)
                .show();

        Intent goBack = new Intent(this,
                MainActivity.class);
        startActivity(goBack);
    }

    // needs to be in a separate static function
    private static void updateDevices (String a, String b){
        MainActivity.devices.put(a, b);
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
