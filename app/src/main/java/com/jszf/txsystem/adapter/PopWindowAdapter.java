package com.jszf.txsystem.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jszf.txsystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hankkin on 16/1/25.
 */
public class PopWindowAdapter extends BaseAdapter {
    private Context context;
    private List<String> dataList;
    private LayoutInflater inflater;
    ArrayList<Boolean> checkedItem=new ArrayList<Boolean>();

    public PopWindowAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.view_popwindow_item, null);
            holder = new ViewHolder();
            holder.checkBox  = (CheckBox) view.findViewById(R.id.checkbox);
            final int p=i;
           holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if(isChecked){
                       //update the status of checkbox to checked
                       Log.d("TAG", "checked");
                       checkedItem.set(p, true);
                   }else{
                       //update the status of checkbox to unchecked
                       checkedItem.set(p, false);
                       Log.d("TAG","unchecked");
                   }
               }
           });
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        if (dataList.size() - 1 == i) {
            holder.checkBox.setVisibility(View.INVISIBLE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setText(dataList.get(i));
        }
        return view;
    }

    private class ViewHolder {
        CheckBox checkBox;
    }
}
