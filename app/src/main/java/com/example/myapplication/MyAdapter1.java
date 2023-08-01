package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyAdapter1 extends RecyclerView.Adapter<MyViewHolder1> {

    private Context context;
    private List<DataClass> dataList;
    DatabaseReference reference, childRef;
    String tanggalAwal, additionalText;


    public MyAdapter1(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_home_item,parent,false);

        return new MyViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position) {
        holder.Title.setText(dataList.get(position).getDataTitle());
        holder.Avail.setText(dataList.get(position).getDataAvail());

        /////Membuat highseason button muncul jika user adalah admin
        TextView name = ((HomeMainActivity) context).findViewById(R.id.NameUser);
        String Name = name.getText().toString();
        if (Name.equals("admin")){
            holder.highSeason.setVisibility(View.VISIBLE);
        }else {
            holder.highSeason.setVisibility(View.GONE);
        }

        /////Membuat warna teks, jika tersedia maka hijau dan seterusnya
        int hijau = ContextCompat.getColor(holder.itemView.getContext(), R.color.hijau);
        int merah = ContextCompat.getColor(holder.itemView.getContext(), R.color.merah);
        int kuning = ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow);
        TextView textView = holder.itemView.findViewById(R.id.avail_deck);
        Drawable deckcolor = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.deck_color);

        String value = dataList.get(position).getDataAvail();
        Drawable wrappedDrawable = DrawableCompat.wrap(deckcolor.mutate());

        if (value.equals("Tersedia")) {
            textView.setTextColor(hijau);
            holder.highSeasonTV.setText("Add as Highseason");
            holder.highSeasonTV.setTextColor(kuning);
            textView.setText("Rp. 750.000");
            DrawableCompat.setTint(wrappedDrawable, hijau);
        } else if (value.equals("Highseason")) {
            textView.setTextColor(kuning);
            holder.highSeasonTV.setText("Remove as Highseason");
            holder.highSeasonTV.setTextColor(merah);
            textView.setText("Rp. 750.000");
        } else {
            textView.setTextColor(merah);
            DrawableCompat.setTint(wrappedDrawable, merah);
        }
        holder.colordeck.setImageDrawable(wrappedDrawable);


        //////jika buttn highseason ditekan oleh admin maka syntax dibawah mengubah dataAvail di database menjadi "Highseason"
        holder.highSeason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataAvail = dataList.get(holder.getAdapterPosition()).getDataAvail();
                if (dataAvail.equals("Tersedia")){
                    TextView tanggal = ((HomeMainActivity) context).findViewById(R.id.tanggal);
                    tanggalAwal = tanggal.getText().toString();
                    additionalText = "Pineustilu1";
                    String dataTitle = dataList.get(holder.getAdapterPosition()).getDataTitle();
                    String path = tanggalAwal+"/"+additionalText.trim();
                    reference = FirebaseDatabase.getInstance().getReference(path);
                    childRef = reference.child(dataTitle);
                    childRef.child("dataAvail").setValue("Highseason");
                } else if (dataAvail.equals("Highseason")) {
                    TextView tanggal = ((HomeMainActivity) context).findViewById(R.id.tanggal);
                    tanggalAwal = tanggal.getText().toString();
                    additionalText = "Pineustilu1";
                    String dataTitle = dataList.get(holder.getAdapterPosition()).getDataTitle();
                    String path = tanggalAwal+"/"+additionalText.trim();
                    reference = FirebaseDatabase.getInstance().getReference(path);
                    childRef = reference.child(dataTitle);
                    childRef.child("dataAvail").setValue("Tersedia");
                }
            }
        });
        holder.picImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tanggal = ((HomeMainActivity) context).findViewById(R.id.tanggal);
                String Tanggalawal = tanggal.getText().toString();
                TextView name = ((HomeMainActivity) context).findViewById(R.id.NameUser);
                TextView rangetanggal = ((HomeMainActivity)context).findViewById(R.id.rangetanggal);
                String Name = name.getText().toString();
                String tanggalAkhir = rangetanggal.getText().toString();
                String dataTitle2 = dataList.get(holder.getAdapterPosition()).getDataTitle();
                String additionalLokasi = "Pineustilu1";
                String title = dataTitle2 + additionalLokasi;

                Intent intent = new Intent(context, DetailBookingDeckActivity.class);
                String fasilities = context.getResources().getString(R.string.DetailFasilitasPt1Pt2);
                String fasilitiesText = context.getResources().getString(R.string.PineusTilu1Fasilitas);
                intent.putExtra("deck", dataTitle2);
                intent.putExtra("lokasi", additionalLokasi);
                intent.putExtra("title", title);
                intent.putExtra("avail", dataList.get(holder.getAdapterPosition()).getDataAvail());
                intent.putExtra("fasilities", fasilities);
                intent.putExtra("fasilitiesText", fasilitiesText);
                intent.putExtra("tanggalawal", Tanggalawal);
                intent.putExtra("tanggalakhir", tanggalAkhir);
                intent.putExtra("name", Name);

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder1 extends RecyclerView.ViewHolder{

    ImageView picImg;
    ImageView colordeck;
    TextView Title, Avail,highSeasonTV;
    ConstraintLayout deck;
    LinearLayout highSeason;

    public MyViewHolder1(@NonNull View itemView) {
        super(itemView);

            deck = itemView.findViewById(R.id.deck);
            picImg = itemView.findViewById(R.id.pic_deck);
            colordeck = itemView.findViewById(R.id.deck_color);
            Title = itemView.findViewById(R.id.title_deck);
            Avail = itemView.findViewById(R.id.avail_deck);
            highSeason = itemView.findViewById(R.id.highSeason);
            highSeasonTV = itemView.findViewById(R.id.highSeasonTV);

    }
}