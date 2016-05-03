package com.celt.lms.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.celt.lms.R;
import com.celt.lms.api.ApiFactory;
import com.celt.lms.api.ApiLms;
import com.celt.lms.dto.NewsDTO;
import com.celt.lms.dto.ParsingJsonLms;
import com.celt.lms.fragments.dialogs.SaveNewsDialogFragment;
import com.google.gson.JsonElement;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.Snackbar.make;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> implements ListAdapter {

    private List data = null;
    private FragmentActivity fragmentActivity;
    public NewsListAdapter(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.data = new ArrayList<NewsDTO>();
    }

    public NewsListAdapter(List<NewsDTO> data) {
        this.data = data;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        final NewsDTO item = (NewsDTO) data.get(position);
        if (!item.getTitle().isEmpty()) {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(item.getTitle());
        } else {
            holder.title.setVisibility(View.GONE);
        }
        holder.body.setText(Html.fromHtml(item.getBody().replace("<\\/p>", "").replace("<p>", "")));
        holder.date.setText(item.getDate());

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make(holder.cardView, "Share. Position:" + String.valueOf(position), Snackbar.LENGTH_SHORT).show();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("subjectId", item.getSubjectId());
                args.putInt("idNews", item.getNewsId());
                args.putBoolean("is", true);
                args.putString("title", item.getTitle());
                args.putString("text", item.getBody());

                SaveNewsDialogFragment df = (new SaveNewsDialogFragment());
                df.setArguments(args);
                df.show(fragmentActivity.getSupportFragmentManager(), "dialog");
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
                builder.setTitle(R.string.remove_news_question)
                        .setMessage(item.getTitle())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new CallTask().execute(createJsonObjectNews(item.getSubjectId(), item.getNewsId()));
                            }
                        })
                        .setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

            class CallTask extends AsyncTask<JSONObject, Void, String> {

                @Override
                protected String doInBackground(JSONObject... params) {

                    ApiLms api = ApiFactory.getService();
                    try {
                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params[0].toString());
                        Call<JsonElement> call = api.deleteNews(body);
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
                        result = fragmentActivity.getString(R.string.network_error);
                    else {
                        int pos = data.indexOf(item);
                        data.remove(pos);
                        notifyItemRemoved(pos);
                    }
                    make(holder.cardView, result, Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private JSONObject createJsonObjectNews(int subjectId, int id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("subjectId", String.valueOf(subjectId));
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView body;
        TextView date;
        ImageButton btnShare;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public NewsViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.body);
            date = (TextView) itemView.findViewById(R.id.date);

            btnShare = (ImageButton) itemView.findViewById(R.id.btnShare);
            btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public List getParse(String s) {
        return ParsingJsonLms.getParseNews(s);
    }

    @Override
    public Call getCall(ApiLms api) {
        return api.getNews(2025);
    }

    @Override
    public void setData(Object data) {
        this.data = (List) data;
    }
}
