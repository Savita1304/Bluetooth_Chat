package com.goo.vapps.bluetooth.chat.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.goo.vapps.bluetooth.chat.Model.ChatData;

import java.util.ArrayList;


/**
 * Created by compind-pc8 on 18/2/17.
 */
public class ChatRecoedDataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="ChatDB";
    private static final String TABLE_CHATDATA="ChatTable";
    private static final String KEY_ID="id";
    private static final String KEY_MESSAGE="message";
    private static final String KEY_FRIENDID="fid";








    public ChatRecoedDataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table ChatTable" +"(id integer primary key autoincrement ,message text,fid text)");
        }catch (Exception e){}

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



    public void addChatRecord(ChatData obj){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put(KEY_MESSAGE,obj.getMessage());
        value.put(KEY_FRIENDID,obj.getFid());
        db.insert(TABLE_CHATDATA, null, value);

    }



    public ArrayList<String> getChat(String fid){

        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        String query="select message from ChatTable where fid='"+fid+"' ";
        Log.e("sds",query);
        Cursor c=db.rawQuery(query,null);

        if (c.getCount()>0){
            while (c.moveToNext()) {
                String str = c.getString(0);
                //list.add(c.getString(c.getColumnIndex(KEY_MESSAGE)));
                list.add(str);
               // list.add(c.getString(c.getColumnIndex(KEY_MESSAGE)));
            }
        }
        return list;


    }





    public ArrayList<String> getChat1(String fid) {

        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select message from ChatTable where fid='" + fid + "' ";
        Log.e("sds", query);
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String str = c.getString(0);
                //list.add(c.getString(c.getColumnIndex(KEY_MESSAGE)));
                list.add(str);
            }
        }
        return list;


    }








}
