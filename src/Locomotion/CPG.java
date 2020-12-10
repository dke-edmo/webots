package Locomotion;



public class CPG {

    String label;

    double phase;                       // phase of the oscillation
    double amplitude;                   // amplitude of the oscillation
    double targetAmplitude;             // amplitude to gradually change to
    double offset;                      // offset for the oscillation (in range of servo 0-180)
    double targetOffset;                // added parameter to offset smoothly
    double rateOfPhase;                 // current rate of change of the phase parameter
    double rateOfAmplitude;             // current rate of change of the amplitude parameter
    double rateOfOffset;                // current rate of change of the offset parameter
    double pos;                         // oscillator output = servos angular position in degrees
    double angle_motor;                 // mapped motor PPM value used to set motor position according to CPG pos
    double[] phaseBias;                 // controls pairwise coupling phase bias
    int[] coupling;                  // controls topology of the network

    public CPG(String label,
               double phase,
               double amplitude,
               double targetAmplitude,
               double offset,
               double targetOffset,
               double rateOfPhase,
               double rateOfAmplitude,
               double rateOfOffset,
               double pos,
               double angle_motor,
               double[] phaseBias,
               int[] coupling
    ){
        this.label = label;
        this.phase = phase;
        this.amplitude = amplitude;
        this.targetAmplitude = targetAmplitude;
        this.offset = offset;
        this.targetOffset = targetOffset;
        this.rateOfPhase = rateOfPhase;
        this.rateOfAmplitude = rateOfAmplitude;
        this.rateOfOffset = rateOfOffset;
        this.pos = pos;
        this.angle_motor = angle_motor;
        this.phaseBias=phaseBias;
        this.coupling = coupling;


    }

    public String toString(){

        String output = "";

        output.concat(label).concat(",")
                .concat(String.valueOf(phase)).concat(",")
                .concat(String.valueOf(amplitude)).concat(",")
                .concat(String.valueOf(offset));

            return output;
    }

}
