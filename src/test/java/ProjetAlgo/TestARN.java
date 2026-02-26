package ProjetAlgo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.NoSuchElementException;

public class TestARN {

  // ========== TESTS DE BASE ==========

  @Test
  public void testARNVide() {
    ARN<Integer> arn = new ARN<>();
    assertEquals("", arn.toString(), "Création d'un ARN vide");
    assertEquals(0, arn.size(), "Taille doit être 0");
    assertTrue(arn.isEmpty(), "L'arbre doit être vide");
  }

  @Test
  public void testInsertionRacineNoire() {
    ARN<Integer> arn = new ARN<>();
    arn.add(37);

    assertEquals(1, arn.size(), "Taille doit être 1");
    assertTrue(arn.contains(37), "Doit contenir 37");
    assertFalse(arn.isEmpty(), "L'arbre ne doit pas être vide");
  }

  @Test
  public void testInsertionNoeudNull() {
    ARN<Integer> arn = new ARN<>();

    assertThrows(NullPointerException.class, () -> {
      arn.add(null);
    }, "L'insertion d'un noeud null devrait lever une exception");
  }

  @Test
  public void testRechercheNoeudNull() {
    ARN<Integer> arn = new ARN<>();
    arn.add(10);

    assertFalse(arn.contains(null), "Recherche d'un noeud null doit retourner false");
  }

  @Test
  public void testRechercheNoeudAbsent() {
    ARN<Integer> arn = new ARN<>();
    arn.add(16);

    assertFalse(arn.contains(0), "Recherche d'un noeud absent");
    assertFalse(arn.contains(100), "Recherche d'un autre noeud absent");
  }

  @Test
  public void testSuppressionNoeudNull() {
    ARN<Integer> arn = new ARN<>();
    arn.add(12);

    assertFalse(arn.remove(null), "Suppression d'un noeud null doit retourner false");
    assertEquals(1, arn.size(), "Taille ne doit pas changer");
  }

  @Test
  public void testSuppressionNoeudAbsent() {
    ARN<Integer> arn = new ARN<>();
    arn.add(20);

    assertFalse(arn.remove(0), "Suppression d'un noeud absent doit retourner false");
    assertEquals(1, arn.size(), "Taille ne doit pas changer");
  }

  // ========== TESTS D'INSERTION AVANCÉS ==========

  @Test
  public void testInsertionMultiple() {
    ARN<Integer> arn = new ARN<>();
    Integer[] valeurs = {50, 25, 75, 10, 30, 60, 80};

    for (Integer val : valeurs) {
      assertTrue(arn.add(val), "L'ajout doit réussir");
    }

    assertEquals(valeurs.length, arn.size(), "Taille incorrecte");

    for (Integer val : valeurs) {
      assertTrue(arn.contains(val), "Doit contenir " + val);
    }
  }

  @Test
  public void testInsertionCroissante() {
    ARN<Integer> arn = new ARN<>();

    // Insertion de 1 à 20 (cas défavorable pour ABR)
    for (int i = 1; i <= 20; i++) {
      arn.add(i);
    }

    assertEquals(20, arn.size(), "Taille doit être 20");

    for (int i = 1; i <= 20; i++) {
      assertTrue(arn.contains(i), "Doit contenir " + i);
    }
  }

  @Test
  public void testInsertionDecroissante() {
    ARN<Integer> arn = new ARN<>();

    // Insertion de 20 à 1
    for (int i = 20; i >= 1; i--) {
      arn.add(i);
    }

    assertEquals(20, arn.size(), "Taille doit être 20");

    for (int i = 1; i <= 20; i++) {
      assertTrue(arn.contains(i), "Doit contenir " + i);
    }
  }

  @Test
  public void testInsertionAleatoire() {
    ARN<Integer> arn = new ARN<>();
    Random rand = new Random(42); // Seed fixe pour reproductibilité
    List<Integer> valeurs = new ArrayList<>();

    for (int i = 0; i < 100; i++) {
      int val = rand.nextInt(1000);
      valeurs.add(val);
      arn.add(val);
    }

    for (Integer val : valeurs) {
      assertTrue(arn.contains(val), "Doit contenir " + val);
    }
  }

