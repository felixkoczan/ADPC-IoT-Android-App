package at.wu_ac.victor_morel.ADPC_IoT.Tools;

import android.util.Log;
import android.util.SparseArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DCR;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DUR;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataController;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DataType;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotPolicy;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.PilotRule;
import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.Purpose;
import at.wu_ac.victor_morel.ADPC_IoT.PilotPolicyProto;

public class PolicyEngine {

    public static HashMap<String, HashMap> retrievedPolicies;
    public static HashMap<Integer, byte[]> retrievedPolicy;

    public static PilotPolicy receivedPolicy;

    public static PilotPolicyProto.PilotPolicy createDummyDCP() {
        //self-explanatory
        PilotPolicyProto.PilotPolicy demo = PilotPolicyProto.PilotPolicy.newBuilder()
                .addPilotRule(
                        PilotPolicyProto.PilotPolicy.PilotRule.newBuilder()
                                .setDatatype("Wi-Fi MAC Address")
                                .addDcr(
                                        PilotPolicyProto.PilotPolicy.PilotRule.DCR.newBuilder()
                                                .setEntity("Google")
                                                .addDur(
                                                        PilotPolicyProto.PilotPolicy.PilotRule.DCR.DUR.newBuilder()
                                                                .setPurpose("Marketing")
                                                                .setRetentionTime(20)
                                                )
                                )
                ).build();
        byte[] demoByte = demo.toByteArray();
        String demoByteString = Arrays.toString(demoByte);
        Log.i("testProto string", demo.toString());
        Log.i("testProto byte", demoByteString);
        Log.i("testProto size", String.valueOf(demoByte.length));
        //  byte pp[pp_size] = { 10, 44, 10, 17, 87, 105, 45, 70, 105, 32, 77, 65, 67, 32, 65, 100, 100, 114, 101, 115, 115, 18, 23, 18, 6, 71, 111, 111, 103, 108, 101, 26, 13, 10, 9, 77, 97, 114, 107, 101, 116, 105, 110, 103, 16, 20 };
        return demo;
    }

    public static PilotPolicyProto.PilotPolicy createExampleDCP() {
        //self-explanatory
        PilotPolicyProto.PilotPolicy demo = PilotPolicyProto.PilotPolicy.newBuilder()
                .addPilotRule(
                        PilotPolicyProto.PilotPolicy.PilotRule.newBuilder()
                                .setDatatype("Location")
                                .addDcr(
                                        PilotPolicyProto.PilotPolicy.PilotRule.DCR.newBuilder()
                                                .setEntity("Interparking")
                                                .addDur(
                                                        PilotPolicyProto.PilotPolicy.PilotRule.DCR.DUR.newBuilder()
                                                                .setPurpose("Marketing")
                                                                .setRetentionTime(30)
                                                )
                                )
                )
                .addPilotRule(
                        PilotPolicyProto.PilotPolicy.PilotRule.newBuilder()
                                .setDatatype("Location")
                                .addDcr(
                                        PilotPolicyProto.PilotPolicy.PilotRule.DCR.newBuilder()
                                                .setEntity("Interparking")
                                                .addDur(
                                                        PilotPolicyProto.PilotPolicy.PilotRule.DCR.DUR.newBuilder()
                                                                .setPurpose("Analytics")
                                                                .setRetentionTime(30)
                                                )
                                )
                ).build();
        byte[] demoByte = demo.toByteArray();
        String demoByteString = Arrays.toString(demoByte);
        Log.i("testProto string", demo.toString());
        Log.i("testProto byte", demoByteString);
        Log.i("testProto size", String.valueOf(demoByte.length));
        //  byte pp[pp_size] = { 10, 41, 10, 8, 76, 111, 99, 97, 116, 105, 111, 110, 18, 29, 18, 12, 73, 110, 116, 101, 114, 112, 97, 114, 107, 105, 110, 103, 26, 13, 10, 9, 77, 97, 114, 107, 101, 116, 105, 110, 103, 16, 20 };
        // [10, 41, 10, 8, 76, 111, 99, 97, 116, 105, 111, 110, 18, 29, 18, 12, 73, 110, 116, 101, 114, 112, 97, 114, 107, 105, 110, 103, 26, 13, 10, 9, 77, 97, 114, 107, 101, 116, 105, 110, 103, 16, 30, 10, 41, 10, 8, 76, 111, 99, 97, 116, 105, 111, 110, 18, 29, 18, 12, 73, 110, 116, 101, 114, 112, 97, 114, 107, 105, 110, 103, 26, 13, 10, 9, 65, 110, 97, 108, 121, 116, 105, 99, 115, 16, 30]
        return demo;
    }

    public static PilotPolicy policyProtoToModel(PilotPolicyProto.PilotPolicy protoPolicy) {
        List<PilotRule> rules = new ArrayList<>();
        for (PilotPolicyProto.PilotPolicy.PilotRule pr : protoPolicy.getPilotRuleList()) {
            rules.add(ruleProtoToModel(pr));
        }
        PilotPolicy modelPolicy = new PilotPolicy(rules, "Test device", false);
        return modelPolicy;
    }

