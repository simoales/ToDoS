package com.example.todos;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.todos.data.TodosContract;
import com.example.todos.data.TodosQueryHandler;
import com.example.todos.databinding.ActivityCategoryBinding;
import com.example.todos.model.Category;
import com.example.todos.model.CategoryList;

public class CategoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int URL_LOADER = 0;
    protected Category category;
    protected CategoryList categories;
    ObservableArrayList<Category> list;
    Cursor cursor;
    CategoryListAdapter adapter;
    ActivityCategoryBinding binding;
    TodosQueryHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_category);
//adds the custom layout
        //inflater
        LayoutInflater inflater = getLayoutInflater();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        getLoaderManager().initLoader(URL_LOADER, null, this);
        //get the listView and add the onclicklistener
        handler =  new TodosQueryHandler(getContentResolver());

//EVENTS
//add the click event to the list, so that the selected item goes to the
        ListView myList = (ListView)findViewById(R.id.lvCategories);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {
            category = categories.ItemList.get(position);
            binding.setCategory(category);
        }
    });





//New button will add a new line on the list
        final Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                category = new Category();
                //categories.ItemList.add(category);
                //binding.setCategories(categories);
                binding.setCategory(category);
            }
        });
//delete button will delete an item from the list
        final Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(CategoryActivity.this)
                        .setTitle(getString(R.string.delete_categories_dialog_title))
                        .setMessage(getString(R.string.delete_categories_dialog))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //delete
                                categories.ItemList.remove(category);
                                String [] args = new String[1];
                                Uri uri =  Uri.withAppendedPath(TodosContract.CategoriesEntry.CONTENT_URI, String.valueOf(category.catId.get()));
                                handler.startDelete(1, null, uri
                                        , null, null);
                                category = null;
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

//save button
        final Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(category != null && category.catId.get() != null) {
                    //update existing category
                    Log.d("Save Click", "update");
                    String [] args = new String[1];
                    ContentValues values = new ContentValues();
                    values.put(TodosContract.CategoriesEntry.COLUMN_DESCRIPTION, category.description.get());
                    args[0] = category.catId.get().toString();
                    handler.startUpdate(1, null, TodosContract.CategoriesEntry.CONTENT_URI,
                            values, TodosContract.CategoriesEntry._ID + "=?", args);
 }
                else if(category != null && category.catId.get() == null) {
                    //add new category
                    ContentValues values = new ContentValues();
                    values.put(TodosContract.CategoriesEntry.COLUMN_DESCRIPTION, category.description.get());
                    handler.startInsert(1, null, TodosContract.CategoriesEntry.CONTENT_URI,
                            values );
                }
            }
        });
    }




    @Override
    public void onResume(){
        getLoaderManager().restartLoader(URL_LOADER, null, this);
        super.onResume();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TodosContract.CategoriesEntry.TABLE_NAME
                + "." + TodosContract.CategoriesEntry._ID,
                TodosContract.CategoriesEntry.COLUMN_DESCRIPTION};
        return new CursorLoader(
                this,
                TodosContract.CategoriesEntry.CONTENT_URI,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        final ListView lv=(ListView) findViewById(R.id.lvCategories);
        list = new ObservableArrayList<>();
        int i=0;
        //fills the observablelist of categories
        // Move cursor before first so we can still iterate after config change
        data.moveToPosition(-1);
        while (data.moveToNext())
        {
            list.add(i, new Category(
                    data.getInt(0),
                    data.getString(1)
            ));
            i++;
        }
        adapter = new CategoryListAdapter(list);
        lv.setAdapter(adapter);
        //set bindings
        //classes
        category = new Category();
        categories = new CategoryList(list);
        binding.setCategories(categories);
        binding.setCategory(category);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.list = null;
    }
}
