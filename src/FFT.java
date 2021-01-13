import org.jtransforms.fft.DoubleFFT_1D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class FFT {
    public static void main(String[] args){

        String data = "0";
        try {
            File myObj = new File("resources/reading1.csv");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        String[] dataStringArray = data.split(", ");

        double[] doubleValues = Arrays.stream(dataStringArray)
                .mapToDouble(Double::parseDouble)
                .toArray();
        for (int i=0; i<dataStringArray.length; i++)
        {
            //System.out.println(doubleValues[i]);
        }



        double[] input = doubleValues;
        DoubleFFT_1D fftDo = new DoubleFFT_1D(input.length);
        double[] fft = new double[input.length * 2];
        System.arraycopy(input, 0, fft, 0, input.length);
        fftDo.realForwardFull(fft);

        for(double d: fft) {
            System.out.println(d);
        }
    }
}
