package comsorry.sosorry;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

	 private Context _context;
     
	    public ConnectionDetector(Context context){
	        this._context = context;
	    }
	 
	    public boolean isConnectingToInternet(){
			boolean connected = false;
			ConnectivityManager connectivityManager = (ConnectivityManager) _context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState() == NetworkInfo.State.CONNECTED
					|| connectivityManager.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
				connected = true;
			}
			return connected;
	    }

}
