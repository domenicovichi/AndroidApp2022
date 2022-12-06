package pjdm.pjdm2022.movieapp;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static final String TAG = "DV_PJDM";
    private Context context;
    private Button btCrime;
    private Button btComedy;
    private Button btDrama;
    private Button btMistery;
    private String genere;
    private Button btGo;
    private EditText etTitolo;
    private Executor executor = Executors.newSingleThreadExecutor();
    private boolean selected;
    private String indirizzo;
    private FilmViewModel filmViewModel;
    private AdapterFilmSearch adapter;
    private ArrayList<Film> listaFilm = new ArrayList<>();
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        RecyclerView filmRecyclerView = view.findViewById(R.id.rvSearch);
        filmRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        filmViewModel = new ViewModelProvider(requireActivity()).get(FilmViewModel.class);

        adapter = new AdapterFilmSearch(context, listaFilm, new AdapterFilmSearch.OnListItemClick() {
            @Override
            public void onClickFilm(Film film) {
                Log.d(TAG, "onClickFilm: "+film.getTitolo());
                filmViewModel.setText(film.getTitolo());
                NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.action_searchFragment_to_filmFragment);
            }
        });
        filmRecyclerView.setAdapter(adapter);
        TextView tvTitolo = view.findViewById(R.id.tvTitolo_search);
        Log.d(TAG, "onCreateView: "+tvTitolo);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btComedy = view.findViewById(R.id.btnComedy);
        btCrime = view.findViewById(R.id.btnCrime);
        btDrama = view.findViewById(R.id.btnDrama);
        btMistery = view.findViewById(R.id.btnMistery);
        btGo = view.findViewById(R.id.btnSearch);
        etTitolo = view.findViewById(R.id.etSearch);

        btMistery.setOnClickListener(this);
        btComedy.setOnClickListener(this);
        btDrama.setOnClickListener(this);
        btCrime.setOnClickListener(this);
        btGo.setOnClickListener(this);
    }

    public void clearTitle(){
        etTitolo.setText("");
    }

    public void loadFilms(BufferedReader br) throws IOException, JSONException {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
            }
        });
        JSONArray arrayFilm = new JSONArray(br.readLine());
        for (int i = 0; i < arrayFilm.length(); i++) {
            JSONObject film = arrayFilm.getJSONObject(i);
            String titolo = film.getString("titolo");
            String genere = film.getString("genereFilm");
            int annoUscita = film.getInt("annoDiUscita");
            int durata = film.getInt("durata");

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add(new Film(titolo,annoUscita,durata,genere));
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }

   public void onClick(View view) {
        if(view.getId()!=R.id.btnSearch){
            clearTitle();
        }
        if(view.getId()==R.id.btnComedy){
            genere = btComedy.getText().toString();}
        else if(view.getId()==R.id.btnCrime){
            genere = btCrime.getText().toString();}
        else if(view.getId()==R.id.btnDrama){
            genere = btDrama.getText().toString();}
        else if(view.getId()==R.id.btnMistery){
            genere = btMistery.getText().toString();}
        else if(view.getId()==R.id.btnSearch)
            selected = TRUE;
        else
            return;
       Log.d(TAG, "onClick: "+selected);

       executor.execute(new Runnable() {
           @Override
           public void run() {
               try {
                   if(selected){
                       indirizzo = getString(R.string.url_servlet)+"/getFilmFiltered?input="+ etTitolo.getText()+"&field=titolo";
                       selected = FALSE;
                   } else {
                       indirizzo = getString(R.string.url_servlet)+"/getFilmFiltered?input=" + genere +"&field=genere";
                   }
                   Log.d(TAG, "run: "+indirizzo);
                   URL url = new URL(indirizzo);
                   HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                   BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                   Log.d(TAG, "run: "+httpURLConnection.getResponseCode());
                   if (httpURLConnection.getResponseCode() == 200) {
                       loadFilms(br);
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