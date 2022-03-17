package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProduceEstimator extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Context context = this;

    Button buttonProduceEstimatorCalculate = null;
    Button buttonProduceEstimatorEditProfiles = null;
    EditText editTextProduceEstimatorQuantity = null;
    Spinner spinnerProduceEstimatorSpinner = null;
    TextView ProduceEstimatorErrorMsg = null;
    TextView textViewProduceEstimatorResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produce_estimator);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        buttonProduceEstimatorCalculate = findViewById(R.id.buttonProduceEstimatorCalculate);
        buttonProduceEstimatorEditProfiles = findViewById(R.id.buttonProduceEstimatorEditProfiles);
        editTextProduceEstimatorQuantity = findViewById(R.id.editTextProduceEstimatorQuantity);
        spinnerProduceEstimatorSpinner = findViewById(R.id.spinnerProduceEstimatorSpinner);
        ProduceEstimatorErrorMsg = findViewById(R.id.ProduceEstimatorErrorMsg);
        textViewProduceEstimatorResult = findViewById(R.id.textViewProduceEstimatorResult);

        ToolServices.makeOptionsButtonVisibleForManager(currentUser, buttonProduceEstimatorEditProfiles);

        // update the spinner for profiles
        //
        ToolServices.updateSpinnerWithProduceEstimatorProfiles(spinnerProduceEstimatorSpinner,context);

        buttonProduceEstimatorCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isValid = true;

                while (isValid) {


                    isValid = GeneralServices.checkIfSpinnerIsNull(spinnerProduceEstimatorSpinner);
                    if (!isValid) {
                        Log.d("ShippingCalculator validation :", "No profile name");
                        ProduceEstimatorErrorMsg.setText("There are no profiles for this farm yet.");
                        break;
                    }
                    ProduceEstimatorErrorMsg.setText("");

                    isValid = GeneralServices.checkEditTextIsNotNull(editTextProduceEstimatorQuantity.getText().toString());
                    if (!isValid) {
                        Log.d("ShippingCalculator validation :", "No quantity entered");
                        ProduceEstimatorErrorMsg.setText("You must enter a quantity first.");

                        break;
                    }
                    ProduceEstimatorErrorMsg.setText("");

                    if(isValid)
                    {

                        ToolServices.getProfileClassAndCallPerformProduceEstimation(Integer.parseInt(editTextProduceEstimatorQuantity.getText().toString()),
                                spinnerProduceEstimatorSpinner.getSelectedItem().toString(),textViewProduceEstimatorResult);
                        break;
                    }
                }
            }
        });


        buttonProduceEstimatorEditProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProduceEstimator.this, ProduceEstimatorManagerProfiles.class));

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ToolServices.updateSpinnerWithProduceEstimatorProfiles(spinnerProduceEstimatorSpinner,context);
    }
}