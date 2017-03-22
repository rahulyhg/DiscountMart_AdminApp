package sourabh.discountmartadmin.helper;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by MWC on 21-04-2016.
 */
public class JsonSeparator {

    JSONObject json;
    Context con;
    public JsonSeparator(Context con, JSONObject json){
        this.con = con;
        this.json = json;
    }

    public String getMessage() throws JSONException {
        return json.getString(Const.KEY_MESSAGE);
    }
    public JSONObject getData() throws JSONException {
        return json.getJSONObject(Const.KEY_DATA);
    }
    public boolean isError() throws JSONException {

        if(json.getBoolean(Const.KEY_ERROR))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}


