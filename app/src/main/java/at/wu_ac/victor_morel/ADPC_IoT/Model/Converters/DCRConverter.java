package at.wu_ac.victor_morel.ADPC_IoT.Model.Converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import at.wu_ac.victor_morel.ADPC_IoT.Model.Entity.DCR;

public class DCRConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<DCR> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<DCR>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ListToString(List<DCR> someObjects) {
        return gson.toJson(someObjects);
    }
}
