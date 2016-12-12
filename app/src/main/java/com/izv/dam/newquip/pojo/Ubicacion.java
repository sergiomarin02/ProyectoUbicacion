package com.izv.dam.newquip.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by sergio on 05/12/2016.
 */
@DatabaseTable
public class Ubicacion {

    public static final String ID = "_id";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";
    public static final String IDNOTA = "idnota";

    @DatabaseField(id = true,columnName = ID)
    long id;//fecha en unix

    @DatabaseField(columnName = LATITUD)
    double latitude;

    @DatabaseField(columnName = LONGITUD)
    double longitude;

    @DatabaseField(columnName = IDNOTA)
    long idnota;//id de la tabla

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getIdnota() {
        return idnota;
    }

    public void setIdnota(long idnota) {
        this.idnota = idnota;
    }
}