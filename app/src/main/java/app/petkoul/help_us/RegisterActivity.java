package app.petkoul.help_us;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button helper_btn;
    Button patienr_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        helper_btn = findViewById(R.id.helper_btn);
        patienr_btn = findViewById(R.id.patient_btn);
        helper_btn.setOnClickListener(this);
        patienr_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            if(view == helper_btn){
                Intent in = new Intent(this , HelperRegister.class);
                startActivity(in);
            }
        if(view == patienr_btn){
            Intent in = new Intent(this , PatientRegister.class);
            startActivity(in);
        }
    }
}
