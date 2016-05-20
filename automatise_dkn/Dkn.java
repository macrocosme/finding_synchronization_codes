import java.lang.Math;
import	java.io.*; 
import java.util.Date;

/**
 *
 * @author danyvohl
 */
public class Dkn {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long StartTime;
        long EndTime;
        long temps;
        
        
        
        String [][] opt = new String [6][6];
        
        String ls_str; 
        StartTime = new Date().getTime();
        for (int d=6; d<9; d++){  
            
            int n_min = 6;
//            int n_max = 7;
            int n=6;
            boolean n_opt = false;
            
            
            String line;
            
          //  while (n>n_min && n_opt==false) {
                
                
                //Calcul du n à parcourir
//                n = (int) Math.floor((n_min+n_max)/2);                
                //Variable relative à k
                int k = 30;
                boolean k_opt = false;
                int k_max = (int)Math.pow(2,n)-d;
                System.out.println("--- n: " + n + " --- k_max:" + k_max);
                while (k_opt == false){
                    
                    //print
                    System.out.println("\td: " + d + "\tn: " + n + "\tk:" + k);
                    System.out.println("k_opt: " + k_opt);
                    
                    try { 
                        PrintStream stdout = System.out;
                        
                        Process proc1 = Runtime.getRuntime().exec("java CompressionCG " + d + " " + n + " " + k); 
                        DataInputStream ls_in1 = new DataInputStream(proc1.getInputStream()); 
                        System.setOut(new PrintStream(new FileOutputStream(d + "_" + n + "_" + k + ".opb")));
                        
                        while ((line = ls_in1.readLine()) != null) { 
                            System.out.println(line);
                        }
                        ls_in1.close();
                        
                        
                        Process proc2 = Runtime.getRuntime().exec("minisat+ " + d + "_" + n + "_" + k + ".opb");
                        DataInputStream ls_in2 = new DataInputStream(proc2.getInputStream()); 
                        System.setOut(new PrintStream(new FileOutputStream("sortie/" + d + "_" + n + "_" + k + ".txt")));
                        
                        while ((line = ls_in2.readLine()) != null) { 
                            System.out.println(line);
                        }
                        ls_in2.close();
                        
                        System.setOut(stdout); 
                        
                        Process proc3 = Runtime.getRuntime().exec("grep UNSATISFIABLE sortie/" + d + "_" + n + "_" + k + ".txt");
                        DataInputStream ls_in3 = new DataInputStream(proc3.getInputStream()); 
                        
                        
                        if ((ls_str = ls_in3.readLine()) == null) { 
                            //satisfiable
                            System.out.println("-satisfiable-");
                            System.out.println(ls_str);
                            k_opt = true;
                            System.out.println("n " + n);                            
                            opt[d-2][n-2] = "d: " + d + "\tn: " + n + "\tk:" + k;
                        } 
                        
                        ls_in3.close();
                    } catch (IOException e1) { 
                        System.out.println("e1");
                        break;
                    } 
                    
                    if (k == k_max){
                        k_opt = true;
                        System.out.println("No optimum");
                        n_opt = true;
                    }

                    k++;
    
                }
                //n--;
            //}
        }
        EndTime = new Date().getTime();
        
        temps = EndTime - StartTime;
        System.out.println("Temps" + temps);
        
        try{
            System.setOut(new PrintStream(new FileOutputStream("sortie/optima.txt")));
            for (int i=0; i<7; i++)
                for (int j=0; j<7; j++)
                    System.out.println(opt[i][j]);
        }catch(IOException e){System.out.println("e1");}
    }
}