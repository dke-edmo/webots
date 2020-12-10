package Locomotion;

import java.util.Random;

public class Opt {


    double reward = 0;
    CPGNeural[] cpgs_neural;

    double alpha = 0.2;


    public Opt(CPGNeural[] cpgNeurals){
        this.cpgs_neural = cpgNeurals;
    }

    public CPGNeural[] iter(){

        Random random = new Random();


        for(int i=0; i<this.cpgs_neural.length; i++){

            double m1 = (random.nextInt((int) (this.alpha*10) + 1) - 10) / 100;
            double m2 = (random.nextInt((int) (this.alpha*10) + 1) - 10) / 100;
            double m3 = (random.nextInt((int) (this.alpha*10) + 1) - 10) / 100;

            if(Math.abs(m1) > 0.2){ m1 = 0;}
            if(Math.abs(m2) > 0.2){ m2 = 0;}
            if(Math.abs(m3) > 0.2){ m3 = 0;}

            System.out.println(m1 + " " + m2 + " " + m3);
            this.cpgs_neural[i].genetic(0, m1,m2,m3);

        }

        return this.cpgs_neural;

    }



}