package su.sorex.com.sorex_front_app.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import su.sorex.com.sorex_front_app.MainActivity;
import su.sorex.com.sorex_front_app.MyExceptions.BalanceNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.IdNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.InstanceNotCreated;
import su.sorex.com.sorex_front_app.MyExceptions.PasswordNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.UsernameNotFound;

public class PrefManager {

    public static String PREF_NAME = MainActivity.PREF_NAME;
    private int PRIVATE_MODE = 0;

    private Context _ctx;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private static PrefManager INSTANCE = new PrefManager();
    private static String USERNAME_PREF_KEY_NAME = "username";
    private static String PASSWORD_PREF_KEY_NAME = "password";
    private static String BALANCE_PREF_KEY_NAME = "balance";


    public static String ID_PREF_KEY_NAME = "id_pref";


    public static  void init(Context ctx) {
        INSTANCE._ctx = ctx;
        INSTANCE.prefs = INSTANCE._ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        INSTANCE.edit = INSTANCE.prefs.edit();
    }

    public static PrefManager getInstance()  {
        return INSTANCE;
    }

    public String getWalletAddr() {
        return INSTANCE.prefs.getString(MainActivity.WALLET_ADDR_NAME, null);
    }

    public void setWalletAddr(String addr) {
        edit.putString(MainActivity.WALLET_ADDR_NAME, addr);
        edit.commit();
    }

    public void saveId(String id){
        edit.putString(ID_PREF_KEY_NAME, id);
        edit.commit();
    }

    public String getId() throws IdNotFound {
        String id = prefs.getString(ID_PREF_KEY_NAME, null);
        if(id == null) {
            throw new IdNotFound("Error while trying to get ID from Shared Preferences.");
        }
        return id;
    }

    public void saveUsername(String username) {
        edit.putString(USERNAME_PREF_KEY_NAME, username);
        edit.commit();
    }

    public String getUsername() throws UsernameNotFound{
        String username = prefs.getString(USERNAME_PREF_KEY_NAME, null);
        if(username == null) {
            throw new UsernameNotFound("Error while trying to get username from Shared Preferences.");
        }
        return username;
    }

    public void savePassword(String pass) {
        edit.putString(PASSWORD_PREF_KEY_NAME, pass);
        edit.commit();
    }

    public String getPassword() throws PasswordNotFound {
        String password = prefs.getString(PASSWORD_PREF_KEY_NAME, null);
        if(password == null) throw new PasswordNotFound("Error while trying to get password from Shared Preferences");
        return password;
    }

    public int getBalance() throws BalanceNotFound {
        int balance = prefs.getInt(BALANCE_PREF_KEY_NAME, 0);
        if(balance == 0) throw new BalanceNotFound("Error while trying to get balance from Shared Preferences.");
        return balance;
    }

    public void saveBalance(int balance) {
        edit.putInt(BALANCE_PREF_KEY_NAME, balance);
        edit.commit();
    }

    public void clear() {
        edit.remove(ID_PREF_KEY_NAME);
        edit.remove(USERNAME_PREF_KEY_NAME);
        edit.remove(BALANCE_PREF_KEY_NAME);
        edit.commit();
    }
}
