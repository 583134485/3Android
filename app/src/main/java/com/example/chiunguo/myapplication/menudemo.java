package com.example.chiunguo.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

public class menudemo extends AppCompatActivity {

 private TextView contextview ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menudemo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.logintoolbar);
        setSupportActionBar(toolbar);

        //contextMenu  register
        contextview=(TextView) findViewById(R.id.textView4);
        registerForContextMenu(contextview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.item1:
//                //newGame();
//                toast("item1");
//                return true;
//            case R.id.item2:
//                // showHelp();
//                toast("item2");
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.red:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
            case R.id.blue:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
  @Override
public void onCreateContextMenu(ContextMenu menu, View v,
                                ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.context_menu, menu);
}

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.item2:
               // editNote(info.id);
                toast("tiem2");
                return true;
            case R.id.item3:
               // deleteNote(info.id);
                toast("item3");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void toast(String m){
        Context context = getApplicationContext();
        CharSequence text = m;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.lpig:
                        //archive(item);
                        toast("lpig");
                        return true;
                    case R.id.bpig:
                        // delete(item);
                        toast("bpig");
                        return true;
                    default:
                        return false;
                }
            }
        });
    }





}
