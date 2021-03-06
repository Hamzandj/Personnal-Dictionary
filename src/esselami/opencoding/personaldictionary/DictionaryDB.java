package esselami.opencoding.personaldictionary;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DictionaryDB {
	
	public static final String ID = "_id";
	public static final String WORD = "word";
	public static final String DEFINITION = "definition";
	public static final String STATUS = "status";
	public static final String USER = "user_created";
    public static final String TABLE_NAME = "words";

	public static final String BOOKMARKED = "b";
	public static final String USER_CREATED = "u";
	
	DatabaseInitializer initializer;
	
	public DictionaryDB(DatabaseInitializer initializer) {
		this.initializer = initializer;
	}
	
	
	public void addWord(String englishWord, String banglaWord) {
		SQLiteDatabase db = initializer.getWritableDatabase();
		
		String sql = "INSERT INTO " + TABLE_NAME + " (" + WORD + 
				", " + DEFINITION + ", " + USER + ") VALUES ('" + englishWord +
				"', '" + banglaWord + "', '" + USER_CREATED + "') ";
		db.execSQL(sql);
	}
	
	
	public List<Bean> getWords(String englishWord) {
		if(englishWord.equals(""))
			return new ArrayList<Bean>();
		
		String sql = "SELECT * FROM " + TABLE_NAME +
    			" WHERE " + WORD + " LIKE ? ORDER BY " + WORD + " LIMIT 100";
		
		SQLiteDatabase db = initializer.getReadableDatabase();
		
		Cursor cursor = null;
		try {
	        cursor = db.rawQuery(sql, new String[]{englishWord + "%"});
	        
	        List<Bean> wordList = new ArrayList<Bean>();
	        while(cursor.moveToNext()) {
	        	int id = cursor.getInt(0);
	        	String english = cursor.getString(1);
	        	String bangla = cursor.getString(2);
	        	String status = cursor.getString(3);
				wordList.add(new Bean(id, english, bangla, status));
			}
	        
	        return wordList;
		} catch (SQLiteException exception) {
			exception.printStackTrace();
			return null;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
	
	public List<Bean> getBookmarkedWords() {
		SQLiteDatabase db = initializer.getReadableDatabase();
		
		String sql = "SELECT * FROM " + TABLE_NAME +
    			" WHERE " + STATUS + " = '" + BOOKMARKED + "'";
        
        Cursor cursor = db.rawQuery(sql, null);
        
        List<Bean> wordList = new ArrayList<Bean>();
        while(cursor.moveToNext()) {
        	int id = cursor.getInt(0);
        	String english = cursor.getString(1);
        	String bangla = cursor.getString(2);
        	String status = cursor.getString(3);
			wordList.add(new Bean(id, english, bangla, status));
		}
        
        cursor.close();
        db.close();
        return wordList;
	}
	
	public void bookmark(int _id) {
		SQLiteDatabase db = initializer.getWritableDatabase();
		
		String sql = "UPDATE " + TABLE_NAME + " SET " + STATUS + " = '"
				+ BOOKMARKED + "' WHERE " + ID + " = " + _id;
		db.execSQL(sql);
		db.close();
	}
	
	public void deleteBookmark(int _id) {
		SQLiteDatabase db = initializer.getWritableDatabase();
		
		String sql = "UPDATE " + TABLE_NAME + " SET " + STATUS + " = '' " +
				" WHERE " + ID + " = " + _id;
		db.execSQL(sql);
		db.close();
	}
}
