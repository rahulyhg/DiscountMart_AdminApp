package sourabh.discountmartadmin.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sourabh.discountmartadmin.R;
import sourabh.discountmartadmin.adaptors.ExpandableListAdapter;
import sourabh.discountmartadmin.app.AppConfig;
import sourabh.discountmartadmin.app.CustomRequest;
import sourabh.discountmartadmin.app.VolleySingleton;
import sourabh.discountmartadmin.data.GenericCategoryData;
import sourabh.discountmartadmin.helper.CommonUtilities;
import sourabh.discountmartadmin.helper.Const;
import sourabh.discountmartadmin.helper.JsonSeparator;

public class ServiceProviderCategories extends AppCompatActivity {


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    ArrayList<GenericCategoryData> genericCategoryDataList = new ArrayList<>();

    Context context;
    Dialog addServiceProviderCategoryDialog;
    EditText serviceProviderCategoryName;
    int service_provider_category_id;
    String selectedServiceProviderCategoryName ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servce_provider_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", nullm ).show();

                service_provider_category_id = 0;
                showAddServiceCategoryDialog(false);
            }
        });

        getServiceProviderCategories();


    }


    void getServiceProviderCategories(){

        String url = AppConfig.URL_GET_SERVICE_CATEGORIES;



        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(this,true, Request.Method.GET, url, CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{

                                JSONObject registerResponceJson = js.getData() ;
                                JSONArray jsonArray =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_CATEGORIES);


                                setList(CommonUtilities.getObjectsArrayFromJsonArray(jsonArray,GenericCategoryData.class));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            throw new IOException("Post failed with error code " + error.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        requestQueue.add(jsObjRequest);
    }


    void setList(ArrayList<Class> classArrayList)
    {

        genericCategoryDataList.clear();


        for (int i = 0; i<classArrayList.size();i++) {

            try {
                genericCategoryDataList.add( (GenericCategoryData) Class.forName(Const.ClassNameGenericCategoryData).cast(classArrayList.get(i)));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // preparing list data
        listAdapter = new ExpandableListAdapter(this, genericCategoryDataList);
        // setting list adapter
        expListView.setAdapter(listAdapter);


        // Listview Group click listener


//        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                int itemType = ExpandableListView.getPackedPositionType(id);
//
//               if(itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
//
//                   return false;
//
//                }
//            });


        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemType = ExpandableListView.getPackedPositionType(l);

                if(itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    //do your per-group callback here
                    pickOption(genericCategoryDataList.get(i),true);
                    return false;
                }

                return false;
            }
        });

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();


                return false;
            }
        });

//        // Listview Group expanded listener
//        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Listview Group collasped listener
//        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                pickOption(genericCategoryDataList.get(groupPosition).getSubcategories().get(childPosition),false);

                return false;
            }
        });




    }


    void pickOption(final GenericCategoryData genericCategoryData, boolean isParent){

        CharSequence options[] = null;
        if(isParent){
            options = new CharSequence[] {"Rename", "Delete","Create SubCategory"};
        }else{
            options = new CharSequence[] {"Rename", "Delete"};

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick option for "+genericCategoryData.getName());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case 0:
                        selectedServiceProviderCategoryName = genericCategoryData.getName();
                        //used service_provider_category_id for id just to save variable
                        service_provider_category_id = genericCategoryData.getId();
                        showAddServiceCategoryDialog(true);
                        break;

                    case 1:
                        DeleteServiceCategory(genericCategoryData.getId());
                        break;
                    case 2:
                        service_provider_category_id = genericCategoryData.getId();
                        showAddServiceCategoryDialog(false);
                        break;
                }
            }
        });
        builder.show();

    }

//    void pickAddOption(){
//
//        CharSequence options[] = new CharSequence[] {"Create new Retailer Category", "Create new Retailer Sub Category"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Pick option");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which){
//                    case 0:
//                        showAddServiceCategoryDialog();
//                        break;
//
//                    case 1:
//
//                        break;
//                }
//            }
//        });
//        builder.show();
//
//    }

    void showAddServiceCategoryDialog(boolean isRename){

        if(isRename){

            addServiceProviderCategoryDialog = new Dialog(ServiceProviderCategories.this, R.style.MyCustomTheme);
            addServiceProviderCategoryDialog.setContentView(R.layout.create_retailer_category_dialog);
            addServiceProviderCategoryDialog.setCancelable(true);

            Button btnAdslider = (Button) addServiceProviderCategoryDialog.findViewById(R.id.btnAddCat);
            serviceProviderCategoryName = (EditText) addServiceProviderCategoryDialog.findViewById(R.id.categoryname);
            serviceProviderCategoryName.setHint(selectedServiceProviderCategoryName);
            btnAdslider.setText("Rename Category");
            btnAdslider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UpdateServiceCategory(service_provider_category_id);

                }
            });

            WindowManager.LayoutParams lp = addServiceProviderCategoryDialog.getWindow().getAttributes();
            lp.dimAmount = 0.5f;
            addServiceProviderCategoryDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            addServiceProviderCategoryDialog.show();


        }else{


            addServiceProviderCategoryDialog = new Dialog(ServiceProviderCategories.this, R.style.MyCustomTheme);
            addServiceProviderCategoryDialog.setContentView(R.layout.create_retailer_category_dialog);
            addServiceProviderCategoryDialog.setCancelable(true);

            Button btnAdslider = (Button) addServiceProviderCategoryDialog.findViewById(R.id.btnAddCat);
            serviceProviderCategoryName = (EditText) addServiceProviderCategoryDialog.findViewById(R.id.categoryname);


            btnAdslider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AddServiceCategory(service_provider_category_id);

                }
            });

            WindowManager.LayoutParams lp = addServiceProviderCategoryDialog.getWindow().getAttributes();
            lp.dimAmount = 0.5f;
            addServiceProviderCategoryDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            addServiceProviderCategoryDialog.show();


        }


    }

    void AddServiceCategory(int parent_id){


        String url = AppConfig.URL_CREATE_SERVICE_CATEGORY;


        Map<String, String> params = new HashMap<>();
        params.put("name", serviceProviderCategoryName.getText().toString());
        params.put("parent_id", String.valueOf(parent_id));



        CustomRequest jsObjRequest   = new CustomRequest(this,true,
                Request.Method.POST, url, params, CommonUtilities.buildGuestHeaders(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);
                        dismissDialog();

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissDialog();

                        try {
                            throw new IOException("Post failed with error code " + error.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(jsObjRequest);


    }


    void DeleteServiceCategory(int id){


        String url = AppConfig.URL_DELETE_SERVICE_CATEGORY+id;


        CustomRequest jsObjRequest   = new CustomRequest(this,true,
                Request.Method.DELETE, url, CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        getServiceProviderCategories();
                        JsonSeparator js = new JsonSeparator(context,response);

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getServiceProviderCategories();

                        try {
                            throw new IOException("Post failed with error code " + error.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(jsObjRequest);


    }


    void UpdateServiceCategory(int id){


        String url = AppConfig.URL_UPDATE_SERVICE_CATEGORY+id;


        Map<String, String> params = new HashMap<>();
        params.put("name", serviceProviderCategoryName.getText().toString());



        CustomRequest jsObjRequest   = new CustomRequest(this,true,
                Request.Method.PUT, url, params, CommonUtilities.buildGuestHeaders(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);
                        dismissDialog();

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissDialog();

                        try {
                            throw new IOException("Post failed with error code " + error.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(jsObjRequest);


    }



    void dismissDialog(){

        addServiceProviderCategoryDialog.dismiss();
        getServiceProviderCategories();
    }



}
