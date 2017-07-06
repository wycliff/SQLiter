package wycliffe.com.sqliter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


//We need to write our own class to handle all database
// CRUD(Create, Read, Update and Delete) operations.

public class DatabaseHandler extends SQLiteOpenHelper {

//===================== All Static variables ============================
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

//=========================================================================
//==== Constructor must be there
    public  DatabaseHandler(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //where we need to write create table statements.
    //Called when DB is created.
    //Creating tables. {C}
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Table Create Statements- MAY BE DONE EVEN AS VAR DECLARATION
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS
                + "("
                + KEY_ID + " INGEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT"+" )";

        // creating required table
        db.execSQL(CREATE_CONTACTS_TABLE);

    }


    //called when database is upgraded like modifying the table structure,
    // adding constraints to database etc.,
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        //Create tables again
        onCreate(db);
    }


//===================== INSERTING NEW RECORD [C}==================
    // Adding new contact
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Building contentValues parameters.
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        //inserting in row
        db.insert(TABLE_CONTACTS, null, values);

        // close the database connection.
        db.close();
    }


//==================READING ROW(S){R} ===========================
/* ------------- A)GET ALL CONTACTS (SELECT *)------------------------*/
    // Getting single contact
    public Contact getContact(int id) {
       SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID, KEY_NAME, KEY_PH_NO},
               KEY_ID+"=?", new String[] {String.valueOf(id)},null,null,null,null );

        if(cursor != null )
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return contact;
    }

/* -------------B) GET ALL CONTACTS (SELECT *)------------------------*/
    // Getting All Contacts
    public Cursor getAllContacts() {

        List<Contact> contactList = new ArrayList<Contact>();
        //NB: The Select All Query
        String selectQuery = "SELECT * FROM "+ TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do {
                try {
                    Contact contact = new Contact();
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNumber(cursor.getString(2));

                    //Add to list
                    contactList.add(contact);
                }catch(NumberFormatException e){

                    //Toast.makeText(this, "The Exception: " + e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }

            }while (cursor.moveToNext());
        }
        //return  contactList;
        return cursor;
    }

/*-------------C)GET TOTAL NO OF CONTACTS -------------------------*/
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT * FROM "+ TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
    }

//===================  UPDATING RECORD {U}===========================
    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        //updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + "= ? ",
                new String[]{ String.valueOf(contact.getID())});
    }


//====================== DELETING A RECORD =============================
    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS,KEY_ID + "=?", new String[] {String.valueOf(contact.getID())});
        db.close();
    }

} // End DatabaseHandler.
