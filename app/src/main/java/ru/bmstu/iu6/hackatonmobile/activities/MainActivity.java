package ru.bmstu.iu6.hackatonmobile.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SwitchCompat;
import android.text.format.Time;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.service.ArmaRssiFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import ru.bmstu.iu6.hackatonmobile.R;
import ru.bmstu.iu6.hackatonmobile.database.RequirementAdapter;
import ru.bmstu.iu6.hackatonmobile.database.RequirementHelper;
import ru.bmstu.iu6.hackatonmobile.network.BeaconDispatcher;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {

    private static final int NOTIFY_ID = 101;

    private CharSequence navDrawerTitle;
    private BeaconManager beaconManager;
    private ArrayList<Beacon> beaconArray;
    private HashMap<Beacon, Date> beaconArrayRecently;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создаем тулбар
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navDrawerTitle = getTitle();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle navActionBarToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Вызывается, когда drawer установился в польностью закрытое состояние. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(navDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Вызывается, когда drawer установился в польностью открытое состояние. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(navDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };

        drawer.setDrawerListener(navActionBarToggle);
        navActionBarToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CategoryActivity.class);
                startActivity(intent);
            }
        });

        configBeaconManager();
    }

    @Override
    public void onResume() {
        super.onResume();

        RequirementHelper requirementHelper = new RequirementHelper(this);
        Cursor cursor = requirementHelper.getModelCursor();

        ListView listView = (ListView) findViewById(R.id.listView);
        RequirementAdapter requirementAdapter = new RequirementAdapter(this, cursor, 0);
        listView.setAdapter(requirementAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_main_drawer_settings : {
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }
            case R.id.nav_main_drawer_subscr : {
                startActivity(new Intent(this, CategoryActivity.class));
                break;
            }
            default : {
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // метод, который выполняет при коннекте к сервису маяков
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, org.altbeacon.beacon.Region region) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String UUID;
                        int Major;
                        int Minor;
                        if (!beacons.isEmpty()) {
                            for ( Beacon tempBeacon : beacons) {
                                // здесь получаем параметры маячка каждого
                                UUID = tempBeacon.getId1().toString();
                                Major = tempBeacon.getId2().toInt();
                                Minor = tempBeacon.getId3().toInt();

                                BeaconDispatcher beaconDispatcher = new BeaconDispatcher(getBaseContext());
                                beaconDispatcher.dispatchBeacon(UUID, Major, Minor);
                            }
                            Context context = getApplicationContext();
                            String bigText = "Большая строка большая строка " +
                                    "Большая строка большая строка " +
                                    "Большая строка большая строка " +
                                    "Большая строка большая строка " +
                                    "Большая строка большая строка " +
                                    "Большая строка большая строка " +
                                    "Большая строка большая строка " +
                                    "Большая строка большая строка " +
                                    "Большая строка большая строка ";

                            Intent notificationIntent = new Intent(context, MainActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(context,
                                    0, notificationIntent,
                                    PendingIntent.FLAG_CANCEL_CURRENT);

                            Resources res = context.getResources();
                            Notification.Builder builder = new Notification.Builder(context);

                            builder.setContentIntent(contentIntent)
                                    .setSmallIcon(R.drawable.ic_menu_manage)
                                            // большая картинка
                                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_menu_manage))
                                            //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                                    .setTicker("Последнее китайское предупреждение!")
                                    .setWhen(System.currentTimeMillis())
                                    .setDefaults(Notification.DEFAULT_ALL)

                                    .setAutoCancel(true)
                                            //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                                    .setContentTitle("Напоминание")
                                            //.setContentText(res.getString(R.string.notifytext))
                                    .setContentText("Пора покормить кота"); // Текст уведомления

                            // Notification notification = builder.getNotification(); // до API 16
//                            Notification notification = builder.build();
                            Notification notification = new Notification.BigTextStyle(builder)
                                    .bigText(bigText).build();


                            NotificationManager notificationManager = (NotificationManager) context
                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(NOTIFY_ID, notification);

                        }
                    }
                });
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new org.altbeacon.beacon.Region("Server", null, null, null));
        } catch (RemoteException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    /** секция, отвечающая за собственные функции **/

    // конфигурируем beaconmanager
    private void configBeaconManager() {
        // получаем инстанс для данного приложения
        beaconManager = BeaconManager.getInstanceForApplication(this);

        BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        beaconManager.setForegroundScanPeriod(600);
        beaconManager.setBackgroundScanPeriod(600);

        // добавляем парсер iBeacon'ов
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // When binding to the service, we return an interface to our messenger for sending messages to the service.
        beaconManager.bind(this);
    }
}
