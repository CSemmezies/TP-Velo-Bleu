package dam.thoretton.semmezies.tp8velobleu;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

//ce fragment gère l'afichage de la liste de station

public class StandsFragment extends Fragment 
{
	private static final int[] BACKGROUND_GREYS = { 0xffffff, 0xf3f3f3 };

	App app;
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState)
	{


		//on récupère la liste via l'instance de App
		app  = (App) getActivity().getApplication();
		final ArrayList<Stand> standList = app.standList;

		ListView listView = (ListView)  inflater.inflate(R.layout.list_fragment,container, false);
		
		listView.setAdapter(new BaseAdapter() 
		{

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				// on récupère la View de chaque cellule
				View standItem = inflater.inflate(R.layout.cell,parent, false);

				
				//on affiche les donnée de la station
				ProgressBar pb = (ProgressBar) standItem.findViewById(R.id.progressBar2);
				Stand stl = getItem(position);

				standItem.setBackgroundColor(BACKGROUND_GREYS[position
						% BACKGROUND_GREYS.length]);
				
				TextView tvName = (TextView) standItem.findViewById(R.id.tvName);
				if(stl.name!=null)tvName.setText(stl.name);

				TextView tvAddress = (TextView) standItem.findViewById(R.id.tvAddress);
				if(stl.address!=null)tvAddress.setText(stl.address);

				TextView tvAvailableBicycle = (TextView) standItem.findViewById(R.id.tvAvailableBicycle);
				if(stl.ab<=1)
				{
					tvAvailableBicycle.setText(stl.ab+" vélo");
				}
				else
				{
					tvAvailableBicycle.setText(stl.ab+" vélos");
				}
			
				
				tvAvailableBicycle.setTextColor(Color.parseColor(stl.getPlace(String.valueOf(stl.ab))));

				TextView tvAvailableSpot = (TextView) standItem.findViewById(R.id.tvAvailableSpot);
				if(stl.ap<=1)
				{
					tvAvailableSpot.setText(stl.ap+" place");
				}
				else
				{
					tvAvailableSpot.setText(stl.ap+" places");
				}
				
				tvAvailableSpot.setTextColor(Color.parseColor(stl.getPlace(String.valueOf(stl.ap))));

				TextView tvDistance = (TextView) standItem.findViewById(R.id.tvDistance);
				tvDistance.setTextColor(Color.parseColor(stl.getDist()));
				
				// dans le cas ou on cherche les coordonées GPS
				// on affiche un message d'attente et un spinner circle ...
				if(stl.distance == 0 && app.lastLoc==null)
				{
					tvDistance.setText("checking");
					pb.setVisibility(View.VISIBLE);
				}
				else
				{
					pb.setVisibility(View.INVISIBLE);
					NumberFormat nf = new DecimalFormat("0.#");
					if(stl.distance<1000)
					{
						
					
						tvDistance.setText(nf.format(stl.distance)+" m");
					}
					else
					{
						tvDistance.setText(nf.format(stl.distance/1000)+" km");
					}
				}
				
				

				return standItem;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Stand getItem(int position) {
				// TODO Auto-generated method stub
				return standList.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return standList.size();
			}
		});
		
		// un click sur une cellule 
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				// TODO Auto-generated method stub
				
				Log.v("test","listenner action");
				App app = (App) getActivity().getApplication();
				final ArrayList<Stand> standList = app.standList;
				// on met a jour l'item selection dans l'instance App
				app.item = standList.get(position);
				
				//Log.v("test",app.item.address);
				
				// on appelle la méthode qui va changer le fragment
				((SecondActivity)getActivity()).showDetailItem();
				Log.v("test","after laucnh method show detail");
			

			}
		});

		return listView;
	}


	

}


