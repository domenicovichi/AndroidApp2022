package pjdm.pjdm2022.movieapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment {

    private ArrayList<FilmRV> listaFilm = new ArrayList<>();
    private Context context;
    private AdapterFilm adapter;
    private FilmViewModel filmViewModel;
    private Button btExit;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        RecyclerView filmRecyclerView = view.findViewById(R.id.rvFilmFav);
        btExit = view.findViewById(R.id.btExit);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        adapter = new AdapterFilm(context,listaFilm);
        filmRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        filmRecyclerView.setAdapter(adapter);
        getListaFilm();
        filmViewModel = new ViewModelProvider(requireActivity()).get(FilmViewModel.class);

        OnListItemClick onListItemClick = new OnListItemClick() {
            @Override
            public void onClick(View view, int position) {
                TextView tvTitolo = view.findViewById(R.id.tvTitolo_s);
                filmViewModel.setText(tvTitolo.getText().toString());
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_filmFragment);
            }
        };
        adapter.setClickListener(onListItemClick);
        return view;
    }


    private Executor executor = Executors.newSingleThreadExecutor();
    private void getListaFilm(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");
                    String indirizzo = getString(R.string.url_servlet)+"/getFilmByGenere?email="+ email;
                    URL url = new URL(indirizzo);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    if (httpURLConnection.getResponseCode()==200){
                        JSONArray arrayFilm = new JSONArray(br.readLine());
                        for (int i = 0; i<arrayFilm.length();i++){
                            JSONObject film = arrayFilm.getJSONObject(i);
                            String titolo = film.getString("titolo");
                            String genere = film.getString("genereFilm");
                            int annoUscita = film.getInt("annoDiUscita");
                            int durata = film.getInt("durata");
                            String immagine = film.getString("immagine");


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.add(new FilmRV(titolo,genere,annoUscita,durata,immagine));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }

                    }else{
                        return;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}