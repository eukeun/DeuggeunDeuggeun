package com.example.gym_platform;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class ReservationAdapter extends FirestoreRecyclerAdapter<Reservation,ReservationAdapter.ReservationHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReservationAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReservationHolder holder, int position, @NonNull Reservation model) {
        SimpleDateFormat printdate = new SimpleDateFormat("yy년 MM월 dd일");
        holder.textViewgymName.setText(model.getGymName());
        holder.textViewcontent.setText(model.getContent());
        holder.textViewtype.setText(model.getType());
        holder.textViewstart.setText(String.valueOf(printdate.format(model.getStart())));
        holder.textViewend .setText(String.valueOf(printdate.format(model.getEnd())));
        holder.textViewprice.setText(String.valueOf(model.getPrice()));
    }

    @NonNull
    @Override
    public ReservationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservation_item,
                viewGroup, false);
        return new ReservationHolder(v);
    }

    class ReservationHolder extends RecyclerView.ViewHolder{
        TextView textViewgymName;
        TextView textViewstart;
        TextView textViewend;
        TextView textViewcontent;
        TextView textViewtype;
        TextView textViewprice;
        public ReservationHolder(@NonNull View itemView) {
            super(itemView);
            textViewgymName = itemView.findViewById(R.id.text_view_gymName);
            textViewstart = itemView.findViewById(R.id.text_view_start);
            textViewcontent = itemView.findViewById(R.id.text_view_content);
            textViewend = itemView.findViewById(R.id.text_view_end);
            textViewtype = itemView.findViewById(R.id.text_view_type);
            textViewprice = itemView.findViewById(R.id.text_view_price);
        }
    }
}
