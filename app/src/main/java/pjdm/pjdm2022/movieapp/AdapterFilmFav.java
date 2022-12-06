package pjdm.pjdm2022.movieapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdapterFilmFav extends RecyclerView.Adapter<AdapterFilmFav.MyViewHolder> {

    private static final String TAG = "DV_PJDM";
    private Context context;
    private ArrayList<FilmRV> listaFilm = new ArrayList<>();
    private OnListItemClick onListItemClick;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Activity activity;


    public AdapterFilmFav(Context context, Activity activity,ArrayList<FilmRV> listaFilm){
        this.context = context;
        this.listaFilm = listaFilm;
        this.activity = activity;
    }



    @NonNull
    @Override
    public AdapterFilmFav.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_film_fav,parent,false);
        return new AdapterFilmFav.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFilmFav.MyViewHolder holder, int position) {
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
        ImageButton ibDelete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titolo = itemView.findViewById(R.id.tvTitolo_s);
            genere = itemView.findViewById(R.id.tvGenere_search);
            annoUscita = itemView.findViewById(R.id.tvAnno_search);
            durata = itemView.findViewById(R.id.tvDurata_search);
            immagine = itemView.findViewById(R.id.ivFilm_rv);
            ibDelete = itemView.findViewById(R.id.ibDelete);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                String email = sharedPreferences.getString("email", "");
                                String title = titolo.getText().toString();
                                String indirizzo = "http://10.0.2.2:8080/Movie/deleteFilm?email="+email+"&titolo="+title;
                                URL url = new URL(indirizzo);
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setRequestMethod("DELETE");
                                httpURLConnection.setDoOutput(true);
                                httpURLConnection.setUseCaches(false);
                                httpURLConnection.connect();
                                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                                wr.write("");
                                wr.flush();
                                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                                if (httpURLConnection.getResponseCode()==200){
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            removeAt(getAdapterPosition());
                                            Log.d(TAG, "run: eliminato");
                                            Log.d(TAG, "run: "+listaFilm);
                                            if(listaFilm.isEmpty()){
                                                Navigation.findNavController(view).navigate(R.id.action_favoriteFragment_to_homeFragment);
                                            }
                                        }
                                    });


                                }else{
                                    Log.d("TAG", "run: "+httpURLConnection.getResponseCode());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "adapterr"+getAdapterPosition());
                }
            });

        }

    }
    public void removeAt(int position){
        listaFilm.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaFilm.size());
    }

}
