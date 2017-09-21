package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 12/21/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.danny7899.dailytoast.AppConfig;
import com.danny7899.dailytoast.AppController;
import com.danny7899.dailytoast.SQLiteHandler;
import com.danny7899.dailytoast.SessionManager;

import org.w3c.dom.Text;


public class AccountFragment extends Fragment {

    Button btnPost;
    Button btnLogout;

    TextView textName;
    TextView textEmail;
    TextView textUsername;

    private SessionManager session;
    private SQLiteHandler db;
    MainActivity activityMain = new MainActivity();
    private ArrayList<CategoryData> dataSet = new ArrayList<CategoryData>(activityMain.categorySet);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // SqLite database handler
        db = new SQLiteHandler(getActivity());

        // session manager
        session = new SessionManager(getActivity());

        String state;
        if (session.isLoggedIn() == true) {
            state = "true";
        } else {
            state = "false";
        }

        Log.i("LOGGED STATE: ", state);
        if (session.isLoggedIn() == false) {
            // Launching the login activity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("FROM_SPLASH", false);
            startActivity(intent);
            return null;
        } else {
            View v = inflater.inflate(R.layout.fragment_account, container, false);

            textName = (TextView) v.findViewById(R.id.Name_text);
            textEmail = (TextView) v.findViewById(R.id.Email_text);
            textUsername = (TextView) v.findViewById(R.id.Username_text);
            btnPost = (Button) v.findViewById(R.id.btnPostToast);
            btnLogout = (Button) v.findViewById(R.id.btnLogout);

            HashMap<String, String> user = db.getUserDetails();

            String name = user.get("name");
            String email = user.get("email");
            String username = user.get("username");

            textName.setText(name);
            textEmail.setText(email);
            textUsername.setText(username);

            btnPost.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //TASK HERE
                    Intent intent = new Intent(getActivity(), CreateActivity.class);
                    intent.putExtra("categorySet", new CategoryWrapper(dataSet));
                    startActivity(intent);
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    logoutUser();
                    Toast.makeText(getActivity(),
                            "User logged out", Toast.LENGTH_LONG).show();
                    FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                    tx.replace(R.id.frame, new HomeFragment());
                    tx.commit();
                }
            });


            return v;
        }
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
    }
}