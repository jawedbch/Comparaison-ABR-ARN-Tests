package ProjetAlgo;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
/*auteur BENNABI ghiles Rayane
 *       BAOUCHE Mohamed Djaouad
 */
/**
 Implémentation d'une collection basée sur un arbre rouge-noir (ARN).
 Les éléments sont ordonnés soit via leur ordre naturel (Comparable),
 soit à l’aide d’un Comparator fourni lors de la création.
 L’ARN garantit des opérations logarithmiques dans le pire cas grâce
 à des contraintes structurelles (équilibrage par couleurs et rotations).
 Certaines méthodes héritées d’AbstractCollection sont redéfinies afin
 d’assurer de bonnes performances.

 @param <E> type des éléments stockés dans l’arbre
 */
public class ARN<E extends Comparable<? super E>> extends AbstractCollection<E> {
    private Noeud racine;
    private Noeud sentinelle; 
    private int taille;
    private Comparator<? super E> cmp; // Comparateur pour l'ordre des éléments

    // Couleurs possibles d’un nœud dans un arbre rouge-noir. N : noir — R : rouge
    private enum couleur {
        N, R
    }

    // Classe interne représentant un nœud de l'arbre
    private class Noeud {
        E cle; 
        Noeud gauche; 
        Noeud droit; 
        Noeud pere; 
        couleur couleur; // Couleur du nœud (rouge ou noire)

        // Crée un nouveau nœud avec une clé donnée
        Noeud(E cle) {
            this.couleur = ARN.couleur.N; // Par défaut, la couleur est noire
            this.cle = cle;
            this.gauche = this.droit = this.pere = sentinelle; // Feuilles pointant vers le sentinelle
        }

        // Trouve le nœud contenant la clé minimale dans le sous-arbre enraciné au nœud actuel
        Noeud minimum() {
            Noeud courant = this;
            while (courant.gauche != sentinelle) {
                courant = courant.gauche;
            }
            return courant;
        }

        // Trouve le successeur du nœud actuel dans l'ordre des clés
        Noeud suivant() {
            if (droit != sentinelle) {
                return droit.minimum();
            }

            Noeud courant = this;
            Noeud parent = pere;
            while (parent != sentinelle && courant == parent.droit) {
                courant = parent;
                parent = parent.pere;
            }
            return parent;
        }
    }

    // Crée un ARN vide où les éléments sont ordonnés selon leur ordre naturel
    public ARN() {
        taille = 0;
        sentinelle = new Noeud(null); // Initialisation du nœud sentinelle
        racine = sentinelle;
        this.cmp = Comparator.naturalOrder(); // Comparateur par défaut
    }

    // Crée un ARN vide où les éléments sont ordonnés selon un comparateur donné
    public ARN(Comparator<? super E> cmp) {
        taille = 0;
        sentinelle = new Noeud(null); // Initialisation du nœud sentinelle
        racine = sentinelle;
        this.cmp = cmp; // Comparateur personnalisé
    }

    // Crée un ARN contenant les mêmes éléments qu'une collection donnée. L'ordre
    //des éléments est basé sur l'ordre naturel
    public ARN(Collection<? extends E> c) {
        this();
        if (c != sentinelle) { // Si la collection n'est pas vide
            for (E element : c) {
                add(element); // Ajout des éléments dans l'arbre
            }
        }
    }

    // Renvoie un itérateur permettant de parcourir l'arbre dans l'ordre des clés
    @Override
    public Iterator<E> iterator() {
        return new ARNIterator();
    }

    // Renvoie le nombre d'éléments stockés dans l'arbre
    @Override
    public int size() {
        return taille;
    }

