package com.zane.mapmarker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.zane.mapmarker.classes.QueryBuilderFriendly;
import com.zane.mapmarker.superclasses.DbHelper;
import com.zane.mapmarker.superclasses.QueryBuilder;
import com.zane.mapmarker.superclasses.Table;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DbHelper.context = this;
        DbHelper.qb = new QueryBuilder();
        DbHelper helper = new DbHelper(this);
        Cursor tables = helper.getTables();
        //System.out.println(tables.getCount());

        if(tables.moveToFirst()){

            do{
                Cursor trovati = helper.getRows(tables.getString(0));
                if(trovati.moveToFirst()){
                    do{
                        System.out.println(trovati.getInt(0));
                    }while(trovati.moveToNext());
                }
                HashMap<String,String> parametri = new HashMap<>();
                parametri.put("nome","Dennis");
                helper.insert(tables.getString(0),parametri);
            }while(tables.moveToNext());
        }
        /*HashMap<String,String> values = new HashMap<String,String>();
        String[] types = new String[3];
        values.put("nome","VARCHAR 200");
        values.put("cognome","VARCHAR 200");
        values.put("codice_fiscale","VARCHAR 16");
        HashMap<String,Object> somevalue = new HashMap<String,Object>();
        somevalue.put("id_utente",1);
        somevalue.put("codice_fiscale","lol");
        DbHelper.context = this;
        Table tab = new Table("Utenti","codice_fiscale",values,new String[]{"","","UNIQUE"});
        //Ora provo a fare query - SELECT
        QueryBuilder que = new QueryBuilder();
        que.Select(new String[]{"nome","cognome","codice_fiscale"},somevalue,new Table("Utenti","ciao",null,null));

        //JOIN
        HashMap<String,Object> where = new HashMap<String,Object>();
        where.put("azienda","WEGO");
        //Questo è l'on
        ArrayList<HashMap<String,Table>> on = new ArrayList<>();
        HashMap<String,Table> dati_uno = new HashMap<String,Table>();
        HashMap<String,Table> dati_due = new HashMap<String,Table>();
        dati_uno.put("id_utente",new Table("Utenti","ciao",null,null));
        dati_due.put("id_utente",new Table("Aziende","ciao",null,null));
        on.add(0,dati_uno);
        on.add(1,dati_due);
        que.Join(new String[]{"azienda", "indirizzo"}, on, where);
        //Join doppio!!
        ArrayList<HashMap<String,Table>> on_due = new ArrayList<HashMap<String,Table>>();
        dati_uno = new HashMap<String,Table>();
        dati_due = new HashMap<String,Table>();
        dati_uno.put("id_azienda", new Table("Aziende", "ciao", null, null));
        dati_due.put("id_azienda", new Table("Informatica", "ciao", null, null));
        on_due.add(0, dati_uno);
        on_due.add(1, dati_due);
        que.Join(new String[]{"settore", "impiegati"}, on_due, where);
        //UPDATE
        HashMap<String,Object> update = new HashMap<String,Object>();
        update.put("nome", "Dennis");
        update.put("cognome", "Zanetti");
        update.put("codice_fiscale","ZNTDNS93S23G642K");
        where = new HashMap<String,Object>();
        where.put("codice_fiscale", "ZNTDNS");
        que.Update(update, where, new Table("Utenti", "ciao", null, null));
        que.OrderBy(new String[]{"nome"}, new Table("Utenti", "ciao", null, null));
        //DELETE
        que.Delete(where, new Table("Utenti", "ciao", null, null)).OrderBy(new String[]{"nome"},new Table("Utenti","ciao",null,null)).Limit("1", "");
        //UPDATE JOIN
        update = new HashMap<>();
        update.put("nome","Dennis");
        where = new HashMap<>();
        where.put("nome", "Sinned");
        que.Update(update, where, new Table("Utenti", "ciao", null, null));
        ArrayList<HashMap<String,Table>> utentiaziende = new ArrayList<>();
        HashMap<String,Table> utenti = new HashMap<>();
        utenti.put("id_azienda",new Table("Utenti","ciao",null,null));
        utenti.put("fulltime", new Table("Utenti", "ciao", null, null));
        HashMap<String,Table> aziende = new HashMap<>();
        aziende.put("id_azienda",new Table("Aziende","ciao",null,null));
        aziende.put("fulltime",new Table("Aziende","ciao",null,null));
        utentiaziende.add(utenti);
        utentiaziende.add(aziende);
        que.JoinUpdate(update,utentiaziende,where);
        System.out.println(que.getQuery());
        //QueryFriendly
        QueryBuilderFriendly que_friendly = new QueryBuilderFriendly();
        que_friendly.Select("Utenti", new String[]{"nome", "cognome"}, new String[]{"id_utente"}, new Object[]{"1"});
        //System.out.println(que_friendly.getQuery());
        //Query History
        TextView testo = (TextView) this.findViewById(R.id.val_sql);
        testo.setText(QueryBuilder.queryHistoryString());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
