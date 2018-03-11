package de.patruck.florian.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AddTODOActivity extends AppCompatActivity {

    public static final String TODO_TEXT = "todo_text";
    public static final String TODO_DATE = "todo_date";
    public static final String TODO_EVERY = "todo_every";

    private ConstraintLayout what;
    private EditText todoText;
    private Button finish;

    private ConstraintLayout when;
    private RadioGroup radioGroup;
    private DatePicker date;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.What:
                    when.setVisibility(View.GONE);
                    what.setVisibility(View.VISIBLE);
                    return true;
                case R.id.When:
                    what.setVisibility(View.GONE);
                    when.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        what = (ConstraintLayout) findViewById(R.id.constraintLayout_What);
        what.setVisibility(View.VISIBLE);
        todoText = (EditText) findViewById(R.id.et_todo);
        finish = (Button) findViewById(R.id.btn_finish);

        when = (ConstraintLayout) findViewById(R.id.constraintLayout_When);
        when.setVisibility(View.GONE);
        date = (DatePicker) findViewById(R.id.date_picker);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra(TODO_TEXT, todoText.getText().toString());
                intent.putExtra(TODO_DATE, new int[]{date.getDayOfMonth(), date.getMonth(), date.getYear()});
                intent.putExtra(TODO_EVERY, radioGroup.getCheckedRadioButtonId() == R.id.rb_every);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
