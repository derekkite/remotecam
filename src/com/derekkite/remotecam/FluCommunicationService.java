package com.derekkite.remotecam;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.net.wifi.*;
import android.content.*;
import android.preference.*;
import java.util.*;


public class FluCommunicationService extends Service
{
	private final IBinder fluBinder = new fluCommunicationBinder();
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	/*api for service*/
	
	String activeaccesspoint = null;
	int connectionstatus = 0;
	ArrayList<String>  prefssidlist;
	String wifis[];
	WifiManager wifiobj;
	List<ScanResult> activewifi;
	
	public Boolean connectToFlucard() {
		
		
		/*four states. already connected to access point. check if flucard. if yes go on */
		/* not connected. default flucard essid available. connect and prompt for */
		/* new password and ssid name, not compulsary. */
		/* not connected and flucard in preferences availableconnect. */
		/* *not connected, no known ssid. show list to allow selection and connect. */
		/* save name and password  */

		/* initialize wifi */
		
		wifiobj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		class WifiReceiver extends BroadcastReceiver {
			public void onReceive(Context c, Intent intent )  {
				if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION )){
					activewifi = wifiobj.getScanResults();
					
				}
			}
		}
		// first are we connected to something, is it a flucard.
		WifiInfo info = wifiobj.getConnectionInfo();
		
		return true;
	}
	
	
	boolean selectAccessPoint() {
		
		return true;
	}
	
	
	WifiConfiguration getDefaultFluWifiConfig( ) {
		
		return this.getWifiConfig("ssid_default","pass_default");
		
		}
		
	
	void populateSSIDList() {
		
		/* list with three items. default ssid alt1,alt2 */
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		/* get first */
		this.prefssidlist.add(SP.getString("ssid_default",""));
		this.prefssidlist.add(SP.getString("ssid_alt1",""));
		this.prefssidlist.add(SP.getString("ssid_alt2",""));
	}	
		
	WifiConfiguration getWifiConfig(String ssid, String pass) {
		
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		if (SP.getString(ssid,"") == "") {return null;}
		
		
		WifiConfiguration wc = new WifiConfiguration();
		wc.SSID = "\"" + SP.getString(ssid,"")+"\"";
		wc.preSharedKey = "\""+ SP.getString(pass,"")+"\"";
		wc.status = WifiConfiguration.Status.ENABLED;
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		
		return wc;
	}
	
	
	public class fluCommunicationBinder extends Binder {
		FluCommunicationService getService()  {
			return FluCommunicationService.this;
		}
	}
	
}
