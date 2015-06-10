package dam.thoretton.semmezies.tp8velobleu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class detailFragment extends Fragment
{
	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		//on récupere les infos de la station : App.item
		App app = (App) getActivity().getApplication();
		if(container==null)
		{
			return null;
		}
		
		// on affiche les informations
		View v = (View) inflater.inflate(R.layout.detail_fragment, container,false)	;
		Button btShowMap = (Button) v.findViewById(R.id.btShowMap);
		TextView adress = (TextView)  v.findViewById(R.id.tvAddress);
		TextView name  = (TextView)  v.findViewById(R.id.tvName);
		TextView tc = (TextView)  v.findViewById(R.id.tvtc);
		TextView ac = (TextView)  v.findViewById(R.id.tvac);
		TextView ap = (TextView)  v.findViewById(R.id.tvap);
		TextView ab = (TextView)  v.findViewById(R.id.tvab);
		
		name.setText(app.item.name);
		adress.setText(app.item.address);
		tc.setText(""+app.item.ab);
		ac.setText(""+app.item.ap);
		ap.setText(""+app.item.ac);
		ab.setText(""+app.item.ab);
		
		
		//--- création d'un listener qui affiche la page activity_map.xml
		btShowMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(getActivity(), MapActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.translate_in_right,R.anim.translate_out_right);
			}
		});

		return v;
		
	}
}
