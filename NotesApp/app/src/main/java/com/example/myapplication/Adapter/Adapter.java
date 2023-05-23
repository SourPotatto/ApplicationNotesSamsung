package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Notes;
import com.example.myapplication.NotesClickListener;
import com.example.myapplication.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<NotesViewHolder>{

    Context context;
    List<Notes> list;
    NotesClickListener listener;

    public Adapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.textViewTitle.setText(list.get(position).getTitle());
        holder.textViewTitle.setSelected(true);
        holder.textView_notes.setText(list.get(position).getNotes());
        holder.textView_Date.setText(list.get(position).getDate());
        holder.textView_Date.setSelected(true);

        if (list.get(position).isPinned()){
            holder.imgPin.setImageResource(R.drawable.pin_24);
        } else {
            holder.imgPin.setImageResource(0);
        }

        final int grey = R.color.grey;
        holder.notes_conteiner.setCardBackgroundColor(holder.itemView.getResources().getColor(grey, null));

        holder.notes_conteiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_conteiner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_conteiner);
                return true ;
            }
        });

    }



    @Override
    public int getItemCount(){return list.size();}

    public void filterList(List<Notes> filteredList) {

        list = filteredList;
        notifyDataSetChanged();

    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notes_conteiner;
    TextView textViewTitle;
    ImageView imgPin;
    TextView textView_notes;
    TextView textView_Date;


    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notes_conteiner = itemView.findViewById(R.id.notes_conteiner);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        imgPin = itemView.findViewById(R.id.imgPin);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        textView_Date = itemView.findViewById(R.id.textView_Date);
    }
}
