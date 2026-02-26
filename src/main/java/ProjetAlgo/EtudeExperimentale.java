package ProjetAlgo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
/*auteur BENNABI ghiles Rayane
 *       BAOUCHE Mohamed Djaouad
 */
public class EtudeExperimentale{

    public static void main(String[] args) {
        // Tailles des ensembles de données
        int[] tailles = {100, 1000, 2500, 5000, 7500, 10000, 30000, 50000, 75000, 100000};

        System.out.println("\nÉtude expérimentale des performances des ABR et ARN");
        System.out.println("\nConstruction en cas favorable: insertion aléatoire");
        afficherResultats(tailles, true);
        System.out.println("\nConstruction en cas défavorable: insertion  en cas croissant");
        afficherResultats(tailles, false);
        System.out.println("\nRecherche en cas  favorable: insertion  en cas aléatoire");
        afficherResultatsRecherche(tailles, true);
        System.out.println("\nRecherche en cas défavorable: insertion  en cas croissant");
        afficherResultatsRecherche(tailles, false);
    }
    
    
    private static void afficherResultatsRecherche(int[] tailles, boolean aleatoire) {
    	System.out.println("--------------------------------------------");
        System.out.printf("%-10s %-20s %-20s%n", "Taille", "Temps ABR ", "Temps ARN");
        System.out.println("--------------------------------------------");

        for (int taille : tailles) {
            List<Integer> cles = genererCles(taille, aleatoire);
            ABR<Integer> abr = new ABR<>();
            ARN<Integer> arn = new ARN<>();

            for (int cle : cles) {   // pre remplire les structure
                abr.add(cle);
                arn.add(cle);
            }
             int tempsRechercheABR = (int) mesurerTempsRecherche(abr, taille);   // Mesure des temps de recherche pour ABR
            int tempsRechercheARN = (int) mesurerTempsRecherche(arn, taille);      // Mesure des temps de recherche pour ARN

            System.out.printf("%-10d %-20d %-20d%n", taille, tempsRechercheABR, tempsRechercheARN);
        }

        System.out.println("--------------------------------------------");
    }
    
    
    private static void afficherResultats(int[] tailles, boolean aleatoire) {
    	System.out.println("--------------------------------------------");
        System.out.printf("%-10s %-20s %-20s%n", "Taille", "Temps ABR ", "Temps ARN ");
        System.out.println("--------------------------------------------");

        for (int taille : tailles) {
            List<Integer> cles = genererCles(taille, aleatoire);
            int tempsABR = (int) mesurerTempsConstruction(new ABR<>(), cles);

            // Mesure des temps de construction pour ARN
            int tempsARN = (int) mesurerTempsConstruction(new ARN<>(), cles);

            System.out.printf("%-10d %-20d %-20d%n", taille, tempsABR, tempsARN);
        }

        System.out.println("--------------------------------------------");
    }

    

    private static List<Integer> genererCles(int taille, boolean aleatoire) {
        List<Integer> cles = new ArrayList<>();
        for (int i = 0; i < taille; i++) {
            cles.add(i);
        }

        if (aleatoire) {
            Collections.shuffle(cles); 
        }

        return cles;
    }
    
    private static double mesurerTempsRecherche(Collection<Integer> structure, int taille) {
        long debut = System.currentTimeMillis();

        for (int cle = 0; cle < taille * 2; cle++) {
            structure.contains(cle);
        }

        long fin = System.currentTimeMillis();
        return (fin - debut); // Retourne la durée en ms
    }
    
    private static double mesurerTempsConstruction(Collection<Integer> structure, List<Integer> cles) {
        long debut = System.currentTimeMillis();

        for (int cle : cles) {
            structure.add(cle);
        }
        long fin = System.currentTimeMillis();
        return (fin - debut); // Retourne la durée en ms
    }
    
   
}