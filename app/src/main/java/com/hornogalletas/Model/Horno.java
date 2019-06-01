package com.hornogalletas.Model;

import android.os.AsyncTask;
import android.util.Log;

import com.hornogalletas.Model.Tarro;

import java.util.Random;

import com.hornogalletas.MainActivity;

/**
 * Created by TorreJL on 04/04/2017.
 */

public class Horno extends AsyncTask<Integer, Integer, Integer> {

    MainActivity activity;
    Tarro tarroAbuela;
    int galletasHechas;
    int hornada;


    public Horno(Tarro tarroAbuela) {
        this.tarroAbuela = tarroAbuela;
        this.galletasHechas = 0;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        activity.setProgressHorno(values[0], values[1]);
        activity.setGalletasTarro(values[2]);
        if(hornada!=0 && values[0] < 25) activity.setClin(hornada);

    }


    @Override
    protected Integer doInBackground(Integer... integers) {

        //  Log.d("Tarea Horno ", "Ha entrado en la tarea " + galletasHechas);

        // Hacer que el monstruo coma galletas mientras que esté dentro del tiempo
        // y no haya llegado a 100 galletas
        while (galletasHechas < 100) {

            Random aleatorio = new Random();
            // la abuela se toma sus descansos, si el aleatorio es mayor de 8 se fuma un piti (Keep Calm and Make Cookies)
            int takeabreak = aleatorio.nextInt(10);

            // esperamos de 5 a 7 segundos (Actualizar cada décima de segundo)
            int tiempoComeGalleta = 50 + aleatorio.nextInt(20);
            Log.d("Tarea Horno ", "dentro del bucle " + tiempoComeGalleta);
            for (int n = 0; n < tiempoComeGalleta; n++) {
                if (isCancelled()) break;
                publishProgress((int) n * 100 / tiempoComeGalleta, takeabreak, tarroAbuela.getNumGalletas());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    cancel(true);
                }


            }

            // cuando acaba el horno añadimos las galletas al tarro si no es que era que la abuela estaba descansando
            if (takeabreak < 6) {
                hornada = 10 + aleatorio.nextInt(5);
                if (tarroAbuela.isAbierto()) tarroAbuela.dejarGalletas(hornada);
                galletasHechas += hornada;
              //

            }
            else hornada = 0;
        }


        return null;

    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        activity.abuelitacansada();
    }
}
