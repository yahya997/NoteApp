package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteapp.Adapter.MyHomeAdapter;
import com.example.noteapp.Adapter.NoteAdapter;
import com.example.noteapp.Common.Common;
import com.example.noteapp.Model.CategoryModel;
import com.example.noteapp.Model.NoteModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class Notes extends AppCompatActivity {

    Unbinder unbinder;

    private Calendar c;
    String formattedDate;

    List<NoteModel> noteList;
    @BindView(R.id.recyclerNotes)
    RecyclerView recyclerNotes;
    @BindView(R.id.btn_add_note)
    FloatingActionButton btn_add_note;
    private LinearLayoutManager linearLayoutManager;

    NoteAdapter adapter;

    //Firebase
    DatabaseReference reference ;
    String categoryId="";

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();

        reference = FirebaseDatabase.getInstance().getReference("Notes");

        noteList = new ArrayList<>();

        unbinder = ButterKnife.bind(this);

        recyclerNotes.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerNotes.setLayoutManager(linearLayoutManager);

        //date
        c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c.getTime());

        //Get Intent
        if (getIntent() !=null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {
            LoadNotes(categoryId);
        }

        btn_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });

    }

    private void showAddCategoryDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        LayoutInflater inflater =this.getLayoutInflater();
        View add_note_layout= inflater.inflate(R.layout.add_new_note_dialog,null);
        builder.setView(add_note_layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText editTitle=add_note_layout.findViewById(R.id.txtTitle);
        final EditText editNote = add_note_layout.findViewById(R.id.txtNote);
        Button btnSave = add_note_layout.findViewById(R.id.btnSaveNote);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NoteModel noteModel = new NoteModel();
                noteModel.setCategoryId(categoryId);
                noteModel.setTitle(editTitle.getText().toString());
                noteModel.setNote(editNote.getText().toString());
                noteModel.setDate(formattedDate);
                reference.push().setValue(noteModel).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Notes.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Notes.this, "Add Note", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            LoadNotes(categoryId);
                        }
                    }
                });
            }
        });


    }

    private void LoadNotes(final String categoryId) {

        dialog.show();
        reference.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                noteList.clear();
                for(DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    NoteModel model =itemSnapshot.getValue(NoteModel.class);
                    model.setCategoryId(categoryId);
                    model.setNoteId(itemSnapshot.getKey());
                    noteList.add(model);
                }
                adapter = new NoteAdapter(getApplicationContext(),noteList);
                recyclerNotes.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(Notes.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
