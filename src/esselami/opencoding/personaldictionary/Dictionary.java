package esselami.opencoding.personaldictionary;

import buet.rafi.dictionary.R;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Dictionary extends ListActivity {
	private EditText input;
	private TextView empty;
	
	private DictionaryDB dictionaryDB;
	private WordListAdapter adapter;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        DatabaseInitializer initializer = new DatabaseInitializer(getBaseContext());
        initializer.initializeDataBase();
        dictionaryDB = new DictionaryDB(initializer);
        
        input = (EditText) findViewById(R.id.input);
        empty = (TextView) findViewById(android.R.id.empty);
        
        adapter = new WordListAdapter(this, dictionaryDB);
		setListAdapter(adapter);
        
        input.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				loadData(input.getText().toString());
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			public void afterTextChanged(Editable s) {
				
			}
		});
    }
    
    private void loadData(String word) {
		DataLoader loader = new DataLoader(dictionaryDB, adapter);
		loader.execute(word);
		if(word.equals(""))
			empty.setText("");
		else
			empty.setText("No match found");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	loadData(input.getText().toString());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.bookmarked_words) {
    		Intent intent = new Intent(this, BookMarkedWords.class);
    		startActivity(intent);
    	}
    	else if(item.getItemId() == R.id.about) {
    		Intent intent = new Intent(this, About.class);
    		startActivity(intent);
    	}
    	
    	else if(item.getItemId() == R.id.add_new) {
    		showInputDialog();
    	}
    	
    	return super.onOptionsItemSelected(item);	
    }
    
	public void showInputDialog() {
		LayoutInflater factory = LayoutInflater.from(this);

		final View addNew = factory.inflate(R.layout.add_new, null);

		final EditText word = (EditText) addNew.findViewById(R.id.Word_input);
		final EditText definition = (EditText) addNew.findViewById(R.id.Definition_input);
		
		

		final AlertDialog.Builder newWordInputDialog = new AlertDialog.Builder(this);
		newWordInputDialog
			.setTitle("Add a new word")
			.setView(addNew)
			.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							String englishWord = word.getText().toString();
							String banglaWord = definition.getText().toString();
							if((englishWord.equals("") || banglaWord.equals("")))
								Toast.makeText(getBaseContext(), "Field can't be blank",
										Toast.LENGTH_SHORT).show();
							else {
								dictionaryDB.addWord(englishWord, banglaWord);
								
								Toast.makeText(getBaseContext(), "Word Added to the Dictionary",
										Toast.LENGTH_SHORT).show();
							}
						}
					})
			.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							
						}
					});
		newWordInputDialog.show();
	}
}