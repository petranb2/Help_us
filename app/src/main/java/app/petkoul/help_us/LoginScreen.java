package app.petkoul.help_us;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    EditText email_edittext;
    EditText pass_edittext;
    Button register_button;
    TextView not_account_yet_textView;
    TextView login_status;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDatabaseRefence;
    final String TAG = "LoginScreen";
    final Boolean sign_in=false;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);

        email_edittext = findViewById(R.id.email_edittext);

        pass_edittext = findViewById(R.id.password_edittext);

        register_button = findViewById(R.id.registerButton);

        not_account_yet_textView = findViewById(R.id.not_account_yet_textView);

        login_status = findViewById(R.id.login_status);


        progressDialog = new ProgressDialog(this);


        //progressBar = new ProgressBar(this);


        register_button.setOnClickListener(this);

        not_account_yet_textView.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

          if(isGooglePlayServicesAvailable()){
              Toast.makeText(this,"google play services is installed",Toast.LENGTH_SHORT).show();
          }else {

              Toast.makeText(this,"google play services isn't installed",Toast.LENGTH_SHORT).show();
          }

          if(isNetworkConnected()){
              Toast.makeText(this,"network enable",Toast.LENGTH_SHORT).show();
          }else {

              Toast.makeText(this,"please reconnetct to network ",Toast.LENGTH_SHORT).show();
          }



        //firebaseAuth.signOut();
    }




    @Override
    public void onClick(View view) {
        // Check if User is signed in (non-null) and update UI accordingly.


        if(view == register_button ){
            signIn(email_edittext.getText().toString(), pass_edittext.getText().toString());
        }
        if(view == not_account_yet_textView){
            //open the proper view
            Intent in = new Intent(this,PatientRegister.class);
            startActivity(in);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if User is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            Intent in = new Intent(this, Main_Console.class);
            startActivity(in);
            //query_users(currentUser);
            Toast.makeText(LoginScreen.this, "Alredy login",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void signIn(final String email, String password) {

        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();
        progressDialog.setCancelable(false);
        progressDialog.show();

        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in User's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d("Login Screen",user.toString());
                            //fire_dummy_console();
                            query_users(user);
                            //task.isComplete();

                        } else {
                            // If sign in fails, display a message to the User.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            login_status.setText(R.string.auth_failed);
                            progressDialog.hide();
                        }

                        //hideProgressDialog();

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = email_edittext.getText().toString();
        if (TextUtils.isEmpty(email)) {
            email_edittext.setError("Required.");
            valid = false;
        } else {
            email_edittext.setError(null);
        }

        String password = pass_edittext.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass_edittext.setError("Required.");
            valid = false;
        } else {
            pass_edittext.setError(null);
        }

        return valid;
    }

    public  void fire_console(String category_user){
        Intent in;
        if (category_user.equals("patient")){
             //in = new Intent(this, PatientConsole.class);
                //in = new Intent(this, PatientConsole_backup.class);
            in = new Intent(this, Main_Console.class);
             startActivity(in);

        }
        if (category_user.equals("helper")){
            //in = new Intent(this, HelperConsole_backup.class);
            in = new Intent(this, Main_Console.class);
            startActivity(in);
        }
        progressDialog.dismiss();

    }


    private void query_users( FirebaseUser current_user) {

        String uid = current_user.getUid();


        usersDatabaseRefence = firebaseDatabase.getReference().child("users/"+uid+"/category");
        Log.d("Login Screen",current_user.getUid());

        usersDatabaseRefence.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                //firebaseAuth.signOut();
                Log.d("Login screen",String.valueOf(dataSnapshot.getChildrenCount()));
                Log.d("Login screen",dataSnapshot.toString());
                String category_user = dataSnapshot.getValue().toString();

                fire_console(category_user);


            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

        usersDatabaseRefence.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //User user = dataSnapshot.getValue(User.class);
                //list.add(user);
                //users_list.add(dataSnapshot.getValue().toString());
                Log.d("Login screen", "Category" + dataSnapshot.getValue());
                //Log.d("Dummy console user: ", user.getCategory());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Dummy Console", "on cancelled");
            }
        });
    }


    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
