package sourabh.discountmartadmin.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sourabh.discountmartadmin.R;
import sourabh.discountmartadmin.app.AppConfig;


public final class CommonUtilities {
	
	// give your server registration url here
    static final String SERVER_URL = "http://10.0.2.2/gcm_server_php/register.php"; 

    // Google project id
    static final String SENDER_ID = ""; 

    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";





    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

//    /**
//     * Function to display simple Alert Dialog
//     * @param context - application context
//     * @param title - alert dialog title
//     * @param message - alert message
//     * @param status - success/failure (used to set icon)
//     * 				 - pass null if you don't want icon
//     * */
    public static void showAlertDialog(Context context, String title, String message,
                                Boolean status ) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    /**
     * Turn drawable resource into byte array.
     *
     * @param context parent context
     * @param id      drawable resource id
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }




    public static String getIMEI(Context context) {

        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

//    public static Map<String,String> buildParams(String... strParams) {
//        Map<String, String> params = new HashMap<String, String>();
//
//
//        for(int i=0; i<strParams.length;i++){
//            StringBuilder local2 = new StringBuilder();
//            String s = local2.append(strParams[i]).append(3).toString();
//            params.put(s,strParams[i]);
//
//        }
//
//
//        return params;
//
//    }
    public static Map<String,String> buildGuestHeaders() {

        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put("Authorization", AppConfig.API_KEY_GUEST);
        return headers;
    }

    public static Map<String,String> buildHeaders(Context contex) {

        HashMap<String, String> headers = new HashMap<String, String>();


        headers.put("Authorization", new SessionManager(contex).getAPIKEY());
        return headers;
    }

    public static <T> T getObjectFromJsonString(String jsonString, Class<T> clazz) {

        return new Gson().fromJson(jsonString, clazz);
    }



    public static String getJsonStringFromObject(Object object) {

        return new Gson().toJson(object);
    }

    public static JSONArray getArrayFromJsonObj(JSONObject jsonObject, String arrayKey) throws JSONException {


        return jsonObject.getJSONArray(arrayKey);
    }
    public static ArrayList<Class> getObjectsArrayFromJsonArray(JSONArray jsonArray,  Class dataClass) throws JSONException {

        ArrayList<Class> arrayList = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            if(jsonArray.get(i) instanceof JSONObject){
                JSONObject jsnObj = (JSONObject)jsonArray.get(i);
                arrayList.add(new Gson().<Class>fromJson(jsnObj.toString(), dataClass));
            }
        }
        return arrayList;
    }


    public static Map<String,String> buildBlankParams(String... strParams) {

        HashMap<String, String> params = new HashMap<String, String>();
        return params;
    }


//    public static ArrayList<String> getProductImagesFromProductData(ProductData productData){
//
//        ArrayList<String> productImages = new ArrayList<>();
//
//        if(productData.getImage1() != null){
//            productImages.add(productData.getImage1());
//        }
//        if(productData.getImage2() != null){
//            productImages.add(productData.getImage2());
//        }
//        if(productData.getImage3() != null){
//            productImages.add(productData.getImage3());
//        }
//        return productImages;
//    }



    public static void showToast(Context context, String message,
                                       int duration) {

        Toast.makeText(context, message, duration).show();
    }
    public static void showShortToast(Context context, String message
                                 ) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Context context, String message
                                      ) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }



    public static void ShowYesNoDialog(Context context,String title, String message, DialogInterface.OnClickListener yesListener,DialogInterface.OnClickListener noListener)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);


        builder.setTitle(title);
        builder.setMessage(message);


        builder.setPositiveButton("Yes", yesListener);
        builder.setNegativeButton("No", noListener);

        builder.create().show();
    }

    public static void ShowDialog(Context context,String buttonText,String title, String message, DialogInterface.OnClickListener yesListener)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);


        builder.setTitle(title);
        builder.setMessage(message);


        builder.setPositiveButton(buttonText, yesListener);

        builder.create().show();
    }

    public static void refreshActivity(Activity activity){

        Intent intent = activity.getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.startActivity(intent);
    }

    public static String validatePhoneNumberLength(EditText num){

        if(num.length()<10){
            num.setError("Minimum Length for phone number should be 10");
        }
        else if(num.getText().toString().length()==0)
        {
            num.setError("Name cannot be Blank");

        }else{
            return "OK";
        }

        return "";


    }

    public static String validatePassword(EditText password, EditText confirm){


        if(password.getText().toString().equals("")){
            password.setError("Password cannot be blank");

        }else if(confirm.getText().toString().equals("")) {
            confirm.setError("Please confirm the Password");

        }
        else if(!password.getText().toString().equals(confirm.getText().toString()))
        {
            confirm.setError("Confirmed Password doesnt match ");

        }else{
            return "OK";
        }


        return "";

    }



    public static String validateAge(EditText ageTxt){

        int age = 0;

        if(ageTxt.getText().toString().length()>0)
        {
             age = Integer.parseInt(ageTxt.getText().toString());

        }


        if(ageTxt.getText().toString().length()==0)
        {
            ageTxt.setError("Age cannot be Blank");


        }else if(age < 0){
            ageTxt.setError("Minimum age number should be 0");
        }
        else if(age>100){
            ageTxt.setError("Maximum age number should be 100");
        }
        else{
            return "OK";
        }

        return "";


    }

    public static String validateName(EditText textView){


        if(textView.getText().toString().length()==0)
        {
            textView.setError("Name cannot be Blank");
        }else{
            return "OK";
        }


        return "";
    }

    public static String validateCity(EditText textView){


        if(textView.getText().toString().length()==0)
        {
                textView.setError("City name cannot be Blank");
        }else{
            return "OK";
        }


        return "";
    }

    public static String validateBloodGrp(Spinner spinner, TextView tvInvisibleError){


        String sss = spinner.getSelectedItem().toString();
        if(spinner.getSelectedItem().toString().startsWith("Select your "))
        {
            spinnerError(spinner,tvInvisibleError,"Plese Select Blood Group. If you dont know your's, choose 'Dont Know'");
        }
        else if(spinner.getSelectedItem().toString().startsWith("Blood Group"))
        {
            spinnerError(spinner,tvInvisibleError,"Plese Select Blood Group.");
        }else{
            return "OK";
        }


        return "";
    }



    static void spinnerError(Spinner spinner, TextView tvInvisibleError, String errorMessage ){
        View view = spinner.getSelectedView();

        // Set TextView in Secondary Unit spinner to be in error so that red (!) icon
        // appears, and then shake control if in error
        TextView tvListItem = (TextView)view;

        // Set fake TextView to be in error so that the error message appears

        // Shake and set error if in error state, otherwise clear error
        if(errorMessage != null)
        {
            tvListItem.setError(errorMessage);
            tvListItem.requestFocus();

            // Shake the spinner to highlight that current selection
            // is invalid -- SEE COMMENT BELOW
//            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//            spnMySpinner.startAnimation(shake);

            tvInvisibleError.requestFocus();
            tvInvisibleError.setError(errorMessage);
        }
        else
        {
            tvListItem.setError(null);
            tvInvisibleError.setError(null);
        }
    }
}