package ca.algonquin.kw2446.vocabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.algonquin.kw2446.vocabook.util.ApplicationClass;
import ca.algonquin.kw2446.vocabook.util.PreferenceManager;

public class SettingActivity extends AppCompatActivity {

    EditText etPwd, etNewPwd;
    Button btnChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        etPwd=findViewById(R.id.etPwd);
        etNewPwd=findViewById(R.id.etNewPwd);

    }

    public void changePwd(View view) {
        String pwd=etPwd.getText().toString().trim();
        String newPwd=etNewPwd.getText().toString().trim();
        if(pwd.isEmpty() || newPwd.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }else if(!pwd.equals(PreferenceManager.getString(this,"pwd"))){
            Toast.makeText(this, "The password is not matched", Toast.LENGTH_SHORT).show();
        }else{
            PreferenceManager.setString(this,"pwd",newPwd);
            ApplicationClass.password=newPwd;
            etPwd.setText("");
            etNewPwd.setText("");
            Toast.makeText(this, "New password is setted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}