    public static PilotPolicyProto.PilotPolicy policyModelToProto(PilotPolicy modelPolicy) {
        PilotPolicyProto.PilotPolicy.Builder policy = PilotPolicyProto.PilotPolicy.newBuilder();
        for (PilotRule pr : modelPolicy.getRules()) {
            policy.addPilotRule(ruleModelToProto(pr));
        }
        return policy.build();
    }

    public static PilotPolicyProto.PilotPolicy.Builder policyModelToBuilder(PilotPolicy modelPolicy) {
        PilotPolicyProto.PilotPolicy.Builder policy = PilotPolicyProto.PilotPolicy.newBuilder();
        for (PilotRule pr : modelPolicy.getRules()) {
            policy.addPilotRule(ruleModelToBuilder(pr));
        }
        return policy;
    }

    public static DUR durProtoToModel(PilotPolicyProto.PilotPolicy.PilotRule.DCR.DUR protoDUR) {
        return new DUR(new Purpose(protoDUR.getPurpose()), protoDUR.getRetentionTime());
    }

    public static PilotPolicyProto.PilotPolicy.PilotRule.DCR.DUR durModelToProto(DUR modelDUR) {
        return PilotPolicyProto.PilotPolicy.PilotRule.DCR.DUR.newBuilder()
                .setPurpose(modelDUR.getPurpose().getPurpose())
                .setRetentionTime(modelDUR.getRetentionTime())
                .build();
    }

    public static PilotPolicyProto.PilotPolicy.PilotRule.DCR.DUR.Builder durModelToBuilder(DUR modelDUR) {
        return PilotPolicyProto.PilotPolicy.PilotRule.DCR.DUR.newBuilder()
                .setPurpose(modelDUR.getPurpose().getPurpose())
                .setRetentionTime(modelDUR.getRetentionTime());
    }

    public static DCR dcrProtoToModel(PilotPolicyProto.PilotPolicy.PilotRule.DCR protoDCR) {
        return new DCR(new DataController(protoDCR.getEntity()), durProtoToModel(protoDCR.getDur(0)));
    }

    public static PilotPolicyProto.PilotPolicy.PilotRule.DCR dcrModelToProto(DCR modelDCR) {
        return PilotPolicyProto.PilotPolicy.PilotRule.DCR.newBuilder()
                .setEntity(modelDCR.getDataController().getDCname())
                .addDur(durModelToProto(modelDCR.getDur()))
                .build();
    }

    public static PilotPolicyProto.PilotPolicy.PilotRule.DCR.Builder dcrModelToBuilder(DCR modelDCR) {
        return PilotPolicyProto.PilotPolicy.PilotRule.DCR.newBuilder()
                .setEntity(modelDCR.getDataController().getDCname())
                .addDur(durModelToBuilder(modelDCR.getDur()));
    }

    public static PilotRule ruleProtoToModel(PilotPolicyProto.PilotPolicy.PilotRule protoRule) {
        return new PilotRule(new DataType(protoRule.getDatatype()), dcrProtoToModel(protoRule.getDcr(0)), null);
    }

    public static PilotPolicyProto.PilotPolicy.PilotRule ruleModelToProto(PilotRule modelRule) {
        return PilotPolicyProto.PilotPolicy.PilotRule.newBuilder()
                .setDatatype(modelRule.getDataType().getDataType())
                .addDcr(dcrModelToProto(modelRule.getDcr()))
                .build();
    }

    public static PilotPolicyProto.PilotPolicy.PilotRule.Builder ruleModelToBuilder(PilotRule modelRule) {
        return PilotPolicyProto.PilotPolicy.PilotRule.newBuilder()
                .setDatatype(modelRule.getDataType().getDataType())
                .addDcr(dcrModelToBuilder(modelRule.getDcr()));
    }

    public static boolean comparePolicies(PilotPolicy DCP, PilotPolicy DSP) {
        //method to compare a DSP and a DCP
        //return true if DSP>DCP
        HashMap<Integer, Boolean> compTable = new HashMap<>();
        int count = 0;
        for (PilotRule prdc : DCP.getRules()) {
            for (PilotRule prds : DSP.getRules()) {
                if ((prdc.getDataType().getDataType().equals(prds.getDataType().getDataType()))
                        && (prdc.getDcr().getDataController().getDCname().equals(prds.getDcr().getDataController().getDCname()))
                        && (prdc.getDcr().getDur().getPurpose().getPurpose().equals(prds.getDcr().getDur().getPurpose().getPurpose()))
                        && (prdc.getDcr().getDur().getRetentionTime() <= prds.getDcr().getDur().getRetentionTime())) {
                    compTable.put(prdc.getIdRule(), true);
                }
            }
            count++;
        }
        if (compTable.size() == count) {
            return true;
        } else
            return false;
    }

