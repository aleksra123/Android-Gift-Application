package pt.simov.pl4;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.List;


public class ListViewAdapter1 extends BaseAdapter {

    //************************HER MÅ DU PUTTE NOE INN I VIEW GETVIEW***************************************************
  public View getView(int position, View convertView, ViewGroup parent) {
       // if( convertView == null ){
            //We must create a View:
        //    convertView = inflater.inflate(R.layout., parent, false);
        //}
        //Here we can do changes to the convertView, such as set a text on a TextView
        //or an image on an ImageView.
        return convertView;
    }

    private final List<Item> items;
    List<Item> mStringFilterList;





    public ListViewAdapter1(final List<Item> items) {
        this.items = items;

    }



    public void changeDataSource(List<Item> newData){
        items.clear();
        items.addAll(newData);

        notifyDataSetChanged(); //*****************************************************************************************************
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int arg0) {
        return this.items.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int arg0, View arg1) {
        // TODO Auto-generated method stub
        final Item row = this.items.get(arg0);
        View itemView = null;


            itemView = arg1;


        // Set the text of the row
        TextView txtId = (TextView) itemView.findViewById(R.id.itemId);
        txtId.setText(Integer.toString(row.getId()));

        TextView itemName = (TextView) itemView.findViewById(R.id.itemName);
        itemName.setText(row.getItemName());



        return itemView;
    }
}
