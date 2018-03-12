package de.patruck.florian.todo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AddTODOActivity extends AppCompatActivity {

    public static final String TODO_TEXT = "todo_text";
    public static final String TODO_DATE = "todo_date";
    public static final String TODO_EVERY = "todo_every";
    public static final String TODO_DAY = "todo_day";

    private ConstraintLayout what;
    private EditText todoText;
    private Button finish;

    private ConstraintLayout when;
    private RadioGroup radioGroup;
    private DatePicker datePicker;
    private ConstraintLayout days;

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

        what = findViewById(R.id.constraintLayout_What);
        what.setVisibility(View.VISIBLE);
        todoText = findViewById(R.id.et_todo);
        finish = findViewById(R.id.btn_finish);

        when = findViewById(R.id.constraintLayout_When);
        when.setVisibility(View.GONE);
        datePicker = findViewById(R.id.date_picker);
        datePicker.setVisibility(View.VISIBLE);
        radioGroup = findViewById(R.id.radio_group);
        days = findViewById(R.id.constraintLayout_days);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_on: {
                        datePicker.setVisibility(View.VISIBLE);
                        days.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.rb_every: {
                        datePicker.setVisibility(View.GONE);
                        days.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra(TODO_TEXT, todoText.getText().toString());
                //NOTE: getMonth() + 1 because datePicker returns zero indexed month
                intent.putExtra(TODO_DATE, new int[]{datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear()});
                intent.putExtra(TODO_EVERY, radioGroup.getCheckedRadioButtonId() == R.id.rb_every);
                intent.putExtra(TODO_DAY, getByteOfWeek(days));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //NOTE: constraintLayout has to hold checkboxes!
    private byte getByteOfWeek(ConstraintLayout constraintLayout) {
        if(BuildConfig.DEBUG && constraintLayout.getChildCount() > 8) {
            throw new AssertionError();
        }

        byte result = 0, orrer = 1;

        for(int i = 0; i < constraintLayout.getChildCount(); ++i, orrer <<= 1) {
            CheckBox checkBox = (CheckBox) constraintLayout.getChildAt(i);
            if(checkBox.isChecked()) {
                result |= orrer;
            }
        }

        return result;
    }
}
