package sourabh.discountmartadmin.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sourabh.discountmartadmin.R;
import sourabh.discountmartadmin.adaptors.SpinAdapter;
import sourabh.discountmartadmin.app.AppConfig;
import sourabh.discountmartadmin.app.CustomRequest;
import sourabh.discountmartadmin.data.CityData;
import sourabh.discountmartadmin.data.GenericCategoryData;
import sourabh.discountmartadmin.helper.CommonUtilities;
import sourabh.discountmartadmin.helper.Const;
import sourabh.discountmartadmin.helper.JsonSeparator;

import static sourabh.discountmartadmin.R.id.spinnerCity;

public class RetailersActivity extends AppCompatActivity {


    Dialog addRetailerDialog;

    Context context;

    Spinner spinnerCat, spinnerSubCat, spinnerCity;
    ArrayList<GenericCategoryData> genericCategoryDataList = new ArrayList<>();
    ArrayList<CityData> cityDataList = new ArrayList<>();
    ArrayList<GenericCategoryData> genericSubCategoryDataList = new ArrayList<>();

    int REQUEST_CODE_PICKER = 0;
    int REQUEST_CODE_PICKER_SLIDER = 1;

    ImageView img1,img2,img3,imgPic;
    private SpinAdapter adapterCat;
    private SpinAdapter adapterSubCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        context = getApplicationContext();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateRetailerDialog();
            }
        });


        getRetailerCategories();



    }

    void getRetailerCategories(){

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
                                JSONArray jsonArrayCategories =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_RETAILER_CATEGORIES);
                                JSONArray jsonArrayCities =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_CITIES);


                                setList(CommonUtilities.getObjectsArrayFromJsonArray(jsonArrayCategories,GenericCategoryData.class),
                                        CommonUtilities.getObjectsArrayFromJsonArray(jsonArrayCities,CityData.class));

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



    void showCreateRetailerDialog(){


        addRetailerDialog = new Dialog(RetailersActivity.this, R.style.MyCustomTheme);
        addRetailerDialog.setContentView(R.layout.create_retailer_dialog);
        addRetailerDialog.setCancelable(true);

        spinnerCity = (Spinner) addRetailerDialog.findViewById(R.id.spinnerCity);
        spinnerCat = (Spinner) addRetailerDialog.findViewById(R.id.spinnercategory);
        spinnerSubCat = (Spinner) addRetailerDialog.findViewById(R.id.spinnerSubcategory);


        setCategoriesSpinner();

        imgPic = (ImageView) addRetailerDialog.findViewById(R.id.imageViewPic);
        img1 = (ImageView) addRetailerDialog.findViewById(R.id.imageViewSlider1);
        img2 = (ImageView) addRetailerDialog.findViewById(R.id.imageViewSlider2);
        img3 = (ImageView) addRetailerDialog.findViewById(R.id.imageViewSlider3);


        imgPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker(1,REQUEST_CODE_PICKER);
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker(3,REQUEST_CODE_PICKER_SLIDER);
            }
        });


//        List<StringWithTag> itemListCategories = new ArrayList<StringWithTag>();
//        for (GenericCategoryData genericCategoryData : genericCategoryDataList) {
//            itemListCategories.add(new StringWithTag(genericCategoryData.getName(), genericCategoryData.getId()));
//        }
//        ArrayAdapter<StringWithTag> spinnerAdapterCategories = new ArrayAdapter<StringWithTag>(getApplicationContext(), android.R.layout.simple_spinner_item, itemListCategories);
//        spinnerAdapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCat.setAdapter(spinnerAdapterCategories);
//
//
//        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//
//
//                List<StringWithTag> itemListCategories = new ArrayList<StringWithTag>();
//
//                StringWithTag swt = (StringWithTag) parentView.getItemAtPosition(position);
//                Integer key = (Integer) swt.tag;
//
//                GenericCategoryData subCategoryData = genericCategoryDataList.get(key)
//                for (GenericCategoryData genericCategoryData : genericCategoryDataList) {
//                    itemListCategories.add(new StringWithTag(genericCategoryData.getName(), genericCategoryData.getId()));
//                }
//                ArrayAdapter<StringWithTag> spinnerAdapterCategories = new ArrayAdapter<StringWithTag>(getApplicationContext(), android.R.layout.simple_spinner_item, itemListCategories);
//                spinnerAdapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinnerSubCat.setAdapter(spinnerAdapterCategories);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });



        List<StringWithTag> itemListCities = new ArrayList<StringWithTag>();
        for (CityData cityData : cityDataList) {
            itemListCities.add(new StringWithTag(cityData.getCity_name(), cityData.getId()));
        }
        ArrayAdapter<StringWithTag> spinnerAdapterCities = new ArrayAdapter<StringWithTag>(getApplicationContext(), android.R.layout.simple_spinner_item, itemListCities);
        spinnerAdapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(spinnerAdapterCities);




        Button btnAdslider = (Button) addRetailerDialog.findViewById(R.id.btnAddCat);


        btnAdslider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AddRetailerCategory(retailer_category_id);

            }
        });

        WindowManager.LayoutParams lp = addRetailerDialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        addRetailerDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        addRetailerDialog.show();

    }


    void openImagePicker(int limit, int code){
        ImagePicker.create(this)
                .folderMode(true) // folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(limit) // max images can be selected (999 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
//                .origin(images) // original selected images, used in multi mode
                .start(code); // start image picker activity with request code
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
                imgPic.setImageBitmap(myBitmap);

            }
        }
        else{
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            // do your logic ....

            ArrayList<ImageView> imgviewList= new ArrayList<>();
            imgviewList.add(img1);
            imgviewList.add(img2);
            imgviewList.add(img3);


            for (int i = 0 ; i< images.size(); i++) {

                Image img = images.get(i);

                String path = img.getPath();
                File imgFile = new  File(path);
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                    imgviewList.get(i).setVisibility(View.VISIBLE);
                    imgviewList.get(i).setImageBitmap(myBitmap);

                }
            }



        }
    }




    void setCategoriesSpinner(){


        adapterCat = new SpinAdapter(context,
                android.R.layout.simple_spinner_item,this,
                genericCategoryDataList);
        spinnerCat.setAdapter(adapterCat); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                GenericCategoryData genericCategoryData = adapterCat.getItem(position);
                genericSubCategoryDataList  = genericCategoryData.getSubcategories();

                setSubCategoriesSpinner();


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    void setSubCategoriesSpinner(){

        adapterSubCat = new SpinAdapter(context,
                android.R.layout.simple_spinner_item,this,
                genericSubCategoryDataList);
        spinnerSubCat.setAdapter(adapterSubCat); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        spinnerSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                GenericCategoryData genericCategoryData = adapterSubCat.getItem(position);



            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    void setList(ArrayList<Class> categoriesList,
                 ArrayList<Class> citiesList) {

        genericCategoryDataList.clear();
        cityDataList.clear();


        for (int i = 0; i < categoriesList.size(); i++) {
            try {
                genericCategoryDataList.add((GenericCategoryData) Class.forName(Const.ClassNameGenericCategoryData).cast(categoriesList.get(i)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < citiesList.size(); i++) {
            try {
                cityDataList.add((CityData) Class.forName(Const.ClassNameCityData).cast(citiesList.get(i)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    private static class StringWithTag {
        public String string;
        public Object tag;

        public StringWithTag(String string, Object tag) {
            this.string = string;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return string;
        }
    }



    }





