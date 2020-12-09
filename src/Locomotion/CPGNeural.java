package Locomotion;

public class CPGNeural {

    double frequency = 1; // oscillator frequency
    double rateOfFrequency = 1;
    double targetFrequency = 1;
    double timeStep = 10;

    double w = 0.025; // we assume that all oscillators use same coupling weight
    double a = 0.5; // we assume that all oscillators use same adaptation rate for amplitude
    double c = 0.5; // adaptation rate for frequency and offset

    double NB_MOTORS;


    double phase = 0;                       // phase of the oscillation
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
    double[] coupling;                  // controls topology of the network

    public CPGNeural(){

        

    }


}
