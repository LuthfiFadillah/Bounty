package com.example.android.bounty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int LOC_AWAL = 1;
    public static final int LOC_AKHIR = 2;
    int currentActivities;
    SignInButton signInButton;
    Button signOutButton;
    Button mCreateJobButton;
    Button mFindJobButton;
    Button mProfileButton;
    Button mOptionsButton;
    Button mBackButton;
    Button mBackJobButton;
    Button mSubmitButton;
    Button mBackFindJobButton;
    Button mButtonStartPoint;
    Button mButtonEndPoint;
    LinearLayout mJobButtonsLayout;
    LinearLayout mProfileLayout;
    LinearLayout mSignOutLayout;
    LinearLayout mBackButtonFindJobLayout;
    LinearLayout mMainLayout1;
    LinearLayout mMainLayout2;
    LinearLayout mMainLayout3;
    LinearLayout mMainLayout4;
    TextView statusTextView;
    TextView mLatlngAwal;
    TextView mLatlngAkhir;
    EditText mJobTitleEditText;
    EditText mPriceEditText;
    EditText mDescriptionEditText;
    ListView mListView;
    PointF startPoint;
    PointF endPoint;
    private static final String TAG = "Tes";
    protected GoogleSignInClient mGoogleSignInClient;
    protected final int RC_SIGN_IN = 9009;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivities = 0;
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        mLatlngAwal = (TextView) findViewById(R.id.startPointTextView);
        mLatlngAkhir = (TextView) findViewById(R.id.endPointTextView);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);
        mCreateJobButton = (Button) findViewById(R.id.createJobButton);
        mCreateJobButton.setOnClickListener(this);
        mFindJobButton = (Button) findViewById(R.id.findJobButton);
        mFindJobButton.setOnClickListener(this);
        mProfileButton = (Button) findViewById(R.id.profileButton);
        mProfileButton.setOnClickListener(this);
        mOptionsButton = (Button) findViewById(R.id.optionsButton);
        mOptionsButton.setOnClickListener(this);
        mBackButton = (Button) findViewById(R.id.backButton);
        mBackButton.setOnClickListener(this);
        mBackJobButton = (Button) findViewById(R.id.backJobButton);
        mBackJobButton.setOnClickListener(this);
        mSubmitButton = (Button) findViewById(R.id.submitButton);
        mSubmitButton.setOnClickListener(this);
        mBackFindJobButton = (Button) findViewById(R.id.backFindJobButton);
        mBackFindJobButton.setOnClickListener(this);
        mButtonStartPoint = (Button) findViewById(R.id.buttonStartPoint);
        mButtonStartPoint.setOnClickListener(this);
        mButtonEndPoint = (Button) findViewById(R.id.buttonEndPoint);
        mButtonEndPoint.setOnClickListener(this);
        mJobButtonsLayout = (LinearLayout) findViewById(R.id.jobButtonLayout);
        mProfileLayout  = (LinearLayout) findViewById(R.id.profileButtonLayout);
        mSignOutLayout =  (LinearLayout) findViewById(R.id.signOutButtonLayout);
        mBackButtonFindJobLayout = (LinearLayout) findViewById(R.id.backButtonFindJobLayout);
        mMainLayout1 = (LinearLayout) findViewById(R.id.mainLayout);
        mMainLayout2 = (LinearLayout) findViewById(R.id.mainLayout2);
        mMainLayout3 = (LinearLayout) findViewById(R.id.mainLayout3);
        mMainLayout4 = (LinearLayout) findViewById(R.id.mainLayout4);
        mJobTitleEditText = (EditText) findViewById(R.id.jobTitleEditText);
        mPriceEditText = (EditText) findViewById(R.id.priceEditText);
        mDescriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        mListView = (ListView) findViewById(R.id.jobList);
        mAuth = FirebaseAuth.getInstance();
        startPoint = new PointF(0,0);
        endPoint = new PointF(0,0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.signOutButton:
                signOut();
                visibilityToggle(mMainLayout2);
                visibilityToggle(mMainLayout1);
                currentActivities = 0;
                break;
            case R.id.createJobButton:
                currentActivities = 1;
                mMainLayout1.setVisibility(View.GONE);
                mMainLayout3.setVisibility(View.VISIBLE);
                break;
            case R.id.backJobButton:
                visibilityToggle(mMainLayout1);
                visibilityToggle(mMainLayout3);
                currentActivities = 0;
                break;
            case R.id.optionsButton:
                visibilityToggle(mMainLayout1);
                visibilityToggle(mMainLayout2);
                currentActivities = 2;
                break;
            case R.id.backButton:
                visibilityToggle(mMainLayout1);
                visibilityToggle(mMainLayout2);
                currentActivities = 0;
                break;
            case R.id.submitButton:
                pushData();
                visibilityToggle(mMainLayout1);
                visibilityToggle(mMainLayout3);
                currentActivities = 0;
                break;
            case R.id.findJobButton:
                visibilityToggle(mMainLayout1);
                visibilityToggle(mMainLayout4);
                viewJobs();
                currentActivities = 2;
                break;
            case R.id.backFindJobButton:
                visibilityToggle(mMainLayout1);
                visibilityToggle(mMainLayout4);
                currentActivities = 0;
                break;
            case R.id.buttonStartPoint:
                viewMapsAwal();
                break;
            case R.id.buttonEndPoint:
                viewMapsAkhir();
                break;
        }
    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Sign In!");
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Sign Out!");
        updateUI(currentUser);
    }

    private void visibilityToggle(View v) {
        if (v.getVisibility() == View.VISIBLE) { // Menghilang
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI(FirebaseUser account) {
        if (account == null) {//Not logged in
            statusTextView.setText("Selamat datang, silahkan login untuk memulai");
            signInButton.setVisibility(View.VISIBLE);
            signInButton.setEnabled(true);
            signOutButton.setVisibility(View.GONE);
            mFindJobButton.setVisibility(View.GONE);;
            mCreateJobButton.setVisibility(View.GONE);
            mProfileButton.setVisibility(View.GONE);
            mOptionsButton.setVisibility(View.GONE);
            mProfileLayout.setVisibility(View.GONE);
            mJobButtonsLayout.setVisibility(View.GONE);
            mSignOutLayout.setVisibility(View.GONE);
        } else {
            signInButton.setEnabled(false);
            String name = account.getDisplayName();
            statusTextView.setText("Selamat datang, " + name + "!");
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            mFindJobButton.setVisibility(View.VISIBLE);
            mCreateJobButton.setVisibility(View.VISIBLE);
            mProfileButton.setVisibility(View.VISIBLE);
            mJobButtonsLayout.setVisibility(View.VISIBLE);
            mProfileLayout.setVisibility(View.VISIBLE);
            mBackButton.setVisibility(View.VISIBLE);
            mOptionsButton.setVisibility(View.VISIBLE);
            mMainLayout1.setVisibility(View.VISIBLE);
            mSignOutLayout.setVisibility(View.VISIBLE);
        }
    }
    public void viewMapsAwal() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, LOC_AWAL);
    }

    public void viewMapsAkhir() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, LOC_AKHIR);
    }

    private void pushData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String id = currentUser.getUid();
        String title = mJobTitleEditText.getText().toString();
        int price = Integer.parseInt(mPriceEditText.getText().toString());
        String description = mDescriptionEditText.getText().toString();
        PointF start = startPoint;
        PointF end = endPoint;

        boolean status = false;
        Job job = new Job(title,price,description,start,end,status);
        myRef.child("jobs").child(id).setValue(job);

        Log.d(TAG, "title = " + job.getJobTitle());
        Log.d(TAG, "price = " + job.getPrice());
        Log.d(TAG, "desc = " + job.getDescription());
        Log.d(TAG, "start = " + job.getStartPoint().toString());
        Log.d(TAG, "end = " + job.getEndPoint().toString());
        Log.d(TAG, "status = " + job.isActive());

        mJobTitleEditText.setText("");
        mPriceEditText.setText("");
        mDescriptionEditText.setText("");
    }
    public void viewJobs() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Job job = dataSnapshot.getValue(Job.class);

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Job newJob = dataSnapshot.getValue(Job.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Job movedJob = dataSnapshot.getValue(Job.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postJob:onCancelled", databaseError.toException());

            }
        };
        ref.addChildEventListener(childEventListener);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
        if (requestCode == LOC_AWAL){
            if(resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String latlng = "Start Point : lat= " + bundle.getFloat("latitude") + " long= " + bundle.getFloat("longitude");
                startPoint.x = bundle.getFloat("latitude");
                startPoint.y = bundle.getFloat("longitude");
                mLatlngAwal.setText(latlng);
                mLatlngAwal.setVisibility(View.VISIBLE);
                currentActivities = 1;
                mMainLayout1.setVisibility(View.GONE);
                mMainLayout3.setVisibility(View.VISIBLE);

            }
        } else if (requestCode == LOC_AKHIR){
            if(resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String latlng = "End Point : lat= " + bundle.getFloat("latitude") + " long= " + bundle.getFloat("longitude");
                endPoint.x = bundle.getFloat("latitude");
                endPoint.y = bundle.getFloat("longitude");
                mLatlngAkhir.setText(latlng);
                currentActivities = 1;
                mLatlngAkhir.setVisibility(View.VISIBLE);
                mMainLayout1.setVisibility(View.GONE);
                mMainLayout3.setVisibility(View.VISIBLE);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


}
