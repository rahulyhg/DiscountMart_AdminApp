package sourabh.discountmartadmin.adaptors;

/**
 * Created by Sourabh on 3/21/2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sourabh.discountmartadmin.R;
import sourabh.discountmartadmin.data.GenericCategoryData;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
//    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
//    private HashMap<String, List<String>> _listDataChild;
    private List<GenericCategoryData> genericCategoryDataList; // header titles


//    public ExpandableListAdapter(Context context, List<String> listDataHeader,
//                                 HashMap<String, List<String>> listChildData) {
//        this._context = context;
//        this._listDataHeader = listDataHeader;
//        this._listDataChild = listChildData;
//    }

    public ExpandableListAdapter(Context context, List<GenericCategoryData> genericCategoryDataList
                                 ) {
        this._context = context;
        this.genericCategoryDataList = genericCategoryDataList;
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        List<GenericCategoryData> retailerSubCategoryData =  new ArrayList<>();

        retailerSubCategoryData = genericCategoryDataList.get(groupPosition).getSubcategories();
        return retailerSubCategoryData.get(childPosititon);

//        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        GenericCategoryData retailerSubCategoryData = (GenericCategoryData) getChild(groupPosition, childPosition);

        final String childText = retailerSubCategoryData.getName();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        List<GenericCategoryData> retailerSubCategoryData =  new ArrayList<>();

        retailerSubCategoryData = genericCategoryDataList.get(groupPosition).getSubcategories();

        return retailerSubCategoryData.size();
//        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.genericCategoryDataList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.genericCategoryDataList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        GenericCategoryData genericCategoryData = (GenericCategoryData) getGroup(groupPosition);
        String headerTitle = genericCategoryData.getName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}