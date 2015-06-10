package dam.thoretton.semmezies.tp8velobleu;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//--- Cette activitée affiche sur une map GoogleMap un point marquant la position de la station sélectionné.

public class MapActivity extends FragmentActivity {
	private GoogleMap map;
	Double lat = 0.0;
	Double lng = 0.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		// on récupère les données de l'item via l'instance de App
		App app = (App) getApplication();
		lat = Double.parseDouble(app.item.lat);
		lng = Double.parseDouble(app.item.lng);
		map = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		LatLng latlng = new LatLng(lat, lng);
		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
		
		map.addMarker(new MarkerOptions().title(app.item.name)
				.snippet(app.item.address)
				.icon(BitmapDescriptorFactory.defaultMarker())
				.position(latlng));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.translate_out_left,R.anim.translate_in_left);
	}
}
