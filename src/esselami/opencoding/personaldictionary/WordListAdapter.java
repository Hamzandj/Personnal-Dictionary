package esselami.opencoding.personaldictionary;

import java.util.ArrayList;
import java.util.List;

import buet.rafi.dictionary.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WordListAdapter extends BaseAdapter {

	private List<Bean> wordList;
	private Activity context;
	private LayoutInflater mLayoutInflater;
	private DictionaryDB dictionaryDB;
	
	public WordListAdapter(Activity context, DictionaryDB dictionaryDB) {
		this.context = context;
		this.dictionaryDB = dictionaryDB;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		wordList = new ArrayList<Bean>();
	}

	public int getCount() {
		return wordList.size();
	}

	public Object getItem(int position) {
		return wordList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final Bean word = wordList.get(position);
		View view = convertView;
		if (view == null) {
			view = mLayoutInflater.inflate(R.layout.word, null);
		}
		TextView Word = (TextView) view.findViewById(R.id.word);
		TextView Definition = (TextView) view.findViewById(R.id.definition);
		final ImageButton bookmark = (ImageButton) view.findViewById(R.id.bookmark);
		
		
		Word.setText(word.word);
		Definition.setText(word.definition);
		
		if(word.status != null && word.status.equals(DictionaryDB.BOOKMARKED)) 
			bookmark.setImageResource(R.drawable.bookmarked);
		else
			bookmark.setImageResource(R.drawable.not_bookmarked);
		
		bookmark.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(word.status != null && word.status.equals(DictionaryDB.BOOKMARKED)) {
					dictionaryDB.deleteBookmark(word.id);
					word.status = "";
					bookmark.setImageResource(R.drawable.not_bookmarked);
					Toast.makeText(context, "Bookmark Deleted", Toast.LENGTH_SHORT).show();
				}
				else {
					dictionaryDB.bookmark(word.id);
					word.status = DictionaryDB.BOOKMARKED;
					bookmark.setImageResource(R.drawable.bookmarked);
					Toast.makeText(context, "Bookmark Added", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return view;
	}
	
	public void updateEntries(List<Bean> wordList) {
		if (wordList == null) {
			AlertDialog dialog = new AlertDialog.Builder(context)
				.setTitle("Sorry!")
				.setMessage("Your phone doesn't support pre-built database")
				.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						context.finish();
					}
				})
				.create();
			dialog.show();
		} else {
			this.wordList = wordList;
			notifyDataSetChanged();
		}
	}
}
