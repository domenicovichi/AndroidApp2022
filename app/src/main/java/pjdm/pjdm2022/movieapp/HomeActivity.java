package pjdm.pjdm2022.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {
    private ImageButton ibPref;
    private ImageButton ibSearch;
    private static final String TAG = "DV_PJDM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        ibPref = findViewById(R.id.ibPreferiti);
        ibSearch = findViewById(R.id.ibSearch);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
        ibPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navHostFragment != null) {
                    Log.d(TAG, "onClick: entroooooo");
                    NavController navController = navHostFragment.getNavController();

                    navController.navigateUp(); // to clear previous navigation history
                    navController.navigate(R.id.favoriteFragment);
                }
            }
        });
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navHostFragment != null) {
                    NavController navController = navHostFragment.getNavController();
                    navController.navigateUp();
                    navController.navigate(R.id.searchFragment);
                }
            }
        });
    }



    public void onHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}