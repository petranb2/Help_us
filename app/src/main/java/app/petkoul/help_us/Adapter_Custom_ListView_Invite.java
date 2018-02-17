package app.petkoul.help_us;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class Adapter_Custom_ListView_Invite extends ArrayAdapter<User> {

    private ArrayList<User> dataSet;
    Context mContext;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDatabaseRefence;



    // View lookup cache
    private static class ViewHolder {
        TextView name_surname;
        TextView email;
        Button invite_btn;
    }

    public Adapter_Custom_ListView_Invite(ArrayList<User> data, Context context) {
        super(context, R.layout.invite_item, data);
        this.dataSet = data;
        this.mContext=context;

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final User current_user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.invite_item, parent, false);
            viewHolder.name_surname = (TextView) convertView.findViewById(R.id.name_surname);
            viewHolder.email = (TextView) convertView.findViewById(R.id.email);
            viewHolder.invite_btn = (Button) convertView.findViewById(R.id.invite_btn);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        viewHolder.name_surname.setText(current_user.getName()+" "+current_user.getSurname());
        viewHolder.email.setText(current_user.getEmail());
        viewHolder.invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(),"Invited user ",Toast.LENGTH_SHORT).show();
                query(current_user);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    private void send_invitation(User invited_user,User current_user){

        Date date = new Date();
        String time_stamp = date.toString();


        Invitation invitation = new Invitation(current_user,time_stamp);
        usersDatabaseRefence = firebaseDatabase.getReference().child("invitations/"+invited_user.getUid());
       // User new_user = new User(uid , email , name ,surname, age, category);
        //usersDatabaseRefence.push().setValue(new_user);
        usersDatabaseRefence.push().setValue(invitation);
        Log.d("Send Invitation", "Invitation send");
        usersDatabaseRefence = firebaseDatabase.getReference().child("users_meta/invitation_status/"+current_user.getUid());
        usersDatabaseRefence.(invited_user.getUid());
        usersDatabaseRefence.setValue("invitation send");
    }

    private boolean query (final User invited_user){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        usersDatabaseRefence = firebaseDatabase.getReference().child("users/");


        usersDatabaseRefence.orderByKey().equalTo(user.getUid()).addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,String s){
                User get_user = dataSnapshot.getValue(User.class);
                Log.d("INVITE","USER FOUND :"+get_user.getEmail());
                send_invitation(invited_user,get_user);

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

        return  false;
    }
}