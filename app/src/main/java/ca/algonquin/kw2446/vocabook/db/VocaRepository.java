package ca.algonquin.kw2446.vocabook.db;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.util.ApplicationClass;
import ca.algonquin.kw2446.vocabook.util.JsonUtil;
import ca.algonquin.kw2446.vocabook.util.Utility;

public class VocaRepository {

    private VocaDB db;


    public VocaRepository(Context context) {
            db=VocaDB.getInstance(context);
    }

    public  long addSample_WordSet(){
        WordSet wordSet= ApplicationClass.list.get(0);
        wordSet.setRegDate(Utility.getDateTime());
        db.open();
        long idx=db.insert_Item(wordSet);

        for (Voca item: ApplicationClass.vocas) {
            item.setWordSetId((int)idx);
            long idx2=db.insert_Item(item);
        }
        db.close();
        return idx;
    }

    public  ArrayList loadWordList(int i){
        ArrayList list;

        db.open();
        list=db.getVocaListBySetId(i);
        db.close();

        return list;

    }

    public  <T> ArrayList getListByKey(Class<T> obj, String keyName, Object keyVal){
        ArrayList list=null;

        try {
            db.open();
            list=JsonUtil.convertCursorToArrayList(obj, db.getListByForeignKey(obj, keyName, keyVal));
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.close();
        }


        return list;

    }


    public  <T> ArrayList getItemList(Class<T> obj){
        ArrayList<T> list=null;


        try {
            db.open();
            list= JsonUtil.convertCursorToArrayList(obj, db.get_List(obj));
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.close();
        }
        return  list;
    }

    public  <T> ArrayList getItemList( Class<T> obj,String foreignKeyName, Object foreignKeyVal){
        ArrayList<T> list=null;

        db.open();
        try {
            Cursor c=db.getListByForeignKey(obj,foreignKeyName,foreignKeyVal);

            list= JsonUtil.convertCursorToArrayList(obj,c );

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.close();
        }
        return  list;
    }

    public  <T> T getItem(Class<T> obj, int idx){
        T result=null;

        db.open();
        try {
            result= JsonUtil.convertCursorToArrayList(obj, db.getItem(obj,idx)).get(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.close();
        }


        return  result;
    }

    public  <T> boolean update_Item( T obj){
        boolean result;

        db.open();
        result=db.update_Item(obj);
        db.close();
        return  result;
    }

    public  <T> boolean delete_Item(T obj){
        boolean result;

        db.open();
        result=db.delete_Item(obj);
        db.close();
        return  result;
    }

    public  <T> int delete_List( List<T> list){
        int result=0;

        db.open();
        for (T item: list) {
           if(db.delete_Item(item)){
               result++;
           }
        }
        db.close();
        return  result;
    }



    public  <T> int insert_Item(T obj){
        int result;

        db.open();
        result=(int)db.insert_Item(obj);
        db.close();
        return  result;
    }

    public  <T> int insert_List(ArrayList<T> list){

        db.open();
        int result=db.insert_ItemList(list);
        db.close();
        return result;
    }

    public  <T> JSONArray getJsonList(Class<T> obj){
        db.open();
        JSONArray list=JsonUtil.convertCursorToJson(db.get_List(obj));
        db.close();
        return list;
    }

    //sample method by studying
    public  <T> ArrayList get_ItemListToHashToList( Class<T> obj){
        ArrayList<T> list = new ArrayList<>();
        db.open();
        Cursor c=db.get_List(obj);



            try {
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    T newItem = null;
                    newItem = obj.newInstance();
                    HashMap<String, Object> itemCols = Utility.getCursorToColumnList(c);
                    T temp = (T) Utility.setData(newItem, itemCols);
                    list.add(temp);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }finally {
                db.close();
            }


        return list;
    }

    //sample method by studying
    public <T> T get_ItemByHashMap( Class<T> obj, int id){
        db.open();
        Cursor c=db.getItem(obj,id);
        T newItem=null;
        try {
            T temp = obj.newInstance();

            HashMap<String, Object> itemCols = Utility.getCursorToColumnList(c);
            newItem = (T) Utility.setData(temp, itemCols);

            c.close();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        return newItem;
    }

    public <T> void DeleteItemsAsyncTask(T... list){
      //  db.open();
         new DeleteItemAsyncTask<T>(db).execute(list);

    }

}
