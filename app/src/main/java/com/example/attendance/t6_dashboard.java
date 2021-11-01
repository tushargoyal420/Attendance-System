package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//import android.widget.Toolbar;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class t6_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout tdrawerLayout;
    private NavigationView tnavigationView;
    private Toolbar tdashboardtoolbar;
    private TextView textView;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t6_dashboard);

        tdrawerLayout = findViewById(R.id.drawer_layout);
        tnavigationView = findViewById(R.id.navigationView);
        tdashboardtoolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.textView);
        setSupportActionBar(tdashboardtoolbar);
        tdashboardtoolbar.setTitle("Dashboard");
        fAuth = FirebaseAuth.getInstance();

        tnavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, tdrawerLayout,tdashboardtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        tdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        to make navigation clickable
        tnavigationView.setNavigationItemSelectedListener(this);
        tnavigationView.setCheckedItem(R.id.nav_dashboard);
    }

    @Override
    public void onBackPressed() {
        if (tdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            tdrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dashboard: {
                startActivity(new Intent(getApplicationContext(), t6_dashboard.class));
                break;
            }case R.id.nav_logout: {
                fAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), t2_login_signup_choice.class));
                break;
            }case R.id.nav_setting: {
                startActivity(new Intent(getApplicationContext(), t8_setting.class));
                break;
            }
            case R.id.nav_profile: {
                startActivity(new Intent(getApplicationContext(), t7_profile.class));
                break;
            }
        }
        return true;
    }

}