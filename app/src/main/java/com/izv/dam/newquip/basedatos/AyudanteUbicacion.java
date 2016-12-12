package com.izv.dam.newquip.basedatos;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import com.izv.dam.newquip.pojo.Ubicacion;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Created by sergio on 06/12/2016.
 */

public class AyudanteUbicacion extends OrmLiteSqliteOpenHelper{
    public static final int VERSION = 2;

    private Dao<Ubicacion,Integer> simpleDao = null;
    private RuntimeExceptionDao<Ubicacion,Integer> simpleRunTimeDao=null;

    public AyudanteUbicacion(Context context){
        super(context, "ormlite", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, Ubicacion.class);
        }catch (SQLException e){

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Ubicacion.class, true);
            onCreate(database,connectionSource);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Ubicacion, Integer> getSimpleDao() throws java.sql.SQLException {
        if(simpleDao == null){
            simpleDao = getDao(Ubicacion.class);
        }
        return simpleDao;
    }

    public RuntimeExceptionDao<Ubicacion, Integer>getSimpleRunTimeDao(){
        if(simpleRunTimeDao == null){
            simpleRunTimeDao = getRuntimeExceptionDao(Ubicacion.class);
        }
        return simpleRunTimeDao;
    }

    @Override
    public void close(){
        super.close();
        simpleDao = null;
        simpleRunTimeDao = null;
    }
}

