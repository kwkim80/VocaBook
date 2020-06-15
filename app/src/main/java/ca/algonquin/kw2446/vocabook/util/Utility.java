package ca.algonquin.kw2446.vocabook.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import ca.algonquin.kw2446.vocabook.R;
import ca.algonquin.kw2446.vocabook.db.VocaDB;
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;

public class Utility {

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

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

    public static <T> JsonArray convertArrayListToJson(ArrayList<T> list){


        JsonArray result = (JsonArray) new Gson().toJsonTree(list,
                new TypeToken<List<T>>() {
                }.getType());

        return result;
    }

    public static <T> HashMap<String, Object> ObjectToMap(T v){
        ObjectMapper oMapper = new ObjectMapper();
       oMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        oMapper.setDefaultPropertyInclusion(Value.construct(Include.NON_EMPTY, Include.CUSTOM, null, ExludeEmptyObjects.class));

        // object -> Map
        HashMap map = oMapper.convertValue(v, HashMap.class);
        //System.out.println(map);
        return map;
    }


    public static <T> T setData(T obj, HashMap<String, Object> fields) {
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            Method m = null;
            try {

               for (Method method:obj.getClass().getMethods()) {
                    if(method.getName().equalsIgnoreCase("set"+entry.getKey())){
                        m=method; break;
                    }
                };

//               List<Method> ms= Arrays.stream(obj.getClass().getMethods())
//                        .filter(e -> e.getName().equalsIgnoreCase("set"+entry.getKey()))
//                        .filter(e -> e.getParameterTypes().length ==  1)
//                       // .filter(e -> e.getParameterTypes()[0].equals(entry.getValue().getClass()))
//                        .collect(Collectors.toList());


                if (m != null) {
                    if (m.getParameterTypes()[0] == boolean.class) {
                        m.invoke(obj,(int)entry.getValue()==0?false:true);
                    }
                    else m.invoke(obj, entry.getValue());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }



    public static void ShowAlertDialog(Activity from){
        View promptsView = LayoutInflater.from(from).inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                from);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }

                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private static class ExludeEmptyObjects{
        @Override
        public boolean equals(Object o) {
            if (o instanceof Map) {
                return ((Map) o).size() == 0;
            }
            if (o instanceof Collection) {
                return ((Collection) o).size() == 0;
            }
            return false;
        }
    }


}


