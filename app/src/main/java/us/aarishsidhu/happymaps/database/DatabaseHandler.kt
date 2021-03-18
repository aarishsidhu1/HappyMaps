package us.aarishsidhu.happymaps.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import us.aarishsidhu.happymaps.models.HappyMapModel

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HappyMapsDatabase"
        private const val TABLE_HAPPY_MAP = "HappyMapsTable"

        //All the Columns names in the table
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
            val CREATE_HAPPY_PLACE_TABLE = ("CREATE TABLE " + TABLE_HAPPY_MAP + "("

                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_IMAGE + " TEXT,"
                    + KEY_DESCRIPTION + " TEXT,"
                    + KEY_DATE + " TEXT,"
                    + KEY_LOCATION + " TEXT,"
                    + KEY_LATITUDE + " TEXT,"
                    + KEY_LONGITUDE + " TEXT)")
            db?.execSQL(CREATE_HAPPY_PLACE_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HAPPY_MAP")
        onCreate(db)
    }

    fun addHappyPlace(happyMap: HappyMapModel): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, happyMap.title)  // HappyMapModelClass TITLE
        contentValues.put(KEY_IMAGE, happyMap.image) // HappyMapModelClass IMAGE
        contentValues.put(KEY_DESCRIPTION, happyMap.description) // HappyMapModelClass DESCRIPTION
        contentValues.put(KEY_DATE, happyMap.date) // HappyMapModelClass DATE
        contentValues.put(KEY_LOCATION, happyMap.location) // HappyMapModelClass LOCATION
        contentValues.put(KEY_LATITUDE, happyMap.latitude) // HappyMapModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, happyMap.longitude) // HappyMapModelClass LONGITUDE

        // Inserting Row
        val result = db.insert(TABLE_HAPPY_MAP, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }

    fun updateHappyPlace(happyMap: HappyMapModel): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, happyMap.title)  // HappyMapModelClass TITLE
        contentValues.put(KEY_IMAGE, happyMap.image) // HappyMapModelClass IMAGE
        contentValues.put(KEY_DESCRIPTION, happyMap.description) // HappyMapModelClass DESCRIPTION
        contentValues.put(KEY_DATE, happyMap.date) // HappyMapModelClass DATE
        contentValues.put(KEY_LOCATION, happyMap.location) // HappyMapModelClass LOCATION
        contentValues.put(KEY_LATITUDE, happyMap.latitude) // HappyMapModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, happyMap.longitude) // HappyMapModelClass LONGITUDE

        // Inserting Row
        val result = db.update(TABLE_HAPPY_MAP, contentValues, KEY_ID + "=" + happyMap.id,null)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }

    fun deleteHappyPlace(happyMap: HappyMapModel): Int{
        val db = this.writableDatabase
        val result = db.delete(TABLE_HAPPY_MAP, KEY_ID + "=" + happyMap.id, null)
        db.close()
        return result
    }

    fun getHappyPlacesList(): ArrayList<HappyMapModel> {
        val happyPlaceList = ArrayList<HappyMapModel>()
        val selectQuery = "SELECT * FROM $TABLE_HAPPY_MAP"
        val db = this.readableDatabase

        try{
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()){
                do{
                    val place = HappyMapModel(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                            cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                            cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                    )
                    happyPlaceList.add(place)

                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch(e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        return happyPlaceList
    }


}
