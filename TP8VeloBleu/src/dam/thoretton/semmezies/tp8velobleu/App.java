package dam.thoretton.semmezies.tp8velobleu;

import java.util.ArrayList;

import android.app.Application;
import android.location.Location;

//instance permettant de partager en activités et fragments
// - la liste d'item actuelle
// - la station dont on veut les details
// - les dernieres coordonnées GPS

public class App extends Application{
	ArrayList<Stand> standList;
	Stand item;
	Location lastLoc;
}
