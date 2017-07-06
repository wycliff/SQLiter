package wycliffe.com.sqliter;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/* Steps: SQLite Database
    - Class: Contact class with getter and setter methods (to maintain single contact as an object).
    - Class: Writing SQLite Database Handler Class. (with onCreate() and onUpgrade()).
            >Handling all CRUD OPERATions within the database class
 */
public class MainActivity extends AppCompatActivity {

    Button selectorButton;
    TextView shower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         selectorButton = (Button) findViewById(R.id.selectAll);
         shower = (TextView) findViewById(R.id.myShower);

        selectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using the DatabaseHandler
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);


        /* C R U D*/

                db.addContact(new Contact("Wycliffe", "0723017872"));
                db.addContact(new Contact("Ramsey", "0723017873"));
                db.addContact(new Contact("Daniel", "0723017874"));
                db.addContact(new Contact("Cyril", "0723017875"));

                //reading all
                //List<Contact> the_contacts =  db.getAllContacts();
                //Toast.makeText(getApplicationContext(),"ALL FROM THE DB: "+ the_contacts.toString(),Toast.LENGTH_LONG).show();
                //shower.setText(the_contacts.toString());


                Cursor myCursor ;
                myCursor = db.getAllContacts();

                if (myCursor.moveToFirst()) {
                    do {


                        String number = myCursor.getString(1);
                        Toast.makeText(getApplicationContext(), "ALL FROM THE DB: " + number, Toast.LENGTH_LONG).show();
                        //shower.setText(the_contacts.toString());

                    } while (myCursor.moveToNext());

                }
            }
        });

    }//onCreate

}
