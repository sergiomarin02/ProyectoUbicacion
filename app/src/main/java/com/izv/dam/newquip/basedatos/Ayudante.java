package com.izv.dam.newquip.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

public class Ayudante extends SQLiteOpenHelper {

    private static final int VERSION = 2;

    public Ayudante(Context context) {
        super(context, ContratoBaseDatos.BASEDATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql="create table if not exists " + ContratoBaseDatos.TablaNota.TABLA +
                " (" +
                ContratoBaseDatos.TablaNota._ID + " integer primary key autoincrement , " +
                ContratoBaseDatos.TablaNota.TITULO + " text, " +
                ContratoBaseDatos.TablaNota.NOTA + " text " +
//                ContratoBaseDatos.TablaNota.UBICACION + " text " +
                ")";
        Log.v("sql",sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists " + ContratoBaseDatos.TablaNota.TABLA;
        db.execSQL(sql);
        Log.v("sql",sql);
    }
}