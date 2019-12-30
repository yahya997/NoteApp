package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.noteapp.Adapter.MyHomeAdapter;
import com.example.noteapp.Common.Common;
import com.example.noteapp.Model.CategoryModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    Unbinder unbinder;
    Context context;

    List<CategoryModel> categoryList;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.recyclerHome)
    RecyclerView recyclerHome;
     GridLayoutManager LayoutManager;
    LayoutAnimationController layoutAnimationController;
    MyHomeAdapter adapter;

    //Firebase
    private FirebaseAuth mAuth;
    DatabaseReference reference ;
    String userId="";

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();

        mAuth = FirebaseAuth.getInstance();
        reference =FirebaseDatabase.getInstance().getReference("Category");
        userId=mAuth.getCurrentUser().getUid();


        categoryList = new ArrayList<>();

        unbinder = ButterKnife.bind(this);

        layoutAnimationController= AnimationUtils.loadLayoutAnimation(this,R.anim.layout_item_from_left);

        recyclerHome.setHasFixedSize(true);
        LayoutManager = new GridLayoutManager(this,2);
        //LayoutManager.setStackFromEnd(true);
       // LayoutManager.setReverseLayout(true);
        recyclerHome.setLayoutManager(LayoutManager);

        getDataFromDatabase(userId);

        //add category
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });


    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater =this.getLayoutInflater();
        View add_category_layout= inflater.inflate(R.layout.add_new_category_loyout,null);

        final EditText edtNameCategory = add_category_layout.findViewById(R.id.edtNameCategory);
        
        builder.setView(add_category_layout);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                CategoryModel model = new CategoryModel();
                model.setFrom(userId);
                model.setName(edtNameCategory.getText().toString());
                reference.push().setValue(model);
                getDataFromDatabase(userId);
                Toast.makeText(MainActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(null);
    }


    private void getDataFromDatabase(String userId) {

        dialog.show();
        reference.orderByChild("from").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                categoryList.clear();
                for(DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    CategoryModel model =itemSnapshot.getValue(CategoryModel.class);
                    model.setCategoryId(itemSnapshot.getKey());
                    categoryList.add(model);
                }
                adapter = new MyHomeAdapter(getApplicationContext(),categoryList);
                recyclerHome.setAdapter(adapter);
                recyclerHome.setLayoutAnimation(layoutAnimationController);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



}
