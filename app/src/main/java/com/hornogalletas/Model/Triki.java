package com.hornogalletas.Model;

import android.os.AsyncTask;
import android.util.Log;

import com.hornogalletas.MainActivity;

import java.util.Random;

import com.hornogalletas.MainActivity;

/**
 * Created by TorreJL on 04/04/2017.
 */

public class Triki extends AsyncTask<Integer, Integer, Integer> {

    MainActivity activity;
    Tarro tarroAbuela;
    int id;
    int galletasComidas;
    int maxGalletasTriki;

    public Triki(Tarro miTarro, int totalGalletas, int queTriki) {
        tarroAbuela = miTarro;
        galletasComidas = 0;
        this.maxGalletasTriki = totalGalletas;
        this.id = queTriki;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);



    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        activity.setProgressTriki(id, values[0]);
        activity.setGalletasComidasTriki(id, values[1]);
        activity.setGalletasTarro(values[2]);

    }

    @Override
    protected Integer doInBackground(Integer... integers) {

        Log.d("Tarea Triki ", "Ha entrado en la tarea " + id);

        // Hacer que el monstruo coma galletas mientras que esté dentro del tiempo
        // y no haya llegado a 100 galletas
        while (galletasComidas < maxGalletasTriki) {
            // si el tarro está abierto, cogemos una galleta
            if (tarroAbuela.isAbierto()) tarroAbuela.cogerGalleta();

            // esperamos de 5 a 7 segundos (Actualizar cada décima de segundo)
            Random aleatorio = new Random();
            int tiempoComeGalleta = 10 + aleatorio.nextInt(25);
            Log.d("Tarea Triki ", "dentro del bucle " + tiempoComeGalleta);
            for (int n = 0; n < tiempoComeGalleta; n++) {
                if (isCancelled()) break;
                publishProgress((int) n * 100 / tiempoComeGalleta, galletasComidas, tarroAbuela.getNumGalletas());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    cancel(true);
                }


            }

            galletasComidas++;

        }


        return null;
    }

    public int getGalletasComidas() {
        return galletasComidas;
    }
}
