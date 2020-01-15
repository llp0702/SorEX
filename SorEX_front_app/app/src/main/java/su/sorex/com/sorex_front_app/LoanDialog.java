package su.sorex.com.sorex_front_app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import cz.msebera.android.httpclient.Header;
import su.sorex.com.sorex_front_app.Models.Item;
import su.sorex.com.sorex_front_app.Utils.HTTPRequestHandler;
import su.sorex.com.sorex_front_app.Utils.PrefManager;

public class LoanDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final String name = getArguments().getString("name");
        float price = Float.parseFloat(getArguments().getString("price"));
        int itemId = Integer.parseInt(getArguments().getString("id"));


        View view = inflater.inflate(R.layout.layout_dialog_loan, null);
        final EditText nbDays = view.findViewById(R.id.dialog_loan_input_days);
        TextView tvName = view.findViewById(R.id.dialog_loan_name);
        tvName.setText(name);
        TextView tvPrice = view.findViewById(R.id.dialog_loan_price);

        tvPrice.setText(Float.toString(price));
        builder.setView(view)
                .setTitle("Loan")
                .setPositiveButton("Loan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            int nbDay = Integer.parseInt(nbDays.getText().toString());
                            String id = PrefManager.getInstance().getId();
                            String username = PrefManager.getInstance().getUsername();
                            String password = PrefManager.getInstance().getPassword();
                            String ret;
                            Date curr = new Date();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(curr);
                            calendar.add(Calendar.DAY_OF_MONTH, nbDay);
                            Date updatedDate = calendar.getTime();
                            Log.i("TOTOTOTO", Item.DATE_FORMAT.format(calendar.getTime()));
                            String urlGetMyItems = HTTPRequestHandler.BASE_URL + "/warehouse/item/rent?walletRenterId=" + id + "&password=" +password+"&itemId="+id+"&dateDeRetour="+ URLEncoder.encode(Item.DATE_FORMAT.format(updatedDate), "UTF-8");
                            new AsyncHttpClient().post(urlGetMyItems, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            Log.i("TOTOTOTOTO", "success");
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            System.out.println(new String(responseBody));
                                        }
                                    });

                        }catch (Exception e){
                            Toast.makeText(getContext(), "Invalid days number.", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }


}
