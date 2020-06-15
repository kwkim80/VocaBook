package ca.algonquin.kw2446.vocabook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.util.Utility;

public class VocaDB {

    private static final String LOG = "DBHelper";

    private static final String KEY_ROWID = "id";

    private final String WordSet_TABLE = "WordSet";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_REGDATE = "regDate";


    private final String Voca_TABLE = "Voca";

    private static final String KEY_SETID = "wordSetId";
    private static final String KEY_WORD = "word";
    private static final String KEY_MEAN = "mean";
    private static final String KEY_CHANGED_WORD = "changedWord";
    private static final String KEY_HARD = "hard";

    private final String DB_NAME = "VocaDB";
    private final int DB_VERSION = 7;

    private DBHelper dbHelper;
    private Context ourContext;
    private SQLiteDatabase ourDB;

    String[] wordset_cols;
    String[] voca_cols;  // = new String[]{KEY_ROWID, KEY_SETID, KEY_WORD, KEY_MEAN, KEY_CHANGED_WORD, KEY_HARD};

    public VocaDB(Context context) {
        this.ourContext = context;
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context, @Nullable String dbName, int version) {
            super(context, dbName, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //to do : make query by using Class
            String create_WordSetTable_Sql = String.format("create table %s ( %s Integer Primary key AUTOINCREMENT," +
                    " %s text not null, " +
                    " %s text not null, " +
                    " %s datetime);", WordSet_TABLE, KEY_ROWID, KEY_TITLE, KEY_CATEGORY, KEY_REGDATE);

            String create_VocaTable_Sql = String.format("create table %s ( %s Integer Primary key AUTOINCREMENT," +
                    " %s int not null, " +
                    " %s text not null, " +
                    " %s text not null, " +
                    " %s text not null, " +
                    " %s boolean not null);", Voca_TABLE, KEY_ROWID, KEY_SETID, KEY_WORD, KEY_MEAN, KEY_CHANGED_WORD,KEY_HARD);

            db.execSQL(create_WordSetTable_Sql);
            db.execSQL(create_VocaTable_Sql);

            wordset_cols=get_Item_Cols(new WordSet());
            voca_cols=get_Item_Cols(new Voca());

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + WordSet_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Voca_TABLE);

            onCreate(db);
        }


        public ContentValues makeContentValue(HashMap<String, Object> data) {
            ContentValues cv = new ContentValues();
            for (String key : data.keySet()) {
                if(!key.equalsIgnoreCase("id")){
                    if(data.get(key) instanceof String )cv.put(key, (String)data.get(key));
                    else if(data.get(key) instanceof Boolean ) cv.put(key, (Boolean) data.get(key));
                    else if(data.get(key) instanceof Integer )cv.put(key, (Integer) data.get(key));
                    else if(data.get(key) instanceof Float )cv.put(key, (Float) data.get(key));
                    else if(data.get(key) instanceof Double ) cv.put(key, (Double) data.get(key));
                    else if(data.get(key) instanceof Byte )cv.put(key, (Byte) data.get(key));
                }
            }
            return cv;
        }

        public HashMap<String, Object> getCursorToColumnList(Cursor cursor) {

            int columnCount = cursor.getColumnCount();
            HashMap row = new HashMap(columnCount);
            for(int i=0; i<columnCount;i++){
                switch (cursor.getType(i))  {
                    case Cursor.FIELD_TYPE_FLOAT:
                        row.put(cursor.getColumnName(i), cursor.getFloat(i));
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        row.put(cursor.getColumnName(i), cursor.getInt(i));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        row.put(cursor.getColumnName(i), cursor.getString(i));
                        break;
                }
              }

            return row;
        }

