package su.sorex.com.sorex_front_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cz.msebera.android.httpclient.Header;
import su.sorex.com.sorex_front_app.Models.Item;
import su.sorex.com.sorex_front_app.MyExceptions.IdNotFound;
import su.sorex.com.sorex_front_app.MyExceptions.PasswordNotFound;
import su.sorex.com.sorex_front_app.R;
import su.sorex.com.sorex_front_app.Utils.HTTPRequestHandler;
import su.sorex.com.sorex_front_app.Utils.PrefManager;

public class ItemMyOfferAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    int mResource;
    ViewGroup parent;

    public ItemMyOfferAdapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String name = getItem(position).getName();
        String desc = getItem(position).getDesc();
        int price = getItem(position).getPrice();
        String type = getItem(position).getType();
        Date ret = getItem(position).getDateRetour();
        String id = Integer.toString(getItem(position).getId());
        this.parent=parent;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = convertView.findViewById(R.id.tv_list_offers_name);
        TextView tvDesc = convertView.findViewById(R.id.tv_desc_my_offer);
        TextView tvPrice = convertView.findViewById(R.id.tv_list_offers_price);
        TextView tvDateRet = convertView.findViewById(R.id.tv_all_offers_dispo);
        TextView tvType = convertView.findViewById(R.id.tv_type);
        Button btn = convertView.findViewById(R.id.btn_delete_item);
        tvName.setText(name);
        tvDesc.setText(desc);
        tvPrice.setText(Integer.toString(price));
        tvType.setText(type);
        if (ret == null) {
            tvDateRet.setText("Available");
            tvDateRet.setTextColor(Color.GREEN);
        } else {
            tvDateRet.setText("Rented");
            tvDateRet.setTextColor(Color.RED);
        }

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {

                    String idWallet = PrefManager.getInstance().getId();
                    String password = PrefManager.getInstance().getPassword();

                    String urlDeleteItem = HTTPRequestHandler.BASE_URL + "/items/remove/id?idWallet=" + idWallet + "&password=" + password + "&id=" + getItem(position).getId();
                    System.out.println(urlDeleteItem);
                    new AsyncHttpClient().get(urlDeleteItem, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(mContext, "Item " + name + " has been deleted successfully.", Toast.LENGTH_SHORT).show();
                            refresh();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                            Toast.makeText(mContext, "An error happened while deleting item " + name + " another may have rented it before it should has deleted.", Toast.LENGTH_SHORT).show();
                            System.out.println(new String(responseBody));
                        }


                    });
                } catch (IdNotFound | PasswordNotFound idNotFound) {
                    idNotFound.printStackTrace();
                }
            }

            ;
        });


        return convertView;
    }

    public void refresh() {
        parent.refreshDrawableState();
    }
}
