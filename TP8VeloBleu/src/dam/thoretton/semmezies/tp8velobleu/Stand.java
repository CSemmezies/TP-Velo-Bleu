package dam.thoretton.semmezies.tp8velobleu;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class Stand implements Serializable, Comparable<Stand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id;
	String name;
	String lat;
	String lng;
	String address;
	String disp;
	int tc;
	int ac;
	int ap;
	int ab;
	Double distance = 0.0;

	// --- Parse des stands ---
	public Stand(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, null, "stand");
		id = parser.getAttributeValue(null, "id");
		name = URLDecoder.decode(parser.getAttributeValue(null, "name"),
				"UTF-8");
		parser.next();

		// --- Vérifie si la balise wcom existe
		if ("wcom".equals(parser.getName())) {
			parser.require(XmlPullParser.START_TAG, null, "wcom");

			// --- Vérifie si la balise wcom est égale a null
			if (parser.isEmptyElementTag()) {
				address = "";
				parser.nextText();
			} else {
				address = URLDecoder.decode(parser.nextText(), "UTF-8");
			}
			parser.next();
		}

		parser.require(XmlPullParser.START_TAG, null, "disp");
		disp = parser.nextText();
		parser.next();

		parser.require(XmlPullParser.START_TAG, null, "lng");
		lng = parser.nextText();
		parser.next();

		parser.require(XmlPullParser.START_TAG, null, "lat");
		lat = parser.nextText();
		parser.next();

		parser.require(XmlPullParser.START_TAG, null, "tc");
		tc = Integer.parseInt(parser.nextText());
		parser.next();

		parser.require(XmlPullParser.START_TAG, null, "ac");
		ac = Integer.parseInt(parser.nextText());
		parser.next();

		parser.require(XmlPullParser.START_TAG, null, "ap");
		ap = Integer.parseInt(parser.nextText());
		parser.next();

		parser.require(XmlPullParser.START_TAG, null, "ab");
		ab = Integer.parseInt(parser.nextText());
		parser.next();

		parser.require(XmlPullParser.END_TAG, null, "stand");
//		Log.v("debug", "nom : " + name + " - lat = " + lat + " - lng = " + lng);

	}

	public String toString() {
		return "id = " + id + " - nom : " + name + " - adresse : " + address
				+ "\n";
		// +" - latitude : "+lat+" - longitute : "+lng+" \n";
	}

	public String getPlace(String c) {
		String coul = "#fff";
		if (Integer.parseInt(c) > 3) {
			coul = "#178a17";
		}
		if (Integer.parseInt(c) <= 3) {
			coul = "#ff8000";
		}
		if (Integer.parseInt(c) <= 0) {
			coul = "#ff0000";
		}
		return coul;
	}

	public String getDist() {
		// Calcul distance
		String coul = "#fff";
		
		if (distance <= 300) {
			coul = "#178a17";
		}
		else if (distance <=800) {
			coul = "#ff8000";
		}
		else
		{
			coul = "#ff0000";
		}

		return coul;
	}

	@Override
	public int compareTo(Stand arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
