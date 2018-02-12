package app.petkoul.help_us;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dummy_Console extends AppCompatActivity implements View.OnClickListener ,SearchView.OnQueryTextListener {
    Button sign_out;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView user_properties;
    SearchView searchView;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    List<String> list = new ArrayList<>();
    List<String> users_list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy__console);

        sign_out = findViewById(R.id.sign_out_button);

        user_properties = findViewById(R.id.user_properties);

        searchView = findViewById(R.id.search_view);

        searchView.onActionViewExpanded();

        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(this);

        listView = findViewById(R.id.list_view);



        sign_out.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String uid = user.getUid();
        String name = user.getDisplayName();
        user_properties.append(" "+uid);
        user_properties.append(" \n "+user.getEmail());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
        //databaseReference.child("users").orderByChild("name").equalTo("pet");
        //databaseReference.child("users").orderByChild("name").equalTo("pet");



        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,users_list);

        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(view == sign_out){
            firebaseAuth.signOut();
            Intent in = new Intent(this , LoginScreen.class);
            startActivity(in);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        //return false;
        //users_list = query_users(s);
        Log.d("Dummy Console",s);
        adapter.clear();
        query_users(s);


        Log.d("Dummy Console",s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //return false;
        Log.d("Dummy Console",s);
        adapter.clear();
        query_users(s);
        return true;
    }

    private void query_users(String s){
        //final  List<String> users = new ArrayList<>();
        Log.d("Dummy Console","start querry");

        databaseReference.orderByChild("name").equalTo(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Dummy Console", String.valueOf(dataSnapshot.getChildrenCount()));
                adapter.addAll(users_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.orderByChild("name").equalTo(s).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                users_list.add(dataSnapshot.getValue().toString());
                Log.d("Dummy console ","The " + dataSnapshot.getKey() + " score is " + dataSnapshot.getValue());
                Log.d("Dummy console user: ",user.toString());
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


        Log.d("Dummy Console","end querry");
        //databaseReference.child("name").orderByValue("pet").addListenerForSingleValueEvent();
        //databaseReference.onDisconnect();
        //return users;
    }

}
