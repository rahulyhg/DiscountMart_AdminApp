package sourabh.discountmartadmin.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import sourabh.discountmartadmin.R;
import sourabh.discountmartadmin.adaptors.SliderAdaptor;
import sourabh.discountmartadmin.app.AppConfig;
import sourabh.discountmartadmin.app.CustomMultipartRequest;
import sourabh.discountmartadmin.app.CustomRequest;
import sourabh.discountmartadmin.app.VolleySingleton;
import sourabh.discountmartadmin.data.AdSliderData;
import sourabh.discountmartadmin.helper.CommonUtilities;
import sourabh.discountmartadmin.helper.Const;
import sourabh.discountmartadmin.helper.JsonSeparator;
import sourabh.discountmartadmin.helper.ProgressWheel;

public class SliderActivity extends AppCompatActivity {

    Context context;

    private SliderAdaptor sliderAdaptor;
    private ArrayList<AdSliderData> adSliderDataArrayList  =new ArrayList<AdSliderData>();
    ;

    int delete_position;
//    @BindView(R.id.list_slider)
//    ListView listView;

    int REQUEST_CODE_PICKER = 0;
    ImageView imgAdslider;
    EditText sliderName;

    SwipeMenuListView listView;
    ProgressWheel progressWheel;
    Dialog addSliderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        ButterKnife.bind(this);

        context = getApplicationContext();
        progressWheel = new ProgressWheel(this);

        listView = (SwipeMenuListView) findViewById(R.id.list_slider);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();


                showAddSliderDialog();

            }
        });


        sliderAdaptor = new SliderAdaptor(context,this, adSliderDataArrayList);
        listView.setAdapter(sliderAdaptor);


        getAdSliders();


       createMenu();






    }

    void showAddSliderDialog(){

        addSliderDialog = new Dialog(SliderActivity.this, R.style.MyCustomTheme);
        addSliderDialog.setContentView(R.layout.create_business_profile_dialog);
        addSliderDialog.setCancelable(true);

        Button btnAdslider = (Button) addSliderDialog.findViewById(R.id.btnaddslider);
        imgAdslider = (ImageView) addSliderDialog.findViewById(R.id.imgslider);
        sliderName = (EditText) addSliderDialog.findViewById(R.id.slidername);

        imgAdslider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openImagePicker();

            }
        });


        btnAdslider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddSliders();

            }
        });


        WindowManager.LayoutParams lp = addSliderDialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        addSliderDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        addSliderDialog.show();

    }

    void openImagePicker(){
        ImagePicker.create(this)
                .folderMode(true) // folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(1) // max images can be selected (999 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
//                .origin(images) // original selected images, used in multi mode
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            // do your logic ....

            String path = images.get(0).getPath();

            File imgFile = new  File(path);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                imgAdslider.setImageBitmap(myBitmap);

            }
        }
    }


    void createMenu(){

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth((int) dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);



        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {


                delete_position=position;
                confirmDelete();


                // false : close the menu; true : not close the menu
                return false;
            }
        });



    }

    void confirmDelete(){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        deleteAdSlider();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

     float dp2px(int dip){
        float scale = context.getResources().getDisplayMetrics().density;
        return dip * scale + 0.5f;
    }

    void getAdSliders(){

        String url = AppConfig.URL_GET_ADSLIDERS;



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
                                JSONArray adsArr =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_ADS);


                                setList(CommonUtilities.getObjectsArrayFromJsonArray(adsArr,AdSliderData.class));

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


    void AddSliders()
    {
        progressWheel.ShowDefaultNonCancellableWheel();

        String url = AppConfig.URL_ADD_ADSLIDERS;



        CustomMultipartRequest multipartRequest = new CustomMultipartRequest(this,
                Request.Method.POST,
                url,
                new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                dismissWheelAndDialog();

                String resultResponse = new String(response.data);
                try {
                    JsonSeparator js = new JsonSeparator(context,new JSONObject(resultResponse));;

                    if(js.isError()){
                        Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, "Slider Added", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissWheelAndDialog();

                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", sliderName.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", Const.GUEST_API_KEY);
                return  headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> data_params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                data_params.put("image", new DataPart("file_avatar.jpg",
                        CommonUtilities.getFileDataFromDrawable(getBaseContext(), imgAdslider.getDrawable()),
                        "image/jpeg"));

                return data_params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }



    void dismissWheelAndDialog(){

        progressWheel.DismissWheel();
        addSliderDialog.dismiss();
        getAdSliders();
    }






    void setList(ArrayList<Class> classArrayList)
    {

        adSliderDataArrayList.clear();

        for (int i = 0; i<classArrayList.size();i++) {

            try {
                adSliderDataArrayList.add( (AdSliderData) Class.forName(Const.ClassNameAdSliderData).cast(classArrayList.get(i)));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
        sliderAdaptor.notifyDataSetChanged();
    }


    void deleteAdSlider(){


        int adslider_id = adSliderDataArrayList.get(delete_position).getId();

        String url = AppConfig.URL_DELETE_ADSLIDER+adslider_id;



        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(this,true, Request.Method.DELETE, url, CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{

                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();


                                refreshAdSliders();

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

    void refreshAdSliders(){

        getAdSliders();
    }


}
