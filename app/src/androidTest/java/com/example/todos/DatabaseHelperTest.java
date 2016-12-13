package com.example.todos;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todos.data.DatabaseHelper;
import com.example.todos.data.TodosContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertTrue;

public class DatabaseHelperTest {
    Context context;
    DatabaseHelper helper;
    SQLiteDatabase db;
    @Before
    public void Setup(){
        context = getTargetContext();
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }
    @After
    public void TearDown(){
        db.close();
    }
    @Test
    public void TestOnCreate() {
        assertTrue("Database could not open", db.isOpen());

    }
    @Test
    public void TestStartData() {
        db.execSQL("DROP TABLE IF EXISTS " + TodosContract.TodosEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TodosContract.CategoriesEntry.TABLE_NAME);
        helper.onCreate(db);
        Cursor c = db.rawQuery("select * from " +
                TodosContract.CategoriesEntry.TABLE_NAME, null);
        assertTrue(c.getCount() == 1);
        assertTrue(c.getColumnCount() == 2);
    }
}
