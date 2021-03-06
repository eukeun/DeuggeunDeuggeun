package com.example.gym_platform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final int RC_SIGN_IN = 10;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("a","LoginActivity ??????//1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        //
        //????????? ????????? ???
        //
        //  Log.d("a","LoginActivity ??????//2");
        final EditText EditText_email = (EditText) findViewById(R.id.EditText_Email);
        final EditText EditText_password = (EditText) findViewById(R.id.EditText_password);
        Button JoinButton = (Button) findViewById(R.id.Button_Join);
        Button LoginButton = (Button) findViewById(R.id.Button_Login);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EditText_email.getText().toString();
                String password = EditText_password.getText().toString();
                firebase_login_email(email, password);
            }
        });

        JoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
                finish();
            }
        });

        //
        //google ????????? ???
        //
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //??????????????? ????????? ?????? ?????????
        SignInButton button = (SignInButton) findViewById(R.id.SigninButton_glogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("???????????????");
                //????????? ???????????????, ??????????????? ????????? ?????? (??????????????? ???????????? ????????? ???)
                //"?????? ?????????????????? ??????????????? ?????? ????????????? "??????????????????
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    public void firebase_login_email(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        Toast.makeText(LoginActivity.this, "???????????? ?????? id ?????????.", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(LoginActivity.this, "????????? ????????? ?????? ????????????.", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseNetworkException e) {
                        Toast.makeText(LoginActivity.this, "Firebase NetworkException", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    currentUser = mAuth.getCurrentUser();
                    startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                    finish();
                }

            }
        });
    }

    @Override
    public void onStart(){
        //Log.d("a","LoginActivity ??????//3");
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            startActivity(new Intent(LoginActivity.this,NavigationActivity.class));
            finish();
        }
    }

    //Google Login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) { //???????????? ????????? ?????????, ??????????????? ???????????? ???????????? ??????
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account); //??????????????? ????????? ???????????? ????????????????????? ?????????
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    //????????????????????? ????????????
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //????????????????????? ?????? ?????????????????? ????????? ???????????? ?????? ???????????? ??????
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Toast.makeText(LoginActivity.this, "????????? ????????????", Toast.LENGTH_SHORT).show();
                            currentUser = mAuth.getCurrentUser();
                            /////////////////////////////////////////
                            //?????? ???????????? ???????????? ????????? 0?????? ?????????
                            DocumentReference docRef = db.collection("User").document(currentUser.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {


                                        } else {
                                            addDocument();
                                        }
                                    } else {
                                        Log.d("a", "get failed with ", task.getException());
                                    }
                                }
                            });
                            /////////////////////////////////////////

                            Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                            //          Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void addDocument() {
        currentUser = mAuth.getCurrentUser();
        Map<String, Object> User = new HashMap<>();
        User.put("userName", currentUser.getDisplayName());
        User.put("userEmail", currentUser.getEmail());
        User.put("userPoint", 0);
        User.put("userUID", currentUser.getUid());

        db.collection("User").document(currentUser.getUid())
                .set(User)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("a", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("a", "Error writing document", e);
                    }
                });
    }
    public void ReadData(){

    }
}
