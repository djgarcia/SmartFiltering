package org.apache.spark.feature.Keel;

import java.util.Arrays;

public class RNG {

    /**
     * Training input data.
     */
    private double datosTrain[][];

    /**
     * Classes input data.
     */
    private int clasesTrain[];

    /*Own parameters of the algorithm*/
    private boolean orden;
    private boolean type;

    /**
     * Default constructor.
     */
    public RNG(double[][] data, int[] classes, Boolean order, Boolean selType) {
        datosTrain = data;
        clasesTrain = classes;
        orden = order;
        type = selType;
    }

    /**
     * Executes the algorithm
     */
    public boolean[] ejecutar() {

        int i, j, k, l;
        boolean grafo[][];
        int nClases;
        boolean marcas[];
        int votos[], votada, votaciones;

        long tiempo = System.currentTimeMillis();

        /*Getting the number of differents classes*/
        nClases = 0;
        for (i = 0; i < clasesTrain.length; i++)
            if (clasesTrain[i] > nClases)
                nClases = clasesTrain[i];
        nClases++;

        /*Inicialization of the flagged instances vector for a posterior copy*/
        marcas = new boolean[datosTrain.length];
        for (i = 0; i < datosTrain.length; i++)
            marcas[i] = true;

        /*Inicialization of the graph without edges and votes container*/
        grafo = new boolean[datosTrain.length][datosTrain.length];
        for (i = 0; i < datosTrain.length; i++) {
            Arrays.fill(grafo[i], true);
            grafo[i][i] = false;
        }
        votos = new int[nClases];

        /*Get the proximity graph using Relative Neighbourhood Graph (RNG)*/
        for (i = 0; i < datosTrain.length; i++) {
            for (j = 0; j < datosTrain.length; j++) {
                for (k = 0; k < datosTrain.length && grafo[i][j]; k++) {
                    if (i != k && j != k) {
                        if (distancia(datosTrain[i], datosTrain[j]) > Math.max(distancia(datosTrain[i], datosTrain[k]), distancia(datosTrain[j], datosTrain[k]))) {
                            grafo[i][j] = false;
                        }
                    }
                }
            }
        }

        /*Check the order graph*/
        if (!orden) {
            for (i = 0; i < datosTrain.length; i++) {
                Arrays.fill(votos, 0);
                for (j = 0; j < grafo[i].length; j++) {
                    if (grafo[i][j]) {
                        votos[clasesTrain[j]]++;
                    }
                }

                /*count of votes for this instance finalized*/
                votada = 0;
                votaciones = votos[0];
                for (j = 1; j < nClases; j++) {
                    if (votaciones < votos[j]) {
                        votaciones = votos[j];
                        votada = j;
                    }
                }
                if (type) {
                    if (votada != clasesTrain[i]) {
                        marcas[i] = false;
                    }
                } else {
                    if (votada == clasesTrain[i]) {
                        marcas[i] = false;
                    }
                }
            }
        } else { //2nd order
            for (i = 0; i < datosTrain.length; i++) {
                Arrays.fill(votos, 0);
                for (j = 0; j < grafo[i].length; j++) {
                    if (grafo[i][j]) {
                        votos[clasesTrain[j]]++;
                    }
                }

                /*count of votes for this instance finalized*/
                votada = 0;
                votaciones = votos[0];
                for (j = 1; j < nClases; j++) {
                    if (votaciones < votos[j]) {
                        votaciones = votos[j];
                        votada = j;
                    }
                }
                if (votada != clasesTrain[i]) {
                    /*Using 2nd order graph*/
                    for (j = 0; j < grafo[i].length; j++) {
                        if (grafo[i][j] && clasesTrain[i] == clasesTrain[j]) {
                            for (k = 0; k < grafo[j].length; k++) {
                                if (grafo[j][k]) {
                                    votos[clasesTrain[k]]++;
                                }
                            }
                        }
                    }

                    /*count of votes for this instance finalized*/
                    votada = 0;
                    votaciones = votos[0];
                    for (j = 1; j < nClases; j++) {
                        if (votaciones < votos[j]) {
                            votaciones = votos[j];
                            votada = j;
                        }
                    }
                    if (type) {
                        if (votada != clasesTrain[i]) {
                            marcas[i] = false;
                        }
                    } else {
                        if (votada == clasesTrain[i]) {
                            marcas[i] = false;
                        }
                    }
                }
            }
        }

        System.out.println("RNG " + (double) (System.currentTimeMillis() - tiempo) / 1000.0 + "s");

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
