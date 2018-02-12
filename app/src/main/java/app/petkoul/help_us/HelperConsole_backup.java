package app.petkoul.help_us;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HelperConsole_backup extends AppCompatActivity {


    private List<FriendlyMessage> messageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Message_Adapter mAdapter;

    public static final String MESSAGES_CHILD = "messages";


    private String mUsername;


    private Button mSendButton;

    private ProgressBar mProgressBar;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;
    private GoogleApiClient mGoogleApiClient;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDatabaseRefence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_console_backup);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mUsername = mFirebaseUser.getEmail();


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();



        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);


        recyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);

        mAdapter = new Message_Adapter(messageList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);


        mMessageEditText = (EditText)findViewById(R.id.messageEditText);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mFirebaseUser.getEmail(),
                        null, null);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD+"/"+mFirebaseUser.getUid()).push().setValue(friendlyMessage);
                Log.d("----------------------",mFirebaseUser.getUid());
                mMessageEditText.setText("");
               // mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
            }
        });


        download_messages();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.helper_console_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                Toast.makeText(getApplicationContext(),"You have sign out", Toast.LENGTH_LONG).show();
                mFirebaseAuth.signOut();
                Intent in = new Intent(this , LoginScreen.class);
                startActivity(in);
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                Toast.makeText(getApplicationContext(),"Item 3 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void download_messages( ) {




        usersDatabaseRefence = firebaseDatabase.getReference().child(MESSAGES_CHILD+"/"+mFirebaseUser.getUid());
        Log.d("messages","Download messages");

        usersDatabaseRefence.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //User user = dataSnapshot.getValue(User.class);
                //list.add(user);
                //users_list.add(dataSnapshot.getValue().toString());
                Log.d("messages", "Category" + dataSnapshot.getValue());
                FriendlyMessage message = dataSnapshot.getValue(FriendlyMessage.class);
                messageList.add(message);
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size()-1);
                mProgressBar.setVisibility(View.INVISIBLE);    
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

}
