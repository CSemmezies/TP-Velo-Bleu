package dam.thoretton.semmezies.tp8velobleu;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

public class MainActivity extends Activity  {
	App app ;
	ProgressBar pb;
	ImageView imgVelo;
	private AsyncHttpClient client = new AsyncHttpClient();
	ArrayList<Stand> standList;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.v("debug","start activity");

		// initialisations
		standList = new ArrayList<Stand>();
		app = (App) getApplication();
		
		pb = (ProgressBar) this.findViewById(R.id.progressBar1);
		imgVelo = (ImageView) this.findViewById(R.id.imgVelo);
		
		app.lastLoc = null;// coordonnées GPS

		
		Log.v("debug", "start client");
		client.get("http://dam.lanoosphere.com/getVelos", new TextHttpResponseHandler(){
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				pb.setVisibility(View.INVISIBLE);
				if(pb.getVisibility() == pb.INVISIBLE){
					Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.velo_out_right);
					imgVelo.startAnimation(anim);
					imgVelo.setVisibility(View.INVISIBLE);		

					
					finish();
				}		
			}

			@Override
			public void onSuccess(String responseBody) {
				// TODO Auto-generated method stub
				try {
					parseResponse(responseBody);

					
					app.standList = standList; // on sauvegarde la liste dans l'instance App
					
					
					LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE); 
					boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
					Intent intent = new Intent(MainActivity.this,SecondActivity.class);
					startActivity(intent);
					
					if(!enabled)
					{
						Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						  startActivity(intent1);
					}
					
					
					
					
					// on démarre l'activité chargé d'afficher les stations en liste ou les details
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(String responseBody, Throwable error) {
				// TODO Auto-generated method stub
				super.onFailure(responseBody, error);
				Log.v("Test", "Erreur de connexion");
				error.printStackTrace();
			}
			
		});
		
	}
	
	private void parseResponse(String responseBody) throws XmlPullParserException, IOException{
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(responseBody));
		
		parser.require(XmlPullParser.START_DOCUMENT, null, null);
		parser.nextTag();
		
		while(parser.nextTag() == XmlPullParser.START_TAG){
			if(parser.getEventType() == XmlPullParser.START_TAG && "stand".equals(parser.getName())){
				
				Stand st = new Stand(parser); // récupère toute les infos pour une station
				
				// calcul de la distance
				Location loc = new Location("StandPoint");
				
				// on crée un item Location a partir des coordonnées des station parsées
				if(!st.lat.equals("0")){
					loc.setLatitude(Double.parseDouble(st.lat));
					loc.setLongitude(Double.parseDouble(st.lng));
				}else{
					loc.setLatitude(0.0);
					loc.setLongitude(0.0);
				}
				
				// on determine la distance entre la station et les coord. GPS du smartphone
				if(app.lastLoc!=null)
				{
					
//					Log.v("check lat",""+app.lastLoc.getLatitude());
//					Log.v("check lat",""+app.lastLoc.getLongitude());
					st.distance=(double) app.lastLoc.distanceTo(loc);
				}
				else
				{
					st.distance =0.0;
				}
		
				// on affiche que les station ouverte ou public
				if(st.disp.equals("1")){
					standList.add(st);
				}
			}else{
				if(parser.getEventType() == XmlPullParser.END_TAG){
					parser.next();
				}else{
					parser.nextText();
				}
			}
		}
		parser.require(XmlPullParser.END_TAG, null, "site");
		parser.next();
		parser.require(XmlPullParser.END_DOCUMENT, null, null);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	
}
