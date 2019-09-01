package pt.simov.pl4;

import android.content.Context;
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

public class ListViewAdapter extends BaseAdapter {
    private final List<Person> items;
    List<Person> mStringFilterList;





    public ListViewAdapter(final List<Person> items) {
        this.items = items;

    }

    public void changeDataSource(List<Person> newData){
        items.clear();
        items.addAll(newData);
        notifyDataSetChanged();
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

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        final Person row = this.items.get(arg0);
        View itemView = null;

        if (arg1 == null) {
            LayoutInflater inflater = (LayoutInflater) arg2.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.person_item, null);
        } else {
            itemView = arg1;
        }

        // Set the text of the row
        TextView txtId = (TextView) itemView.findViewById(R.id.personId);
        txtId.setText(Integer.toString(row.getId()));

        TextView firstName = (TextView) itemView.findViewById(R.id.firstName);
        firstName.setText(row.getFirstName());

        TextView lastName = (TextView) itemView.findViewById(R.id.lastName);
        lastName.setText(row.getLastName());

        return itemView;
    }
/*
    // does not work
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {
                    ArrayList<Person> filterList = new ArrayList<Person>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getFirstName().toUpperCase()).contains(constraint.toString().toUpperCase())
                                || (mStringFilterList.get(i).getLastName().toUpperCase()).contains(constraint.toString().toUpperCase())) {

                            Person person = new Person(mStringFilterList.get(i)
                                    .getId(), mStringFilterList.get(i)
                                    .getFirstName(),mStringFilterList.get(i)
                                    .getLastName());
                            filterList.add(person);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = getCount();
                    results.values = items;
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mStringFilterList = (ArrayList<Person>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    */





}

