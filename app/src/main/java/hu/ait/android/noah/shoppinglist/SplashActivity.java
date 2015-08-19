package hu.ait.android.noah.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);

         new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intentMain = new Intent();
                intentMain.setClass(SplashActivity.this, ShoppingListActivity.class);
                SplashActivity.this.startActivity(intentMain);
                SplashActivity.this.finish();
            }
        }, 3000);
    }
}