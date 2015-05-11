package com.grupp3.projekt_it;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class Login extends ActionBarActivity {

    //instance variabels needed to be able to access
    //the .NET webservice with the Subscription Number API
    private static final String NAMESPACE = "http://api.bm-data.com";
    private static final String URL = "http://api.bm-data.com/services/bm400/1.3/SubscriptionAPI.asmx?wsdl";
    private static final String SOAP_ACTION = "http://api.bm-data.com/GetSubscriptionStatus";
    private static final String METHOD_NAME = "GetSubscriptionStatus";
    protected EditText mUserLogin;
    String acceptedUsers[] = {"1234", "4321"};
    String TAG = "com.grupp3.projekt_it";
    String user;
    int timeout=60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // hide the actionbar in this activity
        getSupportActionBar().hide();

        mUserLogin = (EditText) findViewById(R.id.user_login_info);
        // define the button

        ImageButton button = (ImageButton)findViewById(R.id.login_button);


        //Auto-fill EditText
        Context context = getBaseContext();
        String premNum = getDefaults("premKey", context);


        if (premNum != null) {
            mUserLogin.setText(premNum);

        }


        // wait for the button to be clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get text from textbox
                user = mUserLogin.getText().toString().trim();
                //call method for login save response
                new LoginTask().execute(user);
                //boolean serviceResponse = doLogin(user);

               /* // check if the entered information is a valid user, if not show error message
                if (Arrays.asList(acceptedUsers).contains(user)) {
                    // Save valid premnumber

                    Context context = getBaseContext();
                    setDefaults("premKey", user, context);


                    // Change activity and send the user number to be used in other activities
                    Intent i = new Intent(Login.this, MainActivity.class);
                    //i.putExtra("user", user);
                    startActivity(i);
                } else {
                    Toast.makeText(Login.this, "Ogiltigt prenumerationsnummer", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    private boolean doLogin(String subscriptionNr){
        boolean result = false;
        //SoapObject request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        Log.i(TAG, request.toString());
        //add property to request that when user enters e.g. their SubNr
        request.addProperty("serviceID", "BT_KTH");
        request.addProperty("subscriptionNr", subscriptionNr);
        Log.i(TAG, "1: " + request.toString());
        //specify which version of soap to be used for serial communication
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        Log.i(TAG, "2:" + soapEnvelope.toString());
        //to make sure that object are not null, need not for e.g. php but for .NET services
        soapEnvelope.dotNet = true;
        //pass everything to the soap envelope
        soapEnvelope.setOutputSoapObject(request);
        Log.i(TAG, "3: " + soapEnvelope.toString());
        //passing in URL
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, timeout);
        Object response = null;
        Log.i(TAG, "4: "+ androidHttpTransport.requestDump);
        androidHttpTransport.debug = true;
        try {
            //this is the actual part that will call the webservice
            androidHttpTransport.call(SOAP_ACTION, soapEnvelope);
            Log.i(TAG, "5: " + androidHttpTransport.requestDump);
            // Get the response from the envelope body.
            response = (Object)soapEnvelope.getResponse();
            String responseResult = response.toString();


            //log response
            Log.i(TAG, "6: Server svar: " + responseResult);

            //if user subscription is active
            //return result equals true
            if (responseResult.contains("IsActive=true")){
                result = true;
                Log.i(TAG, "7: IsActive=true: " + responseResult);

            }
            //if user subscription is inactive
            //return result false
            else if(responseResult.contains("IsActive=false")){
                result = false;
                Log.i(TAG, "8: IsActive=false: " + responseResult);

            }
            //if user subscription is unknown
            //return result false
            else {
                result = false;
                Log.i(TAG, "9: IsActive=false && IsFound=false: " + responseResult);

            }
            // this else statement handle the other response code from .NET service
            //SubscriptionNrFormatNotValid or DatabaseNotValid or
            // ServiceIdNotValid or CommunicationError or PostalNumberIncorrect or
            // ProductNumberIncorrect or Bundled or SingleCopy

        } catch (Exception e) {
            e.printStackTrace();
        }
        // returns either true or false
        //for later processing
        Log.i(TAG, "10: return: " + result);
        return result;



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // on back button click, exit the application, hence skip the login screen
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
    //create new AsyncTask for connection to .NET service in SubNr API
    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        //create dialog for Login task
        private final ProgressDialog dialog = new ProgressDialog(Login.this);
        private boolean response = true;
         //method before execute on main thread
        //displays message for login dialog
        protected void onPreExecute(){

            this.dialog.setMessage("Loggar in...");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... subscriptionNrs) {
            response = doLogin(subscriptionNrs[0]);
            Log.i(TAG, "11: response: " + response);
            //don't interact with UI
            //everything is done on the new thread
            return response;

        }
        @Override
        // pass result back to main thread
        protected void onPostExecute(Boolean response){
            Log.i(TAG, "12: här: " + response);


            // check if the entered information is a valid user, if not show error message
            //Case 1: if users subscriptions is Active
            //Now false=true as there is problems with the subscriptionNrs
            //TODO: Change false in first if statement to true when
            //TODO: and vice versa for else-if correct subscriptionNrs are given

            if (response == false) {

                // Save valid prem number in settings
                Context context = getBaseContext();
                setDefaults("premKey", user, context);

                // Change activity and send the user number to be used in other activities
                Intent i = new Intent(Login.this, MainActivity.class);
                //start the MainActivity after successful login
                startActivity(i);
                //close dialog if is showing
                /*if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }*/
                Log.i(TAG, "13: startActivity: " + MainActivity.class.toString());
            }
            //Case 2: Users subscriptions is inactive
            else if(response == true) {

                Toast.makeText(Login.this,
                        "Prenumerationen har utgått, " +
                                "vänligen förnya innan du kan logga in igen ",
                        Toast.LENGTH_SHORT).show();
                Log.i(TAG, "14: don't startActivity ");

                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
            }
            //Case 3: Handle all other cases
            ////SubscriptionNrFormatNotValid or DatabaseNotValid or
            // ServiceIdNotValid or CommunicationError or PostalNumberIncorrect or
            // ProductNumberIncorrect or Bundled or SingleCopy
            else {

                Toast.makeText(getApplicationContext(),
                        "Ogilitigt  eller inaktivt konto, testa igen eller kontakta supporten",
                        Toast.LENGTH_LONG).show();

                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
            }
        }
            //close dialog


    }

}
