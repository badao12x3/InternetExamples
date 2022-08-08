package com.example.internetexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ItemJSONAdapter extends RecyclerView.Adapter<ItemJSONAdapter.ItemViewHolder> {
    JSONArray jsonArray;

    public ItemJSONAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public ItemJSONAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemJSONAdapter.ItemViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.textName.setText(jsonObject.getString("name"));
            holder.textEmail.setText(jsonObject.getString("email"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView textName;
        TextView textEmail;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.name);
            textEmail = itemView.findViewById(R.id.email);

        }
    }
}
