    package me.twodee.friendlyneighbor;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;

    import androidx.appcompat.app.AppCompatActivity;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;

    public class OtpVerifiedActivity extends AppCompatActivity {

        private FirebaseAuth mAuth;
        private FirebaseUser mCurrentUser;

        private Button mLogoutBtn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_otp_verified);

            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();

            mLogoutBtn = findViewById(R.id.logout_btn);

            mLogoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mAuth.signOut();
                    sendUserToLogin();

                }
            });

        }

        @Override
        protected void onStart() {
            super.onStart();
            if(mCurrentUser == null){
                sendUserToLogin();
            }
        }

        private void sendUserToLogin() {
            Intent loginIntent = new Intent(OtpVerifiedActivity.this, SignInActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        }
    }
