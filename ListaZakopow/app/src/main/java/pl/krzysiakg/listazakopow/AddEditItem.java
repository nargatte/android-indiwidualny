package pl.krzysiakg.listazakopow;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEditItem extends AppCompatActivity {

    int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);
        Intent intent = getIntent();
        int operaition = intent.getIntExtra("requestCode", MainFragment.ADD);
        if(operaition != MainFragment.ADD){
            Button b = findViewById(R.id.button);
            b.setText("Modyfikuj");

            EditText editText = findViewById(R.id.NameTextBox);
            editText.setText(intent.getStringExtra("name"));

            number = intent.getIntExtra("number", -1);
        }
    }

    public void click(View view) {
        Intent returnIntent = new Intent();
        EditText editText = findViewById(R.id.NameTextBox);
        returnIntent.putExtra("result",editText.getText().toString());
        returnIntent.putExtra("number", number);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
