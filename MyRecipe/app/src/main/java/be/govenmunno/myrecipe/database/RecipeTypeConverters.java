package be.govenmunno.myrecipe.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecipeTypeConverters {
    @TypeConverter
    public String fromUuid(UUID uuid) {
        return uuid.toString();
    }

    @TypeConverter
    public UUID toUuid(String uuid) {
        return UUID.fromString(uuid);
    }


    @TypeConverter
    public List<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String fromArrayList(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public Map<Integer,String>fromStrin(String value){
        Type list = new TypeToken<HashMap<Integer,String>>(){}.getType();
        return new Gson().fromJson(value,list);
    }

    @TypeConverter
    public String fromMap(Map<Integer, String> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
