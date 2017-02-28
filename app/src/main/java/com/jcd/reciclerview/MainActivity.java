package com.jcd.reciclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jcd.reciclerview.adapters.RecyclerAdapter;
import com.jcd.reciclerview.entity.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    RecyclerAdapter adapter;

    ArrayList<User> listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUser();

        mRecyclerView = (RecyclerView) findViewById(R.id.recicler);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        adapter = new RecyclerAdapter(listUser);
        mRecyclerView.setAdapter(adapter);
    }

    private void loadUser() {

        listUser = new ArrayList<>();

        for(int i = 0; i<20; i++){
            User user = new User();

            user.setName("Name " + i);
            user.setDir("Dir " + i);

            listUser.add(user);
        }

    }
}
