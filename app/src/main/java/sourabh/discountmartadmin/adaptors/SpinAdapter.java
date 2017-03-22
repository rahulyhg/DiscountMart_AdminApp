package sourabh.discountmartadmin.adaptors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sourabh.discountmartadmin.R;
import sourabh.discountmartadmin.data.GenericCategoryData;

/**
 * Created by Sourabh on 3/21/2017.
 */

public class SpinAdapter extends ArrayAdapter<GenericCategoryData> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<GenericCategoryData> genericCategoryDataArrayList ;
    private LayoutInflater inflater;
    private Activity activity;

    public SpinAdapter(Context context, int textViewResourceId,Activity activity,
                       ArrayList<GenericCategoryData> genericCategoryDataArrayList ) {
        super(context, textViewResourceId, genericCategoryDataArrayList);
        this.context = context;
        this.activity = activity;
        this.genericCategoryDataArrayList = genericCategoryDataArrayList;
    }

    public int getCount(){
        return genericCategoryDataArrayList.size();
    }

    public GenericCategoryData getItem(int position){
        return genericCategoryDataArrayList.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_group, null);


        TextView txtName = (TextView) convertView.findViewById(R.id.lblListHeader);


        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        txtName.setText(genericCategoryDataArrayList.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return convertView;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(genericCategoryDataArrayList.get(position).getName());

        return label;
    }
}