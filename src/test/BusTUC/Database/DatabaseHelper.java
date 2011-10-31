package test.BusTUC.Database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String dbName="tucApp";
	public static final String queryTable="queries";
	public static final String areaTable="areas";
	public static final String rowId="_id";
	public static final String maxLat="maxLat";
	public static final String minLat="minLat";
	public static final String maxLong="maxLong";
	public static final String minLong="minLong";
	public static final String destination="destination";
	public static final String origin="origin";
	public static final String time="time";
	public static final String day="day";
	public static final String success="success";

	public DatabaseHelper(Context context) {
		super(context, dbName, null, 6);
		//db.execSQL("DROP TABLE IF EXISTS "+queryTable);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		System.out.println("EKSEKVERER CREATE TABLE");

		db.execSQL("CREATE TABLE "+queryTable+" ("+rowId+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
				destination+" TEXT, "+origin+" Integer, "+time+" INTEGER NOT NULL, "+day+" INTEGER NOT NULL);");
		
		db.execSQL("CREATE TABLE "+areaTable+" ("+rowId+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
				maxLat+" REAL, "+minLat+" REAL, "+maxLong+" REAL, "+minLong+" REAL);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+areaTable);
		db.execSQL("DROP TABLE IF EXISTS "+queryTable);
		onCreate(db);
	}

	public int AddArea(double maxLat,double minLat, double maxLong, double minLong){
		SQLiteDatabase db= this.getWritableDatabase();
		//db.execSQL("INSERT INTO "+areaTable+" VALUES("+maxLat+", "+minLat+", "+maxLong+", "+minLong+")");
		ContentValues cv=new ContentValues();

		cv.put(this.maxLat, maxLat);
		cv.put(this.minLat, minLat);
		cv.put(this.maxLong, maxLong);
		cv.put(this.minLong, minLong);

		db.insert(areaTable, this.maxLat, cv);
		Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
		cursor.moveToFirst();
		int id = cursor.getInt(0);
		db.close();
		return id;
	}
	
	public Cursor getAreaId(double lat, double lon){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT "+rowId+" from "+areaTable+ " t WHERE t.maxLat > "+lat+" AND t.minLat < "+lat+
				" AND t.maxLong > "+lon+" AND t.minLong < "+lon, null);
		return cursor;
	}
	
	public Cursor getArea(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * from "+areaTable+ " WHERE "+rowId+" = "+id , null);
		return cursor;
	}
	
	public void AddQuery(Query query)
	{
		SQLiteDatabase db= this.getWritableDatabase();

		ContentValues cv=new ContentValues();

		cv.put(destination, query.getDestination());
		cv.put(origin, query.getOrigin());
		cv.put(time, query.getTime());
		cv.put(day, query.getDay());

		db.insert(queryTable, destination, cv);
		db.close();
	}
	public Cursor getAllQueries(){
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cur= db.rawQuery("Select * from "+queryTable, null);
		
		if(cur!=null){
			//db.close();
			return cur;
		}
		//db.close();
		return null;	
	}
	
	public Cursor getQueryFromArea(double lat, double lon, int maxTime, int minTime){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * from "+queryTable+" WHERE "+origin+" = (SELECT "+rowId+" from "+areaTable+ " WHERE maxLat > "+lat+" AND minLat < "+lat+
				" AND maxLong > "+lon+" AND minLong < "+lon+") AND "+time+" < "+maxTime+" AND "+time+" > "+minTime, null);
		return cursor;
	}
	
	public void clearDatabase(){
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS "+areaTable);
		db.execSQL("DROP TABLE IF EXISTS "+queryTable);
		onCreate(db);
		db.close();
	}
	public int getQueryCount()
	{
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cur= db.rawQuery("Select * from "+queryTable, null);
		if(cur!=null){
			int x= cur.getCount();
			cur.close();
			db.close();
			return x;
		}
		db.close();
		return 0;		
	}
}
