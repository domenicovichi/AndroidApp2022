package pjdm.pjdm2022.movieapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FilmViewModel filmViewModel;

    // TODO: Rename and change types of parameters
    private static final String TAG = "DV_PJDM";
    private String mParam1;
    private String mParam2;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView tvTitolo;
    private TextView tvGenere;
    private TextView tvAnnoDiUscita;
    private TextView tvDurataSel;
    private TextView tvTrama;
    private TextView tvAttori;
    private Button btPref;
    private ImageButton ibPref;


    public FilmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilmFragment newInstance(String param1, String param2) {
        FilmFragment fragment = new FilmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        filmViewModel = new ViewModelProvider(requireActivity()).get(FilmViewModel.class);
        Log.d(TAG, filmViewModel.getText().getValue());
        return inflater.inflate(R.layout.fragment_film, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFilmFromTitle();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String titolo = filmViewModel.getText().getValue();
        btPref = view.findViewById(R.id.btnPreferiti);
        btPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               executor.execute(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           String indirizzo = getString(R.string.url_servlet)+"/addFilmPrefServlet";
                           URL url = new URL(indirizzo);
                           HttpURLConnection http = (HttpURLConnection) url.openConnection();
                           http.setRequestMethod("POST");
                           Map<String,String> params = new LinkedHashMap<>();
                           params.put("email",email);
                           params.put("titolo",titolo);
                           Log.d(TAG, "run: "+params);
                           StringBuilder postData = new StringBuilder();
                           for (Map.Entry<String,String> param : params.entrySet()) {
                               if (postData.length() != 0) postData.append('&');
                               Log.d(TAG, "run: PROVA2");
                               postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                               postData.append('=');
                               postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                           }
                           byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                           http.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                           http.setDoOutput(true);
                           http.getOutputStream().write(postDataBytes);
                           BufferedReader br = new BufferedReader(new InputStreamReader((http.getInputStream())));
                           StringBuilder sb = new StringBuilder();
                           String output;
                           Log.d(TAG, "run: TRYYYYY");
                           while ((output = br.readLine()) != null) {
                               sb.append(output);
                           }
                           onSuccess();
                       } catch (MalformedURLException e) {
                           e.printStackTrace();
                       } catch (IOException e) {
                           onCatchError();
                           e.printStackTrace();
                       }

                   }
               });
            }
        });
    }

    public void getFilmFromTitle(){
        tvTitolo = getView().findViewById(R.id.tvTitoloSelected);
        tvAnnoDiUscita = getView().findViewById(R.id.tvAnnoUscita);
        tvAttori = getView().findViewById(R.id.tvAttori);
        tvGenere = getView().findViewById(R.id.tvGenere);
        tvTrama = getView().findViewById(R.id.tvTrama);
        tvDurataSel = getView().findViewById(R.id.tvDurata);
        String titolo = filmViewModel.getText().getValue();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String indirizzo = getString(R.string.url_servlet)+"/getFilmByTitolo?titolo="+ titolo;
                    URL url = new URL(indirizzo);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    if (httpURLConnection.getResponseCode()==200){
                        JSONObject film = new JSONObject(br.readLine());
                        tvTitolo.setText(film.getString("titolo"));
                        tvAnnoDiUscita.setText("year of release: " + film.getInt("annoDiUscita"));
                        tvDurataSel.setText("duration: " + film.getInt("durata") + " min");
                        tvAttori.setText("protagonists: " + film.getString("attori"));
                        tvGenere.setText("film genre: " + film.getString("genereFilm"));
                        tvTrama.setText("plot: " + film.getString("trama"));
                    } else if(httpURLConnection.getResponseCode()==400){
                        Log.d(TAG, "run: errore");
                        Toast.makeText(getActivity(),"titolo non valido",Toast.LENGTH_LONG).show();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void onCatchError(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), "Film già inserito", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onSuccess(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), "Film inserito", Toast.LENGTH_SHORT).show();
            }
        });
    }
}