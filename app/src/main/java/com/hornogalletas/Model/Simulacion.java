package com.hornogalletas.Model;


import android.os.AsyncTask;
import android.util.Log;

import com.hornogalletas.MainActivity;

import com.hornogalletas.MainActivity;

/**
 * Created by TorreJL on 04/04/2017.
 */

public class Simulacion extends AsyncTask<Integer, Integer, String> {


    public int tiemposimulacion;
    MainActivity activity;

    public Simulacion(int tiemposimulacion) {

        this.tiemposimulacion = 120;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        activity.fintiempo();
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        activity.setTiempoSimulacion(values[0]);

    Log.d("Simulacion", " " + values[0]);

    }

    @Override
    protected String doInBackground(Integer... tiempo) {

        for(int n=0 ; n<tiemposimulacion ; n++){
            if (isCancelled()) break;
            publishProgress(n);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                cancel(true);
            }
        }
        return null;
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


}
