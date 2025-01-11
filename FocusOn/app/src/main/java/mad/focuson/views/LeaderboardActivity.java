package mad.focuson.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mad.focuson.R;
import mad.focuson.views.adapters.LeaderboardAdapter;

public class LeaderboardActivity extends AppCompatActivity {
    Spinner spinner;
    ListView listView;
    private ArrayList<Map<String, Object>> leaderboard = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.imgBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.ranks);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("leaderboard");

        ref.orderBy("score", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> user = new HashMap<>();
                                // TODO: improve: reduce it to one table
                                user.put("score", document.getData().get("score"));
                                user.put("userId", ((DocumentReference) document.getData().get("userId")).getPath().replace("users/",""));
                                leaderboard.add(user);
                                Log.d("success", document.getId() + " => " + document.getData());
                            }
                           // notifyDatasetChanged
                            ((LeaderboardAdapter) listView.getAdapter()).notifyDataSetChanged();
                        } else {
                            Log.w("error", "Error getting documents", task.getException());
                        }

                    }
                });



        listView.setAdapter(new LeaderboardAdapter(this, leaderboard));
        

    }
}

