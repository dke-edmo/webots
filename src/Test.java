import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.DoubleFFT_2D;

public class Test {
    public static void main(String[] args){
        double[][] array1 = new double[][]{{1,2,3},{1,4,3},{1,2,7}};
        double[][] array = new double[][]{{1, 2, 3, 0, 0, 0},{1,4,3,0,0,0},{1,2,7,0,0,0}};
        print(array1);
        print(array);
        DoubleFFT_2D test = new DoubleFFT_2D(array1.length,array1[0].length);
        test.realForwardFull(array);
        print(array);
        double[][] magnitude = computeMagnitude(array);
        print(magnitude);

    }
    public static void print(double[] array){
        for (double d: array)
        {
            System.out.print(d + ", ");
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

}
