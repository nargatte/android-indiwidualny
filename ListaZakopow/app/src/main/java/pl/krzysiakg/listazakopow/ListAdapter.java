package pl.krzysiakg.listazakopow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    public MainActivity mainActivity;

    public ListAdapter(List<ListItem> products) {
        this.products = products;
    }

    private List<ListItem> products;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageButton deleteButton;
        public ImageButton editButton;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);

            deleteButton = view.findViewById(R.id.deleteButton);
            editButton = view.findViewById(R.id.editButton);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ListItem item = products.get(position);
        holder.name.setText(item.getName());

        View.OnClickListener deleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<Void, Void, Void>(){
                    ListItem li;
                    @Override
                    protected void onPreExecute() {
                        li = products.get(position);
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        mainActivity.db.listItemDao().delete(li);
                        return null;
                    }
                }.execute();

                products.remove(position);
                ListAdapter.this.notifyDataSetChanged();
            }
        };

        holder.deleteButton.setOnClickListener(deleteListener);

        View.OnClickListener editListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = products.get(position);
                Intent i = new Intent(mainActivity, AddEditItem.class);
                i.putExtra("requestCode", MainFragment.MODIFY);
                i.putExtra("number", position);
                i.putExtra("name", item.getName());
                mainActivity.startActivityForResult(i, MainFragment.MODIFY);
            }
        };

        holder.editButton.setOnClickListener(editListener);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        int size = Integer.parseInt(sharedPref.getString("text_size", "12"));
        int color = Color.parseColor(sharedPref.getString("text_color", "black"));

        holder.name.setTextColor(color);
        holder.name.setTextSize(size);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


}
