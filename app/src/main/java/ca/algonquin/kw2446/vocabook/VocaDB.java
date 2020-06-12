package ca.algonquin.kw2446.vocabook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class VocaDB {

    private static final String LOG = "DBHelper";

    private static final String KEY_ROWID="id";

    private final String WordSet_TABLE="WordSet";
    private static final String KEY_TITLE="title";
    private static final String KEY_CATEGORY="category";
    private static final String KEY_REGDATE="regDate";


    private final String Voca_TABLE="Voca";
    private static final String KEY_SETID="wordSetId";
    private static final String KEY_WORD="word";
    private static final String KEY_MEAN="mean";
    private static final String KEY_PAST="past";

    private static final String KEY_HARD="isHard";

    private final String DB_NAME="VocaDB";
    private final int DB_VERSION=3;

    private DBHelper dbHelper;
    private Context ourContext;
    private SQLiteDatabase ourDB;

    String[] wordset_cols =new String[]{KEY_ROWID,KEY_TITLE,KEY_CATEGORY,KEY_REGDATE};
    String[] voca_cols=new String[]{KEY_ROWID,KEY_SETID ,KEY_WORD,KEY_MEAN, KEY_HARD};

    public VocaDB(Context context) {
        this.ourContext = context;
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context, @Nullable String dbName, int version) {
            super(context, dbName, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String create_WordSetTable_Sql = String.format("create table %s ( %s Integer Primary key AUTOINCREMENT," +
                    " %s text not null, " +
                    " %s text not null, " +
                    " %s datetime);", WordSet_TABLE, KEY_ROWID, KEY_TITLE, KEY_CATEGORY, KEY_REGDATE);

            String create_VocaTable_Sql = String.format("create table %s ( %s Integer Primary key AUTOINCREMENT," +
                    " %s int not null, " +
                    " %s text not null, " +
                    " %s text not null, " +
                    " %s boolean not null);", Voca_TABLE, KEY_ROWID, KEY_SETID, KEY_WORD, KEY_MEAN, KEY_HARD);

            db.execSQL(create_WordSetTable_Sql);
            db.execSQL(create_VocaTable_Sql);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + WordSet_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Voca_TABLE);

            onCreate(db);
        }

        public long createSql(String table_name, ContentValues cv) {
            return ourDB.insert(table_name,null,cv);
        }

        public ContentValues makeContentValue(Map<String, Object> data){
            ContentValues cv = new ContentValues();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                // System.out.println(entry.getValue().getClass());
                String theType = entry.getValue().getClass().toString();
                Object theVal = entry.getValue();
                try {
                    if (theType.equalsIgnoreCase("java.lang.String")) {
                        Class<? extends String> theClass;
                        theClass = Class.forName(theType).asSubclass(String.class);

                        String obj = theClass.cast(theVal);
                        cv.put(entry.getKey(), obj);
                    } else {
                        Class<? extends Integer> theClass;
                        theClass = Class.forName(theType).asSubclass(Integer.class);
                        Integer obj = theClass.cast(theVal);
                        cv.put(entry.getKey(), obj);
                    }
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.getMessage());
                }

            }
            return cv;
        }
    }


    public VocaDB open() throws SQLException {
        dbHelper=new DBHelper(ourContext,DB_NAME,DB_VERSION);
        ourDB=dbHelper.getWritableDatabase();
        return this;

    }

    public void close(){
        dbHelper.close();
    }

    public long createWordSetEntry(String title, String category){
        ContentValues cv=new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_CATEGORY,category);
        cv.put(KEY_REGDATE,getDateTime());
        return dbHelper.createSql(WordSet_TABLE,cv );

    }

    public long createVocaEntry(long idx, String word, String mean){
        ContentValues cv=new ContentValues();
        cv.put(KEY_SETID, idx);
        cv.put(KEY_WORD, word);
        cv.put(KEY_MEAN,mean);
        cv.put(KEY_HARD,false);
        return dbHelper.createSql(Voca_TABLE,cv );

    }

    public boolean  createVocaListEntry(long idx, ArrayList<Voca> list){
        boolean result=false;
        try {
            for(Voca voca:list){
                if(!voca.getWord().isEmpty()){
                    createVocaEntry(idx,voca.getWord(), voca.getMean());
                }
            }
            result=true;
        }catch (Exception e){
            Log.d("createVocaListEntry", e.getMessage());
            result=false;
        }

        return  result;
    }

    /*
     * get single WordSet
     */
    public WordSet getWordSet(long wordset_id) {

//        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " WHERE " + KEY_ID + " = " + todo_id;
//        Cursor c = db.rawQuery(selectQuery, null);


        String whereClause = KEY_ROWID+" = ? ";
        String[] whereArgs = new String[] {String.valueOf(wordset_id)};
        String orderBy = KEY_ROWID+" desc";

        Cursor c=ourDB.query(WordSet_TABLE, wordset_cols,whereClause,whereArgs,null,null,orderBy,null);

        if (c != null)
            c.moveToFirst();

       WordSet ws=new WordSet();
        ws.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
        ws.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
        ws.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));
        ws.setRegDate(c.getString(c.getColumnIndex(KEY_REGDATE)));
        return ws;
    }

    public ArrayList<WordSet> getAll_WordSet(){
        ArrayList<WordSet> list=new ArrayList<>();


        String orderBy = KEY_ROWID+" desc";
        Cursor c=ourDB.query(WordSet_TABLE, wordset_cols,null,null,null,null,orderBy,null);


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            WordSet ws=new WordSet();
            ws.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
            ws.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
            ws.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));
            ws.setRegDate(c.getString(c.getColumnIndex(KEY_REGDATE)));
            list.add(ws);
        }

        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                ws.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
