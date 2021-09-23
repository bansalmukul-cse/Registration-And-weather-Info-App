 package com.example.registrationapp;

 import android.content.Intent;
 import android.os.Bundle;
 import android.text.TextUtils;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.widget.Toast;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.Spinner;
 import android.app.DatePickerDialog;
 import android.widget.DatePicker;
 import java.util.Calendar;

 import androidx.appcompat.app.AppCompatActivity;

 import com.android.volley.Request;
 import com.android.volley.RequestQueue;
 import com.android.volley.Response;
 import com.android.volley.VolleyError;
 import com.android.volley.toolbox.JsonObjectRequest;
 import com.android.volley.toolbox.Volley;
 import android.view.View.OnClickListener;

 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;

 import static com.loopj.android.http.AsyncHttpClient.log;

 public class MainActivity extends AppCompatActivity  {
     // creating variables for edi text,
     // button and our text views.
     private EditText pinCodeEdt;
     private Button getDataBtn;
     private TextView pinCodeDetailsTV;
     private TextView dateText;

     /** EditText field to enter  gender */
     private Spinner mGenderSpinner;

     // creating a variable for our string.
     String pinCode;

     // creating a variable for request queue.
     private RequestQueue mRequestQueue;
     private int mGender ;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);


         Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);
         Button register = findViewById(R.id.register);

         ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                 android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
         myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         mySpinner.setAdapter(myAdapter);

         register.setOnClickListener(new OnClickListener() {
             // The code in this method will be executed when the numbers category is clicked on.
             @Override
             public void onClick(View view) {
                 // Create a new intent to open the {@link NumbersActivity}
                 Intent numbersIntent = new Intent(MainActivity.this, weatherActivity.class);

                 // Start the new activity
                 startActivity(numbersIntent);
             }
         });


         // initializing our variables.
         pinCodeEdt = findViewById(R.id.pincode);
         getDataBtn = findViewById(R.id.BtnGetCityandState);
         pinCodeDetailsTV = findViewById(R.id.idTVPinCodeDetails);

         // initializing our request que variable with request
         // queue and passing our context to it.
         mRequestQueue = Volley.newRequestQueue(MainActivity.this);

         // initialing on click listener for our button.
         getDataBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // getting string from EditText.
                 pinCode = pinCodeEdt.getText().toString();

                 // validating if the text is empty or not.
                 if (TextUtils.isEmpty(pinCode)) {
                     // displaying a toast message if the
                     // text field is empty
                     Toast.makeText(MainActivity.this, "Please enter valid pin code", Toast.LENGTH_SHORT).show();
                 } else {
                     // calling a method to display
                     // our pincode details.
                     getDataFromPinCode(pinCode);
                 }
             }
         });
     }

     private void getDataFromPinCode(String pinCode) {

         // clearing our cache of request queue.
         mRequestQueue.getCache().clear();

         // below is the url from where we will be getting
         // our response in the json format.
         String url = "http://www.postalpincode.in/api/pincode/"+pinCode;

         // below line is use to initialize our request queue.
         RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

         // in below line we are creating a
         // object request using volley.
         JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
             // inside this method we will get two methods
             // such as on response method
             // inside on response method we are extracting
             // data from the json format.
             try {
                 // we are getting data of post office
                 // in the form of JSON file.
                 JSONArray postOfficeArray = response.getJSONArray("PostOffice");
                 if (response.getString("Status").equals("Error")) {
                     // validating if the response status is success or failure.
                     // in this method the response status is having error and
                     // we are setting text to TextView as invalid pincode.
                     pinCodeDetailsTV.setText("Pin code is not valid.");
                 } else {
                     // if the status is success we are calling this method
                     // in which we are getting data from post office object
                     // here we are calling first object of our json array.
                     JSONObject obj = postOfficeArray.getJSONObject(0);


                     // state and country from our data.

                     String state = obj.getString("State");
                     String country = obj.getString("Country");
                     Toast.makeText(MainActivity.this, "Valid", Toast.LENGTH_SHORT).show();
                     // after getting all data we are setting this data in
                     // our text view on below line.
                     pinCodeDetailsTV.setText(  "State : " + state + "\n" + "Country : " + country);
                 }
             } catch (JSONException e) {
                 // if we gets any error then it
                 // will be printed in log cat.
                 e.printStackTrace();

                 pinCodeDetailsTV.setText("Pin code is not valid");
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 // below method is called if we get
                 // any error while fetching data from API.
                 // below line is use to display an error message.
                 Toast.makeText(MainActivity.this, "Pin code is not valid.", Toast.LENGTH_SHORT).show();
                 pinCodeDetailsTV.setText("Pin code is not valid");
             }
         });
         // below line is use for adding object
         // request to our request queue.
         queue.add(objectRequest);
     }

 }
