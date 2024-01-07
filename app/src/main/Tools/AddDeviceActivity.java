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

/**
 * Activity class for adding a new device.
 * This class provides the user interface to add a new controller device.
 */
public class AddDeviceActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        
        // Set up the toolbar at the top of the screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the floating action button and set its click listener
        FloatingActionButton fab_more = (FloatingActionButton) findViewById(R.id.fab_more);
        fab_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Call addDevice method when the button is clicked
                addDevice();
            }
        });

        // Set up the action bar
        setupActionBar();
    }

    /**
     * Method to add a new device.
     * Gathers input from the user, updates the device list, and navigates back to the main activity.
     */
    private void addDevice() {
        // Variables to hold MAC address and name input by the user
        String strMAC;
        String strName;

        // Get input from AutoCompleteTextView fields
        AutoCompleteTextView acMAC = findViewById(R.id.label_autocomplete_mac);
        AutoCompleteTextView acname = findViewById(R.id.label_autocomplete_name);

        // Convert input text to strings
        strMAC = String.valueOf(acMAC.getText());
        strName = String.valueOf(acname.getText());

        // Update the list of devices with the new device
        updateDevices(strMAC, strName);

        // Display a confirmation message
        Toast.makeText(AddDeviceActivity.this,
                "Device added!",
                Toast.LENGTH_SHORT)
                .show();

        // Intent to navigate back to the main activity
        Intent goBack = new Intent(this,
                MainActivity.class);
        startActivity(goBack);
    }

    /**
     * Static method to update the list of devices.
     * @param a MAC address of the new device.
     * @param b Name of the new device.
     */
    private static void updateDevices (String a, String b){
        // Add the new device to the device list in MainActivity
        MainActivity.devices.put(a, b);
    }

    /**
     * Sets up the action bar for the activity.
     * Adds an 'Up' button to the action bar for navigation.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // These methods are part of the AdapterView.OnItemSelectedListener interface
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Method stub for handling item selection in an AdapterView
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Method stub for handling the scenario when nothing is selected in an AdapterView
    }
}
