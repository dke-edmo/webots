import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.DoubleFFT_2D;

public class Test {
    public static void main(String[] args){
        double[] array1 = new double[]{1,2,3};
        double[] array = new double[]{1,2,3,0,0,0};
        DoubleFFT_1D test = new DoubleFFT_1D(array1.length);
        test.realForwardFull(array);
        print(array);
    }
    public static void print(double[] array){
        for (double d: array)
        {
            System.out.print(d + ", ");
        }
    }
}
