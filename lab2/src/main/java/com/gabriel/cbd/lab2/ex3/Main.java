package com.gabriel.cbd.lab2.ex3;

import java.util.Scanner;

import com.gabriel.cbd.lab2.ex3.a.RestaurantA;
import com.gabriel.cbd.lab2.ex3.b.AlineaB;
import com.gabriel.cbd.lab2.ex3.c.AlineaC;
import com.gabriel.cbd.lab2.ex3.d.RestaurantsDAO;

// import java.util.List;
// Dont forget to start the docker and the mongodb server

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        boolean run = true;
        while (run) {
            System.out.println("################## Escolha a alínea que quer correr ################## ");

            System.out.println("1 - Alinea A");
            System.out.println("2 - Alinea B");
            System.out.println("3 - Alinea C");
            System.out.println("4 - Alinea D");
            System.out.println("0 - Exit");
            switch(sc.nextInt()) {
               case 1:
                    System.out.println("################## Running Alinea A ##################");
                    RestaurantA.main(args);     
                    System.out.println("\n");           
                    break;
               case 2:
                    System.out.println("################## Running Alinea B ##################");
                    try {
                        AlineaB.main(args);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\n");
                   break;
               case 3:
                    System.out.println("################## Running Alinea C ##################");
                    AlineaC.main(args);
                    System.out.println("\n");
                   break;
                case 4:
                    System.out.println("################## Running Alinea D ##################");
                    System.out.println("\n");
                    RestaurantsDAO.main(args);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    run = false;
                    break;
               default:
                   System.out.println("Escolha inválida");
                   break;
           }
        }

        sc.close();
        
    }

}
