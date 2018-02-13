package app.petkoul.help_us;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Invite extends Fragment {
        Button sign_out;
        Button btn_querry;
        FirebaseAuth firebaseAuth;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        TextView user_properties;
        EditText editText;
        SearchView searchView;
        ListView listView;
        ArrayAdapter<String> arrayAdapter;
        //List<String> users_list=new ArrayList<>();
         List<User> users_list=new ArrayList<>();
        List<User> list=new ArrayList<>();
        //ArrayAdapter<String> adapter;
        Adapter_Custom_ListView_Invite adapter;

public Frag_Invite() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    View view = (RelativeLayout)inflater.inflate(R.layout.fragment_frag__invite, container, false);

        listView= (ListView) view.findViewById(R.id.list_view_frag);
        editText = (EditText) view.findViewById(R.id.src_text);
        btn_querry = (Button) view.findViewById(R.id.querry_btn);
        btn_querry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Dummy Console",editText.getText().toString());
                adapter.clear();
                query_users(editText.getText().toString());
            }
        });
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("users");
        //databaseReference.child("users").orderByChild("name").equalTo("pet");
        //databaseReference.child("users").orderByChild("name").equalTo("pet");


        //adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,users_list);
        adapter = new Adapter_Custom_ListView_Invite((ArrayList<User>) list,getContext());
        list.clear();
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag__invite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Invite");

        listView= (ListView) view.findViewById(R.id.list_view_frag);
        editText = (EditText) view.findViewById(R.id.src_text);
        btn_querry = (Button) view.findViewById(R.id.querry_btn);
        btn_querry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Dummy Console",editText.getText().toString());
                adapter.clear();
                list.clear();
                query_users(editText.getText().toString());
            }
        });
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("users");
        //databaseReference.child("users").orderByChild("name").equalTo("pet");
        //databaseReference.child("users").orderByChild("name").equalTo("pet");


        //adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,users_list);
        adapter = new Adapter_Custom_ListView_Invite((ArrayList<User>) list,getContext());
        list.clear();
        listView.setAdapter(adapter);
    }







    private void query_users(String s){
        //final  List<String> users = new ArrayList<>();
        Log.d("Dummy Console","start querry");

        databaseReference.orderByChild("name").equalTo(s).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Log.d("Dummy Console",String.valueOf(dataSnapshot.getChildrenCount()));
                //adapter.clear();
                //adapter.addAll(users_list);
                /*if (!list.isEmpty()){
                    adapter.addAll(String.valueOf(list));

                }
                adapter.notifyDataSetChanged();*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

        databaseReference.orderByChild("name").equalTo(s).addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,String s){
                User user=dataSnapshot.getValue(User.class);
                //if(user.getCategory().equals("patient")){
                   // list.add(user);
               // }
                list.add(user);

                //users_list.add(dataSnapshot.getValue().toString());
                if (!list.isEmpty()){
                    //adapter.addAll(String.valueOf(list));
                   // adapter.add(user.toString());
                        //adapter.add(user);
                }
                adapter.notifyDataSetChanged();
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
