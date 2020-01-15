package su.sorex.com.sorex_front_app.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import su.sorex.com.sorex_front_app.Models.Item;
import su.sorex.com.sorex_front_app.R;

public class ItemListAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    int mResource;
    public ItemListAdapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        float price = getItem(position).getPrice();
        Date dateRet = getItem(position).getDateRetour();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = convertView.findViewById(R.id.tv_list_offers_name);
        tvName.setText(name);

        TextView tvPrice = convertView.findViewById(R.id.tv_list_offers_price);
        tvPrice.setText(Float.toString(price));

        TextView tvRet = convertView.findViewById(R.id.tv_all_offers_dispo);
        if (dateRet != null){
            tvRet.setText("Not available");
            tvRet.setTextColor(Color.RED);
        }
        else {
            tvRet.setText("Available");
            tvRet.setTextColor(Color.GREEN);
        }

        return convertView;
    }

}
