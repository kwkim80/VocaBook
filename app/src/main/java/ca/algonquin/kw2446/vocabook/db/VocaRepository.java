package ca.algonquin.kw2446.vocabook.db;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;

import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.util.ApplicationClass;
import ca.algonquin.kw2446.vocabook.util.Utility;

public class VocaRepository {

    public static long addSample_WordSet(Context context){
        WordSet wordSet= ApplicationClass.list.get(0);
        wordSet.setRegDate(Utility.getDateTime());
        VocaDB db=new VocaDB(context);
        db.open();
        long idx=db.insert_Item(wordSet);

        for (Voca item: ApplicationClass.vocas) {
            item.setWordSetId((int)idx);
            long idx2=db.insert_Item(item);
        }
        db.close();
        return idx;
    }

    public static ArrayList loadWordList(Context context, int i){
        ArrayList list;
        VocaDB db=new VocaDB(context);
        db.open();
        list=db.getVocaListBySetId(i);
        db.close();

        return list;

    }


    public static <T> ArrayList<T> getItemList(Context context, Class<T> obj){
        ArrayList<T> list;
        VocaDB db=new VocaDB( context);
        db.open();
        list=(ArrayList<T>) db.get_ItemList(obj);
        db.close();
        return  list;
    }

//    public static <T> T getItem(Context context){
//        T result;
//        VocaDB db=new VocaDB( context);
//        db.open();
//        result=(T) db.getItem<T>(i);
//        db.close();
//        return  result;
//    }

    public static <T> JsonArray getJson_ItemList(Context context, Class<T> target){
        ArrayList<T> list=getItemList(context,target);
        return Utility.convertArrayListToJson(list);
    }
}
