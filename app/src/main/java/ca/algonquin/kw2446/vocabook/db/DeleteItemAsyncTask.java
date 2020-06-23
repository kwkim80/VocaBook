package ca.algonquin.kw2446.vocabook.db;

import android.os.AsyncTask;

import java.util.ArrayList;

public class DeleteItemAsyncTask<T> extends AsyncTask<T, Void,Void> {
    private static final String TAG="InsertAsyncWordSetTask";
    private VocaDB db;

    public DeleteItemAsyncTask(VocaDB db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(T... list) {
        db.delete_List(list);
        return null;
    }


}
