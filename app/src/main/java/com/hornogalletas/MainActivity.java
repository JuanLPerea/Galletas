package com.hornogalletas;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hornogalletas.Model.Simulacion;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hornogalletas.Model.Horno;
import com.hornogalletas.Model.Tarro;
import com.hornogalletas.Model.Triki;

public class MainActivity extends AppCompatActivity {

    MainActivity activity = this;
    Context context = this;
    Tarro miTarro;

    final int tiemposimulacion = 120;
    final int galletasInicio = 20;
    final int maxGalletasTriki = 100;
    public TextView tiempotxt;
    TextView galletastriki1;
    TextView galletastriki2;
    TextView galletasHorno;
    TextView galletasHornoTXT;
    TextView galletasHornoTXT2;
    TextView yum1;
    TextView yum2;
    TextView yum3;
    TextView yum4;

    ProgressBar barratiempo;
    ProgressBar barratriki1;
    ProgressBar barratriki2;
    ProgressBar barrahorno;

    private ExecutorService executorService;

    Simulacion simulaciontask;
    Triki triki1;
    Triki triki2;
    Horno horno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tiempotxt = (TextView)findViewById(R.id.tiemposim);
        galletastriki1 = (TextView)findViewById(R.id.galletastriki1);
        galletastriki2 = (TextView)findViewById(R.id.galletastriki2);
        galletasHorno = (TextView)findViewById(R.id.galletashorno);
        galletasHornoTXT = (TextView)findViewById(R.id.galletastxt);
        galletasHornoTXT2 = (TextView)findViewById(R.id.hornadaTXT);
        yum1 = (TextView)findViewById(R.id.yum1);
        yum2 = (TextView)findViewById(R.id.yum2);
        yum3 = (TextView)findViewById(R.id.yum3);
        yum4 = (TextView)findViewById(R.id.yum4);

        barratiempo = (ProgressBar)findViewById(R.id.barratiempo);
        barratiempo.setMax(tiemposimulacion);
        barratriki1 = (ProgressBar)findViewById(R.id.barratriki1);
        barratriki1.setMax(maxGalletasTriki);
        barratriki2 = (ProgressBar)findViewById(R.id.barratriki2);
        barratriki2.setMax(maxGalletasTriki);
        barrahorno = (ProgressBar)findViewById(R.id.barrahorno);
        barrahorno.setProgress(0);

        setGalletasTarro(galletasInicio);
    }



    void empezarSimulacion(View v){
        // creamos un objeto único Tarro con el patrón singleton
        miTarro = Tarro.getInstance(galletasInicio);

        // movidas para que ejecute todos los hilos simultaneamente (Gracias Adolfo)
        int processors = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(4);

        // con este hilo controlamos el tiempo total que estará en marcha el programa
        simulaciontask = new Simulacion(tiemposimulacion);
        simulaciontask.setActivity(MainActivity.this);
        simulaciontask.executeOnExecutor(executorService);

        // creamos nuestros Trikis
        triki1 = new Triki(miTarro, maxGalletasTriki, 1);
        triki1.setActivity(MainActivity.this);
        triki2 = new Triki(miTarro, maxGalletasTriki, 2);
        triki2.setActivity(MainActivity.this);

        triki1.executeOnExecutor(executorService);
        triki2.executeOnExecutor(executorService);

        // ponemos en marcha el horno de la abuela
        barrahorno.setVisibility(View.VISIBLE);
        horno = new Horno(miTarro);
        horno.setActivity(MainActivity.this);
        horno.executeOnExecutor(executorService);

    }

    public void fintiempo(){
        // hacer lo que sea cuando se acaba el tiempo
        ganador();
        simulaciontask.cancel(true);
        triki1.cancel(true);
        triki2.cancel(true);
        horno.cancel(true);
        barrahorno.setVisibility(View.GONE);
        barratriki1.setProgress(0);
        barratriki2.setProgress(0);
        barratiempo.setProgress(0);

        Toast.makeText(context, "Fin de tiempo", Toast.LENGTH_LONG).show();
        tiempotxt.setText("FIN TIEMPO");



    }

    void cancelarSimulacion(View v){
        simulaciontask.cancel(true);
        triki1.cancel(true);
        triki2.cancel(true);
        horno.cancel(true);

        miTarro.setNumGalletas(galletasInicio);
        galletasHorno.setText("" + galletasInicio);
        galletastriki1.setText("0");
        galletastriki2.setText("0");
        barrahorno.setVisibility(View.GONE);
        galletasHornoTXT.setText("");

        barratriki1.setProgress(0);
        barratriki2.setProgress(0);
        barratiempo.setProgress(0);
        Toast.makeText(context, "Simulación cancelada", Toast.LENGTH_LONG).show();
        tiempotxt.setText("Galletas!!!");
    }