        public <T> String[] get_Item_Cols(T obj){
           HashMap<String, Object> map = Utility.ObjectToMap(obj);

            String[] cols = new String[map.keySet().size()];
            return map.keySet().toArray(cols);
        }
    }

    public VocaDB open() throws SQLException {
        dbHelper = new DBHelper(ourContext, DB_NAME, DB_VERSION);
        ourDB = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbHelper.close();
    }


    public <T> long insert_Item(T obj) {
        HashMap map = Utility.ObjectToMap(obj);
        ContentValues cv = dbHelper.makeContentValue(map);
        long result =ourDB.insert(obj.getClass().getSimpleName(), null, cv);
        return  result;
    }

    public <T> int insert_ItemList(ArrayList<T> list){
        int cnt=0;
        try {
            for(T item:list)this.insert_Item(item);
            cnt++;
        }catch (Exception e){
            Log.d("insert_ItemList", e.getMessage());
           //to do : if cnt is same list_size, it will have to rollback
        }
        return cnt;
    }


    public <T> boolean update_Item(T obj) {
        HashMap map = Utility.ObjectToMap(obj);
        ContentValues cv = dbHelper.makeContentValue(map);

        return ourDB.update(obj.getClass().getSimpleName(), cv, KEY_ROWID + "=?", new String[]{map.get(KEY_ROWID).toString()}) == 1;
    }

    public <T> boolean delete_Item(T obj) {
        HashMap map = Utility.ObjectToMap(obj);
        return ourDB.delete(obj.getClass().getSimpleName(), KEY_ROWID + "=?", new String[]{map.get(KEY_ROWID).toString()}) == 1;
    }


    public <T> T get_Item(Class<T> targetClass, int id){
        //Class<T> clazzOfT;
        T newItem=null;
        try {
            T temp = targetClass.newInstance();
            String[] cols =dbHelper.get_Item_Cols(targetClass); //new String[map.keySet().size()];
            String whereClause = KEY_ROWID + " = ? ";
            String[] whereArgs = new String[]{String.valueOf(id)};
            String orderBy = KEY_ROWID + " desc";
            Cursor c = ourDB.query(targetClass.getClass().getSimpleName(), cols, whereClause, whereArgs, null, null, orderBy, null);


            HashMap<String, Object> itemCols = dbHelper.getCursorToColumnList(c);
            newItem = (T) Utility.setData(temp, itemCols);

            c.close();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        return newItem;
    }


    public <T> ArrayList<T> get_ItemList(Class<T> targetClass) {

        ArrayList<T> list = new ArrayList<>();
//        Class<T> clazzOfT;
//        Map<String, String> map = Utility.ObjectToMap(targetClass);
        try {

            T item = targetClass.newInstance();
        String[] cols = dbHelper.get_Item_Cols(item); //new String[map.keySet().size()];

        String orderBy = KEY_ROWID + " desc";
        Cursor c = ourDB.query(item.getClass().getSimpleName(), cols, null, null, null, null, orderBy, null);


            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                T newItem = targetClass.newInstance();
                HashMap<String, Object> itemCols = dbHelper.getCursorToColumnList(c);
                T temp = (T) Utility.setData(newItem, itemCols);
                list.add(temp);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return list;
    }




    public ArrayList<Voca> getVocaListBySetId(int i) {
        ArrayList<Voca> list = new ArrayList<>();

        String whereClause = KEY_SETID + " = ? ";
        String[] whereArgs = new String[]{String.valueOf(i)};
        String orderBy = KEY_ROWID + " desc";
        Cursor c = ourDB.query(Voca_TABLE, voca_cols, whereClause, whereArgs, null, null, null, null);
        //String selectQuery = "SELECT  * FROM " + Voca_TABLE + " WHERE " + KEY_ID + " = " + todo_id;
//        String selectQuery = "SELECT  * FROM " + Voca_TABLE ;
//        Log.d("selectQuery",selectQuery);
//      Cursor c = ourDB.rawQuery(selectQuery, null);


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Voca voca = new Voca();
            voca.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
            voca.setWordSetId(c.getInt(c.getColumnIndex(KEY_SETID)));
            voca.setWord(c.getString(c.getColumnIndex(KEY_WORD)));
            voca.setMean(c.getString(c.getColumnIndex(KEY_MEAN)));
            voca.setHard(c.getInt(c.getColumnIndex(KEY_HARD)) == 1);
            list.add(voca);
        }
        c.close();
        return list;
    }

    public JSONArray get_WordSetList_Json() {
        String selectQuery = "SELECT  * FROM WordSet";
        Cursor cursor = ourDB.rawQuery(selectQuery, null);
        return Utility.convertCursorToJson(cursor);
    }

    public JSONArray get_VocaList_Json() {
        String selectQuery = "SELECT  * FROM Voca";
        Cursor cursor = ourDB.rawQuery(selectQuery, null);
        return Utility.convertCursorToJson(cursor);
    }







}
