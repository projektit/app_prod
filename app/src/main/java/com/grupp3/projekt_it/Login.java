package com.grupp3.projekt_it;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
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

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

/*********************************************************************************************
 * This class, is used for Login to the application "Min trädgård"
 * This class and its methods, are implementing Bonniers's SubscriptionAPI and the method
 * GetSubscriptionStatus, with the SOAP 1.1. Protocol. An external libary ksoap2 was used
 * To create an HTTP POST request as described on this website:
 * http://api.bm-data.com/services/bm400/1.3/SubscriptionAPI.asmx?op=GetSubscriptionStatus
 * To be able to login, the user needs an active subscription, this information is fetched from
 * the .NET webservice in the API, and processed, if isActive=true, an new Activity is started
 * "MainActivity to take the user to the home screen. if isActive=false, a toast with info
 * that the subscription is inactive
 * result = 1, -1, 0 corresponds to true, false, other cases e.g. network error.
 * @Throws SocketTimeOutException
 *
 * @author Marcus Elwin
 * @author Daniel Freberg
 * @author Esra Kahraman
 * @author Oscar Melin
 * @author Mikael Mölder
 * @author Erik Nordell
 * @author Felicia Schnell
 *
 **********************************************************************************************/

public class Login extends ActionBarActivity {

    //instance variabels needed to be able to access
    //the .NET webservice with the Subscription Number API
    private static final String NAMESPACE = "http://api.bm-data.com/";
    private static final String URL = "http://api.bm-data.com/services/bm400/1.3/SubscriptionAPI.asmx";
    private static final String SOAP_ACTION = "http://api.bm-data.com/GetSubscriptionStatus";
    private static final String METHOD_NAME = "GetSubscriptionStatus";
    protected EditText mUserLogin;
    String TAG = "com.grupp3.projekt_it";
    String user;
    String serviceId ="BT_KTH";
    //URL connection timeout for 6 seconds
    int timeout=3000;

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
                //Start Async Task for Login save responses
                new LoginTask().execute(user);
                //boolean serviceResponse = doLogin(user);
            }
        });
    }
    //method used in another thread for the SOAP connection
    //and handling of response codes for requests
    private int doLogin(String subscriptionNr) throws SocketTimeoutException{
        int result = -1;
        //SoapObject request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //add property to request that when user enters e.g. their SubNr
        //also needed ServiceID to validate in API
        request.addProperty("serviceID", serviceId);
        request.addProperty("subscriptionNr", subscriptionNr);
        //specify which version of soap to be used for serial communication
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //to make sure that object are not null, need not for e.g. php but for .NET services
        soapEnvelope.dotNet = true;
        //pass everything to the soap envelope
        soapEnvelope.setOutputSoapObject(request);
        //passing in URL starting HTTP POST
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, timeout);
        //empty object to receive response in from request
        Object response = null;
        androidHttpTransport.debug = true;
        Log.i(TAG, "4: "+ androidHttpTransport.requestDump);

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
            //return result equals 1
            if (responseResult.contains("IsActive=true")){
                result = 1;
                Log.i(TAG, "7: IsActive=true: " + responseResult);

            }
            //if user subscription is inactive
            //return result -1
            else if(responseResult.contains("IsActive=false")){
                result = -1;
                Log.i(TAG, "8: IsActive=false: " + responseResult);

            }
            //if user subscription is unknown
            //return result false
            else {
                result = 0;
                Log.i(TAG, "9: IsActive=false && IsFound=false: " + responseResult);

            }

        }
        // Handle Socket TimeoutException
        //by printing Toast on UI thread
        catch(SocketTimeoutException e) {
         //print StackTrace
         e.printStackTrace();
            //set result to 0 to indicate that an network error has occurred
            //that the new activity shall not be started
            result = 0;

        }

        catch (Exception e) {
            e.printStackTrace();
        }
        // returns either true or false
        //for later processing
        Log.i(TAG, "10: return: " + result);
        return result;
    }

    public void showToast(String text, int duration) {
        Toast toast = Toast.makeText(this.getBaseContext(), text, duration);
        toast.show();
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
    //String, as in parameter for doInBackground method
    //Boolean, as in parameter for onPostExecute method
    //Android requires Network operating e.g. HTTP GET/POST, SOAP etc
    //To be performed on another thread for efficiency
    //doInBackground is performed on the new thread
    //onPostExecute is performed on the main UI thread
    private class LoginTask extends AsyncTask<String, Void, Integer> {
        //create dialog for Login task
        private final ProgressDialog dialog = new ProgressDialog(Login.this);
        private int response = 1;
         //method before execute on main thread
        //displays message for login dialog
        protected void onPreExecute(){

            this.dialog.setMessage("Loggar in...");
            this.dialog.show();
        }

        //don't interact with UI
        //everything is done on the new thread
        @Override
        protected Integer doInBackground(String... subscriptionNrs) {
            try {
                response = doLogin(subscriptionNrs[0]);
            } catch (SocketTimeoutException e) {
                Log.i(TAG, "Will this happen? " + response);
                return response;
            }
            Log.i(TAG, "11: response: " + response);
            return response;
        }

        @Override
        // pass result back to main thread
        protected void onPostExecute(Integer response){
            Log.i(TAG, "12: här: " + response);
            // check if the entered information is a valid user, if not show error message
            //Case 1: if users subscriptions is Active
            if (response == 1) {
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
            else if(response == -1 ) {

                Toast.makeText(Login.this,
                        "Prenumerationen har utgått, " + "eller prenumerationsnumret är ogiltigt"+
                                " vänligen förnya, eller testa med annat prenumerationsnumret",
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
            else if(response == 0) {
                Toast.makeText(Login.this,
                        "Problem med att kontakta servern, " +
                                "vänligen kolla er internet uppkoppling och testa igen ",
                        Toast.LENGTH_LONG).show();

                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
            }
        }
    }
}
