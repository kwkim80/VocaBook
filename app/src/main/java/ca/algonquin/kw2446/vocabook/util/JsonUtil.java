package ca.algonquin.kw2446.vocabook.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.algonquin.kw2446.vocabook.model.Voca;

public class JsonUtil {

    public static JSONArray convertCursorToJson(Cursor cursor) {
        JSONArray resultSet     = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else rowObject.put( cursor.getColumnName(i) ,  "" );
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", e.getMessage()  );
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME", resultSet.toString() );
        return resultSet;

    }

    public static <T> JsonArray convertArrayListToJsonArray(ArrayList<T> list){

        JsonArray result = (JsonArray) new Gson().toJsonTree(list,
                new TypeToken<List<T>>() {
                }.getType());

        return result;
    }

    public static <T> ArrayList<T> convertJsonStringToArrayList(String json, Class<T> target)
    {
        ObjectMapper mapper = new ObjectMapper();

        ArrayList<Voca> list=new ArrayList<>();
        try {

//            List<Voca> tempList=  Arrays.asList(mapper.readValue(json, Voca[].class));
//            list.addAll(tempList);
//            Log.d("list",String.valueOf(list.size()));

            TypeFactory typeFactory = mapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(
                    ArrayList.class, target);
            return mapper.readValue(json, collectionType);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public  static <T>  ArrayList<T> convertListFromJSONArray(Class<T> forClass, JSONArray array)
            throws JSONException {
        ArrayList<T> tObjects = new ArrayList<>();

        for (int i = 0; i < (array != null ? array.length() : 0); ){
            tObjects.add( (T) createT(forClass, array.getJSONObject(i++)));
            }

        return tObjects;
    }

    private static <T> T createT(Class<T> forClass, JSONObject jObject) {
        // instantiate via reflection / use constructor or whatsoever
        T tObject = null;
        try {
            tObject = forClass.newInstance();
        } catch (IllegalAccessException e) {
           Log.d("IllegalAccessException", e.getMessage());
        } catch (InstantiationException e) {
            Log.d("InstantiationException", e.getMessage());
    }
        // if not using constuctor args  fill up
        //
        // return new pojo filled object
        return tObject;
    }


    public static <T> ArrayList<T> convertCursorToArrayList(Class<T> targetClass, Cursor cursor) throws JSONException {
        JSONArray jsonArray=convertCursorToJson(cursor);
        return convertJsonStringToArrayList( jsonArray.toString(),targetClass);

    }

//    public static <T> JSONArray convert(Collection<T> list)
//    {
//        return new JSONArray(list);
//    }

}
