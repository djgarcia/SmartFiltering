package org.apache.spark.feature.Keel;

import java.util.Arrays;

public class NCNEdit {

    /**
     * Training input data.
     */
    private double datosTrain[][];

    /**
     * Classes input data.
     */
    private int clasesTrain[];

    /*Own parameters of the algorithm*/
    private int k;

    /**
     * Default constructor.
     */
    public NCNEdit(double[][] data, int[] classes, Integer kval) {
        datosTrain = data;
        clasesTrain = classes;
        k = kval;
    }

    /**
     * Executes the algorithm
     */
    public boolean[] ejecutar() {

        int i, j, l, m;
        int nClases;
        int claseObt;
        boolean marcas[];
        int nvecinos[];
        double centroide[], centroideT[];
        double dist, minDist;
        int pos;
        int votos[], votada, votaciones;

        long tiempo = System.currentTimeMillis();

        /*Inicialization of the flagged instances vector for a posterior copy*/
        marcas = new boolean[datosTrain.length];
        for (i = 0; i < datosTrain.length; i++)
            marcas[i] = false;

        /*Getting the number of differents classes*/
        nClases = 0;
        for (i = 0; i < clasesTrain.length; i++)
            if (clasesTrain[i] > nClases)
                nClases = clasesTrain[i];
        nClases++;

    /*Body of the algorithm. For each instance in T, search the correspond class agreeing its majority
     from the nearest centroid neighborhood. Is it is positive, the instance is selected.*/
        nvecinos = new int[k];
        centroide = new double[datosTrain[0].length];
        centroideT = new double[datosTrain[0].length];
        votos = new int[nClases];

        for (i = 0; i < datosTrain.length; i++) {
            /*Apply K-NCN to the instance*/
            for (j = 0; j < k; j++) {
                Arrays.fill(centroide, 0.0);
                for (l = 0; l < j; l++) {
                    for (m = 0; m < datosTrain[0].length; m++) {
                        if (nvecinos[l] >= 0)
                            centroide[m] += datosTrain[nvecinos[l]][m];
                    }
                }
                pos = -1;
                minDist = Double.POSITIVE_INFINITY;
                for (l = 0; l < datosTrain.length; l++) {
                    if (i != l) {
                        for (m = 0; m < centroide.length; m++) {
                            centroideT[m] = centroide[m] + datosTrain[l][m];
                        }
                        for (m = 0; m < centroide.length; m++) {
                            centroideT[m] /= (double) (j + 1);
                        }
                        dist = distancia(datosTrain[i], centroideT);
                        if (dist < minDist) {
                            minDist = dist;
                            pos = l;
                        }
                    }
                }
                nvecinos[j] = pos;
            }

            /*Obtain the voted class*/
            for (j = 0; j < nClases; j++) {
                votos[j] = 0;
            }
            for (j = 0; j < k; j++) {
                if (nvecinos[j] >= 0)
                    votos[clasesTrain[nvecinos[j]]]++;
            }
            votada = 0;
            votaciones = votos[0];
            for (j = 1; j < nClases; j++) {
                if (votaciones < votos[j]) {
                    votaciones = votos[j];
                    votada = j;
                }
            }

            claseObt = votada;
            if (claseObt == clasesTrain[i]) { //agree with your majority, it is included in the solution set
                marcas[i] = true;
            }
        }

        System.out.println("NCNEdit " + (double) (System.currentTimeMillis() - tiempo) / 1000.0 + "s");

        return marcas;
    }

    private static double distancia(double ej1[], double ej2[]) {
        int i;
        double suma = 0;

        for (i = 0; i < ej1.length; i++) {
            suma += (ej1[i] - ej2[i]) * (ej1[i] - ej2[i]);
        }
        suma = Math.sqrt(suma);

        return suma;
    }
}
