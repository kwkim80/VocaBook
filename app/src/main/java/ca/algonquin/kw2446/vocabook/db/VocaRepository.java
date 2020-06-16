package ca.algonquin.kw2446.vocabook.db;

import android.content.Context;

import com.google.gson.JsonArray;

import org.json.JSONException;

import java.util.ArrayList;

import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.util.ApplicationClass;
import ca.algonquin.kw2446.vocabook.util.JsonUtil;
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
        ArrayList<T> list=null;
        VocaDB db=new VocaDB( context);
        db.open();
        //list=db.get_ItemListToJsonToList(obj);
        try {
            list= JsonUtil.convertCursorToArrayList(obj, db.get_List(obj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public static <T> boolean update_Item(Context context, T obj){
        boolean result;
        VocaDB db=new VocaDB( context);
        db.open();
        result=db.update_Item(obj);
        db.close();
        return  result;
    }

    public static <T> boolean delete_Item(Context context,T obj){
        boolean result;
        VocaDB db=new VocaDB( context);
        db.open();
        result=db.delete_Item(obj);
        db.close();
        return  result;
    }

    public static <T> int insert_Item(Context context,T obj){
        int result;
        VocaDB db=new VocaDB( context);
        db.open();
        result=(int)db.insert_Item(obj);
        db.close();
        return  result;
    }

    public static <T> int insert_List(Context context,ArrayList<T> list){
        VocaDB db=new VocaDB(context);
        db.open();

        int result=db.insert_ItemList(list);
        db.close();

        return result;
    }
}
