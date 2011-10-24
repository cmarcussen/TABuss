package test.BusTUC.Stops;

import test.BusTUC.Main.Route;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.maps.GeoPoint;

/*
 * Class containing properties for the closest stops.
 * This to avoid doing huge lookup regarding lat/long
 * foooo
 */
public class ClosestHolder implements Parcelable
{
	private GeoPoint point;
	private int busStopID;
	private String stopName;
	
	public ClosestHolder(GeoPoint point, int busStopID, String stopName)
	{	
		this.point = point;
		this.busStopID = busStopID;
		this.stopName = stopName;
	}
	
	public ClosestHolder(Parcel source)
	{
		readFromParcel(source);

	}
	
	public String getStopName()
	{
		return stopName;
	}
	
	public void setStopName(String newName)
	{
		stopName = newName;
	}
	
	public GeoPoint getPoint()
	{
		return point;
	}
	
	public int getBusStopID()
	{
		return busStopID;
	}
	
	public void setPoint(GeoPoint newPoint)
	{
		point = newPoint;
	}
	
	public void setBusStopID(int newID)
	{
		busStopID = newID;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	
		dest.writeInt(busStopID);
		dest.writeString(stopName);

		
	}
	
	private void readFromParcel(Parcel source) {
		 
		//GeoPoint geo = new GeoPoint(source.readInt(), source.readInt());
		busStopID = source.readInt();
    	stopName = source.readString();
	} 
	
	public static final Parcelable.Creator CREATOR =
	    	new Parcelable.Creator() {
	            public ClosestHolder createFromParcel(Parcel in) {
	                return new ClosestHolder(in);
	            }
	 
	            public ClosestHolder[] newArray(int size) {
	                return new ClosestHolder[size];
	            }
	        };

}
