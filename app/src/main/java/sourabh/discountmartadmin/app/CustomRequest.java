package sourabh.discountmartadmin.app;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import sourabh.discountmartadmin.helper.CommonUtilities;
import sourabh.discountmartadmin.helper.ConnectionDetector;
import sourabh.discountmartadmin.helper.ProgressWheel;


public class CustomRequest extends Request<JSONObject> {

	private Listener<JSONObject> listener;
	private ErrorListener errorListener;

	private Map<String, String> params;
	private Map<String, String> headers;
	private String url;
	private Context con;
	private boolean showLoadingWheel;
	ProgressWheel progressWheel;
	ConnectionDetector cd ;

	public CustomRequest(String url, Map<String, String> params,
						 Listener<JSONObject> reponseListener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
		this.errorListener = errorListener;
	}

	public CustomRequest(
			Context con,
			boolean showloadingwheel,
			int method,
			String url,
			Map<String, String> params,
			Map<String, String> headers,
			Listener<JSONObject> reponseListener,
			ErrorListener errorListener)
	{
		super(method, url, errorListener);

		this.listener = reponseListener;
		this.errorListener = errorListener;

		this.params = params;
		this.headers = headers;
		this.url = url;
		this.con = con;
		this.showLoadingWheel = showloadingwheel;

		cd =  new ConnectionDetector(con);
		progressWheel = new ProgressWheel(con);

		showLogs(showloadingwheel, method, url, params, headers);

		if (!cd.isConnectingToInternet()) {
			CommonUtilities.showAlertDialog(con,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
            return;
        }

		if(this.showLoadingWheel){
			progressWheel.ShowDefaultWheel();
		}
	}

	void showLogs(boolean showloadingwheel,
				  int method,
				  String url,
				  Map<String, String> params,
				  Map<String, String> headers)
	{

		Log.d("showloadingwheel",showloadingwheel+"");
		Log.d("method",method+"");
		Log.d("url",url);
		Log.d("params",params.toString());
		Log.d("headers",headers.toString());

	}




	protected Map<String, String> getParams()
			throws AuthFailureError {
		return this.params;
	};

	public Map<String, String> getHeaders() throws AuthFailureError {

		return this.headers;
	}


	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					    HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}finally{
			if(this.showLoadingWheel){
				progressWheel.DismissWheel();
			}}
	}

	protected void onErrorResponse(VolleyError error) {

		if(this.showLoadingWheel){
			progressWheel.DismissWheel();
		}
	}




	@Override
	protected void deliverResponse(JSONObject response) {
		// TODO Auto-generated method stub
		listener.onResponse(response);
	}
}