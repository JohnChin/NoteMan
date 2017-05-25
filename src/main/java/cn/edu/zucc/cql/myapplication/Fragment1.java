package cn.edu.zucc.cql.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cn.edu.zucc.cql.myapplication.DB.NotesDB;


public class Fragment1 extends Fragment implements CardsAdapter.InnerItemOnclickListener{
    private ListView mlistview;
    private Cursor cursor;
    private SQLiteDatabase dbReader;
    private NotesDB notesDB;
    private CardsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment1,
                container, false);
        mlistview = (ListView) rootView.findViewById(R.id.cards_list);
        mlistview.setNestedScrollingEnabled(true);
        notesDB = new NotesDB(this.getActivity());
        dbReader = notesDB.getReadableDatabase();
        selectDB();
        adapter.setOnInnerItemOnClickListener(this);
        mlistview.setAdapter(adapter);
        return rootView;
    }
    public void selectDB() {
        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
        adapter = new CardsAdapter(this.getActivity(), cursor);
    }
    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.card:
                cursor.moveToPosition(position);
                Intent i = new Intent(this.getActivity(), NoteActivity.class);
                i.putExtra(NotesDB.ID, cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(notesDB.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(notesDB.TIME, cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                startActivity(i);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        selectDB();
    }
}