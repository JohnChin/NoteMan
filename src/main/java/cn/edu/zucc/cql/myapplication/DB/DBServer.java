package cn.edu.zucc.cql.myapplication.DB;

import java.util.ArrayList;

import cn.edu.zucc.cql.myapplication.Note;
import android.content.Context;

public class DBServer {
	public static void addNote(Context context,Note note){
		DBDao dbDao = new DBDao(context);
		dbDao.addNote(note);
	}
	public static ArrayList<Note> searchNoteByDate(Context context,String date){
		DBDao dbDao = new DBDao(context);
		return dbDao.searchNoteByDate(date);
	}
	public static Note searchNoteById(Context context,int id){
		DBDao dbDao = new DBDao(context);
		return dbDao.searchNoteById(id);
	}
	public static void updateNote(Context context,Note note){
		DBDao dbDao = new DBDao(context);
		dbDao.updateNote(note);
	}
	public static void deleteNoteByDate(Context context,int id){
		DBDao dbDao = new DBDao(context);
		dbDao.deleteNoteByDate(id);
	}
}
