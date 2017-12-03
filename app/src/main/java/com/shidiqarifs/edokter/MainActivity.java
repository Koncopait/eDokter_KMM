package com.shidiqarifs.edokter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.shidiqarifs.edokter.AccountFragment;
import com.shidiqarifs.edokter.Helper.UserSessionManager;
import com.shidiqarifs.edokter.HomeFragment;
import com.shidiqarifs.edokter.R;

import java.util.HashMap;

/**
 * Created by shidiqarifs on 31/10/2017.
 */

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    UserSessionManager session;
    String status ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new UserSessionManager(getApplicationContext());

        if(session.checkLogin())
            finish();
        if (session.checkLogin()){
            session.logoutUser();
        }else{
            HashMap<String, String> user = session.getUserDetails();
            status = user.get(UserSessionManager.KEY_USER);
            int status_user = Integer.parseInt(status);
        }
        if (status.equals("1")){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, HomeFragment.newInstance())
                        .commit();
        }else if (status.equals("0")){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, Pasien_HomeFragment.newInstance())
                        .commit();
            }





        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                if (status.equals("1")){
                    fragment = HomeFragment.newInstance();
                    break;
                }else {
                    fragment = Pasien_HomeFragment.newInstance();
                    break;
                }

            case R.id.navigation_account:
                fragment = AccountFragment.newInstance();
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();

        return false;
    }

}