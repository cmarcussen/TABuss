package test.BusTUC.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import test.BusTUC.R;
import test.BusTUC.Queries.Browser;
import test.BusTUC.Stops.BusStops;
import test.BusTUC.Stops.ClosestHolder;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class RealTimeList extends ListActivity
{
	public static String ID;
	Bundle extras;
	TextView text;
	ArrayList <ClosestHolder> holder;
	String [] stopNames;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	  super.onCreate(savedInstanceState);
	  ListView lv = getListView();
	  text  = new TextView(this);
	  extras = getIntent().getExtras();
	  if(extras == null)
	  {
		  
		  text.setText(MapOverlay.foundBusStop +"\n");
		  lv.addHeaderView(text);
		  lv.setTextFilterEnabled(true);
		  // No extras, which means access from map
		  // Use static list from MapOverlay foundStopsList
		  setFromMap();
		  
	  }
	  else 
	  {
		text.setText("Busstopper n�r deg"+"\n");
		  lv.setTextFilterEnabled(true);
		  lv.addHeaderView(text);
		  setFromExtras();

	  }
	
	}
	
	public void setFromMap()
	{
		  try
		  {
			  String [] neededStopsOutgoing;

			  neededStopsOutgoing = new String [MapOverlay.foundStopsList.size()];
			  StringBuffer buf;
			  String minute1;
			  neededStopsOutgoing[0] =  "Kommende busser:\n";
			  
			  // Find outgoing bustops
			  for(int i=0; i<neededStopsOutgoing.length; i++)
			  {		
				// if minte = 0-9, add a zero
				minute1 = "" +MapOverlay.foundStopsList.get(i).arrivalTime.getMinutes();
				buf = new StringBuffer("" + minute1);
				if(buf.length() == 1)
				{
					buf.insert(0, "0");
				}
				// Append to string array
				neededStopsOutgoing[i]= "Buss " + MapOverlay.foundStopsList.get(i).getLine() + " g�r " +MapOverlay.foundStopsList.get(i).arrivalTime.getHours() + ":" +buf + " til " +MapOverlay.foundStopsList.get(i).getDest() ;
				  
			  }
			  // Show in list
			  setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, neededStopsOutgoing));
			  }
		  catch(Exception e)
		  {
			  System.out.println("No routes found");
			  Toast.makeText(this, "No routes found", Toast.LENGTH_SHORT);
		  }
	}
	
	public void setFromExtras()
	{
		holder = extras.getParcelableArrayList("test");
	    stopNames = new String[holder.size()];
	    Collections.sort(holder);
		for(int i=0; i<holder.size(); i++)
		{
			
			String tmp = ""+holder.get(i).getBusStopID();		
			if(Integer.parseInt((tmp.substring(4,5))) == 1)
			{
				tmp = "til byen";
			} else tmp = "fra byen";
			holder.get(i).setStopName(holder.get(i).getStopName() + " " + tmp);
			stopNames[i] = holder.get(i).getStopName();
		}
	
		  setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, stopNames));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		new LoadThread(this, position).execute();

	}
	

	class LoadThread extends AsyncTask<Void, Void, Void>
	{
		private Context context;    
		Intent intent;
		ProgressDialog myDialog = null;
		ClosestHolder pressedStop;
		private int position;
		int outgoing;
		String [] neededStopsOutgoing;
		String pressedStopName;
		public LoadThread(Context context, int position)
		{

			this.context = context;
			this.position = position;
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			ArrayList <BusStops> stops = Browser.specificRequestForStop(outgoing);
			StringBuffer buf;
		    String minute1;
		    System.out.println("STOPS SIZE: " + stops.size());
			neededStopsOutgoing = new String[stops.size()];
			

			for(int i=0; i<stops.size(); i++)
			{
				minute1 = "" +stops.get(i).arrivalTime.getMinutes();
				buf = new StringBuffer("" + minute1);
				
				
				if(buf.length() == 1)
				{
					buf.insert(0, "0");
				}
				// Append to string array
				neededStopsOutgoing[i]= "Buss " +stops.get(i).getLine() + " g�r " +stops.get(i).arrivalTime.getHours() + ":" +buf + " til " +stops.get(i).getDest();
			 }
			return null;
		}

		@Override
		protected void onPreExecute()
		{
			pressedStop = holder.get(position-1);
			//text.setText(pressedStop.getStopName()+"\n");
			outgoing = Integer.parseInt(Homescreen.realTimeCodes.get(pressedStop.getBusStopID()).toString());
			myDialog = ProgressDialog.show(context, "Loading!", "Laster sanntid");
			
			System.out.println("navn satt: " + pressedStop.getStopName());
		}

		@Override
		protected void onPostExecute(Void unused)
		{
			intent = new Intent(context, RealTimeListFromMenu.class);
			ArrayList <String> retList = new ArrayList <String>();
			
			for(int i=0; i<neededStopsOutgoing.length; i++)
			{
				retList.add(neededStopsOutgoing[i]);
			}
			intent.putExtra("tag", pressedStop.getStopName());
			intent.putExtra("test", retList);
			context.startActivity(intent);
			myDialog.dismiss();

		}
	}  



	
}
