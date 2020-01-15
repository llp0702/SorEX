package su.sorex.com.sorex_front_app.FragmentsClasses;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;
import su.sorex.com.sorex_front_app.Adapters.ItemListAdapter;
import su.sorex.com.sorex_front_app.LoanDialog;
import su.sorex.com.sorex_front_app.Models.Item;
import su.sorex.com.sorex_front_app.MyExceptions.IdNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.PasswordNotFound;
import su.sorex.com.sorex_front_app.R;
import su.sorex.com.sorex_front_app.Utils.HTTPRequestHandler;
import su.sorex.com.sorex_front_app.Utils.PrefManager;

public class AllOffersFragment extends Fragment {

    private ListView lv;
    private List<Item> items;
    private EditText search;
    private Spinner spinner;
    private String sortKey;
    private String searchStr;
    private ProgressBar pb;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = v = inflater.inflate(R.layout.fragment_all_offers, container, false);
        lv = v.findViewById(R.id.lv_all_offers);
        pb = v.findViewById(R.id.pb_all_offers);
        pb.setVisibility(View.VISIBLE);
        items = Item.getTemplateList();
        search = v.findViewById(R.id.all_offers_search);
        spinner = v.findViewById(R.id.all_offers_dropdown);

            String urlGetAllOffers = HTTPRequestHandler.BASE_URL + "/items/get/all";
            new AsyncHttpClient().get(urlGetAllOffers, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            System.out.println(new String(responseBody));
                            items = Item.getItemsFromString(new String(responseBody));
                            for (Item i: items) {
                                System.out.println(i.getName());
                            }

                            if(items.size() <= 0) return;;
                            ItemListAdapter adapter = new ItemListAdapter(getContext(), R.layout.row, items);
                            lv.setAdapter(adapter);
                            // Set on lick listener to loan an object
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    final Item item = items.get(i);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", item.getName());
                                    bundle.putString("price", Float.toString(item.getPrice()));
                                    bundle.putString("id", Integer.toString(item.getId()));
                                    LoanDialog dialog = new LoanDialog();
                                    dialog.setArguments(bundle);
                                    dialog.show(getFragmentManager(), null);
                                }
                            });


                            search.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    searchStr = search.getText().toString();
                                    ArrayList<Item> tmp= new ArrayList<>();
                                    for(Item item: items) {
                                        if(item.getName().toLowerCase().contains(searchStr.toLowerCase())) tmp.add(item);
                                        if(Float.toString(item.getPrice()).contains(searchStr.toLowerCase())) tmp.add(item);

                                    }
                                    ItemListAdapter adapter = new ItemListAdapter(getContext(), R.layout.row, tmp);
                                    lv.setAdapter(adapter);
                                }
                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });

                            // Spinner

                            ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(),
                                    R.array.sorts_available,android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapterSpinner);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    lv.invalidate();
                                    final String value = adapterView.getItemAtPosition(i).toString().toLowerCase();
                                    List<Item> tmp = new ArrayList<>();
                                    if(searchStr != null) {
                                        for(Item item: items) {
                                            if(item.getName().toLowerCase().contains(searchStr.toLowerCase())) tmp.add(item);
                                            else if(Float.toString(item.getPrice()).contains(searchStr.toLowerCase())) tmp.add(item);
                                        }
                                    }else{
                                        tmp = items;
                                    }

                                    Collections.sort(tmp, new Comparator<Item>() {
                                        @Override
                                        public int compare(Item item, Item t1) {
                                            if(value.toLowerCase().equals("name")){
                                                return item.getName().compareTo(t1.getName());
                                            }else if(value.toLowerCase().equals("price")){
                                                return Float.compare(item.getPrice(), t1.getPrice());
                                            }else{
                                                if(item.getDateRetour() == null && t1.getDateRetour() != null) return 1;
                                                else if(item.getDateRetour() != null && t1.getDateRetour() == null)return -1;
                                                else return 0;
                                            }
                                        }
                                    });
                                    ItemListAdapter adapter = new ItemListAdapter(getContext(), R.layout.row, tmp);
                                    lv.setAdapter(adapter);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getContext(), "Error while retrieving offers, try again later please.", Toast.LENGTH_SHORT).show();

                        }
                    });
        pb.setVisibility(View.GONE);
        return v;

    }

    public void refreshListView() {
        final ItemListAdapter adapter = new ItemListAdapter(getContext(), R.layout.row, items);
        lv.setAdapter(adapter);
    }

    }
