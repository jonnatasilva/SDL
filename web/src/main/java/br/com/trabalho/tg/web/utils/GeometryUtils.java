package br.com.trabalho.tg.web.utils;

import org.json.JSONArray;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryUtils {
	
	public static Geometry arrayToPolygon(JSONArray array) throws ParseException {
		StringBuffer sb = new StringBuffer("POLYGON((");
		for(int i = 0; i < array.length(); i++) {
			sb.append(array.getJSONArray(i).get(0) + " " + array.getJSONArray(i).get(1));
			
			if((i+1) < array.length()) {
				sb.append(",");
			}
		}
		sb.append("))");
		Geometry polygon = new WKTReader(new GeometryFactory(new PrecisionModel(), 4326)).read(sb.toString());
		return polygon;
	}

}
