package pt.simov.pl4;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LVAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Subject> items; //data source of the list adapter
    private MyWishList mwl = new MyWishList();

    //public constructor
    public LVAdapter(Context context, ArrayList<Subject> items) {
        this.context = context;
        this.items = items;


    }
    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.note_item, parent, false);
        }

        // get current item to be displayed
        final Subject currentItem = (Subject) getItem(position);

        // get the TextView for note title and item description
        TextView title = (TextView) convertView.findViewById(R.id.note_title);
        TextView description = (TextView) convertView.findViewById(R.id.note_description);
        //final TextView reserved = (TextView) convertView.findViewById(R.id.Reserved);

        //sets the text for item name and item description from the current item object
        title.setText(currentItem.getTitle());
        description.setText(currentItem.getDescription());
        //reserved.setText(currentItem.getReserved());


        /*

        final CheckBox checkBox = convertView.findViewById(R.id.checkBox22);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentItem.setReserved("Reserved!");
                    reserved.setText(currentItem.getReserved());
                    System.out.println("IS CHECKED");
                }
                else{
                    currentItem.setReserved("");
                    reserved.setText(currentItem.getReserved());
                    System.out.println("IS NOT CHECKED");
                }
            }
        });
         */


        // returns the view for the current row
        return convertView;
    }
    public void filter(String charText) {
        items.clear();
        if (charText.length() == 0) {
            items.addAll(mwl.getItemsArrayList());
        } else {
            System.out.println(" ITEMS " + items + " ITEMS ");
            for (Subject sub : mwl.getItemsArrayList()) {
                if (sub.getTitle().contains(charText)) {
                    items.add(sub);
                }
            }
        }
        notifyDataSetChanged();
    }
}
