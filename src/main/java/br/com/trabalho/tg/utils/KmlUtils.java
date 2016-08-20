package br.com.trabalho.tg.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class KmlUtils {

	private static JSONArray parseFeature(Feature feature) {
		JSONArray aux = new JSONArray();
		if (feature != null) {
			if (feature instanceof Document) {
				Document document = (Document) feature;
				List<Feature> featureList = document.getFeature();
				for (Feature documentFeature : featureList) {
					if (documentFeature instanceof Placemark) {
						JSONObject auxFeature = new JSONObject();
						Placemark placemark = (Placemark) documentFeature;
						Geometry geometry = placemark.getGeometry();
						auxFeature.put("descricao", placemark.getName());
						auxFeature.put("locale", parseGeometry(geometry));
						aux.put(auxFeature);
					} else if (documentFeature instanceof Folder) {
						Folder folder = (Folder) documentFeature;
						for (Feature folderFeature : folder.getFeature()) {
							JSONArray arrayAux = parseFeature(folderFeature);
							if(arrayAux.length() > 0)
								aux.put(arrayAux);
						}
						// if (f instanceof Document) {
						// Document d = (Document) f;
						// for (Feature f2 : d.getFeature()) {
						// if (f2 instanceof Placemark) {
						// Placemark p2 = (Placemark) f2;
						// Geometry g = p2.getGeometry();
						// System.out.println(p2.getName());
						// parseGeometry(g);
						// }
						// }
						// } else if (f instanceof Placemark) {
						// Placemark p = (Placemark) f;
						// System.out.println(p.getName());
						// }
						// }
					}
				}
			}
		}
		return aux;
	}

	private static List<Coordinate> parseGeometry(Geometry geometry) {
		List<Coordinate> coordinates = null;
		if (geometry != null) {
			if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				Boundary outerBoundaryIs = polygon.getOuterBoundaryIs();
				if (outerBoundaryIs != null) {
					LinearRing linearRing = outerBoundaryIs.getLinearRing();
					if (linearRing != null) {
						coordinates = linearRing.getCoordinates();
						// if(coordinates != null) {
						// for(Coordinate coordinate : coordinates) {
						// parseCoordinate(coordinate);
						// }
						// }
					}
				}
			} else if (geometry instanceof Point) {
				Point point = (Point) geometry;
				coordinates = point.getCoordinates();
			}
		}
		return coordinates;
	}
	
	public static JSONArray parseToKMLERetorna(String objKML) throws Exception{
		return parseFeature(Kml.unmarshal(objKML).getFeature());
	}
	
//	public static void main(String args[]) {
//		final Kml kml = Kml.unmarshal(new File("C:/Users/Jonnatas/teste.kml"));
//		Feature feature = kml.getFeature();
//		JSONArray array = parseFeature(feature);
//		for (Object obj : array) {
//			System.out.println(obj.toString());
//		}
//		Kml.
//
//	}

}
