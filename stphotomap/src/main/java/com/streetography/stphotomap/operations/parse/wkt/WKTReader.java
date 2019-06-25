package com.streetography.stphotomap.operations.parse.wkt;

import com.streetography.stphotomap.models.geojson.GeoJSON;
import com.streetography.stphotomap.models.geojson.geometry.*;
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONGeometry;
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class WKTReader {
	private GeoJSONPoint getGeoJSONPoint(StringTokenizer st) {
		double[] coordinates = getDoubleArray(st);
		return coordinates == null || coordinates.length < 2 ? null : new GeoJSON().getPoint(coordinates[0], coordinates[1]);
	}

	private GeoJSONLineString getGeoJSONLineString(StringTokenizer st) {
		return new GeoJSON().getLineString(getPointsArrayList(st));
	}

	private GeoJSONPolygon getGeoJSONPolygon(StringTokenizer stringTokenizer) {
		return new GeoJSON().getPolygon(getLineStringsArrayList(stringTokenizer));
	}

	private GeoJSONMultiPoint getGeoJSONMultiPoint(StringTokenizer st) {
		return new GeoJSON().getMultiPoint(getPointsArrayList(st));
	}

	private GeoJSONMultiLineString getGeoJSONMultiLineString(StringTokenizer st) {
		return new GeoJSON().getMultiLineString(getLineStringsArrayList(st));
	}

	private GeoJSONMultiPolygon getGeoJSONMultiPolygon(StringTokenizer st) {
		return new GeoJSON().getMultiPolygon(getGeoJSONPolygonsArrayList(st));
	}

	/**
	 * Transforms the input WKT-formatted String into GeoJSONObject object
	 */
	public GeoJSONObject getGeoJSONObjectFromWKT(String wktString) {
		StringTokenizer stringTokenizer = new StringTokenizer(wktString,"(), ",true);
		if(!stringTokenizer.hasMoreElements()) {
			return null;
		}
		String name = (String) stringTokenizer.nextElement();
		GeoJSONObject geoJSONObject;
		switch (name) {
			case Keys.POINT:
				geoJSONObject = getGeoJSONPoint(stringTokenizer);
				break;
			case Keys.LINESTRING:
				geoJSONObject = getGeoJSONLineString(stringTokenizer);
				break;
			case Keys.POLYGON:
				geoJSONObject = getGeoJSONPolygon(stringTokenizer);
				break;
			case Keys.MULTIPOINT:
				geoJSONObject = getGeoJSONMultiPoint(stringTokenizer);
				break;
			case Keys.MULTILINESTRING:
				geoJSONObject = getGeoJSONMultiLineString(stringTokenizer);
				break;
			case Keys.MULTIPOLYGON:
				geoJSONObject = getGeoJSONMultiPolygon(stringTokenizer);
				break;
			case Keys.GEOMETRYCOLLECTION:
				ArrayList<GeoJSONGeometry> geometries = new ArrayList<>();
				String token = stringTokenizer.nextToken();
				while (!token.equals(")")) {
					switch (token) {
						case Keys.POINT:
							geometries.add(getGeoJSONPoint(stringTokenizer));
							break;
						case Keys.LINESTRING:
							geometries.add(getGeoJSONLineString(stringTokenizer));
							break;
						case Keys.POLYGON:
							geometries.add(getGeoJSONPolygon(stringTokenizer));
							break;
						case Keys.MULTIPOINT:
							geometries.add(getGeoJSONMultiPoint(stringTokenizer));
							break;
						case Keys.MULTILINESTRING:
							geometries.add(getGeoJSONMultiLineString(stringTokenizer));
							break;
						case Keys.MULTIPOLYGON:
							geometries.add(getGeoJSONMultiPolygon(stringTokenizer));
							break;
					}
					token = stringTokenizer.nextToken();
				}
				return new GeoJSON().getGeometryCollection(geometries);
			default:
				System.err.println("Unknown geometry type: " + name);
				return null;
		}
		return geoJSONObject;
	}

	private ArrayList<Double> getDoubleList(StringTokenizer stringTokenizer) {
		ArrayList<Double> doubleArrayList = new ArrayList<>();

		String[] skip = new String[] {"(", ",", " "};

		String token = stringTokenizer.nextToken();
		while(!token.equals(")")) {
			if(token.equals("EMPTY")) {
				return null;
			}
			if(!Arrays.asList(skip).contains(token)) {
				doubleArrayList.add(Double.parseDouble(token));
			}
			token = stringTokenizer.nextToken();
		}
		return doubleArrayList;
	}

	private double[] getDoubleArray(StringTokenizer stringTokenizer) {
		ArrayList<Double> doubleArrayList = getDoubleList(stringTokenizer);

		int doubleListSize = doubleArrayList == null ? 0 : doubleArrayList.size();
		double[] result = new double[doubleListSize];
		for(int i=0; i<doubleListSize; i++) {
			result[i] = doubleArrayList.get(i);
		}
		return result;
	}

	private ArrayList<GeoJSONPoint> getPointsArrayList(StringTokenizer stringTokenizer) {
		ArrayList<Double> doubleArrayList = getDoubleList(stringTokenizer);
		int doubleListSize = doubleArrayList == null ? 0 : doubleArrayList.size();

		ArrayList<GeoJSONPoint> points = new ArrayList<>();
		for(int i=0; i<doubleListSize; i += 2) {
			Double firstCoordinate = doubleArrayList.get(i);
			Double secondCoordinate = doubleArrayList.get(i + 1);

			GeoJSONPoint point = new GeoJSON().getPoint(firstCoordinate, secondCoordinate);
			points.add(point);
		}
		return points;
	}

	private ArrayList<GeoJSONLineString> getLineStringsArrayList(StringTokenizer stringTokenizer) {
		String token = stringTokenizer.nextToken();
		ArrayList<GeoJSONLineString> lineStrings = new ArrayList<>();
		while(!token.equals(")")) {
			lineStrings.add(getGeoJSONLineString(stringTokenizer));
			token = stringTokenizer.nextToken();
		}
		return lineStrings;
	}

	private ArrayList<GeoJSONPolygon> getGeoJSONPolygonsArrayList(StringTokenizer stringTokenizer) {
		String token = stringTokenizer.nextToken();
		ArrayList<GeoJSONPolygon> polygons = new ArrayList<>();
		while(!token.equals(")")) {
			polygons.add(getGeoJSONPolygon(stringTokenizer));
			token = stringTokenizer.nextToken();
		}
		return polygons;
	}

	private static final class Keys {
		static final String POINT = "POINT";
		static final String LINESTRING = "LINESTRING";
		static final String POLYGON = "POLYGON";
		static final String MULTIPOINT = "MULTIPOINT";
		static final String MULTILINESTRING = "MULTILINESTRING";
		static final String MULTIPOLYGON = "MULTIPOLYGON";
		static final String GEOMETRYCOLLECTION = "GEOMETRYCOLLECTION";
	}
}
