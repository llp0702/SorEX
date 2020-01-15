package su.sorex.com.sorex_front_app.FragmentsClasses;

import android.content.AsyncQueryHandler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;
import su.sorex.com.sorex_front_app.Adapters.ItemListAdapter;
import su.sorex.com.sorex_front_app.Adapters.ItemMyOfferAdapter;
import su.sorex.com.sorex_front_app.Models.Item;
import su.sorex.com.sorex_front_app.MyExceptions.IdNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.PasswordNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.UsernameNotFound;
import su.sorex.com.sorex_front_app.R;
import su.sorex.com.sorex_front_app.Utils.HTTPRequestHandler;
import su.sorex.com.sorex_front_app.Utils.PrefManager;

public class MyOffersFragment extends Fragment {

    private ListView lv;
    private ArrayList<String> arrayLoading = new ArrayList<String>();
    private ProgressBar pb;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_offers, container, false);
        pb = v.findViewById(R.id.pb_my_offers);
        lv = v.findViewById(R.id.lv_my_offer);


        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.VISIBLE);
        try {
            String password = PrefManager.getInstance().getPassword();
            String id = PrefManager.getInstance().getId();
            String urlGetMyItems = HTTPRequestHandler.BASE_URL + "/item/list/byId?id=" + id + "&password=" + password;
            new AsyncHttpClient().get(urlGetMyItems, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    Toast.makeText(getContext(), "c bon jai les offres", Toast.LENGTH_SHORT).show();
//                    System.out.println(new String(responseBody));
                    pb.setVisibility(View.GONE);
                    List<Item> items = Item.getItemsFromString(new String(responseBody));
                    ItemMyOfferAdapter adapter = new ItemMyOfferAdapter(getContext(), R.layout.row_my_offer, items);
                    lv.setAdapter(adapter);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getContext(), "Error while fetching your items, please try again later.", Toast.LENGTH_SHORT).show();
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    pb.setVisibility(View.GONE);
                }
            });

            ItemListAdapter adapter = new ItemListAdapter(getContext(), R.layout.row_my_offer, Item.myItems);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        } catch (IdNotFound | PasswordNotFound usernameNotFound) {
            usernameNotFound.printStackTrace();
        }
        return v;
    }






}