//                ws.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
//                ws.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));
//                ws.setRegDate(c.getString(c.getColumnIndex(KEY_REGDATE)));
//                list.add(ws);
//            } while (c.moveToNext());
//        }

        c.close();
        return list;

    }

    public ArrayList<WordSet> getAll_WordSetWithVocaList(){
        ArrayList<WordSet> list=new ArrayList<>();


        String orderBy = KEY_ROWID+" desc";
        Cursor c=ourDB.query(WordSet_TABLE,wordset_cols,null,null,null,null,orderBy,null);


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            WordSet ws=new WordSet();
            ws.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
            ws.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
            ws.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));
            ws.setRegDate(c.getString(c.getColumnIndex(KEY_REGDATE)));
            list.add(ws);
        }


        c.close();
        return list;

    }

    public ArrayList<Voca> getAll_VocaList(int i){
        ArrayList<Voca> list=new ArrayList<>();
        
        String whereClause = KEY_SETID+" = ? ";
        String[] whereArgs = new String[] {String.valueOf(i)};
        String orderBy = KEY_ROWID+" desc";
        Cursor c=ourDB.query(Voca_TABLE,voca_cols,whereClause,whereArgs,null,null,null,null);
        //String selectQuery = "SELECT  * FROM " + Voca_TABLE + " WHERE " + KEY_ID + " = " + todo_id;
//        String selectQuery = "SELECT  * FROM " + Voca_TABLE ;
//        Log.d("selectQuery",selectQuery);
//      Cursor c = ourDB.rawQuery(selectQuery, null);


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Voca voca=new Voca();
            voca.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
            voca.setWordSet_id(c.getInt(c.getColumnIndex(KEY_SETID)));
            voca.setWord(c.getString(c.getColumnIndex(KEY_WORD)));
            voca.setMean(c.getString(c.getColumnIndex(KEY_MEAN)));
            voca.setHard(c.getInt(c.getColumnIndex(KEY_HARD)) == 1);
            list.add(voca);
        }
        c.close();
        return list;
    }

    public JSONArray get_WordSetList_Json(){
        String selectQuery = "SELECT  * FROM WordSet";
        Cursor cursor = ourDB.rawQuery(selectQuery, null);
        return getDataByJson(cursor);
    }

    public JSONArray get_VocaList_Json(){
        String selectQuery = "SELECT  * FROM Voca";
        Cursor cursor = ourDB.rawQuery(selectQuery, null);
        return getDataByJson(cursor);
    }

    public JSONArray getDataByJson(Cursor cursor) {



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
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
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

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


}