  @Test
  public void testInsertionDoublons() {
    ARN<Integer> arn = new ARN<>();

    assertTrue(arn.add(10), "Première insertion doit réussir");
    assertTrue(arn.add(10), "Deuxième insertion doit aussi réussir (doublons autorisés)");

    assertTrue(arn.contains(10), "Doit contenir 10");
  }

  // ========== TESTS DE SUPPRESSION AVANCÉS ==========

  @Test
  public void testSuppressionRacine() {
    ARN<Integer> arn = new ARN<>();
    arn.add(50);

    assertTrue(arn.remove(50), "La suppression doit réussir");
    assertEquals(0, arn.size(), "Taille doit être 0");
    assertFalse(arn.contains(50), "Ne doit plus contenir 50");
    assertTrue(arn.isEmpty(), "L'arbre doit être vide");
  }

  @Test
  public void testSuppressionFeuille() {
    ARN<Integer> arn = new ARN<>();
    arn.add(50);
    arn.add(25);
    arn.add(75);

    assertTrue(arn.remove(25), "Suppression de feuille doit réussir");
    assertEquals(2, arn.size(), "Taille doit être 2");
    assertFalse(arn.contains(25), "Ne doit plus contenir 25");
    assertTrue(arn.contains(50), "Doit encore contenir 50");
    assertTrue(arn.contains(75), "Doit encore contenir 75");
  }

  @Test
  public void testSuppressionNoeudUnEnfant() {
    ARN<Integer> arn = new ARN<>();
    arn.add(50);
    arn.add(25);
    arn.add(75);
    arn.add(10);

    assertTrue(arn.remove(25), "Suppression noeud avec 1 enfant doit réussir");
    assertEquals(3, arn.size(), "Taille doit être 3");
    assertFalse(arn.contains(25), "Ne doit plus contenir 25");
    assertTrue(arn.contains(10), "Doit encore contenir 10");
  }

  @Test
  public void testSuppressionNoeudDeuxEnfants() {
    ARN<Integer> arn = new ARN<>();
    Integer[] valeurs = {50, 25, 75, 10, 30, 60, 80};
    for (Integer val : valeurs) {
      arn.add(val);
    }

    assertTrue(arn.remove(50), "Suppression noeud avec 2 enfants doit réussir");
    assertEquals(6, arn.size(), "Taille doit être 6");
    assertFalse(arn.contains(50), "Ne doit plus contenir 50");
  }

  @Test
  public void testSuppressionSequentielle() {
    ARN<Integer> arn = new ARN<>();
    Integer[] valeurs = {50, 25, 75, 10, 30, 60, 80, 5, 15, 27, 35};

    for (Integer val : valeurs) {
      arn.add(val);
    }

    // Supprimer dans un ordre différent
    Integer[] aSupprimer = {25, 75, 10, 80, 50, 30, 60, 5, 15, 27, 35};

    for (Integer val : aSupprimer) {
      int tailleBefore = arn.size();
      assertTrue(arn.remove(val), "Suppression de " + val + " doit réussir");
      assertEquals(tailleBefore - 1, arn.size(), "Taille doit diminuer de 1");
      assertFalse(arn.contains(val), "Ne doit plus contenir " + val);
    }

    assertEquals(0, arn.size(), "L'arbre doit être vide");
    assertTrue(arn.isEmpty(), "isEmpty doit retourner true");
  }

  @Test
  public void testSuppressionAleatoire() {
    ARN<Integer> arn = new ARN<>();
    Random rand = new Random(42);
    List<Integer> valeurs = new ArrayList<>();

    // Insertion
    for (int i = 0; i < 50; i++) {
      int val = i * 2; // Valeurs paires pour éviter les doublons
      valeurs.add(val);
      arn.add(val);
    }

    // Suppression de la moitié
    for (int i = 0; i < 25; i++) {
      Integer val = valeurs.get(i);
      assertTrue(arn.remove(val), "Suppression de " + val + " doit réussir");
      assertFalse(arn.contains(val), "Ne doit plus contenir " + val);
    }

    assertEquals(25, arn.size(), "Taille doit être 25");
  }

