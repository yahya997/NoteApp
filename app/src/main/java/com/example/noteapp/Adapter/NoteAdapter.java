package com.example.noteapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.editNoteActivity;
import com.example.noteapp.Interface.ItemClickListener;
import com.example.noteapp.Model.NoteModel;
import com.example.noteapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {
    Context context;
    List<NoteModel> noteList;

    public NoteAdapter(Context context, List<NoteModel> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_note_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteAdapter.MyViewHolder holder, int position) {
        //Glide.with(context).load(categoryList.get(position).getImage()).into(holder.category_image);
        holder.txtTitle.setText(noteList.get(position).getTitle());
        holder.txtNote.setText(noteList.get(position).getNote());
        holder.txtTime.setText(noteList.get(position).getDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, editNoteActivity.class);
                intent.putExtra("title",noteList.get(position).getTitle());
                intent.putExtra("note",noteList.get(position).getNote());
                intent.putExtra("noteId",noteList.get(position).getNoteId());
                intent.putExtra("date",noteList.get(position).getDate());
                intent.putExtra("categoryId",noteList.get(position).getCategoryId());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtNote)
        TextView txtNote;
        @BindView(R.id.txtTime)
        TextView txtTime;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
}
