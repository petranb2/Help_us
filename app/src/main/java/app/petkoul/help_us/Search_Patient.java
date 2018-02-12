package app.petkoul.help_us;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class Search_Patient extends AppCompatActivity implements View.OnClickListener ,SearchView.OnQueryTextListener{
        Button sign_out;
        FirebaseAuth firebaseAuth;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        TextView user_properties;
        SearchView searchView;
        ListView listView;
        ArrayAdapter<String> arrayAdapter;
        List<String> users_list=new ArrayList<>();
        List<User> list=new ArrayList<>();
        ArrayAdapter<String> adapter;

@Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search__patient);


        searchView=findViewById(R.id.search_view);

        searchView.onActionViewExpanded();

        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(this);

        listView=findViewById(R.id.list_view);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("users");
        //databaseReference.child("users").orderByChild("name").equalTo("pet");
        //databaseReference.child("users").orderByChild("name").equalTo("pet");


        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,users_list);
        list.clear();
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                       User user = new User();
                       user = list.get(i);
                       start_helper_register(user);
                       return true;
                }
        });
        }

@Override
public void onClick(View view){
        }

        private void start_helper_register(User patient){

                Intent in = new Intent(this, HelperRegister.class);
                in.putExtra("patient",patient);
                startActivity(in);

        }


@Override
public boolean onQueryTextSubmit(String s){
        //return false;
        //users_list = query_users(s);
        Log.d("Dummy Console",s);
        //adapter.clear();
        query_users(s);


        Log.d("Dummy Console",s);
        return true;
        }

@Override
public boolean onQueryTextChange(String s){
        return false;
        //Log.d("Dummy Console",s);
        //adapter.clear();
        //query_users(s);
        //return true;
        }

private void query_users(String s){
        //final  List<String> users = new ArrayList<>();
        Log.d("Dummy Console","start querry");

        databaseReference.orderByChild("name").equalTo(s).addListenerForSingleValueEvent(new ValueEventListener(){
@Override
public void onDataChange(DataSnapshot dataSnapshot){
        Log.d("Dummy Console",String.valueOf(dataSnapshot.getChildrenCount()));
        adapter.clear();
        //adapter.addAll(users_list);
        if (!list.isEmpty()){
                adapter.addAll(String.valueOf(list));
        }
        }

@Override
public void onCancelled(DatabaseError databaseError){

        }
        });

        databaseReference.orderByChild("name").equalTo(s).addChildEventListener(new ChildEventListener(){
@Override
public void onChildAdded(DataSnapshot dataSnapshot,String s){
        User user=dataSnapshot.getValue(User.class);
        if(user.getCategory().equals("patient")){
                list.add(user);
        }

        //users_list.add(dataSnapshot.getValue().toString());

        Log.d("Dummy console ","The "+dataSnapshot.getKey()+" score is "+dataSnapshot.getValue());
        Log.d("Dummy console user: ",user.toString());
        }

@Override
public void onChildChanged(DataSnapshot dataSnapshot,String s){

        }

@Override
public void onChildRemoved(DataSnapshot dataSnapshot){

        }

@Override
public void onChildMoved(DataSnapshot dataSnapshot,String s){

        }

@Override
public void onCancelled(DatabaseError databaseError){
        Log.d("Dummy Console","on cancelled");
        }
        });


        Log.d("Dummy Console","end querry");
        //databaseReference.child("name").orderByValue("pet").addListenerForSingleValueEvent();
        //databaseReference.onDisconnect();
        //return users;
        }
}