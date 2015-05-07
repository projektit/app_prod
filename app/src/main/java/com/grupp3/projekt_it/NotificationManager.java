package com.grupp3.projekt_it;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class NotificationManager extends BaseActivity{

    ListView ls;
    ArrayList<UserNotification> allUserNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_notification_manager);
        getLayoutInflater().inflate(R.layout.activity_notification_manager, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ls = (ListView) findViewById(R.id.notification_listView);
        buildListView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_manager, menu);
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
    public void buildListView() {
        Context context = getApplicationContext();
        GardenUtil gardenUtil = new GardenUtil();
        allUserNotifications = gardenUtil.loadAllUserNotifications(context);
        ArrayAdapter<UserNotification> adapter = new NotificationListAdapter();
        ls.setAdapter(adapter);

    }
    protected void openActivity(int position) {

        /**
         * We can set title & itemChecked here but as this BaseActivity is parent for other activity,
         * So whenever any activity is going to launch this BaseActivity is also going to be called and
         * it will reset this value because of initialization in onCreate method.
         * So that we are setting this in child activity.
         *
         * All activities with navigation drawer must contain this override function in order
         * to not be able to launch another instance of itself from navigation bar.
         * Remove correct startActivity(..) call to do so.
         */

//		mDrawerList.setItemChecked(position, true);
//		setTitle(listArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 0:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, MyGardenListActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, PlantSearchActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, Login.class));
                break;
            case 5:
                startActivity(new Intent(this, Preferences.class));
                break;
            case 6:
                break;
            default:
                break;
        }
    }

    private class NotificationListAdapter extends ArrayAdapter <UserNotification>{
        public NotificationListAdapter(){
            super(NotificationManager.this, R.layout.notification_list_item, allUserNotifications);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View notificationItemView = convertView;
            //check if given view is null, if so inflate a new one
            if(notificationItemView == null){
                notificationItemView = getLayoutInflater().inflate(R.layout.notification_list_item, parent, false);
            }

            UserNotification notification = allUserNotifications.get(position);

            TextView textView1 = (TextView) notificationItemView.findViewById(R.id.text_title);
            textView1.setText(notification.getTitle());

            TextView textView2 = (TextView) notificationItemView.findViewById(R.id.text_text);
            textView2.setText(notification.getText());

            return notificationItemView;
        }
    }
}