package cn.edu.zucc.cql.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.edu.zucc.cql.myapplication.DB.DBServer;

public class EditNoteActivity extends Activity {

    RelativeLayout etNoteLayout = null;
    EditText etNoteTitle  =  null;
    Button btnSave	 = null;
    Button btnDelete = null;
    Button btnEdit	 = null;
    String noteTitle	="";
    String noteDate 	="";
    Note currentNote = null;
    String UItype = "" ;
    ImageButton im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_note);

        initView();
        UItype = getIntent().getStringExtra("UItype");
        if (UItype.equals("newNote")) {
            showNewNoteUI();
        }
        else if (UItype.equals("showNote")) {
            showTextNoteUI();
        }
        else if (UItype.equals("editNote")) {
            showEditNoteUI();
        }
    }

    private void showTextNoteUI() {
        currentNote = (Note) getIntent().getSerializableExtra("note");
        etNoteLayout.setVisibility(View.VISIBLE);
        etNoteTitle.setText(currentNote.title);
        etNoteTitle.setEnabled(false);
        etNoteTitle.setBackgroundColor(Color.WHITE);
        etNoteTitle.setTextColor(Color.BLACK);

        btnSave.setVisibility(View.GONE);
        btnDelete.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.VISIBLE);
    }

    private void showNewNoteUI() {
        etNoteLayout.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
    }

    private void showEditNoteUI() {

        etNoteLayout.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        currentNote = (Note) getIntent().getSerializableExtra("note");
        etNoteTitle.setText(currentNote.title);;
    }


    private void initView() {

        etNoteLayout =  (RelativeLayout) findViewById(R.id.etNoteLayout);
        etNoteTitle  =  (EditText) findViewById(R.id.etNoteTitle);
        btnSave		 =  (Button)findViewById(R.id.btnSave);
        btnDelete	 =  (Button)findViewById(R.id.btnDelete);
        btnEdit		 =  (Button)findViewById(R.id.btnEdit);
        im=(ImageButton)findViewById(R.id.imb2);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (UItype.equals("newNote")) {
                    saveNewNote();
                }
                if (UItype.equals("editNote")) {
                    saveEditNote();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                deleteNote(currentNote);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                editNote(currentNote);
            }
        });
    }


    private void editNote(Note currentNote) {

        Intent intent = new Intent(EditNoteActivity.this,EditNoteActivity.class);
        intent.putExtra("UItype", "editNote");
        intent.putExtra("note", currentNote);
        startActivity(intent);
        this.finish();
    }


    private void saveNewNote() {

        noteDate = getIntent().getStringExtra("noteDate");
        noteTitle 	= etNoteTitle.getText().toString().trim();

        if (!noteTitle.equals("")) {
            currentNote = new Note();
            currentNote.title= noteTitle;
            currentNote.date= noteDate;
            DBServer.addNote(getApplicationContext(), currentNote);
            EditNoteActivity.this.finish();

        }else {
            Toast.makeText(getApplicationContext(), R.string.empty,Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteNote(Note note) {
        DBServer.deleteNoteByDate(getApplicationContext(), note.id);
        EditNoteActivity.this.finish();
    }

    private void saveEditNote() {

        currentNote = (Note) getIntent().getSerializableExtra("note");
        noteTitle 	= etNoteTitle.getText().toString().trim();

        if (!noteTitle.equals("")) {
            currentNote.title= noteTitle;
            DBServer.updateNote(getApplicationContext(), currentNote);
            EditNoteActivity.this.finish();

        }else {
            Toast.makeText(getApplicationContext(),R.string.empty,Toast.LENGTH_SHORT).show();
        }
    }



}
