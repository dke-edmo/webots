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

            double m1 = (Math.random() * alpha * 100 - 10) / 100;
                    //(random.nextInt((int) (this.alpha*100) + 1) - 10) / 100;
            double m2 = (Math.random() * alpha * 100 - 10) / 100;
            double m3 = (Math.random() * alpha * 100 - 10) / 100;


            System.out.println(m1 + " " + m2 + " " + m3);
            this.cpgs_neural[i].genetic(0, m1,m2,m3);

            if(Math.abs(m1) > 0.2){ m1 = 0;}
            if(Math.abs(m2) > 0.2){ m2 = 0;}
            if(Math.abs(m3) > 0.2){ m3 = 0;}



        }

        return this.cpgs_neural;

    }



}
