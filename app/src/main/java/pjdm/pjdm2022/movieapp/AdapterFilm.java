package pjdm.pjdm2022.movieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AdapterFilm extends RecyclerView.Adapter<AdapterFilm.MyViewHolder> {

    private Context context;
    private ArrayList<FilmRV> listaFilm = new ArrayList<>();
    private OnListItemClick onListItemClick;
    private static final String TAG = "DV_PJDM";


    public AdapterFilm(Context context, ArrayList<FilmRV> listaFilm){
        this.context = context;
        this.listaFilm = listaFilm;
    }

    @NonNull
    @Override
    public AdapterFilm.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_film,parent,false);
        return new AdapterFilm.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFilm.MyViewHolder holder, int position) {
        holder.titolo.setText(listaFilm.get(position).getTitolo());
        holder.genere.setText("Genre: " + listaFilm.get(position).getGenere());
        holder.annoUscita.setText(String.valueOf("Year: "+listaFilm.get(position).getAnnoDiUscita()));
        holder.durata.setText(String.valueOf("Duration: " +listaFilm.get(position).getDurata() +" min"));
        try {
            URL url = new URL(listaFilm.get(position).getImmagine());
            Bitmap icon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.immagine.setImageBitmap(icon);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            try {
                URL url = new URL(context.getString(R.string.url_pic)+ position);
                Bitmap icon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                holder.immagine.setImageBitmap(icon);
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listaFilm.size();
    }

    public void setClickListener(OnListItemClick context) {
        this.onListItemClick = context;
    }


    public void add(FilmRV filmRV){
        this.listaFilm.add(filmRV);
        notifyDataSetChanged();
    }

        class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titolo;
        TextView genere;
        TextView annoUscita;
        TextView durata;
        ImageView immagine;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titolo = itemView.findViewById(R.id.tvTitolo_s);
            genere = itemView.findViewById(R.id.tvGenere_search);
            annoUscita = itemView.findViewById(R.id.tvAnno_search);
            durata = itemView.findViewById(R.id.tvDurata_search);
            immagine = itemView.findViewById(R.id.ivFilm_rv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onListItemClick.onClick(view,getAdapterPosition());
                }
            });

        }

    }

}