    public static PilotPolicy intersectionPolicies(PilotPolicy DCP, PilotPolicy DSP) {
        List<PilotRule> rules = new ArrayList<>();
        for (PilotRule prdc : DCP.getRules()) {
            for (PilotRule prds : DSP.getRules()) {
                if ((prdc.getDataType().getDataType().equals(prds.getDataType().getDataType()))
                        && (prdc.getDcr().getDataController().getDCname().equals(prds.getDcr().getDataController().getDCname()))
                        && (prdc.getDcr().getDur().getPurpose().getPurpose().equals(prds.getDcr().getDur().getPurpose().getPurpose()))
                        && (prdc.getDcr().getDur().getRetentionTime() <= prds.getDcr().getDur().getRetentionTime())) {
                    rules.add(prdc);
                }
            }
        }
        PilotPolicy intersection = new PilotPolicy(rules, "intersection", false);
        return intersection;
    }

    public static PilotPolicy outerJoinPolicies(PilotPolicy DCP, PilotPolicy DSP) {
        List<PilotRule> rules = new ArrayList<>();
        HashMap<Integer, Boolean> compTable = new HashMap<>();
        for (PilotRule prdc : DCP.getRules()) {
            for (PilotRule prds : DSP.getRules()) {
                if ((prdc.getDataType().getDataType().equals(prds.getDataType().getDataType()))
                        && (prdc.getDcr().getDataController().getDCname().equals(prds.getDcr().getDataController().getDCname()))
                        && (prdc.getDcr().getDur().getPurpose().getPurpose().equals(prds.getDcr().getDur().getPurpose().getPurpose()))
                        && (prdc.getDcr().getDur().getRetentionTime() <= prds.getDcr().getDur().getRetentionTime())) {
                    compTable.put(prdc.getIdRule(), true);
                }
            }
        }

        for (PilotRule prdc : DCP.getRules()) {
            if (!compTable.containsKey(prdc.getIdRule())) {
                rules.add(prdc);
            }
        }

        PilotPolicy outer = new PilotPolicy(rules, "outer", false);
        return outer;
    }

    public static PilotPolicy reconstitutePolicy(byte[] fragment) throws IOException {
        if (retrievedPolicy.size() == (int) fragment[1]) {
            return null;
            //if policy is already displayed, then return null
        }
        //otherwise add the fragment of the byte array to an hashmap
        retrievedPolicy.put((int) fragment[2], Arrays.copyOfRange(fragment, 3, fragment.length));
        if (retrievedPolicy.size() == (int) fragment[1]) {
            //when hashmap is full, merge the fragments
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (int i = 0; i < retrievedPolicy.size(); i++) {
                outputStream.write(retrievedPolicy.get(i));
            }
            byte[] toReturn = outputStream.toByteArray();
            //finally remove the extra 0 at the end of the array
            //rebuild the protoPilotPolicy from the byte array
            //and transform it into a comparable modelPilotPolicy
            return policyProtoToModel(PilotPolicyProto.PilotPolicy.parseFrom(trim(toReturn)));
        } else
            //if hashmap is not full, return null and wait a bit
            return null;
    }

    public static PilotPolicy reconstitutePoliciesSA(SparseArray<byte[]> fragment, String addressDCG) throws IOException {
//        if (retrievedPolicies.containsKey(addressDCG)) {
//            retrievedPolicy = retrievedPolicies.get(addressDCG);
//            if (retrievedPolicy.size() ==  fragment.get(1)) {
//                return null;
//                //if policy is already displayed, then return null
//            } else {
//                Log.i("test_demo","fragment retrieved");
//                retrievedPolicy.put((int) fragment[2], Arrays.copyOfRange(fragment, 3, fragment.length));
//                //otherwise add the fragment of the byte array to an hashmap
//            }
//        } else {
//            //if this is a new DCG
//            retrievedPolicy.put((int) fragment[2], Arrays.copyOfRange(fragment, 3, fragment.length));
//            retrievedPolicies.put(addressDCG, retrievedPolicy);
//        }
//
//        if (retrievedPolicy.size() == (int) fragment[1]) {
//            //when hashmap is full, merge the fragments
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            for (int i = 0; i < retrievedPolicy.size(); i++) {
//                outputStream.write(retrievedPolicy.get(i));
//            }
//            byte[] toReturn = outputStream.toByteArray();
//            //finally remove the extra 0 at the end of the array
//            //rebuild the protoPilotPolicy from the byte array
//            //and transform it into a comparable modelPilotPolicy
//            return policyProtoToModel(PilotPolicyProto.PilotPolicy.parseFrom(trim(toReturn)));
//        } else
//            //if hashmap is not full, return null and wait a bit
            return null;
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
            //rebuild the protoPilotPolicy from the byte array
            //and transform it into a comparable modelPilotPolicy
            String str = new String(toReturn);
            return str;
//            return policyProtoToModel(PilotPolicyProto.PilotPolicy.parseFrom(trim(toReturn)));
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
