package com.izv.dam.newquip.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

public class Nota implements Parcelable {

    private long id;
    private String titulo, nota;

    public Nota() {
        this(0, null, null);
    }

    public Nota(long id, String titulo, String nota) {
        this.id = id;
        this.titulo = titulo;
        this.nota = nota;
//        this.ubicacion = ubicacion;
    }

    protected Nota(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        nota = in.readString();
//        ubicacion = in.readString();
    }

    public static final Creator<Nota> CREATOR = new Creator<Nota>() {
        @Override
        public Nota createFromParcel(Parcel in) {
            return new Nota(in);
        }

        @Override
        public Nota[] newArray(int size) {
            return new Nota[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(String id) {
        try {
            this.id = Long.parseLong(id);
        } catch(NumberFormatException e){
            this.id = 0;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

//    public String getUbicacion() {return ubicacion; }
//
//    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public ContentValues getContentValues(){
        return this.getContentValues(false);
    }

    public ContentValues getContentValues(boolean withId){
        ContentValues valores = new ContentValues();
        if(withId){
            valores.put(ContratoBaseDatos.TablaNota._ID, this.getId());
        }
        valores.put(ContratoBaseDatos.TablaNota.TITULO, this.getTitulo());
        valores.put(ContratoBaseDatos.TablaNota.NOTA, this.getNota());
//        valores.put(ContratoBaseDatos.TablaNota.UBICACION, this.getUbicacion());
        return valores;
    }

    public static Nota getNota(Cursor c){
        Nota objeto = new Nota();
        objeto.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaNota._ID)));
        objeto.setTitulo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.TITULO)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.NOTA)));
//        objeto.setUbicacion(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.UBICACION)));
        return objeto;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nota='" + nota + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(nota);
//        dest.writeString(ubicacion);
    }
}