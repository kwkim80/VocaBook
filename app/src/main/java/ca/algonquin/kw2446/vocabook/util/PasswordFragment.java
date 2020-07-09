package ca.algonquin.kw2446.vocabook.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ca.algonquin.kw2446.vocabook.R;

public class PasswordFragment extends DialogFragment {


    private  OnOkClickListener okClickListener;


    public interface OnOkClickListener{
        void onOkClicked();
    }

    public PasswordFragment() {
    }

    public void setOkClickListener(OnOkClickListener listener){
        okClickListener=listener;
    }

    public static PasswordFragment newInstance(OnOkClickListener listener) {

        Bundle args = new Bundle();

        PasswordFragment fragment = new PasswordFragment();
        fragment.setOkClickListener(listener);
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String pwd=PreferenceManager.getString(getContext(),"pwd");
        View v= LayoutInflater.from(getContext()).inflate(R.layout.prompt,null, false);

        final EditText userInput = (EditText) v.findViewById(R.id.editTextDialogUserInput);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(v);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String inputPwd = userInput.getText().toString();
                if(pwd.equalsIgnoreCase(inputPwd)){
                        okClickListener.onOkClicked();
                }
                else{
                    Toast.makeText(getContext(), "The password is not matched.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("취소", null);
        return builder.create();

    }
}
