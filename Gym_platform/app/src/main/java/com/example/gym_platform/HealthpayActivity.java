package com.example.gym_platform;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HealthpayActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthpay);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        final TextView Text_start = (TextView)findViewById(R.id.start);
        final TextView Text_end = (TextView)findViewById(R.id.end);


        Button btnPayReserve = (Button) findViewById(R.id.botton);
        btnPayReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UID = currentUser.getEmail();
                Date Ustart = new Date();
                Calendar cal = Calendar.getInstance();
                cal.add(cal.MONTH,3);
                Date Uend = cal.getTime();
                String Ukind = "3개월";
                addDocument(UID,Ustart,Uend,Ukind);
            }
        });
    }

    public void addDocument(String name, Date start, Date end, String kind){
        Map<String, Object> user = new HashMap<>();
        user.put("userName", name );
        user.put("userStart",start);
        user.put("userEnd", end);
        user.put("userKind",kind);

        // Add a new document with a generated ID
        db.collection("sangwoousers")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("a","결제 성공 " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("a", "결제 실패", e);
                    }
                });
    }
}