// Funciones para escribir en pantalla los valores de la simulacion

    public void setTiempoSimulacion(int tiempo){

        tiempotxt.setText(tiempo+ " / " + tiemposimulacion + " seg");
        barratiempo.setProgress(tiempo);

        // comprobaciones para terminar la simulación
        // si los trikis se comen todas las galletas:
        if(miTarro.getNumGalletas()<=0) {
            Toast.makeText(context, "Los Trikis se comieron todas las galletas antes de que la abuela pudiera hacer mas", Toast.LENGTH_LONG).show();
            finSimulacion();

        }
    }



    public void setGalletasTarro(int galletasTarro){
        galletasHorno.setText("" + galletasTarro);
    }


    public void setGalletasComidasTriki(int numTriki , int galletasComidas){

        switch (numTriki){
        case 1:
        galletastriki1.setText("" + galletasComidas);
        break;

        case 2:
        galletastriki2.setText("" + galletasComidas);
        break;
    }

    }

    public void setProgressTriki(int numTriki, int comidoTriki) {

        Log.d("actualizar triki", "" + numTriki);

        switch (numTriki){
            case 1:

                barratriki1.setProgress(comidoTriki);
                break;

            case 2:
                barratriki2.setProgress(comidoTriki);
                break;
        }

        Random aleatorio = new Random();
        yum1.setTextSize(10 + (aleatorio.nextFloat()*5));
        yum2.setTextSize(10 + (aleatorio.nextFloat()*5));
        yum3.setTextSize(10 + (aleatorio.nextFloat()*5));
        yum4.setTextSize(10 + (aleatorio.nextFloat()*5));
    }

    public void setProgressHorno(int progreso, int descanso){
        // activar o desactivar la barra si la abuelita se toma un descanso
        if(descanso>5) {
            barrahorno.setVisibility(View.GONE);
            galletasHornoTXT.setText("Abuelita Fumando");

        }
        else {
            barrahorno.setVisibility(View.VISIBLE);
            galletasHornoTXT.setText("Cocinando");
        }

        if(progreso >= 50) galletasHornoTXT2.setText(" --- ");
     //   barrahorno.setProgress(progreso);
    }



    // devuelve si está funcionando la simulacion
   public boolean dentroTiempo(){

       return simulaciontask.isCancelled();
   }


    public void setClin(int hornada) {

        galletasHornoTXT2.setText("¡¡ Clin !! + " + hornada + " Galletas");
    }

    public void ganador(){

        String trikiWin = "";


        if (triki1.getGalletasComidas() > triki2.getGalletasComidas()) trikiWin = "Triki Azul!!";
        else trikiWin = "Triki Morado!!";
        if (triki1.getGalletasComidas() == triki2.getGalletasComidas()) trikiWin = "Empate!!!";

        Toast.makeText(context, "El ganador es..." + trikiWin, Toast.LENGTH_LONG).show();
        tiempotxt.setText("" + triki1.getGalletasComidas() + " a " + triki2.getGalletasComidas());

    }

    private void finSimulacion() {

        Toast.makeText(context, "FIN", Toast.LENGTH_LONG).show();
        ganador();

        simulaciontask.cancel(true);
        triki1.cancel(true);
        triki2.cancel(true);
        horno.cancel(true);


        barrahorno.setVisibility(View.GONE);
        galletasHornoTXT.setText("");

        barratriki1.setProgress(0);
        barratriki2.setProgress(0);
        barratiempo.setProgress(0);

        tiempotxt.setText("Galletas!!!");
    }

    public void abuelitacansada(){
        // paramos el horno si se hacen 100 galletas
        Toast.makeText(context, "La abuela ha hecho 100 galletas y dice que no hace mas", Toast.LENGTH_LONG).show();
        horno.cancel(true);
        barrahorno.setVisibility(View.GONE);
        galletasHornoTXT.setText("Horno apagado");

    }

}
