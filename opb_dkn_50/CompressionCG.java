/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.*; 


/**
 *
 * @author danyvohl
 */
public class CompressionCG {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //int d=32; int k=13; int n=32;
        int d=0; 
        int k=0; 
        int n=0;
        
        if (args.length > 0) {
            try {
                d = Integer.parseInt(args[0]);
                n = Integer.parseInt(args[1]);
                k = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Usage: java CompressionCG {d} {n} {k} > filename.opb");
                System.exit(1);
            }
        }
        
        int num_variables = 0; 
        
        //Déclaration des variables: C, V, Y
        //==================================================
        //Ci element {0,1} pour tout i element [0,d+k)
        int [] C = new int [d+k];
        
        for (int i=0; i<d+k; i++)
            C[i] = ++num_variables;
        
        //Vi element {0,1} pour tout i element [0,d+k)
        int [] V = new int [d+k];
        for (int i=0; i<d+k; i++)
            V[i] = ++num_variables;
        
        //Yi,g element {0,1} pour tout i, g element [0,d+k)
        int [][] Y = new int [d+k][d+k];
        for (int i=0; i<d+k; i++)
            for (int g=1; g<d+k; g++)
                Y[i][g] = ++num_variables;
        //==================================================
        
        //Print les contraintes
        
        StringBuffer contrainte = new StringBuffer();
        
        //Contrainte 1:
        //Somme_{i=1}^{d+k} Ci = k
        for (int i=0; i<d+k; i++)
        {
            // sum_i^{k+k} C[i] = k
            contrainte.append("+1 x" + C[i] + " ");
        }
        contrainte.append("= " + k + ";\n");
        
        //Contrainte 2:
        //Somme_{j element {i%(d+k),(i+1)%(d+k),...,(i+n)%(d+k)}} Yig >= 1
        for (int i=0; i<d+k; i++)
            for (int g=1; g<d+k; g++)
            {
                // sum_{j = i, i+1 % d+k, i+2 % d+k, ..., i + n % d + k} Y[j][g] >= 1
                for (int a = 0; a < n; a++){
                    int j = (i + a) % (d+k);
                    contrainte.append("+1 x" + Y[j][g] + " ");
                }
                contrainte.append(">= 1;\n");
            }
        
        //Contrainte 3:
        //Yig <= Ci
        for (int i=0; i<d+k; i++)
            for (int g=1; g<d+k; g++)
            {
                // Y[i][g] --> C[i]
                contrainte.append("+1 x" + Y[i][g] + " -1 x" + C[i] + " <= 0;\n");
                // Y[i][g] --> C[i+g % d+k]
                contrainte.append("+1 x" + Y[i][g] + " -1 x" + C[(i+g)%(d+k)] + " <= 0;\n");
            }
        
        //Contrainte 5:
        //(1- Yig) + (1 - Vi) + (1 - V_{(i+g)%(d+k)) >= 1
        for (int i=0; i<d+k; i++)
            for (int g=1; g<d+k; g++)
            {
                // -Y[i][g] - V[i] - V[i+g % d+k] >= -2
                contrainte.append("-1 x" + Y[i][g] + " -1 x" + V[i] + "-1 x" + V[(i+g)%(d+k)] + " >= -2;\n");
                // -Y[i][g] + V[i] + V[i+g % d+k] >= 0
                contrainte.append("-1 x" + Y[i][g] + " +1 x" + V[i] + " +1 x" + V[(i+g)%(d+k)] + " >= 0;\n");
            }
        
        //C_i = 1
        contrainte.append("+1 x" + C[0] + " = 1;\n");        
        //V_i = 1
        contrainte.append("+1 x" + V[0] + " = 1;\n");        
        //Y_1^1 = 1
        contrainte.append("+1 x" + Y[0][1] + " = 1;\n");
        
        
        //C_i >= V_i
        for (int i=0; i<d+k; i++)
            contrainte.append("+1 x" + C[i] + " -1 x" + V[i] + " >= 0;\n");            
        
        //Somme_{V_i} <= k/2
        for (int i=0; i<d+k; i++)
            contrainte.append("+1 x" + V[i] + " ");
         
        
        contrainte.append(" <= " + k/2 + ";\n");            
        
        
        String s = contrainte.toString();
        
        // Count the number of constraints
        int num_constraints = 0;
        for (int i = s.length() - 1; i >= 0; i--)
            if (s.charAt(i) == '\n')
                num_constraints++;
        
        //Print l'en-tête
        System.out.println("* #variable= " + num_variables + " #constraint= " + num_constraints);
        System.out.println("*");
        System.out.println("* Synchronization bits problem");
        System.out.println("*");
        // Print les contraintes
        System.out.print(s);
    }
}