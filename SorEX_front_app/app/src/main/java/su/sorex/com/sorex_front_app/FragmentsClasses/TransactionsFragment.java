package su.sorex.com.sorex_front_app.FragmentsClasses;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;
import su.sorex.com.sorex_front_app.MyExceptions.IdNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.PasswordNotFound;
import su.sorex.com.sorex_front_app.R;
import su.sorex.com.sorex_front_app.Utils.HTTPRequestHandler;
import su.sorex.com.sorex_front_app.Utils.PrefManager;

public class TransactionsFragment extends Fragment {

    private ProgressBar pb;
    private ListView lv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trans, container, false);
        pb = v.findViewById(R.id.pb_show_trans);
        lv = v.findViewById(R.id.lv_fragment_trans);
        pb.setVisibility(View.VISIBLE);
        String password = "", id = "";

        try {
            password = PrefManager.getInstance().getPassword();
            id = PrefManager.getInstance().getId();
        } catch (PasswordNotFound | IdNotFound passwordNotFound) {
            passwordNotFound.printStackTrace();
        }

        String urlGetTrans = HTTPRequestHandler.BASE_URL + "/wallet/transaction/get/all?idWallet=" + id + "&password="+password;
        new AsyncHttpClient().get(urlGetTrans, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("ASYNC_QUERY", new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("ASYNC_QUERY", new String(responseBody));
            }
        });

        return v;
    }
}