    // Ajoute un élément dans l'arbre.
    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        Noeud z = new Noeud(e);
        ajouter(z);
        taille++;
        return true;
    }


    //  Insère un nouveau nœud dans l’arbre en respectant la structure d’un arbre binaire de recherche (ABR)
    private void ajouter(Noeud z) {
        Noeud y = sentinelle;
        Noeud x = racine;
        while (x != sentinelle) {
            y = x;
            x = cmp.compare(z.cle, x.cle) < 0 ? x.gauche : x.droit;
        }
        z.pere = y;
        if (y == sentinelle) // arbre vide
            racine = z;
        else if (cmp.compare(z.cle, y.cle) < 0)
            y.gauche = z;
        else
            y.droit = z;

        z.gauche = z.droit = sentinelle;
        z.couleur = couleur.R;
        ajouterCorrection(z);
    }

    // Répare les éventuelles violations des propriétés rouge-noir après une insertion
    private void ajouterCorrection(Noeud z) {
        Noeud y;
        while (z.pere != sentinelle && z.pere.couleur == couleur.R) {
            if (z.pere == z.pere.pere.gauche) {
                y = z.pere.pere.droit;
                if (y != sentinelle && y.couleur == couleur.R) { // Cas 1
                    z.pere.couleur = couleur.N;
                    y.couleur = couleur.N;
                    z.pere.pere.couleur = couleur.R;
                    z = z.pere.pere;
                } else {
                    if (z == z.pere.droit) { // Cas 2
                        z = z.pere;
                        rotationGauche(z);
                    }
                    z.pere.couleur = couleur.N; // Cas 3
                    z.pere.pere.couleur = couleur.R;
                    rotationDroite(z.pere.pere);
                }
            } else {
                y = z.pere.pere.gauche;
                if (y != sentinelle && y.couleur == couleur.R) { // Cas 1
                    z.pere.couleur = couleur.N;
                    y.couleur = couleur.N;
                    z.pere.pere.couleur = couleur.R;
                    z = z.pere.pere;
                } else {
                    if (z == z.pere.gauche) { // Cas 2
                        z = z.pere;
                        rotationDroite(z);
                    }
                    z.pere.couleur = couleur.N; // Cas 3
                    z.pere.pere.couleur = couleur.R;
                    rotationGauche(z.pere.pere);
                }
            }
        }
        racine.couleur = couleur.N;
    }

    /**
     * Effectue une rotation gauche sur un nœud dans un arbre rouge-noir.
     * Cette opération réorganise les liens entre les nœuds pour équilibrer l'arbre
     * lorsque le sous-arbre droit devient plus lourd. La rotation met à jour le
     * sous-arbre gauche du nœud droit, réassigne les parents des nœuds impliqués,
     * et ajuste la racine si nécessaire. Enfin, la méthode appelle
     * {@code rotationGauheCorrection()} pour corriger les métadonnées associées.
     *
     * @param x le nœud autour duquel effectuer la rotation
     */
    private void rotationGauche(Noeud x) {
        Noeud y = x.droit;
        x.droit = y.gauche;

        if (y.gauche != sentinelle)
            y.gauche.pere = x;

        y.pere = x.pere;

        if (x.pere == sentinelle)
            racine = y;
        else if (x == x.pere.gauche)
            x.pere.gauche = y;
        else
            x.pere.droit = y;

        y.gauche = x;
        x.pere = y;
    }



    /**
     * Effectue une rotation droite sur un nœud dans un arbre rouge-noir.
     * Rééquilibre l'arbre en ajustant les liens entre les nœuds, avec
     * mise à jour des sous-arbres et des parents.
     * Corrige les métadonnées via {@code rotationDroiteCorrection()}.
     *
     * @param y le nœud pivot de la rotation
     */
    private void rotationDroite(Noeud y) {
        Noeud x = y.gauche;

        y.gauche = x.droit;
        if (x.droit != sentinelle)
            x.droit.pere = y;

        x.pere = y.pere;

        if (y.pere == sentinelle)
            racine = x;
        else if (y == y.pere.droit)
            y.pere.droit = x;
        else
            y.pere.gauche = x;

        x.droit = y;
        y.pere = x;
    }


    // Itérateur pour parcourir les éléments de l'arbre dans l'ordre
    private class ARNIterator implements Iterator<E> {
        private Noeud noeudSuivant; // Le prochain nœud à retourner
        private Noeud courant; // Le dernier nœud retourné

        public ARNIterator() {
            // Initialise le prochain nœud avec le plus petit élément
            noeudSuivant = racine != sentinelle ? racine.minimum() : sentinelle;
            courant = sentinelle;
        }

        @Override
        public boolean hasNext() {
            return noeudSuivant != sentinelle;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // Avance au prochain nœud et retourne la clé de l'actuel
            courant = noeudSuivant;
            noeudSuivant = noeudSuivant.suivant();
            return courant.cle;
        }

        @Override
        public void remove() {
            if (courant == sentinelle) {
                throw new IllegalStateException();
            }

            // Supprime le dernier nœud retourné
            supprimer(courant);
            taille--;
            courant = sentinelle;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        Noeud noeud = rechercher(o);
        if (noeud == sentinelle) {
            return false;
        }
        supprimer(noeud);
        taille--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        return (rechercher(o) != sentinelle);
    }

    // Vérifie si l'arbre est vide
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    // Réinitialise l'arbre à l'état vide
    @Override
    public void clear() {
        racine = sentinelle;
        taille = 0;
    }

    // Supprime tous les éléments de la collection donnée
    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object element : c) {
            remove(element);
        }
        return true;
    }

    // Recherche un nœud contenant une clé donnée dans l'arbre.
    private Noeud rechercher(Object o) {
        return rechercher(racine, o); // Démarre la recherche depuis la racine
    }

    // Recherche récursive d'un nœud contenant une clé donnée dans un sous-arbre
    private Noeud rechercher(Noeud racine, Object o) {
        if (racine == sentinelle)
            return sentinelle;

        if (racine.cle.equals(o))
            return racine;


        // Compare la clé pour décider du chemin (gauche ou droit)
        if (cmp.compare((E) o, racine.cle) < 0) {
            return rechercher(racine.gauche, o); // Recherche dans le sous-arbre gauche
        } else {
            return rechercher(racine.droit, o); // Recherche dans le sous-arbre droit
        }
    }

    // Supprime un nœud de l'arbre rouge-noir selon l'algorithme du prof.
    private void supprimer(Noeud z) {
        Noeud y, x;

        // Déterminer le nœud à détacher
        if (z.gauche == sentinelle || z.droit == sentinelle)
            y = z;
        else
            y = z.suivant(); // successeur de z

        // Déterminer le fils unique de y (ou sentinelle)
        if (y.gauche != sentinelle)
            x = y.gauche;
        else
            x = y.droit;

        // INCONDITIONNELLE : même si x == sentinelle
        x.pere = y.pere;

        // Détacher y de son parent
        if (y.pere == sentinelle) {
            racine = x;
        } else {
            if (y == y.pere.gauche)
                y.pere.gauche = x;
            else
                y.pere.droit = x;
        }

        // Copier la clé si nécessaire
        if (y != z)
            z.cle = y.cle;

        if (y.couleur == couleur.N)
            supprimerCorrection(x);
    }


    // Corrige les violations des propriétés rouge-noir après la suppression d’un nœud noir
    private void supprimerCorrection(Noeud x) {
        Noeud w;

        while (x != racine && x.couleur == couleur.N) {
            if (x == x.pere.gauche) {
                w = x.pere.droit;

                if (w.couleur == couleur.R) {
                    // Cas 1
                    w.couleur = couleur.N;
                    x.pere.couleur = couleur.R;
                    rotationGauche(x.pere);
                    w = x.pere.droit;
                }

                if (w.gauche.couleur == couleur.N && w.droit.couleur == couleur.N) {
                    // Cas 2
                    w.couleur = couleur.R;
                    x = x.pere;
                } else {
                    if (w.droit.couleur == couleur.N) {
                        // Cas 3
                        w.gauche.couleur = couleur.N;
                        w.couleur = couleur.R;
                        rotationDroite(w);
                        w = x.pere.droit;
                    }
                    // Cas 4
                    w.couleur = x.pere.couleur;
                    x.pere.couleur = couleur.N;
                    w.droit.couleur = couleur.N;
                    rotationGauche(x.pere);
                    x = racine;
                }
            } else {
                w = x.pere.gauche;

                if (w.couleur == couleur.R) {
                    // Cas 1'
                    w.couleur = couleur.N;
                    x.pere.couleur = couleur.R;
                    rotationDroite(x.pere);
                    w = x.pere.gauche;
                }

                if (w.droit.couleur == couleur.N && w.gauche.couleur == couleur.N) {
                    // Cas 2'
                    w.couleur = couleur.R;
                    x = x.pere;
                } else {
                    if (w.gauche.couleur == couleur.N) {
                        // Cas 3'
                        w.droit.couleur = couleur.N;
                        w.couleur = couleur.R;
                        rotationGauche(w);
                        w = x.pere.gauche;
                    }
                    // Cas 4'
                    w.couleur = x.pere.couleur;
                    x.pere.couleur = couleur.N;
                    w.gauche.couleur = couleur.N;
                    rotationDroite(x.pere);
                    x = racine;
                }
            }
        }

        x.couleur = couleur.N;
    }



    // Calcule la longueur maximale des représentations textuelles des clés
    private int maxLongChaine(Noeud x) {
        if (x == sentinelle) {
            return 0; // Pas de clé pour un nœud sentinelle
        }
        // Renvoie la longueur maximale entre la clé du nœud actuel et les longueurs maximales de ses sous-arbres
        return Math.max(x.cle.toString().length(),
                Math.max(maxLongChaine(x.gauche), maxLongChaine(x.droit)));
    }

    @Override
    public String toString() {
        // Utilise un StringBuffer pour construire une représentation textuelle de l'arbre
        StringBuffer buf = new StringBuffer();
        toString(racine, buf, "", maxLongChaine(racine));
        return buf.toString();
    }


    // Construit récursivement une vue textuelle de l’arbre, en indiquant limites, chemins et couleurs. Le parcours est effectué en infixe
    private void toString(Noeud x, StringBuffer buf, String path, int len) {
        if (x == sentinelle) {
            return; // Arrête le parcours si on atteint un nœud sentinelle
        }

        // Parcours du sous-arbre droit en premier (ordre décroissant)
        toString(x.droit, buf, path + "D", len);

        // Ajoute des espaces pour aligner correctement les nœuds
        for (int i = 0; i < path.length(); i++) {
            for (int j = 0; j < len + 6; j++) {
                buf.append(' ');
            }
            char c = ' ';
            if (i == path.length() - 1) {
                c = '+'; // Indique le dernier niveau du chemin
            } else if (path.charAt(i) != path.charAt(i + 1)) {
                c = '|'; // Indique un changement de direction dans le chemin
            }
            buf.append(c);
        }

        // Ajoute la clé et la couleur du nœud actuel
        buf.append("--- " + x.cle.toString() + ":" + x.couleur);

        // Ajoute des traits pour indiquer que le nœud a des enfants
        if (x.gauche != sentinelle || x.droit != sentinelle) {
            buf.append(" ---");
            for (int j = x.cle.toString().length(); j < len; j++) {
                buf.append('-');
            }
            buf.append('|'); // Indique la continuation vers les enfants
        }

        buf.append("\n");

        // Parcours du sous-arbre gauche
        toString(x.gauche, buf, path + "G", len);
    }

}