package su.sorex.com.sorex_front_app.FragmentsClasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import cz.msebera.android.httpclient.Header;
import su.sorex.com.sorex_front_app.MainActivity;
import su.sorex.com.sorex_front_app.MyExceptions.BalanceNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.IdNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.NoWalletFoundException;
import su.sorex.com.sorex_front_app.MyExceptions.PasswordNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.UsernameNotFound;
import su.sorex.com.sorex_front_app.R;
import su.sorex.com.sorex_front_app.Utils.HTTPRequestHandler;
import su.sorex.com.sorex_front_app.Utils.PrefManager;
import su.sorex.com.sorex_front_app.VolleyCallBacks;

public class WalletFragment extends Fragment {

    private String walletAdrr;
    private EditText inputPassword;
    private EditText inputUsername;
    private String idWallet;
    private View v;
    private ProgressBar pb;
    private TextView tv_balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        String username = null;
        try {
            username = PrefManager.getInstance().getUsername();
            System.out.println(username);
        } catch (UsernameNotFound usernameNotFound) {
            usernameNotFound.printStackTrace();
        }
        if (username != null) {
            v = inflater.inflate(R.layout.fragment_wallet, container, false);
            TextView tv_username = (TextView) v.findViewById(R.id.tv_username);
            tv_username.setText(username);
            final TextView tv_balance = v.findViewById(R.id.tv_blance);
            pb = v.findViewById(R.id.pb_update_balance_wallet);
            pb.setVisibility(View.VISIBLE);
            String balance;
            try {
                balance = Integer.toString(PrefManager.getInstance().getBalance());
            } catch (BalanceNotFound balanceNotFound) {
                balance = "-,-";
            }
            tv_balance.setText(balance);
            try {
                String id = PrefManager.getInstance().getId();
                String password = PrefManager.getInstance().getPassword();
                String urlGetBalance = HTTPRequestHandler.BASE_URL + "/wallet/balance/get?id="+ id + "&password=" + password;
                new AsyncHttpClient().get(urlGetBalance, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        tv_balance.setText(new String(responseBody));
                        PrefManager.getInstance().saveBalance(Integer.parseInt(new String(responseBody)));
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            } catch (IdNotFound | PasswordNotFound idNotFound) {
                pb.setVisibility(View.GONE);
                tv_balance.setText("error");
                Toast.makeText(getContext(), "Error while connecting to SorEx, please try again later.", Toast.LENGTH_SHORT).show();
            }

        } else {
            v = inflater.inflate(R.layout.fragment_wallet_no_wall, container, false);
            pb = v.findViewById(R.id.progressBar);
            inputPassword = v.findViewById(R.id.no_wall_input_password);
            inputUsername = v.findViewById(R.id.input_username);
            v.findViewById(R.id.btn_get_wallet).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateFields()) return;
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    pb.setVisibility(View.VISIBLE);


                    String urlCreateWallet = HTTPRequestHandler.BASE_URL + "/wallet/create?password=" + getInputPasswordTxt();
                    new AsyncHttpClient().get(urlCreateWallet, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                System.out.println(new String(responseBody));
//                                Toast.makeText(getContext(), new String(responseBody), Toast.LENGTH_SHORT).show();
                                PrefManager.getInstance().saveId(new String(responseBody));
                                PrefManager.getInstance().saveUsername(getInputUsernameTxt());
                                PrefManager.getInstance().savePassword(getInputPasswordTxt());
                                PrefManager.getInstance().saveBalance(0);
//                                Toast.makeText(getContext(), PrefManager.getInstance().getUsername(), Toast.LENGTH_SHORT).show();
                                Bundle b = new Bundle();
                                b.putString("username", PrefManager.getInstance().getUsername());
                                WalletFragment wf = new WalletFragment();
                                pb.setVisibility(View.GONE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                                Toast.makeText(getContext(), "icic", Toast.LENGTH_SHORT).show();
                                wf.setArguments(b);
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, wf).commit();
                            } catch (UsernameNotFound usernameNotFound) {
//                                Toast.makeText(getContext(), "iciciczrezrz", Toast.LENGTH_SHORT).show();
                                usernameNotFound.printStackTrace();
                                pb.setVisibility(View.GONE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getContext(), "An error happened during the request, please try again.", Toast.LENGTH_LONG).show();
                            pb.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
                }
            });
        }
        return v;
    }


    public boolean validateFields() {
        boolean returned = false;

        if (inputPassword != null && inputUsername != null) {
            if (inputPassword.getText().toString().length() > 0 && inputUsername.getText().toString().length() > 0)
                returned = true;
            else {
                Toast.makeText(getContext(), "Username or Password not valid.", Toast.LENGTH_SHORT).show();
            }
        }
        return returned;
    }

    public String getInputUsernameTxt() {
        return inputUsername.getText().toString();
    }

    public String getInputPasswordTxt() {
        return inputPassword.getText().toString();
    }


}
