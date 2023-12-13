package com.example.myapplicationcustlist_youbike;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class JsonParser_yb {
	MainActivity m;
	Location nowl;
	// constructor
	public JsonParser_yb(MainActivity c, Location nowl) {
      m=c;
      nowl=nowl;

		Log.d(" XY2 ", ""+nowl.getLatitude()+","+nowl.getLongitude() );
	}

	/**
	 * Getting XML from URL making HTTP request
	 * @param url string
	 * */
	public void  getjsonFromUrl(final String url,final Location nowl) {

			//http
			Thread okHttpExecuteThread = new Thread() {
				@Override
				public void run() {
					final String xml;
			       okhttp_util linkweb=new okhttp_util();
	         		xml=linkweb.urlget(url);

					m.runOnUiThread(new Runnable() {
										@SuppressLint("SuspiciousIndentation")
										@Override
										public void run() {
											//
											try {

												JSONArray warray = new JSONArray(xml);
												//
												for (int i = 0; i < warray.length(); i++) {
													// creating new HashMap
													HashMap<String, String> map = new HashMap<String, String>();
													JSONObject jsonObject = warray.getJSONObject(i);
													String locationName = jsonObject.getString("sna");
													String lat = jsonObject.getString("lat");
													String lon = jsonObject.getString("lng");
													String sno = jsonObject.getString("sno");
													//
													double lat_d=Double.parseDouble(lat);
													double lon_d=Double.parseDouble(lon);
													Location targetLocation = new Location("");//provider name is unnecessary
													targetLocation.setLatitude(lat_d);//your coords of course
													targetLocation.setLongitude(lon_d);


													//
													String sbi = jsonObject.getString("sbi");
													String bemp = jsonObject.getString("bemp");
													String sarea = jsonObject.getString("sarea");
													String tot = jsonObject.getString("tot");
													// adding each child node to HashMap key => value
													map.put(m.KEY_ID, sno);
													map.put(m.KEY_TITLE, locationName+"-"+sarea);
													map.put(m.KEY_lat, lat);
													map.put(m.KEY_lon, lon);
													Log.d(" TAG ", lat+","+lon);
													float distanceInMeters =-1;
													if(nowl !=null) {
														distanceInMeters = targetLocation.distanceTo(nowl);
														map.put(m.KEY_DURATION, "" + distanceInMeters);
														Log.d(" TAG ", "" + distanceInMeters);
													}
													else
														map.put(m.KEY_DURATION, "X");
													map.put(m.KEY_ARTIST, "容量:"+tot+",目前可借:" + sbi + "台,可還:" + bemp + "台");

													// /
													// adding HashList to ArrayList
													//if (distanceInMeters>=0 && distanceInMeters<=7000)
													     m.bList.add(map);

												}
												//

												// Getting adapter by passing xml data ArrayList
												m.adapter = new LazyAdapter(m, m.bList);
												m.list.setAdapter(m.adapter);


											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									});
							//


							}

		};                    // Start the child thread.
		okHttpExecuteThread.start();





	}
	
   //計算出距離
	public double Distance(double latitude1,double longitude1, double latitude2, double longitude2)
	{
		double radLatitude1 = latitude1 * Math.PI / 180;
		double radLatitude2 = latitude2 * Math.PI / 180;
		double l = radLatitude1 - radLatitude2;
		double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
		double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
				+ Math.cos(radLatitude1) * Math.cos(radLatitude2)
				* Math.pow(Math.sin(p / 2), 2)));
		distance = distance * 6378137.0;
		distance = Math.round(distance * 10000) / 10000;

		return distance ;
	}



}