  // ========== TESTS D'ITÉRATEUR ==========

  @Test
  public void testIterateurOrdre() {
    ARN<Integer> arn = new ARN<>();
    Integer[] valeurs = {50, 25, 75, 10, 30, 60, 80};

    for (Integer val : valeurs) {
      arn.add(val);
    }

    List<Integer> parcouru = new ArrayList<>();
    for (Integer val : arn) {
      parcouru.add(val);
    }

    // Vérifier l'ordre croissant
    for (int i = 1; i < parcouru.size(); i++) {
      assertTrue(parcouru.get(i - 1) <= parcouru.get(i),
              "L'itérateur doit parcourir dans l'ordre croissant");
    }
  }

  @Test
  public void testIterateurVide() {
    ARN<Integer> arn = new ARN<>();
    Iterator<Integer> it = arn.iterator();

    assertFalse(it.hasNext(), "L'itérateur d'un arbre vide ne doit pas avoir de next");
  }

  @Test
  public void testIterateurNext() {
    ARN<Integer> arn = new ARN<>();

    assertThrows(NoSuchElementException.class, () -> {
      Iterator<Integer> it = arn.iterator();
      it.next();
    }, "next() sur un itérateur vide doit lever NoSuchElementException");
  }

  @Test
  public void testIterateurRemove() {
    ARN<Integer> arn = new ARN<>();
    arn.add(50);
    arn.add(25);
    arn.add(75);

    Iterator<Integer> it = arn.iterator();
    assertTrue(it.hasNext());
    Integer val = it.next();
    it.remove();

    assertFalse(arn.contains(val), "La valeur doit être supprimée");
    assertEquals(2, arn.size(), "Taille doit être 2");
  }

  @Test
  public void testIterateurRemoveSansNext() {
    ARN<Integer> arn = new ARN<>();
    arn.add(50);

    assertThrows(IllegalStateException.class, () -> {
      Iterator<Integer> it = arn.iterator();
      it.remove();
    }, "remove() sans next() doit lever IllegalStateException");
  }

  @Test
  public void testIterateurRemoveDouble() {
    ARN<Integer> arn = new ARN<>();
    arn.add(50);
    arn.add(25);

    Iterator<Integer> it = arn.iterator();
    it.next();
    it.remove();

    assertThrows(IllegalStateException.class, () -> {
      it.remove();
    }, "Deux remove() consécutifs doivent lever IllegalStateException");
  }

  // ========== TESTS DE COLLECTION ==========

  @Test
  public void testClear() {
    ARN<Integer> arn = new ARN<>();
    arn.add(50);
    arn.add(25);
    arn.add(75);

    arn.clear();

    assertEquals(0, arn.size(), "Taille doit être 0");
    assertTrue(arn.isEmpty(), "L'arbre doit être vide");
    assertFalse(arn.contains(50), "Ne doit plus contenir d'éléments");
  }

  @Test
  public void testRemoveAll() {
    ARN<Integer> arn = new ARN<>();
    Integer[] valeurs = {50, 25, 75, 10, 30, 60, 80};
    for (Integer val : valeurs) {
      arn.add(val);
    }

    List<Integer> aSupprimer = Arrays.asList(25, 75, 80);
    arn.removeAll(aSupprimer);

    for (Integer val : aSupprimer) {
      assertFalse(arn.contains(val), "Ne doit plus contenir " + val);
    }

    assertEquals(4, arn.size(), "Taille doit être 4");
  }

  @Test
  public void testConstructeurCollection() {
    List<Integer> liste = Arrays.asList(50, 25, 75, 10, 30, 60, 80);
    ARN<Integer> arn = new ARN<>(liste);

    assertEquals(liste.size(), arn.size(), "Taille incorrecte");

    for (Integer val : liste) {
      assertTrue(arn.contains(val), "Doit contenir " + val);
    }
  }

  @Test
  public void testIsEmpty() {
    ARN<Integer> arn = new ARN<>();

    assertTrue(arn.isEmpty(), "Arbre vide doit retourner true");

    arn.add(10);
    assertFalse(arn.isEmpty(), "Arbre non vide doit retourner false");

    arn.remove(10);
    assertTrue(arn.isEmpty(), "Arbre vidé doit retourner true");
  }

