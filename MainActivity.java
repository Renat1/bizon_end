package com.example.bizone_eclipse;
import java.text.DecimalFormat;

import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class MainActivity extends ActionBarActivity {
	 String[] data = {"Device Model", "b", "OS + OS Version", "d", "IMEI", "f", "Screen Resolution", "h", "Screen DPI", "j", "GPS Coordinates", "k", "Network Connection type", "x", "WiFi SSID", "x", "WiFi BSSID", "x", "Current time", "x"};	   
	  GridView gvMain;
	  ArrayAdapter<String> adapter;
	  private LocationManager myLocationManager;
	    private LocationListener myLocationListener;
	    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        adapter = new ArrayAdapter<String>(this, R.layout.activity_main, R.id.tvText, data);
        gvMain = (GridView) findViewById(R.id.gvMain);
        
        Button start = (Button)findViewById(R.id.Start);
        start.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) public void onClick(View v) {
            	String manufacturer;
            	try{            	
            		manufacturer = Build.MANUFACTURER;
            	}catch(Exception e) {manufacturer = null; data[1] = "null";}
            	String model;
            	try{
            		model = Build.MODEL;
                }catch(Exception e) {model = null; data[3] = "null";}
            	
                data[1] = manufacturer + " " + model;
                data[3] = Build.VERSION.RELEASE;
                String tm_s;               
                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                try{
                	 tm_s = tm.getDeviceId();
                	 data[5] = tm_s;
                }catch(Exception e) {tm_s = null; data[5] = "null";}
                               
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                Integer width;
                try{
                	width = size.x;
                }catch(Exception e) {width = null; data[7] = "null";}
                Integer height;
                try{
                	height = size.y;
                }catch(Exception e) {height = null; data[7] = "null";}
                if ((height!=null)&&(width!=null))
                	data[7] = width + "X" + height;
                
                Integer densityDpi;
                try{
                DisplayMetrics metrics = getResources().getDisplayMetrics();                               
                	densityDpi = (int)(metrics.density);
                	data[9] = densityDpi.toString();
                }catch (Exception e) {densityDpi = null;  data[9] = "null";}                
               
               
                
              /*  LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude(); */
                
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Double longitude;
                try{
                	longitude = location.getLongitude();
                	longitude = Math.round(longitude*100)/100.0d;
                }catch(Exception e) {longitude = null; data[11] = "null";}
               
                Double latitude;
                try{
                	latitude = location.getLatitude();
                	latitude = Math.round(latitude*100)/100.0d;
                }catch(Exception e) {latitude = null; data[11] = "null";}
                Double altitude;
                try{
                	altitude = location.getAltitude();
                }catch(Exception e) {altitude = null; data[11] = "null";} 
                if ((longitude!=null)&&(latitude!=null)&&(altitude!=0))
                	data[11] = longitude + "X" + latitude + "X" + altitude; 
                
                ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); //this.getSystemService(Context.CONNECTIVITY_SERVICE);
                      
                final ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();   
                String network;
                try{
                	network = networkInfo.getTypeName();
                	data[13] = network;
                }catch(Exception e) {network = null; data[13] = "null";}                	

               
                String wifiInfo_string;
                String ssid = null, bssid = null;
                try{
                	WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);              
                	WifiInfo wifiInfo = wifiManager.getConnectionInfo();               	
                	wifiInfo_string = wifiInfo.toString();
                }catch(Exception e) {wifiInfo_string = null;}
                if (wifiInfo_string != null)
                {
                	int ssid_number_1 = wifiInfo_string.indexOf(":");
                	int ssid_number_2 = wifiInfo_string.indexOf(",");
                	ssid = wifiInfo_string.substring(ssid_number_1 + 1, ssid_number_2);
                	data[15] = ssid;
                             
                	String ssid_cut = wifiInfo_string.substring(ssid_number_2 + 1);
                	int bssid_number_1 = ssid_cut.toString().indexOf(":");
                	int bssid_number_2 = ssid_cut.toString().indexOf(",");
                	bssid = ssid_cut.substring(bssid_number_1 + 1, bssid_number_2);
                	data[17] = bssid;
                }
                if (ssid.compareTo(" <unknown ssid>")==0) 
                {
                	ssid = null; 
                	data[15] = "null"; 
                }
                if (bssid.compareTo(" <none>")==0) 
                {
                	bssid = null; 
                	data[17] = "null"; 
                }
                String time;
                try{
                	Time now = new Time();
                	now.setToNow();
                	time = now.toString();
                	data[19] = null;
                }catch (Exception e) {time = null;}
                if (time != null)
                {
                	int loc_1 = time.indexOf("T");
                	int loc_2 = time.indexOf("E");
                	time = time.substring(loc_1 + 1, loc_2);
                	String time_h = time.substring(0,2) + "÷ ";
                	String time_m = time.substring(2, 4) + "ì ";
                	String time_s = time.substring(4,6) + "ñ";
                	data[19] = time_h + time_m + time_s;
                }                
            	               
                gvMain.setAdapter(adapter);
                               
            }        
        });        
    }   
}

