package zpi.pls.zpidominator2000;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import retrofit2.Retrofit;
import zpi.pls.zpidominator2000.Api.ZpiApiRetrofitClient;
import zpi.pls.zpidominator2000.Api.ZpiApiService;
import zpi.pls.zpidominator2000.Fragments.HomePlanFragment;
import zpi.pls.zpidominator2000.Fragments.OneRoomFragment;
import zpi.pls.zpidominator2000.Fragments.RoomsFragment;
import zpi.pls.zpidominator2000.Model.Rooms;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RoomsFragment.OnRoomSelectedListener,
        OneRoomFragment.OnOneRoomInteractionListener, ZpiApiRetrofitClient.OnApiAddressChangedListener {

    private static final String ONE_ROOM_BACKSTACK_NAME = "oneRoomFragmentBackstackName";
    private static final String HOME_PLAN_BACKSTACK_NAME = "homePlanFragmentBackstackName";

    private DrawerLayout drawerLayout;
    private View drawerView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private Retrofit retrofit;
    private ZpiApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            popEntireBackstack();
            Snackbar.make(view, "Witaj w domku", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", this)
                    .show();
            goToHomePlan();
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.setting_api_address_default);
        String apiAddress = sharedPref.getString(getString(R.string.setting_api_address_key), defaultValue);
        retrofit = new ZpiApiRetrofitClient(apiAddress, this).getRetrofit();
        apiService = retrofit.create(ZpiApiService.class);

        goToHomePlan();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent openSettingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(openSettingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToHomePlan() {
        // Create new fragment and transaction
        Fragment newFragment = new HomePlanFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragmentsContainer, newFragment);
//        transaction.addToBackStack(null);
//        if (fragmentManager.getBackStackEntryCount() == 0) {
//            transaction.addToBackStack(HOME_PLAN_BACKSTACK_NAME);
//        }
        // Commit the transaction
        transaction.commit();

        getDrawerView().setCheckedItem(R.id.nav_home);
    }

    private void goToRooms() {
        // Create new fragment and transaction
        Fragment newFragment = RoomsFragment.newInstance(apiService);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragmentsContainer, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        getDrawerView().setCheckedItem(R.id.nav_rooms);

//        tabLayout.addTab();
    }

    private void goToOneRoom(Rooms.Room item) {
        // Create new fragment and transaction
        Fragment newFragment = OneRoomFragment.newInstance(apiService, item);
//        newFragment.setRetainInstance(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragmentsContainer, newFragment);
        transaction.addToBackStack(ONE_ROOM_BACKSTACK_NAME);

        // Commit the transaction
        transaction.commit();

        tabLayout.setVisibility(View.VISIBLE);
        ((OneRoomFragment) newFragment).setTabLayout(tabLayout);

        getDrawerView().setCheckedItem(R.id.nav_rooms);
    }

    private NavigationView getDrawerView() {
        if (drawerView == null) {
            drawerView = drawerLayout.findViewById(R.id.nav_view);
            return (NavigationView) drawerView;
        } else {
            return (NavigationView) drawerView;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof HomePlanFragment) {
            getDrawerView().setCheckedItem(R.id.nav_home);
        } else if (fragment instanceof RoomsFragment) {
            getDrawerView().setCheckedItem(R.id.nav_rooms);
        }
        super.onAttachFragment(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        popEntireBackstack();
        int id = item.getItemId();

        if (id == R.id.nav_rooms) {
            goToRooms();
            closeDrawers();
            return true;
        } else if (id == R.id.nav_home) {
            goToHomePlan();
            closeDrawers();
            return true;
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void popEntireBackstack() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
//        if (backStackEntryCount > 0) {
//            android.support.v4.app.FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
//            fragmentManager.popBackStack(backStackEntry.getId(), 0);
//        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()).getId(), 0);
    }

    private void closeDrawers() {
        drawerLayout.closeDrawers();
    }

    @Override
    public void onOneRoomFragmentInteraction(Uri uri) {

    }

    @Override
    public void onOneRoomFragmentByeBye() {
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void onRoomListFragmentInteraction(Rooms.Room item) {
        goToOneRoom(item);
    }

    @Override
    public void OnApiAddressChanged() {
        apiService = retrofit.create(ZpiApiService.class);
    }
}
