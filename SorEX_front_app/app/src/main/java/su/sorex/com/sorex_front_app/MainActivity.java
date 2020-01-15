package su.sorex.com.sorex_front_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import cz.msebera.android.httpclient.Header;
import su.sorex.com.sorex_front_app.FragmentsClasses.AddOfferFragment;
import su.sorex.com.sorex_front_app.FragmentsClasses.AllOffersFragment;
import su.sorex.com.sorex_front_app.FragmentsClasses.InfosFragment;
import su.sorex.com.sorex_front_app.FragmentsClasses.MyOffersFragment;
import su.sorex.com.sorex_front_app.FragmentsClasses.TransactionsFragment;
import su.sorex.com.sorex_front_app.FragmentsClasses.WalletFragment;
import su.sorex.com.sorex_front_app.MyExceptions.BalanceNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.IdNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.NoWalletFoundException;
import androidx.preference.PreferenceManager;
import su.sorex.com.sorex_front_app.MyExceptions.PasswordNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.UsernameNotFound;
import su.sorex.com.sorex_front_app.Utils.HTTPRequestHandler;
import su.sorex.com.sorex_front_app.Utils.PrefManager;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    //private String walletAddr;
    public static final String WALLET_ADDR_NAME = "wallet_addr";
    public static final String PREF_NAME = "sp_file_name";
    private View hView;
    public static int SPASH_TIME_OUT = 2000;
    private ProgressBar pbUdateBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HTTPRequestHandler.init(this);
        // Setting up the Shared Preferences Manager
        PrefManager.init(this);

        // Retrieving all of the layout's components
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView nv = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        hView = nv.getHeaderView(0);
        pbUdateBalance = hView.findViewById(R.id.pb_update_balance_drawer);



        // Setting up hView
        nv.setNavigationItemSelectedListener(this);
        // Whenever you open the drawer it updates
        ActionBarDrawerToggle toggler = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                pbUdateBalance.setVisibility(View.VISIBLE);
                TextView tvAddr = hView.findViewById(R.id.toolbar_wallet_addr);
                final TextView tvWalletCurrency = hView.findViewById(R.id.toolbar_wallet_currency);
                tvWalletCurrency.setText("-");
                try {
                    String addr = PrefManager.getInstance().getUsername();
                    tvAddr.setText(addr);
                    int balance = PrefManager.getInstance().getBalance();
                    tvWalletCurrency.setText(Integer.toString(balance));
                }catch (UsernameNotFound | BalanceNotFound usernameNotFound) {
                    //usernameNotFound.printStackTrace();
                }
                try {
                    String urlGetBalance = HTTPRequestHandler.BASE_URL +"/wallet/balance/get?id=" + PrefManager.getInstance().getId() + "&password="+ PrefManager.getInstance().getPassword();

                    new AsyncHttpClient().get(urlGetBalance,  new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            tvWalletCurrency.setText(new String(responseBody));
                            PrefManager.getInstance().saveBalance(Integer.parseInt(new String(responseBody)));
//                            Toast.makeText(MainActivity.this, "Retrieved balance", Toast.LENGTH_SHORT).show();
                            pbUdateBalance.setVisibility(GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            System.out.println(new String(responseBody));
                            tvWalletCurrency.setText("error");
                            pbUdateBalance.setVisibility(GONE);
                            Toast.makeText(MainActivity.this, "Error while retrieving balance..." + new String(responseBody), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IdNotFound | PasswordNotFound idNotFound) {
                    idNotFound.printStackTrace();
                }


            }
        };
        drawer.addDrawerListener(toggler);
        toggler.syncState();

        // Default fragments displayed
        if (savedInstanceState == null) {
            String username = null;
            WalletFragment walletFragment = new WalletFragment();
            try {
                username = PrefManager.getInstance().getUsername();
            } catch (UsernameNotFound idNotFound) {
                Bundle b = new Bundle();
                b.putString("username", username);
                walletFragment.setArguments(b);
            }
            setTitle("Wallet");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    walletFragment).commit();

        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_wallet:
                String username= null;
                try {
                    username = PrefManager.getInstance().getUsername();
                } catch (UsernameNotFound usernameNotFound) {
                    usernameNotFound.printStackTrace();
                } finally {
                    Bundle b = new Bundle();
                    b.putString("username", username);
                    WalletFragment walletFragment = new WalletFragment();
                    walletFragment.setArguments(b);
                    setTitle("Wallet");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            walletFragment).commit();
                }
                break;

            case R.id.nav_all_offers:
                if(!isAllowed()){
                    Toast.makeText(this, "You must create a wallet before.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                setTitle("All items");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AllOffersFragment()).commit();
                break;
            case R.id.nav_del_wall:
                if(!isAllowed()){
                    Toast.makeText(this, "You must create a wallet before.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                clearWallet();
                break;

            case R.id.nav_infos:
                setTitle("Info");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfosFragment()).commit();
                break;

            case R.id.nav_add_offer:
                if(!isAllowed()){
                    Toast.makeText(this, "You must create a wallet before.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                setTitle("Add item");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddOfferFragment()).commit();
                break;

            case R.id.nav_my_offers:
                if(!isAllowed()){
                    Toast.makeText(this, "You must create a wallet before.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                setTitle("My items");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyOffersFragment()).commit();
                break;
            case R.id.nav_consult_reserved:
                if(!isAllowed()){
                    Toast.makeText(this, "You must create a wallet before.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                setTitle("My Loans");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TransactionsFragment());
                break;

        }
        onBackPressed();
        return true;
    }


    private void saveWalletAddr(String addr) {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor e = sp.edit();
        e.putString(WALLET_ADDR_NAME, addr);
        e.commit();
    }

    public void clearWallet() {
        PrefManager.getInstance().clear();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new WalletFragment()).commit();
    }

    public boolean isAllowed() {
        try{
            PrefManager.getInstance().getId();
//            Toast.makeText(this, PrefManager.getInstance().getId(), Toast.LENGTH_SHORT).show();
            return true;
        } catch (IdNotFound idNotFound) {
            idNotFound.printStackTrace();
            return false;
        }
    }

    public void updateDrawer() {

    }
}
