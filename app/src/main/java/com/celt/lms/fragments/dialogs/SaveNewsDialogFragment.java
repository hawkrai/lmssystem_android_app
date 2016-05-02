package com.celt.lms.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.celt.lms.R;
import com.celt.lms.api.ApiFactory;
import com.celt.lms.api.ApiLms;
import com.google.gson.JsonElement;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;

import java.io.IOException;

public class SaveNewsDialogFragment extends DialogFragment {
    private EditText editTitle;
    private EditText editText;
    private CheckBox editDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_news, null);

        editTitle = (EditText) view.findViewById(R.id.titleNews);
        editText = (EditText) view.findViewById(R.id.textNews);
        editDate = (CheckBox) view.findViewById(R.id.checkBoxDate);

        builder.setView(view);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = editTitle.getText().toString();
                    String body = "<p>" + editText.getText().toString() + "</p>";
                    boolean isDate = editDate.isChecked();

                    ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (cm.getActiveNetworkInfo() != null) {
                        new CallTask().execute(createJsonObjectNews(title, body, isDate));
                    } else
                        Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private JSONObject createJsonObjectNews(String title, String body, boolean isDate) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("subjectId", String.valueOf(2014));
            jsonObject.put("id", "0");
            jsonObject.put("title", title);
            jsonObject.put("body", body);
            jsonObject.put("isOldDate", isDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private class CallTask extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected String doInBackground(JSONObject... params) {

            ApiLms api = ApiFactory.getService();
            try {
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params[0].toString());
                Call<JsonElement> call = api.addNews(body);
                JsonElement json = call.execute().body();
                if (json != null)
                    return (new JSONObject(json.toString()).optString("Message"));
                else
                    return null;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null)
                result = getResources().getString(R.string.network_error);
            else
                dismiss();
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}

