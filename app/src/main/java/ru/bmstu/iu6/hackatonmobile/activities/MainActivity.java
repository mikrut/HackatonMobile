package ru.bmstu.iu6.hackatonmobile.activities;

import android.app.Activity;
import android.content.Intent;
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


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {

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

                break;
            }
            case R.id.nav_main_drawer_subscr : {
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
                        filterBeacons(beacons);

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
        beaconManager.setForegroundScanPeriod(1000);
        beaconManager.setBackgroundScanPeriod(1000);

        // добавляем парсер iBeacon'ов
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // When binding to the service, we return an interface to our messenger for sending messages to the service.
        beaconManager.bind(this);
    }

    private void filterBeacons(Collection<Beacon> beacons) {

        //TODO фильтрация Beacon по времении (если было в течение последних n часов, то не отправляем на сервер)
        //TODO изменить возвращаемое значение на массив beacons
    }

    private void pushToServer() {
        //TODO отправка на сервер
    }
}
