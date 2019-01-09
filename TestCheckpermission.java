package test.sinh.test.testcontentprovider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import test.sinh.test.R;

public class Main18Activity extends AppCompatActivity {
    Button show;
    ListView listView;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main18);
        show = findViewById(R.id.show);
        listView = findViewById(R.id.listView);
        checkPermission();


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showAllContacts();
                showAllContacts2();
            }
        });
    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("tusinh", "Permission is not granted");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
//                Log.e("tusinh", "Show an explanation to the user");

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Nhắc nhỏw")
                        .setMessage("Bạn cần phải cho phép mở quyền thì mới thực hiện đc chức năng này")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Stop the activity
                                requestPermission();
                            }

                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                requestPermission();
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                Log.e("tusinh", "The callback method gets the result of the request");
            }
        } else {
            Log.e("tusinh", "Permission has already been granted");
            // Permission has already been granted
        }
    }

    void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    private void showAllContacts() {
        Uri uri = Uri.parse("content://contacts/people");
        ArrayList<String> list = new ArrayList();
        CursorLoader loader = new CursorLoader(this, uri, null, null, null, null);
        Cursor c1 = loader.loadInBackground();
        c1.moveToFirst();
        while (c1.isAfterLast() == false) {
            String s = "";
            String idColumnName = ContactsContract.Contacts._ID;
            int idIndex = c1.getColumnIndex(idColumnName);
            s = c1.getString(idIndex) + " - ";
            String nameColumnName = ContactsContract.Contacts.DISPLAY_NAME;
            int nameIndex = c1.getColumnIndex(nameColumnName);
            s += c1.getString(nameIndex);
            Log.e("tusinh", "name: " + s);
//            String phoneColumnName = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//            int phoneIndex = c1.getColumnIndex(phoneColumnName);
//            s += c1.getString(phoneIndex)+" - ";
            c1.moveToNext();
            list.add(s);
        }
        c1.close();
        ArrayAdapter<String> adapter = new ArrayAdapter(Main18Activity.this, android.R.layout.simple_expandable_list_item_1, list);
        listView.setAdapter(adapter);

    }

    public void showAllContacts2() {
//        Uri uri = Uri.parse("content://contacts/people");
        ArrayList<String> list = new ArrayList<String>();
        Cursor c1 = getContentResolver()
                .query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Log.e("tusinh", "cursor: " + c1);
        c1.moveToFirst();
        while (c1.isAfterLast() == false) {
            String s = "";

            String idColumnName = ContactsContract.Contacts._ID;
            int idIndex = c1.getColumnIndex(idColumnName);
            s = c1.getString(idIndex) + " - "; // lay theo id nay trong query cursor phones.

            String id = c1.getString(
                    c1.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
            s += id + " - ";

            String nameColumnName = ContactsContract.Contacts.DISPLAY_NAME;
            int nameIndex = c1.getColumnIndex(nameColumnName);
            s += c1.getString(nameIndex) + " - ";


            Cursor phones = getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + c1.getString(idIndex),
                            null,
                            null);
            if (phones != null) {
                while (phones.moveToNext()) {
                    String phoneNumber = phones.getString(
                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    s += phoneNumber;
                    Log.e("tusinh", "doMagicContacts: " + c1.getString(nameIndex) + " " + phoneNumber);
                }
                phones.close();
            }
            c1.moveToNext();
            list.add(s);
        }
        c1.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main18Activity.this,
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(Main18Activity.this, "quyen dc bat", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(Main18Activity.this, "quyen k dc bat", Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
//  finish();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Main18Activity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
