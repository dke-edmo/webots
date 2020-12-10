package Locomotion;


//VERSION FOR TWO MOTOR CONFIGURATION

public class CPGNeural {

    static double frequency = 1; // oscillator frequency
    double rateOfFrequency = 1;
    double targetFrequency = 1;
    double timeStep = 10;

    static double w = 0.025; // we assume that all oscillators use same coupling weight
    static double a = 0.5; // we assume that all oscillators use same adaptation rate for amplitude
    static double c = 0.5; // adaptation rate for frequency and offset

    double NB_MOTORS = 2;


    static CPG[] CPGs = new CPG[2];
    static double[] motorPos = new double[CPGs.length];

    public CPGNeural(){

        CPG cpg1 = new CPG("motor1", 0,30,30,90,90,0,0,0,0,0, new double[]{0, Math.PI, 0}, new int[]{0,1,0});
        CPG cpg2 = new CPG("motor2", 0,30,30,90,90,0,0,0,0,0, new double[]{-Math.PI, 0, Math.PI}, new int[]{1,0,1});
      //  CPG cpg3 = new CPG("motor3", 0,30,30,90,90,0,0,0,0,0, new double[]{0, -Math.PI, 0}, new int[]{0,1,0});

        CPGs[0] = cpg1;
        CPGs[1] = cpg2;
   //     CPGs[2] = cpg3;

    }

    public void genetic(int index, double phase_incr, double amplitude_incr, double offset_incr){
        CPGs[0].phase += phase_incr;
        CPGs[0].amplitude += amplitude_incr;
    }


    // -add timeStep
    public static void step(){
        double timeStep = 32;
        for(int i=0; i< CPGs.length; i++){
            double sum = 0;


            for(int j=0; j< CPGs.length; j++){
                if(i!=j){
                    sum += w * CPGs[i].coupling[j] * CPGs[i].amplitude * Math.sin(CPGs[j].phase - CPGs[i].phase - CPGs[i].phaseBias[j]);
                 }
            }

            double phaseDerivative = 2 * Math.PI * frequency + sum;

            double ri = a * (CPGs[i].targetAmplitude - CPGs[i].amplitude);

            CPGs[i].amplitude = CPGs[i].amplitude + ri * ((double) timeStep / 1000);

            CPGs[i].phase = CPGs[i].phase + phaseDerivative * ((double) timeStep / 1000);

            double modul_offset = c * (CPGs[i].offset - CPGs[i].targetOffset);

            CPGs[i].pos = (CPGs[i].amplitude * Math.sin(CPGs[i].phase) + modul_offset);

            System.out.println("Motor position for: " + i + " is " + CPGs[i].pos);


        } //end loop



        for(int k=0; k<CPGs.length;k++){
            motorPos[k] = CPGs[k].pos;
        }


    }

    public static double[] getMotorPos(){

        return motorPos;

    }

}
