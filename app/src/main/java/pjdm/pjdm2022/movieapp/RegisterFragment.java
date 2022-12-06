package pjdm.pjdm2022.movieapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    private Executor executor = Executors.newSingleThreadExecutor();

    private EditText etNome;
    private EditText etCognome;
    private EditText etEmailreg;
    private EditText etPasswordreg;
    private EditText etGenerereg;
    private Button btRegistrati;
    private ImageButton ibBack;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etEmailreg = view.findViewById(R.id.etEmailreg);
        etPasswordreg = view.findViewById(R.id.etPasswordReg);
        btRegistrati = view.findViewById(R.id.btRegistratiReg);
        etNome = view.findViewById(R.id.etNome);
        etCognome = view.findViewById(R.id.etCognome);
        etGenerereg = view.findViewById(R.id.etGenerereg);
        ibBack = view.findViewById(R.id.ibBack);


        btRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                register(etEmailreg.getText().toString(), etPasswordreg.getText().toString(), etNome.getText().toString(), etCognome.getText().toString(), etGenerereg.getText().toString());
                Toast.makeText(getActivity(),"Registrato",Toast.LENGTH_LONG).show();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RegisterFragment.this).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
    }

    public void register(String email, String password, String nome, String cognome, String genere) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String indirizzo = getString(R.string.url_servlet)+"/RegisterServlet";
                try {
                    URL url = new URL(indirizzo);
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    Map<String, String> params = new LinkedHashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("nome", nome);
                    params.put("cognome", cognome);
                    params.put("genere", genere);

                    StringBuilder postData = new StringBuilder();

                    for (Map.Entry<String, String> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                    http.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    http.setDoOutput(true);
                    http.getOutputStream().write(postDataBytes);
                    int code = http.getResponseCode();
                    if (code == 500) {
                        return;
                    }

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
