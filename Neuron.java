import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Grzegorz on 2016-11-07.
 */

public class Neuron {

    private static final Scanner NULL = null;

    public static Random losowe = new Random();

    public double suma = 0;
    public double[] w;                  //wagi
    public double[][] x;				//wejście
    public double[] outputs;
    public int ilosc = 0;
    public double wspU = 0.05;            //współczynik uczenia
    public double y = 0;
    public double theta = 1;            //wyjście neuronu
    public double localError = 0;
    public Scanner odczyt;
    public int kol;
    public Neuron()
    {
        try {
            odczyt = new Scanner(new File("dane.txt"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        kol = 2;
        ilosc = odczyt.nextInt();
        x = new double[kol][ilosc];
        w = new double[kol +1];      		//wag o jedną więcej niż wejść
        outputs = new double[ilosc];       //wyjścia
        losowanieWag();
    }

    public void losowanieWag()
    {
        for(int i = 0; i< kol +1; i++)
        {
            w[i] = (losowe.nextDouble() * 2) - 1;
        }
    }

    public void uczenie() throws FileNotFoundException
    {
        PrintWriter zapis = new PrintWriter("blad.txt");
        int n=0;
        while(odczyt.hasNextDouble())
        {
            x[0][n]=odczyt.nextDouble();
            x[1][n]=odczyt.nextDouble();
            outputs[n]=odczyt.nextDouble();
            n++;
        }
        int iter = 0;
        double globalError;
        double MSE =0;
        do
        {
            globalError=0;
            iter++;
            System.out.println("Iteracja nr "+iter);
            for(int p = 0; p< ilosc; p++)
            {
                y = obliczanie(x[0][p],x[1][p]);
                localError = outputs[p]-(int)y;
                w[0] += wspU *localError*x[0][p];
                w[1] += wspU *localError*x[1][p];
                w[2] += wspU *localError;
                globalError = globalError + (localError*localError); //suma bledow
                System.out.println(x[0][p] +"   "+x[1][p] +"  :  "+ outputs[p]+"    wynik: "+y );
            }
            MSE = Math.pow(globalError, 2)/ ilosc;
            System.out.println("MSE = " + MSE);
            zapis.println(MSE);
        } while(globalError!=0 && iter < 10000);
        zapis.close();
        System.out.println("Koniec nauki. Wszystko nauczone! ");
        System.out.println(w[0]);
        System.out.println(w[1]);
        System.out.println(w[2]);
    }

    public double obliczanie(double x1, double x2)
    {
        suma = 0;
        suma = x1 * w[0] + x2 * w[1] +w[2]; //sumator

        //Funkcja progowa unipolarna
        if(suma >= theta) suma = 1;
        else suma = 0;

        return suma;
    }

    public static void main(String[] args) throws FileNotFoundException

    {
        Neuron neuron = new Neuron();
        neuron.uczenie();
    }
}