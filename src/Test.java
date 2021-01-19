import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.DoubleFFT_2D;

import java.io.*;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {




        int rows = 233;
        int columns = 22;

        double[][][] vectors = readVectors("resources/ModuleB.csv", rows, columns);

        double[][][] fouriers = computeFouriers(vectors);

        double[][][] vectors2 = readVectors("resources/ModuleA.csv", rows, columns);
        double[][][] fouriers2 = computeFouriers(vectors2);

        double[][] distances = computeDistances(fouriers, fouriers2);
        double[] avgDistances = computeMeanDistances(distances);


        System.out.println("average distances: ");
        print(avgDistances);






    }
    public static double[] computeMeanDistances(double[][] distances)
    {
        double[] meanDistances = new double[distances.length];

        for(int i=0; i<distances.length; i++)
        {
            double total = 0.0;
            for (int j=0; j<distances[0].length; j++)
            {
                total += distances[i][j];
            }
            meanDistances[i] = Math.abs(total / distances[0].length);
        }
        return meanDistances;
    }
    public static double[][] computeDistances(double[][][] vectors, double[][][] vectors2)
    {
        double[][] difference = new double[vectors.length][vectors[0].length];
        System.out.println(vectors[0].length);
        for (int i=0; i<vectors.length; i++)
        {
            for(int j=0; j<vectors[0].length; j++)
            {
                double total = 0.0;
                for(int k=0; k<vectors[0][0].length; k++)
                {
                    total += Math.pow(vectors[i][j][k] - vectors2[i][j][k], 2);
                }
                difference[i][j] = Math.sqrt(total);
            }
        }
        return difference;
    }
    public static String makeString(double[][][] vectors, int rows, int columns)
    {
        StringBuilder back = new StringBuilder();
        back.append("\uFEFF,Vector1,,,Vector2,,,Vector3,,,Vector4,,,Vector5,,,Vector6,,,Vector7,,\n");
        for(int i=0; i<(rows-2); i++)
        {
            back.append(5 * (i + 1)).append(", ");
            for(int j=0; j<(columns-1); j++)
            {
                back.append(vectors[j / 7][i][j % 3]).append(", ");
            }
            back.append(vectors[6][i][2]).append("\n");
        }
        String result = back.toString();
        return result;
    }
    public static double[][][] computeFouriers(double[][][] vectors)
    {
        double[][][] fouriers = new double[7][0][0];
        for (int i=0; i<vectors.length; i++)
        {
            fouriers[i] = computeMagnitude(fourier(vectors[i]));
        }
        return fouriers;
    }
    public static double[][][] readVectors(String fileName, int rows, int columns)
    {
        double[][] dataMatrix = new double[rows][columns];
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            int line = 0;
            for (int j=0; j<rows; j++) {
                String data = myReader.nextLine();
                String[] dat = data.split(",");


                for (int i=0; i<dat.length; i++)
                {
                    try {
                        dataMatrix[line][i] = Double.parseDouble(dat[i]);
                    }catch (NumberFormatException e){
                        dataMatrix[line][i] = -1.0;
                    }

                }
                line++;

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        double[][] vector1 = extractVector(dataMatrix, 1, rows);
        double[][] vector2 = extractVector(dataMatrix, 4, rows);
        double[][] vector3 = extractVector(dataMatrix, 7, rows);
        double[][] vector4 = extractVector(dataMatrix, 10, rows);
        double[][] vector5 = extractVector(dataMatrix, 13, rows);
        double[][] vector6 = extractVector(dataMatrix, 16, rows);
        double[][] vector7 = extractVector(dataMatrix, 19, rows);
        double[][][] vectors = new double[][][]{vector1,vector2,vector3,vector4,vector5,vector6,vector7};
        return vectors;
    }
    public static double[][] extractVector(double[][] dataMatrix, int v, int rows)
    {
        double[][] vector = new double[rows-2][3];
        for(int i=0; i<vector.length; i++)
        {
            vector[i][0] = dataMatrix[i+2][v];
            vector[i][1] = dataMatrix[i+2][v+1];
            vector[i][2] = dataMatrix[i+2][v+2];

        }
        return vector;
    }
    public static void print(double[] array){
        for (double d: array)
        {
            System.out.print(d + ", ");
        }
    }
    public static void print(String[] array)
    {
        for (String d: array)
        {
            System.out.print("["+ d+"]" + "; ");
        }
    }
    public static void print(String[][] matrix)
    {
        System.out.println("matrix: ");
        for(String[] array: matrix)
        {
            for (String s: array)
            {
                System.out.print(s);
            }
            System.out.println(" ");
        }
    }

    public static void print(double[][] matrix){
        System.out.println("matrix: ");
        for(double[] array: matrix)
        {
            for (double d: array)
            {
                System.out.print(d + ", ");
            }
            System.out.println(" ");
        }
    }
    public static double[][] computeMagnitude(double[][] data)
    {
        double[][] result = new double[data.length][data[0].length/2];
        for(int i=0; i<result.length; i++)
        {
            for (int j=0; j<result[0].length; j++)
            {
                result[i][j] = Math.sqrt(Math.pow(data[i][j*2], 2) + Math.pow(data[i][(j*2+1)], 2));

            }
        }
        return result;
    }
    public static void copy(double[][] source, double[][] target)
    {
        for(int i=0; i<source.length; i++)
        {
            System.arraycopy(source[i], 0, target[i], 0, source[i].length);
        }
    }
    public static double[][] fourier(double[][] data)
    {
        //make fourier transform
        DoubleFFT_2D fourier = new DoubleFFT_2D(data.length, data[0].length);
        double[][] fft = new double[data.length][data[0].length*2];
        //print(fft);
        copy(data, fft);
        fourier.realForwardFull(fft);
        return fft;
    }

}
