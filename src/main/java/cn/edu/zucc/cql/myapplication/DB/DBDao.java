package cn.edu.zucc.cql.myapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.edu.zucc.cql.myapplication.Note;
import java.util.ArrayList;

public class DBDao {

	private DBOpenHelper dbOpenHelper;
	public DBDao(Context context) {
		dbOpenHelper=new DBOpenHelper(context);
	}



	public void addNote(Note note){
		SQLiteDatabase db=dbOpenHelper.getWritableDatabase();

		ContentValues values=new ContentValues();
		values.put("title", note.title);
		values.put("date", note.date);
		db.insert("note", "_id", values);
		db.close();
	}


	public ArrayList<Note> searchNoteByDate(String date){
		SQLiteDatabase db=dbOpenHelper.getWritableDatabase();

		Cursor cursor=db.query("note", new String[]{"_id","date","title"}," date = ?",new String[]{date},null,null,"_id asc");
		ArrayList<Note> list=new ArrayList<Note>();

		Log.e("date", "db noteDate = "+date);

		while(cursor.moveToNext()){
			Log.e("date", "db new Note() = ");
			Note note = new Note();
			note.id = cursor.getInt(cursor.getColumnIndex("_id"));;
			note.title=cursor.getString(cursor.getColumnIndex("title"));
			note.date=cursor.getString(cursor.getColumnIndex("date"));
			list.add(note);
		}
		db.close();
		return list;
	}

	public Note searchNoteById(int id){
		SQLiteDatabase db=dbOpenHelper.getWritableDatabase();

		Cursor cursor=db.query("note", new String[]{"_id","title","date"}," _id =?",new String[]{String.valueOf(id)},null,null,"_id asc");
		Note note = null;

		while(cursor.moveToNext()){
			note = new Note();
			note.id = cursor.getInt(cursor.getColumnIndex("_id"));;
			note.title=cursor.getString(cursor.getColumnIndex("title"));
			note.date=cursor.getString(cursor.getColumnIndex("date"));
		}
		db.close();
		return note;
	}


	public void deleteNoteByDate(int id){
		SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
		db.delete("note", "_id=?", new String[]{String.valueOf(id)});
	}


	public void updateNote(Note note){

		SQLiteDatabase db=dbOpenHelper.getWritableDatabase();

		ContentValues values=new ContentValues();
		values.put("title", note.title);
		values.put("date", note.date);
		db.update("note", values, "_id=?",new String[]{String.valueOf(note.id)});
	}



}
