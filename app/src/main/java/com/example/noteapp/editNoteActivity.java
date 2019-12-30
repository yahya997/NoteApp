package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteapp.Model.NoteModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class editNoteActivity extends AppCompatActivity {

    private DatabaseReference reference ;
    private String categoryId="";
    private String noteId ="";
    private Calendar c;
    String formattedDate;

    Unbinder unbinder;
    @BindView(R.id.txtTitle)
    EditText txtTitle;
    @BindView(R.id.txtNote)
    EditText txtNote;
    @BindView(R.id.btnSaveNote)
    Button btnSaveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        unbinder = ButterKnife.bind(this);
        reference = FirebaseDatabase.getInstance().getReference("Notes");
        //date
         c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         formattedDate = df.format(c.getTime());

        //Get Intent
        if (getIntent() !=null) {
            categoryId = getIntent().getStringExtra("categoryId");
            noteId = getIntent().getStringExtra("noteId");
            txtTitle.setText(getIntent().getStringExtra("title"));
            txtNote.setText(getIntent().getStringExtra("note"));
        }
        if (!categoryId.isEmpty() && categoryId != null) {
            btnSaveNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editNoteToFirebase(categoryId , noteId);
                }
            });
        }
    }

    private void editNoteToFirebase(final String categoryId, String noteId) {
        NoteModel noteModel = new NoteModel();
        noteModel.setCategoryId(categoryId);
        noteModel.setTitle(txtTitle.getText().toString());
        noteModel.setNote(txtNote.getText().toString());
        noteModel.setDate(formattedDate);
        reference.child(noteId).setValue(noteModel).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editNoteActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(editNoteActivity.this,MainActivity.class);
                    //intent.putExtra("CategoryId",categoryId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
