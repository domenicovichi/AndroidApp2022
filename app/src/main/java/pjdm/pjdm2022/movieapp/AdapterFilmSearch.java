package pjdm.pjdm2022.movieapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterFilmSearch extends RecyclerView.Adapter<AdapterFilmSearch.MyViewHolder> implements RecyclerView.OnItemTouchListener {

    private static final String TAG = "DV_PJDM";
    private Context context;
    private ArrayList<Film> listaFilm = new ArrayList<>();
    private OnListItemClick listener;


    public AdapterFilmSearch(Context context, ArrayList<Film> listaFilm, OnListItemClick listener){
        this.context = context;
        this.listaFilm = listaFilm;
        this.listener = listener;
    }

    public interface OnListItemClick {
        void onClickFilm(Film film);
    }

    @NonNull
    @Override
    public AdapterFilmSearch.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_film_search,parent,false);
        return new AdapterFilmSearch.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFilmSearch.MyViewHolder holder, int position) {
        holder.titolo.setText(listaFilm.get(position).getTitolo());
        holder.annoUscita.setText(String.valueOf("Year: "+listaFilm.get(position).getAnnoDiUscita()));
        holder.durata.setText(String.valueOf("Duration: " +listaFilm.get(position).getDurata()+" min"));
        holder.genere.setText("Genre: " + listaFilm.get(position).getGenereFilm());
        holder.bind(listaFilm.get(position), listener);
    }



    @Override
    public int getItemCount() {
        return listaFilm.size();
    }

    public void setClickListener(OnListItemClick context) {
        this.listener = context;
    }


    public void add(Film film){
        this.listaFilm.add(film);
        notifyDataSetChanged();
    }

    public void clear(){
        this.listaFilm = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titolo;
        TextView genere;
        TextView annoUscita;
        TextView durata;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titolo = itemView.findViewById(R.id.tvTitolo_search);
            genere = itemView.findViewById(R.id.tvGenere_search);
            annoUscita = itemView.findViewById(R.id.tvAnno_search);
            durata = itemView.findViewById(R.id.tvDurata_search);


        }

        public void bind(final Film item, final OnListItemClick listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onClickFilm(item);
                }
            });
        }
    }

}
