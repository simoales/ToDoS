package com.example.todos;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.todos.databinding.CategoryListItemBinding;
import com.example.todos.model.Category;

//import com.example.todos.databinding.TodoListItemBinding;

public class CategoryListAdapter extends BaseAdapter
implements ListAdapter, SpinnerAdapter {
    public ObservableArrayList<Category> list;
    private ObservableInt position = new ObservableInt();
    private LayoutInflater inflater;
    public CategoryListAdapter(ObservableArrayList<Category> l) {
        list = l;
    }
    public CategoryListAdapter() {
        list = new ObservableArrayList<Category>();
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        int id = list.get(position).catId.get();
        return id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        CategoryListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_list_item, parent, false);
        binding.setCategory(list.get(position));
        return binding.getRoot();
    }
    //for the spinner
    public int getPosition(Spinner spinner) {
        return spinner.getSelectedItemPosition();
    }

    public int getPosition() {
        //return 0;
        return position.get();
    }
    public void setPosition(int position) {
        this.position.set(position);
    }
}
