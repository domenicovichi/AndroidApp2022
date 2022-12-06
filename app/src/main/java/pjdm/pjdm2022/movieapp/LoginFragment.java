package pjdm.pjdm2022.movieapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
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
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Executor executor = Executors.newSingleThreadExecutor();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    private Button btReg;
    private Button btAccedi;
    private EditText etEmail;
    private EditText etPassword;
    boolean flag = true;



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btReg = view.findViewById(R.id.btnRegistrati);
        btAccedi = view.findViewById(R.id.btnAccedi);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);

        btReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this ).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        btAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(etEmail.getText().toString(), etPassword.getText().toString());
                if(flag == false){
                    System.out.println("sono dentro l'if");
                }
            }
        });
    }

    public void login(String email, String password) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String indirizzo = getString(R.string.url_servlet)+"/LoginUtenteServlet";
                try {
                    URL url = new URL(indirizzo);
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    Map<String, String> params = new LinkedHashMap<>();
                    params.put("email", email);
                    params.put("password", password);
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
                    BufferedReader br = new BufferedReader(new InputStreamReader((http.getInputStream())));
                    StringBuilder sb = new StringBuilder();
                    String output;
                    int code = http.getResponseCode();

                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }
                    if (code == 404) {
                        return;
                    }
                    else {
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor edt = sharedPreferences.edit();
                        edt.putString("email", jsonObject.getString("email"));
                        edt.apply();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    onCatchError();
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onCatchError(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), "Credenziali errate", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnRegistrati) {
            NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_registerFragment);
        }
    }
}
