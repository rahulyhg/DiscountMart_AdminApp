package sourabh.discountmartadmin.helper;

import android.app.ProgressDialog;
import android.content.Context;

public  class ProgressWheel {


	ProgressDialog dialog;
	Context context;
	boolean cancelable;
	public ProgressWheel(Context context) {
		this.context = context;
	}

	public  void ShowWheel (String title, String message, boolean cancelable){

		dialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);

		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(cancelable);
		dialog.show();
	}
	
	public  void DismissWheel (){
	
		dialog.dismiss();
	}
	
	public  void ShowDefaultWheel (){
		
		ShowWheel("Loading", "Please Wait", false);
	}

	public  void ShowDefaultNonCancellableWheel (){

		ShowWheel("Loading", "Please Wait", true);
	}

	
}
