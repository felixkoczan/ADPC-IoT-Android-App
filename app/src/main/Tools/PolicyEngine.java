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

    // Static maps to store retrieved policies.
    public static HashMap<String, HashMap> retrievedPolicies;
    public static HashMap<Integer, byte[]> retrievedPolicy;

    /**
     * Parses a JSON string representing ADPC notices into a HashMap.
     * @param fullNotice The JSON string to be parsed.
     * @return A HashMap with purpose IDs as keys and their text as values.
     */
    public static HashMap<String, String> parseADPCNotice (String fullNotice){
        HashMap<String, String> purposes = new HashMap<>();
        JSONArray jArray = null;
        try {
            jArray = new JSONArray(fullNotice);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject oneObject = jArray.getJSONObject(i);
                // Add each purpose's ID and text to the HashMap.
                purposes.put(oneObject.getString("id"), oneObject.getString("text"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return purposes;
    }

    /**
     * Reconstitutes policy fragments into a single String.
     * @param fragment The byte array fragment of the policy.
     * @param addressDCG The address of the Data Control Gateway.
     * @return A String representation of the reconstituted policy, or null if incomplete.
     * @throws IOException If an I/O error occurs.
     */
    public static String reconstitutePolicies(byte[] fragment, String addressDCG) throws IOException {
        if (retrievedPolicies.containsKey(addressDCG)) {
            retrievedPolicy = retrievedPolicies.get(addressDCG);
            if (retrievedPolicy.size() == (int) fragment[1]) {
                return null; // Policy is already displayed.
            } else {
                retrievedPolicy.put((int) fragment[2], Arrays.copyOfRange(fragment, 3, fragment.length));
            }
        } else {
            retrievedPolicy = new HashMap<>();
            retrievedPolicy.put((int) fragment[2], Arrays.copyOfRange(fragment, 3, fragment.length));
            retrievedPolicies.put(addressDCG, retrievedPolicy);
        }

        if (retrievedPolicy.size() == (int) fragment[1]) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (int i = 0; i < retrievedPolicy.size(); i++) {
                outputStream.write(retrievedPolicy.get(i));
            }
            String str = new String(outputStream.toByteArray());
            return str;
        } else {
            return null; // Wait for more fragments.
        }
    }

    /**
     * Trims trailing zeros from a byte array.
     * @param bytes The byte array to be trimmed.
     * @return A trimmed byte array.
     */
    static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }

        return Arrays.copyOf(bytes, i + 1);
    }

    /**
     * Converts a byte array to a hexadecimal string.
     * @param a The byte array to convert.
     * @return A String representing the hexadecimal values of the bytes.
     */
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
