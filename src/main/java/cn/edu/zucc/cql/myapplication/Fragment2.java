package cn.edu.zucc.cql.myapplication;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.edu.zucc.cql.myapplication.DB.DBServer;

public class Fragment2 extends Fragment {
    CalendarView calendarView = null;
    ListView noteListView;
    String noteDate = "";
    ArrayList<Note> noteDatas = new ArrayList<Note>();
    Note currentNote = null;
    TextView txvTip = null;
    RelativeLayout rm;
    long date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment2,
                container, false);
        txvTip = (TextView) rootView.findViewById(R.id.txv);
        rm=(RelativeLayout) rootView.findViewById(R.id.adddate);
        rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });
        calendarView = (CalendarView)  rootView.findViewById(R.id.calendarView);
        calendarView.setShowWeekNumber(false);
        date=calendarView.getDate();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year,month,dayOfMonth);
                date = c.getTime().getTime();
                refreshNoteList();
            }
        });
        noteListView = (ListView) rootView.findViewById(R.id.lv1);
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position,long id) {
                refreshNoteList();
            }
        });
        registerForContextMenu(noteListView);
        noteListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("日程编辑");
                contextMenu.add(0, 0, 0, "修改");
                contextMenu.add(0, 1, 0, "删除");
                contextMenu.add(0, 2, 0, "查看");
                Fragment2.super.onCreateContextMenu(contextMenu,view,contextMenuInfo);
            }
        });
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        refreshNoteList();
    }

    private void refreshNoteList() {
        noteDate = getDateString(date);
        loadData();
        if (noteDatas!=null && noteDatas.size()>0) {
            ListAdapter adapter = new ListAdapter(getActivity().getApplicationContext());
            noteListView.setAdapter(adapter);
            txvTip.setText(noteDate+" 日程列表：");
        }else {
            txvTip.setText(noteDate+" 暂无记录");
            ListAdapter adapter = new ListAdapter(getActivity().getApplicationContext());
            noteListView.setAdapter(adapter);
        }
    }

    private void loadData() {
        noteDatas = DBServer.searchNoteByDate(getActivity().getApplicationContext(), noteDate);
    }

    private void newNote() {
        Intent intent = new Intent(getActivity(),EditNoteActivity.class);
        intent.putExtra("UItype", "newNote");
        intent.putExtra("noteDate", noteDate);
        startActivity(intent);
    }

    private void showNote(Note note) {
        Intent intent = new Intent(getActivity(),EditNoteActivity.class);
        intent.putExtra("UItype", "showNote");
        intent.putExtra("note", note);
        startActivity(intent);
    }
    private void editNote(Note note) {
        Intent intent = new Intent(getActivity(),EditNoteActivity.class);
        intent.putExtra("UItype", "editNote");
        intent.putExtra("note", note);
        startActivity(intent);
    }

    private String getDateString(long date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate  =  new Date(date);
        return  formatter.format(curDate);
    }

    class ListAdapter extends BaseAdapter {

        private Context mContext = null;
        private LayoutInflater mInflater = null;

        public ListAdapter(Context c) {
            mContext = c;
            mInflater = LayoutInflater.from(this.mContext);
        }

        @Override
        public int getCount() {
            return noteDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewHolder holder;
            if (convertView == null) {
                holder = new ListViewHolder();
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder.txvName   = (TextView) convertView.findViewById(R.id.txvName);
                convertView.setTag(holder);
            } else {
                holder=(ListViewHolder) convertView.getTag();
            }
            holder.txvName.setText(noteDatas.get(position).title);
            return convertView;
        }
        class ListViewHolder {
            TextView txvName;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();  //获得AdapterContextMenuInfo,以此来获得选择的listview项目
        switch (item.getItemId()) {
            case 0:
                editNote(noteDatas.get(menuInfo.position));
                break;
            case 1:
                deleteNote(noteDatas.get(menuInfo.position));
                refreshNoteList();
                break;
            case 2:
                showNote(noteDatas.get(menuInfo.position));
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteNote(Note note) {
        DBServer.deleteNoteByDate(getActivity().getApplicationContext(), note.id);
    }
}