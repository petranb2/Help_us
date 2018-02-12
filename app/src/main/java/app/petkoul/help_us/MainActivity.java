package app.petkoul.help_us;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email_edittext;
    EditText pass_edittext;
    Button register_button;
    TextView already_signin_textview;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    final String TAG = "mainactivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        email_edittext = findViewById(R.id.email_edittext);

        pass_edittext = findViewById(R.id.password_edittext);

        register_button = findViewById(R.id.registerButton);

        already_signin_textview = findViewById(R.id.alreadysignintextView);

        progressDialog = new ProgressDialog(this);

        register_button.setOnClickListener(this);

        already_signin_textview.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void register_user(){
        String email = email_edittext.getText().toString().trim();
        String pass = pass_edittext.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter a valid email address",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter a password",Toast.LENGTH_SHORT).show();
            return;
        }

        //progressDialog.setMessage("Registering User ...");

        //progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in User's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(MainActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } else {
                            // If sign in fails, display a message to the User.
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                        // ...
                    }
                });


    }




    @Override
    public void onClick(View view) {
            if(view == register_button ){
                register_user();
            }
            if(view==already_signin_textview){
                //open the proper view

            }
    }
}
