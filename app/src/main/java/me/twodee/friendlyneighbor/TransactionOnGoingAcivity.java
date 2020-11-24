package me.twodee.friendlyneighbor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.twodee.friendlyneighbor.service.TransactionService;

public class TransactionOnGoingAcivity extends AppCompatActivity {

    Button stopTransactionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_on_going_acivity);

        stopTransactionButton = findViewById(R.id.stop_transaction_service_btn);
        stopTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTransactionService();
            }
        });
    }

    void stopTransactionService() {
        Intent transactionIntent = new Intent(this, TransactionService.class);
        stopService(transactionIntent);

    }
}