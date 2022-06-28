package at.wu_ac.victor_morel.ADPC_IoT.Tools;

import android.util.Log;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PolicyEngine {

    public static HashMap<String, HashMap> retrievedPolicies;
    public static HashMap<Integer, byte[]> retrievedPolicy;


    public static HashMap<String, String> parseADPCNotice (String fullNotice){
        HashMap<String, String> purposes = new HashMap<>();
        JSONArray jArray = null;
        try {
            jArray = new JSONArray(fullNotice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i=0; i < jArray.length(); i++)
        {
            try {
                JSONObject oneObject = jArray.getJSONObject(i);
                // Pulling items from the array
                purposes.put(oneObject.getString("id"), oneObject.getString("text"));
            } catch (JSONException e) {
                // Oops
            }
        }

        return purposes;
    }

    public static String reconstitutePolicies(byte[] fragment, String addressDCG) throws IOException {
        if (retrievedPolicies.containsKey(addressDCG)) {
            retrievedPolicy = retrievedPolicies.get(addressDCG);
            if (retrievedPolicy.size() == (int) fragment[1]) {
                return null;
                //if policy is already displayed, then return null
            } else {
                Log.i("test_demo","fragment retrieved");
                retrievedPolicy.put((int) fragment[2], Arrays.copyOfRange(fragment, 3, fragment.length));
                //otherwise add the fragment of the byte array to an hashmap
            }
        } else {
            //if this is a new DCG
            retrievedPolicy.put((int) fragment[2], Arrays.copyOfRange(fragment, 3, fragment.length));
            retrievedPolicies.put(addressDCG, retrievedPolicy);
        }

        if (retrievedPolicy.size() == (int) fragment[1]) {
            //when hashmap is full, merge the fragments
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (int i = 0; i < retrievedPolicy.size(); i++) {
                outputStream.write(retrievedPolicy.get(i));
            }
            byte[] toReturn = outputStream.toByteArray();
            //finally remove the extra 0 at the end of the array
            String str = new String(toReturn);
            return str;
        } else
            //if hashmap is not full, return null and wait a bit
            return null;
    }

    static byte[] trim(byte[] bytes)
    //remove the extra 0 at the end of a byte array
    {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }

        return Arrays.copyOf(bytes, i + 1);
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
