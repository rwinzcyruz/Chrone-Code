package com.mikroskil.android.qattend;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.mikroskil.android.qattend.fragment.EventFragment;
import com.mikroskil.android.qattend.fragment.MemberFragment;
import com.mikroskil.android.qattend.fragment.ProfileFragment;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ORG_POS_KEY = "org_pos_key";
    private static String[] sectionMenus;

    private int mOrgPos;
    private int mPosition;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();
        sectionMenus = getResources().getStringArray(R.array.section_menus);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mPosition = position;

        // update the main content by replacing fragments
        if (position == 0) {
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            ProfileFragment fragment = new ProfileFragment();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
        else if (position >= 1 && position <= 3) {
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            EventFragment fragment = new EventFragment();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
        else if (position >= 4 && position <= 6) {
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            MemberFragment fragment = new MemberFragment();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void onSectionAttached(int number) {
        mTitle = sectionMenus[number];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            MenuInflater inflater = getMenuInflater();
            if (mPosition == 0) inflater.inflate(R.menu.profile, menu);
            else if (mPosition >= 1 && mPosition <= 3) inflater.inflate(R.menu.event, menu);
            else if (mPosition >= 4 && mPosition <= 6) inflater.inflate(R.menu.member, menu);
            inflater.inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_event:
                Toast.makeText(this, R.string.action_create_event, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_add_member:
                Toast.makeText(this, R.string.action_add_member, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_create_organization:
                startActivity(new Intent(this, CreateOrganizationActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!writeInstanceState())
            Toast.makeText(this, "Failed to write instance state!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (readInstanceState() && mOrgPos != -1) {
            NavigationDrawerFragment.setSpinnerState(mOrgPos);
        }
    }

    public boolean writeInstanceState() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(ORG_POS_KEY, NavigationDrawerFragment.getSpinnerState());
        return editor.commit();
    }

    public boolean readInstanceState() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mOrgPos = sp.getInt(ORG_POS_KEY, -1);
        return sp.contains(ORG_POS_KEY);
    }

}