package br.com.levisaturnino.tipster;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.renderscript.Double2;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import br.com.levisaturnino.tipster.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private  int  radioCheckId = -1;

    ActivityMainBinding binding;
    private View.OnKeyListener mKeyListener;
    private NumberPickerLogic mLogic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.txtAmount.requestFocus();

        binding.btnCalculate.setEnabled(false);
        binding.txtTipOther.setEnabled(false);

        binding.RadioGroupTips.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioFifteen || checkedId == R.id.radioTwenty){
                    binding.txtTipOther.setEnabled(false);

                    binding.btnCalculate.setEnabled(binding.txtAmount.getText().length() > 0 && binding.txtPeople.getText().length() > 0);
                }

                if(checkedId == R.id.radioOther){
                    binding.txtTipOther.setEnabled(true);
                    binding.txtTipOther.requestFocus();
                    binding.btnCalculate.setEnabled(
                            binding.txtAmount.getText().length() > 0
                                    && binding.txtPeople.getText().length() > 0
                                    && binding.txtTipOther.getText().length() > 0);

                }

            }
        });

        binding.txtAmount.setOnKeyListener(mKeyListener);
        binding.txtPeople.setOnKeyListener(mKeyListener);
        binding.txtTipOther.setOnKeyListener(mKeyListener);

        mKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                switch (v.getId())
                {
                    case R.id.txtAmount:
                    case R.id.txtPeople:
                        binding.btnCalculate.setEnabled(binding.txtAmount.getText().length() > 0 && binding.txtPeople.getText().length() > 0);
                        break;
                    case R.id.txtTipOther:
                        binding.btnCalculate.setEnabled(binding.txtAmount.getText().length() > 0 && binding.txtPeople.getText().length() > 0);
                        break;
                }


                return false;
            }
        };

        View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(v.getId() == R.id.btnCalculate){
                   calculate();
               }else{
                   reset();
               }
            }
        };
        binding.btnCalculate.setOnClickListener(mClickListener);
        binding.btnReset.setOnClickListener(mClickListener);

        /** Create a NumberPickerLogic to handle the + and - keys */
        mLogic = new NumberPickerLogic(binding.txtPeople, 1, Integer.MAX_VALUE);
    }

    private void reset() {
        binding.txtAmount.setText("");
        binding.txtTotalToPay.setText("");
        binding.txtTipPerPerson.setText("");
        binding.txtTipAmount.setText("");
        binding.txtPeople.setText("");
        binding.txtTipOther.setText("");
        binding.RadioGroupTips.clearCheck();
        binding.RadioGroupTips.check(R.id.radioFifteen);
        binding.txtAmount.requestFocus();
    }

    private void calculate() {

        Double billAmount = Double.parseDouble(binding.txtAmount.getText().toString());
        Double totalPeople = Double.parseDouble(binding.txtPeople.getText().toString());

        Double percentage = null;

        boolean isError = false;

        if(billAmount < 1.0){
            showErroralert("Enter a valid Total Amount.",binding.txtAmount.getId());
            isError = true;
        }

        if(totalPeople < 1.0){
            showErroralert("Enter a valid value for No. of People.",binding.txtPeople.getId());
            isError = true;
        }

        if(radioCheckId == R.id.radioFifteen){
            percentage = 15.00;
        }else if(radioCheckId == R.id.radioOther){
            percentage = Double.parseDouble(binding.txtTipOther.getText().toString());

            if(percentage < 1.0){
                showErroralert("Enter a valid Tip percentage",binding.txtTipOther.getId());
                isError = true;
            }
        }

        if(!isError){
            Double tipAmount = ((billAmount * percentage)/100);
            Double totalToPay = billAmount + tipAmount;

            binding.txtAmount.setText(tipAmount.toString());
            binding.txtTotalToPay.setText(totalToPay.toString());
        }
    }

    private void showErroralert(String errorMessage, final int fieldId) {

        new AlertDialog.Builder(this).setTitle("Error")
                .setMessage(errorMessage).setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                findViewById(fieldId).requestFocus();
            }
        }).show();

    }

    public void decrement(View v) {
        mLogic.decrement();
    }

    public void increment(View v) {
        mLogic.increment();
    }
}
