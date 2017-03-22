package sourabh.discountmartadmin.adaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import sourabh.discountmartadmin.R;
import sourabh.discountmartadmin.app.AppController;
import sourabh.discountmartadmin.data.AdSliderData;

/**
 * Created by Sourabh on 3/20/2017.
 */

public class SliderAdaptor extends BaseAdapter {


    ArrayList<AdSliderData> adSliderDataArrayList;
    AdSliderData adSliderData = null;
    private Context context;
    private LayoutInflater inflater;
    private Activity activity;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public SliderAdaptor(Context context, Activity activity,
                         ArrayList<AdSliderData> adSliderDataArrayList)

    {
        this.context = context;
        this.activity = activity;
        this.adSliderDataArrayList = adSliderDataArrayList;
    }

    @Override
    public int getCount() {
        return adSliderDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return adSliderDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {



        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView txtName = (TextView) convertView.findViewById(R.id.name);


        adSliderData = adSliderDataArrayList.get(position);


        thumbNail.setImageUrl(adSliderData.getImage(), imageLoader);

        txtName.setText(adSliderData.getName());



        return convertView;
    }
}
