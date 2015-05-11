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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class NotificationManager extends BaseActivity{
    Menu menu;
    ListView ls;
    //ArrayList<UserNotification> allUserNotifications;
    ArrayList<Integer> selectedNotifications;
    Boolean onModify = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up navigation drawer
        getLayoutInflater().inflate(R.layout.activity_notification_manager, frameLayout);
        mDrawerList.setItemChecked(position, true);

        ls = (ListView) findViewById(R.id.notification_listView);
        // Build the list view
        selectedNotifications = new ArrayList<>();
        buildListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_manager, menu);
        this.menu = menu;
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
            onModify = true;
            Toast.makeText(NotificationManager.this, "Tryck för att välja notifikation", Toast.LENGTH_LONG).show();
            deleteNotificationView();
            return true;
        }
        if (id == R.id.action_discard){
            if(selectedNotifications == null){
                return true;
            }
            GardenUtil gardenUtil = new GardenUtil();
            for(int notId : selectedNotifications){
                gardenUtil.deleteUserNotification(notId, getApplicationContext());
            }

            selectedNotifications.clear();
            MenuItem discardItem = menu.findItem(R.id.action_discard);
            discardItem.setVisible(false);
            MenuItem newItem = menu.findItem(R.id.action_new);
            newItem.setVisible(true);
            buildListView();
            onModify = false;
            return true;
        }
        if(id == R.id.action_new){
            startActivityForResult(new Intent(this, NewNotificationActivity.class), 0);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if(onModify == true){
            onModify = false;
            MenuItem discardItem = menu.findItem(R.id.action_discard);
            discardItem.setVisible(false);
            MenuItem newItem = menu.findItem(R.id.action_new);
            newItem.setVisible(true);
            buildListView();
            return;
        }else {
            super.onBackPressed();
        }
    }
    // Method for setting up the list view with the users own notifications
    public void buildListView() {
        Context context = getApplicationContext();
        GardenUtil gardenUtil = new GardenUtil();
        final ArrayList <UserNotification> allUserNotifications = gardenUtil.loadAllUserNotifications(context);
        ArrayAdapter<UserNotification> adapter = new NotificationListAdapter(allUserNotifications);
        ls.setAdapter(adapter);
        ls.setOnItemClickListener(null);
    }
    public void deleteNotificationView() {
        MenuItem newItem = menu.findItem(R.id.action_new);
        newItem.setVisible(false);
        MenuItem discardItem = menu.findItem(R.id.action_discard);
        discardItem.setVisible(true);

        final Context context = getApplicationContext();
        final GardenUtil gardenUtil = new GardenUtil();
        final ArrayList<UserNotification> allUserNotifications = gardenUtil.loadAllUserNotifications(context);
        ArrayAdapter<UserNotification> adapter = new DeleteNotificationListAdapter(allUserNotifications);
        ls.setAdapter(adapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserNotification userNotification = allUserNotifications.get(position);
                gardenUtil.deleteUserNotification(userNotification.getId(), context);
                buildListView();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        buildListView();
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
                break;
            case 3:
                startActivity(new Intent(this, PlantSearchActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, Login.class));
                break;
            case 6:
                startActivity(new Intent(this, Preferences.class));
                break;
            default:
                break;
        }
    }

    private class NotificationListAdapter extends ArrayAdapter <UserNotification>{
        ArrayList<UserNotification> allUserNotifications;
        public NotificationListAdapter(ArrayList<UserNotification> allUserNotifications){
            super(NotificationManager.this, R.layout.notification_list_item, allUserNotifications);
            this.allUserNotifications = allUserNotifications;
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

            TextView textView3 = (TextView) notificationItemView.findViewById(R.id.text_notification_date);
            textView3.setText(padding_str(notification.getDay()) + "-" + padding_str(notification.getMonth()+1) + "-" + notification.getYear());

            return notificationItemView;
        }
    }

    private class DeleteNotificationListAdapter extends ArrayAdapter <UserNotification>{
        ArrayList<UserNotification> allUserNotifications;
        public DeleteNotificationListAdapter(ArrayList<UserNotification> allUserNotifications){
            super(NotificationManager.this, R.layout.notification_list_item_delete, allUserNotifications);
            this.allUserNotifications = allUserNotifications;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View notificationItemView = convertView;
            //check if given view is null, if so inflate a new one
            if(notificationItemView == null){
                notificationItemView = getLayoutInflater().inflate(R.layout.notification_list_item_delete, parent, false);
            }

            UserNotification notification = allUserNotifications.get(position);

            TextView textView1 = (TextView) notificationItemView.findViewById(R.id.text_title);
            textView1.setText(notification.getTitle());

            TextView textView2 = (TextView) notificationItemView.findViewById(R.id.text_text);
            textView2.setText(notification.getText());

            final int userNotificationId = allUserNotifications.get(position).getId();

            CheckBox checkBox1 = (CheckBox) notificationItemView.findViewById(R.id.checkBox1);
            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        boolean inList = false;
                        for(int id : selectedNotifications){
                            if(id == userNotificationId) {
                                inList = true;
                                break;
                            }
                        }
                        if(!inList){
                            selectedNotifications.add(userNotificationId);
                        }
                    }else if(!isChecked){
                        boolean inList = false;
                        for(int id : selectedNotifications){
                            if(id == userNotificationId) {
                                inList = true;
                                break;
                            }
                        }
                        if(inList){
                            selectedNotifications.remove(new Integer(userNotificationId));
                        }
                    }
                }
            });

            return notificationItemView;
        }
    }
    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}