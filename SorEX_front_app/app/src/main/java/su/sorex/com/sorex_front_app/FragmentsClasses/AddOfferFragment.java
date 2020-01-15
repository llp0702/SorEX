package su.sorex.com.sorex_front_app.FragmentsClasses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;
import su.sorex.com.sorex_front_app.Models.Item;
import su.sorex.com.sorex_front_app.R;
import su.sorex.com.sorex_front_app.Utils.HTTPRequestHandler;
import su.sorex.com.sorex_front_app.Utils.PrefManager;

public class AddOfferFragment extends Fragment {

    private Button btn;
    private EditText name;
    private EditText price;
    private EditText desc;
    private ProgressBar pb;
    private Spinner sp;
    private List<JSONObject> typesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_offer, container, false);
        name = v.findViewById(R.id.editName);
        price = v.findViewById(R.id.ed_price);
        desc = v.findViewById(R.id.ed_desc);
        btn = v.findViewById(R.id.btnSubmitItem);
        pb = v.findViewById(R.id.pb_add_offer);
        sp = v.findViewById(R.id.spinner_add_offer);
        typesList = new ArrayList<JSONObject>();

        // Init spinner values
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pb.setVisibility(View.VISIBLE);
        fillSpinner();
        pb.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameStr = name.getText().toString();
                String descStr = desc.getText().toString();
                String selectedType = (String) sp.getSelectedItem();
                if (nameStr.length() <= 0) {
                    Toast.makeText(view.getContext(), "Enter a name please.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (descStr.length() <= 0) {
                    Toast.makeText(view.getContext(), "Enter a description please.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedType == null) {
                    Toast.makeText(getContext(), "Please select a type.", Toast.LENGTH_SHORT).show();
                }

                int idType = getTypeIdFromName(selectedType);
                try {
                    int fPrice = Integer.parseInt(price.getText().toString());
                    pb.setVisibility(View.VISIBLE);
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    pb.setVisibility(View.VISIBLE);

                    String id = PrefManager.getInstance().getId();
                    String password = PrefManager.getInstance().getPassword();
                    String urlAddOffer = HTTPRequestHandler.BASE_URL + "/warehouse/item/insert?id=" + id + "&password=" + password + "&title=" + URLEncoder.encode(nameStr , "UTF-8")+"&description=" + URLEncoder.encode(descStr,"UTF-8") + "&idType=" + idType + "&priceByDay=" + fPrice;
                    new AsyncHttpClient().post(urlAddOffer, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            System.out.println(new String(responseBody));
                            pb.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getContext(), "Succes ! Your object was added to the system.", Toast.LENGTH_SHORT).show();
                            // TODO Switch to My Offers
                            getActivity().setTitle("All offers");
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyOffersFragment()).commit();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            System.out.println(new String(responseBody));
                            pb.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getContext(), "Error while trying to add your item, please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }


            }
        });


        return v;
    }

    public void fillSpinner() {
        final String urlGetTypes = HTTPRequestHandler.BASE_URL + "/type/get/all";
        new AsyncHttpClient().get(urlGetTypes, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    List<String> spList = new ArrayList<>();

                    JSONArray array = new JSONArray(new String(responseBody));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        spList.add(o.getString("nom"));
                        typesList.add(o);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, spList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
                System.out.println("err");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).commit();

            }
        });
    }

    public int getTypeIdFromName(String s) {
        for (JSONObject o : typesList) {
            try {
                if (o.getString("nom").equals(s)) {
                    return o.getInt("idType");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
