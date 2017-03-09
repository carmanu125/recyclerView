package com.jcd.reciclerview.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jcd.reciclerview.Main2Activity;
import com.jcd.reciclerview.R;
import com.jcd.reciclerview.entity.User;

import java.util.ArrayList;

/**
 * Created by Argosoft03 on 28/02/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ContentHolder>{

    public static  ArrayList<User> listUser;

    public RecyclerAdapter(ArrayList<User> listUser) {
        this.listUser = listUser;
    }

    @Override
    public RecyclerAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new ContentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ContentHolder holder, int position) {

        User user = listUser.get(position);
        holder.bindUser(user);

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public static class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName;
        private TextView txtDir;

        public ContentHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDir = (TextView) itemView.findViewById(R.id.txtDir);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Context context = itemView.getContext();
            Intent intent = new Intent(context, Main2Activity.class);
            context.startActivity(intent);

            Toast.makeText(context, "usuario: " + listUser.get(this.getLayoutPosition()).getName()
                    , Toast.LENGTH_SHORT).show();
        }

        public void bindUser(User user){

            txtName.setText(user.getName());
            txtDir.setText(user.getDir());
        }
    }
}
