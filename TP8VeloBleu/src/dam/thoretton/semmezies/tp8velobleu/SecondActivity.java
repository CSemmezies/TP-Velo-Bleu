package dam.thoretton.semmezies.tp8velobleu;

import java.util.Collections;
import java.util.Comparator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


// Cette activitée affiche les stations en liste, ou bien le detail (fragments)
// récupère la position GPS, et calcule la distance en entre la station et le tel
// Gère l'affichage en liste en fonction de trois modes tri

public class SecondActivity extends FragmentActivity 
{

	App app;
	Boolean quit,list,item,tri_velo,tri_place,tri_distance;
	LocationManager service;
	LocationListener GPSListener;
	String customProvider;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		app = (App) getApplication();
		tri_place=true;
		tri_velo = tri_distance = false;
		
		quit=true;
		list=true;
		item=false;
		
		// on récupère le location manager
		context = this.getApplicationContext();
		service = (LocationManager) getSystemService(LOCATION_SERVICE); 
		boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		//dans le cas ou le service n'est pas, on demande la permission Ã  l'utilisateur de l'activer.
		if (!enabled)
		{
			Log.v("check GPS service", "disable");
			
			  //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			 // startActivity(intent);
		} 
		else
		{
			Log.v("check GPS service", "enable");
			
			
			// critère pour la position GPS
			Criteria locationCritera = new Criteria();
		    locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
		    locationCritera.setAltitudeRequired(false);
		    locationCritera.setBearingRequired(false);
		    locationCritera.setCostAllowed(true);
		    locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
		    customProvider = service.getBestProvider(locationCritera, true);
		    

		    
			
			GPSListener =  new LocationListener()
			{

				@Override
				public void onLocationChanged(Location location) {
					
					Log.v("GPS listener", "location changed");
					int inc;
					app.lastLoc = location; // on met Ã  jour la variable globale
					for(inc=0;inc<app.standList.size();inc++)
					{
						Location loc = new Location("StandPoint");
						//Log.v("Test", "lat = " + app.standList.get(inc).lat);
						
						// pour chaque item on récupère la location
						if(!app.standList.get(inc).lat.equals("0"))
						{
							//Log.v("station", "lng = " + Double.parseDouble(app.standList.get(inc).lng));
							//Log.v("station", "lat = " + Double.parseDouble(app.standList.get(inc).lat));
							loc.setLatitude(Double.parseDouble(app.standList.get(inc).lat));							
							loc.setLongitude(Double.parseDouble(app.standList.get(inc).lng));
						}else
						{
							loc.setLatitude(0.0);						
							loc.setLongitude(0.0);
						}
						//Log.v("ma position", "lat = " + String.valueOf(app.lastLoc.getLatitude()));
						//Log.v("ma position", "lng = " + app.lastLoc.getLongitude());
						
						//Log.v("Test", "distance = " + loc.distanceTo(app.lastLoc));
						
						//pour chaque item on calcule la distance
						app.standList.get(inc).distance = (double) (app.lastLoc.distanceTo(loc));
						if(list)// on met a jour le fragment
						{
							StandsFragment standsFragment = new StandsFragment();
							getSupportFragmentManager().beginTransaction().replace(R.id.frag, standsFragment).commit();
						}
						
					}
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onProviderDisabled(String arg0) {}
				@Override
				public void onProviderEnabled(String arg0) {}
				@Override
				public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
				
			};
			
			//on vérifie toute les 5 secondes la position GPS
			service.requestLocationUpdates(customProvider, 5000, 10,GPSListener);
		}
		
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		showStandItems(false);
		

		// Log.v("Test", standList.toString());

		final Button btBicycles = (Button) this.findViewById(R.id.btBicycles);
		final Button btPlaces = (Button) this.findViewById(R.id.btPlaces);
		final Button btDistances = (Button) this.findViewById(R.id.btDistances);

		btBicycles.setBackgroundResource(R.drawable.btn_pressed);

		btBicycles.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				//on veut trier en fonction des velos disponibles
				tri_velo=true;
				tri_place=false;
				tri_distance=false;
				
				// TODO Auto-generated method stub
				btBicycles.setBackgroundResource(R.drawable.btn_pressed);
				btPlaces.setBackgroundResource(R.drawable.btn);
				btDistances.setBackgroundResource(R.drawable.btn);
				
				Collections.sort(app.standList,new Comparator<Stand>()
				{

					@Override
					public int compare(Stand arg0, Stand arg1) {
						return arg1.ab-arg0.ab;
					}
					
				});
				
				if(list)// on met a jour le fragment
				{
					StandsFragment standsFragment = new StandsFragment();
					getSupportFragmentManager().beginTransaction().replace(R.id.frag, standsFragment).commit();
				}
			}
		});

		btPlaces.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) {
				
				tri_velo=false;
				tri_place=true;
				tri_distance=false;
				// TODO Auto-generated method stub
				btBicycles.setBackgroundResource(R.drawable.btn);
				btPlaces.setBackgroundResource(R.drawable.btn_pressed);
				btDistances.setBackgroundResource(R.drawable.btn);
				
				
				Collections.sort(app.standList,new Comparator<Stand>()
						{

							@Override
							public int compare(Stand arg0, Stand arg1) {
								return arg1.ap-arg0.ap;
							}
							
						});
				
				if(list)
				{
					StandsFragment standsFragment = new StandsFragment();
					getSupportFragmentManager().beginTransaction().replace(R.id.frag, standsFragment).commit();
				}


			}
		});

		btDistances.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) {
				
				tri_velo=false;
				tri_place=false;
				tri_distance=true;
				// TODO Auto-generated method stub
				btBicycles.setBackgroundResource(R.drawable.btn);
				btPlaces.setBackgroundResource(R.drawable.btn);
				btDistances.setBackgroundResource(R.drawable.btn_pressed);
				
				
				if(app.lastLoc!=null)
				{
					Collections.sort(app.standList,new Comparator<Stand>()
							{

								@Override
								public int compare(Stand arg0, Stand arg1) {
									//return arg1.distance-arg0.distance;
									return arg0.distance.compareTo(arg1.distance);
								}
								
							});
				}
				
				if(list)
				{
					StandsFragment standsFragment = new StandsFragment();
					getSupportFragmentManager().beginTransaction().replace(R.id.frag, standsFragment).commit();
				}


			}
		});
	}
	
	
	public void showStandItems(Boolean animation)
	{
		quit=true;
		item=false;
		list=true;
		
		if(tri_place==true)
		{
			Collections.sort(app.standList,new Comparator<Stand>()
					{

						@Override
						public int compare(Stand arg0, Stand arg1) {
							return arg1.ap-arg0.ap;
						}
						
					});
		}
		if(tri_distance==true)
		{
			Collections.sort(app.standList,new Comparator<Stand>()
					{

						@Override
						public int compare(Stand arg0, Stand arg1) {
							//return arg1.distance-arg0.distance;
							return arg1.distance.compareTo(arg0.distance);
						}
						
					});
		}
		if(tri_velo==true)
		{
			Collections.sort(app.standList,new Comparator<Stand>()
					{

						@Override
						public int compare(Stand arg0, Stand arg1) {
							return arg0.ab-arg1.ab;
						}
						
					});
		}
		((View)this.findViewById(R.id.menuTri)).setVisibility(View.VISIBLE);
		
		if(animation)
		{
			StandsFragment standsFragment = new StandsFragment();
			getSupportFragmentManager().beginTransaction()
			.setCustomAnimations(R.anim.translate_out_left,R.anim.translate_in_left)
			.replace(R.id.frag, standsFragment).commit();
		}
		else
		{
			StandsFragment standsFragment = new StandsFragment();
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.frag, standsFragment).commit();
		}
		
	}
	
	public void showDetailItem()
	{
		((View)this.findViewById(R.id.menuTri)).setVisibility(View.INVISIBLE);
		quit=false;
		item=true;
		list=false;
		detailFragment detail = new detailFragment();
		getSupportFragmentManager()
		.beginTransaction()
		.setCustomAnimations(R.anim.translate_in_right,R.anim.translate_out_right)
		.replace(R.id.frag, detail)
		.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
		if(quit==true)
		{
			finish();
		}
		else
		{
			this.showStandItems(true);
		}
	}
	
	public void onResume()
	{
		super.onResume();
		// on cherche la postion GPS
		boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(enabled)
		{
			service.requestLocationUpdates(customProvider, 5000, 10,GPSListener);
			showStandItems(false);
		}
		
		
	}
	
	public void onPause()
	{
		super.onPause();
		// on arrete la recherche des coordonnées GPS
		if(service!=null)
		service.removeUpdates(GPSListener);
	}
	
}