  // ========== TESTS DE STRESS ==========

  @Test
  public void testStressInsertionSuppression() {
    ARN<Integer> arn = new ARN<>();
    Random rand = new Random(123);
    List<Integer> valeursAjoutees = new ArrayList<>();

    // Insertion de 1000 éléments uniques
    for (int i = 0; i < 1000; i++) {
      int val = i;
      valeursAjoutees.add(val);
      arn.add(val);
    }

    assertEquals(1000, arn.size(), "Taille doit être 1000");

    // Suppression de 500 éléments aléatoires
    for (int i = 0; i < 500; i++) {
      Integer val = valeursAjoutees.get(i);
      arn.remove(val);
    }

    assertEquals(500, arn.size(), "Taille doit être 500");

    // Vérifier que les éléments restants sont présents
    for (int i = 500; i < 1000; i++) {
      assertTrue(arn.contains(valeursAjoutees.get(i)),
              "Doit contenir " + valeursAjoutees.get(i));
    }
  }

  @Test
  public void testGrandArbre() {
    ARN<Integer> arn = new ARN<>();

    // Insertion de 1000 éléments
    for (int i = 0; i < 1000; i++) {
      arn.add(i);
    }

    assertEquals(1000, arn.size(), "Taille doit être 1000");

    // Vérifier que tous les éléments sont présents
    for (int i = 0; i < 1000; i++) {
      assertTrue(arn.contains(i), "Doit contenir " + i);
    }

    // Vérifier l'ordre avec l'itérateur
    int compteur = 0;
    for (Integer val : arn) {
      assertEquals(compteur, val.intValue(), "L'ordre doit être respecté");
      compteur++;
    }
  }

  @Test
  public void testInsertionSuppressionMelangees() {
    ARN<Integer> arn = new ARN<>();

    // Mélange d'insertions et suppressions
    arn.add(10);
    arn.add(20);
    arn.add(30);
    assertEquals(3, arn.size());

    arn.remove(20);
    assertEquals(2, arn.size());

    arn.add(40);
    arn.add(50);
    assertEquals(4, arn.size());

    arn.remove(10);
    arn.remove(30);
    assertEquals(2, arn.size());

    assertTrue(arn.contains(40));
    assertTrue(arn.contains(50));
    assertFalse(arn.contains(10));
    assertFalse(arn.contains(20));
    assertFalse(arn.contains(30));
  }

  // ========== TESTS AVEC COMPARATEUR ==========

  @Test
  public void testComparateurPersonnalise() {
    // Comparateur inverse (ordre décroissant)
    ARN<Integer> arn = new ARN<>((a, b) -> b.compareTo(a));

    arn.add(10);
    arn.add(20);
    arn.add(30);
    arn.add(5);

    List<Integer> parcouru = new ArrayList<>();
    for (Integer val : arn) {
      parcouru.add(val);
    }

    // Vérifier l'ordre décroissant
    assertEquals(30, parcouru.get(0).intValue());
    assertEquals(20, parcouru.get(1).intValue());
    assertEquals(10, parcouru.get(2).intValue());
    assertEquals(5, parcouru.get(3).intValue());
  }

  // ========== TESTS DE ROBUSTESSE ==========

  @Test
  public void testSuppressionTousLesElements() {
    ARN<Integer> arn = new ARN<>();

    for (int i = 0; i < 20; i++) {
      arn.add(i);
    }

    for (int i = 0; i < 20; i++) {
      assertTrue(arn.remove(i), "Suppression doit réussir");
    }

    assertEquals(0, arn.size(), "L'arbre doit être vide");
    assertTrue(arn.isEmpty());
  }

  @Test
  public void testOperationsApresVidage() {
    ARN<Integer> arn = new ARN<>();

    arn.add(10);
    arn.add(20);
    arn.clear();

    // L'arbre doit être réutilisable
    arn.add(30);
    assertEquals(1, arn.size());
    assertTrue(arn.contains(30));

    arn.remove(30);
    assertEquals(0, arn.size());
  }
}