package br.com.veritime2;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class Servico extends Service {

	public static final String CATEGORIA = "servico";
	public Double latPoint;
	public Double lngPoint; 
	private SharedPreferences sharedPreferences;
	private final String PREFS_PRIVATE = "PREFS_PRIVATE";

	@Override
	public void onStart(Intent intent, int startId) {
		sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
		
		final LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		if(enabled){
			LocationListener locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					
					try{
						Thread tr= new Thread();
						tr.sleep(5000);
						latPoint = location.getLatitude();
						lngPoint = location.getLongitude();
						Log.i("Latitude"," Latitude = "+latPoint);
						Log.i("Logitude"," lngPoint = "+lngPoint);
						GravaPreferencias("latitudeFinal",""+latPoint);
						GravaPreferencias("longitudeFinal",""+lngPoint);
						
						
					}
					catch(InterruptedException e){
						
					}
					
			    }
			
			    public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			    public void onProviderEnabled(String provider) {}
			
			    public void onProviderDisabled(String provider) {}
			};
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 50, locationListener);
		}
		else{
			String bestProvider = locationManager.getBestProvider(new Criteria(),true); 
			Location location = locationManager.getLastKnownLocation(bestProvider);
			latPoint = location.getLatitude();
			lngPoint = location.getLongitude();
			Log.i("Latitude"," Latitude = "+latPoint);
			Log.i("Logitude"," lngPoint = "+lngPoint);
			GravaPreferencias("latitudeFinal",""+latPoint);
			GravaPreferencias("longitudeFinal",""+lngPoint);
			
		}
		Log.i("Atenção"," Finalizado Service ");
		stopSelf();
	}
	
	public void GravaPreferencias( String nomeCampo, String conteudoCampo) {
		 
		sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
		 
		Editor prefsPrivateEditor = sharedPreferences.edit();
		
		prefsPrivateEditor.putString(nomeCampo, conteudoCampo);
		prefsPrivateEditor.commit();
		 
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
