/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.lithoprototype.model;

import interop.lithologyDataCollector.SampleLithology;

import java.util.ArrayList;
import java.util.List;


/**
 * Contém uma tabela de covariância
 *
 * @author Bruno Zanette
 */
public class LogCovarianceMatrix {
    private List<List<Double>> covarianceMatrix;
    private List<List<Integer>> numberOfSamples;
    private List<List<String>> samples;
    ;

    public LogCovarianceMatrix() {
        covarianceMatrix = new ArrayList<>();
        numberOfSamples = new ArrayList<>();
        samples = new ArrayList<>();
    }

    public List<List<Double>> getCovarianceMatrix() {
        return covarianceMatrix;
    }

    public List<List<Double>> calculateCovarianceMatrix(List<Double> averageVector) {
/*
        TODO: FIX THIS (null value)

        //WILL COMPARE EVERY LOG WITH EACH OTHER
        for (int logA = 0; logA < averageVector.size(); logA++) {
            List<Double> line = new ArrayList<>();
            for (int logB = 0; logB < averageVector.size(); logB++) {

                double valueOfCovMatrix = 0.0;
                double numberOfvalues = 0.0;

                //CREATE THE COVARIANCE MATRIX AND GET THE AVERAGE VALUE OF IT
                for (List<String> sample : samples) {
                    if (samples.size() == 1) {
                        System.out.println("DEU MERDA");
                    }
                    double valueA = Double.parseDouble(sample.get(logA));
                    double averageA = averageVector.get(logA);

                    double valueB = Double.parseDouble(sample.get(logB));
                    double averageB = averageVector.get(logB);
                    //System.out.println(nullValue);
                    if (valueA != nullValue && averageA != nullValue && valueB != nullValue && averageB != nullValue
                            && !Double.isNaN(valueA) && !Double.isNaN(valueB) && !Double.isNaN(averageA) && !Double.isNaN(averageB)) {
                        valueOfCovMatrix += (valueA - averageA) * (valueB - averageB);
                        numberOfvalues += 1.0;
                    } else {
                        //System.out.println(averageA + " " + averageB);
                    }

                }

                //CONSTRUCTING THE MATRIX LINE BY LINE
                //System.out.println("ADDING " + valueOfCovMatrix / numberOfvalues);
                line.add(valueOfCovMatrix / numberOfvalues);
            }
            covarianceMatrix.add(line);

        }
        //printMatrix(sumCovarianceMatrix);
        if (!isEspelhada(covarianceMatrix)) System.out.println("MATRIX NAO ESPELHADA!");
        //System.out.println(covarianceMatrix);
        return covarianceMatrix;*/
        return null;
    }


    public static void printMatrix(List<List<Double>> matrix) {
        for (List<Double> line : matrix) {
            System.out.println(line + "\n");
        }
    }

    boolean isEspelhada(List<List<Double>> matrix) {
        for (int logA = 0; logA < matrix.size(); logA++) {
            for (int logB = 0; logB < matrix.size(); logB++) {
                if (matrix.get(logB).get(logA).compareTo(matrix.get(logA).get(logB)) != 0) {
                    System.out.println("TRETA : " + matrix.get(logB).get(logA) + " " + matrix.get(logA).get(logB));
                    return false;
                }
            }
        }
        return true;
    }

    void feedCovarianceMatrix(List<String> OrganizedSample) {
        samples.add(OrganizedSample);
    }

    void prepareMatrix() {//WONT BE USED?
        if (covarianceMatrix.isEmpty()) {
            List<Double> line = new ArrayList<>();
            for (String a : samples.get(1)) {
                line.add(0.0);
            }
            for (String b : samples.get(1)) {
                covarianceMatrix.add(line);
            }
        }
    }


}
                    