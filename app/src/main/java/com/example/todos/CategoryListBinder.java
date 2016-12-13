package com.example.todos;


import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.widget.ListView;

import com.example.todos.model.Category;

public class CategoryListBinder {

    @BindingAdapter("bind:items")
    public static void bindList(ListView view, ObservableArrayList<Category> list) {
        CategoryListAdapter adapter;
        if (list == null) {
            adapter = new CategoryListAdapter();
        }
        else {
            adapter = new CategoryListAdapter(list);
        }
        view.setAdapter(adapter);
    }
/*
    @InverseBindingMethods({
            @InverseBindingMethod(
                    type = Spinner.class,
                    attribute = "android:selectedItemPosition",
                    method = "getText")
    })
    public class MyTextViewBindingAdapters { }
*/

}
