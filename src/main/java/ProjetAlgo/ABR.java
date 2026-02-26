package ProjetAlgo;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
/*auteur BENNABI ghiles Rayane
 *       BAOUCHE Mohamed Djaouad
 */
/*
Implantation de l'interface Collection basée sur les ABR.
Les éléments sont triés soit avec un Comparator fourni à la création, soit en utilisant l'ordre naturel (Comparable).
 */
public class ABR<E> extends AbstractCollection<E> {
	private Noeud racine;
	private int taille;
	private Comparator<? super E> cmp;

	//classe qui represente un noeu interne
	private class Noeud {
		E cle; 
		Noeud gauche; 
		Noeud droit; 
		Noeud pere; 

		//cree un noeud avec une cle donne a l'interieur
		Noeud(E cle) {
			this.cle = cle;
		}

        //fonction qui retourne le min du sous arbre gauche du noeud ou bien lui meme si ya pas de fils gauche
		Noeud minimum() {
			Noeud courant = this;
			while (courant.gauche != null) {
				courant = courant.gauche;
			}
			return courant;
		}
		/*
		 * Renvoie le successeur de ce nœud dans l'ordre des clés.
		 * ca retourne  le nœud contenant la clé imédiatement supérieure dans l'ordre des
		 * clés, ou {@code null} si ce nœud contient la clé maximale de l'arbre.
		 */
		Noeud suivant() {
			if (this.droit != null) {
				return this.droit.minimum();
			}
			Noeud courant = this;
			Noeud parent = courant.pere;
			while (parent != null && courant == parent.droit) {
				courant = parent;
				parent = parent.pere;
			}
			return parent;
		}
	}

	//Crée un arbre binaire de recherche vide. Les éléments seront ordonnés selon leur ordre naturel.
	public ABR() {
		this.taille = 0;
		this.racine = null;
		this.cmp = null;
	}

	// Crée un arbre binaire de recherche vide avec un comparateur personnalisé
	public ABR(Comparator<? super E> cmp) {
		this.cmp = cmp;
		this.racine = null;
		this.taille = 0;
	}

	// Crée un arbre binaire de recherche à partir d'une collection existante
	public ABR(Collection<? extends E> c) {
		this(); 
		addAll(c); // Ajoute tous les éléments de la collection
	}

	@Override
	public Iterator<E> iterator() {
		return new ABRIterator();
	}

	// Renvoie le nombre d'éléments dans l'arbre
	@Override
	public int size() {
		return taille;
	}

	// Compare deux éléments en utilisant soit le comparateur fourni, soit l'ordre naturel
	private int comparer(E a, E b) {
		if (cmp != null) {
			return cmp.compare(a, b);
		} else {
			try {
				return ((Comparable<? super E>) a).compareTo(b);
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("Éléments non comparables", e);
			}
		}
	}

	// Cherche un nœud contenant une clé spécifique dans l'arbre
	private Noeud rechercher(Object o) {
		if (o == null || (cmp == null && !(o instanceof Comparable))) {
			return null;
		}

		try {
			E element = (E) o;
			Noeud courant = racine;
			while (courant != null) {
				int cmpResult = comparer(element, courant.cle);
				if (cmpResult == 0) {
					return courant;
				} else if (cmpResult < 0) {
					courant = courant.gauche;
				} else {
					courant = courant.droit;
				}
			}
		} catch (ClassCastException e) {
			return null;
		}

		return null;
	}

	// Remplace un nœud par un autre dans l'arbre
	private void remplacer(Noeud u, Noeud v) {
		if (u.pere == null) {
			racine = v; // Si u est la racine, v devient la nouvelle racine
		} else if (u == u.pere.gauche) {
			u.pere.gauche = v;
		} else {
			u.pere.droit = v;
		}

		if (v != null) {
			v.pere = u.pere; // vas Mets à jour le parent du nœud remplaçant
		}
	}

	/**
	 * Supprime un nœud donné de l'arbre
	 * ca retourne le nœud contenant la clé qui suit celle de z dans l'ordre des
	 * clés. Cela est utile pour maintenir l'itérateur.
	 */
	private Noeud supprimer(Noeud z) {
		if (z == null)
			return null;

		Noeud y;

		if (z.gauche == null || z.droit == null) { // 0 ou 1 enfant
			y = (z.gauche != null) ? z.gauche : z.droit;
			remplacer(z, y);
			taille--;
		} else { // 2 enfants
			y = z.droit.minimum(); // Trouver le successeur
			z.cle = y.cle; // Copie la clé du successeur dans le nœud courant
			supprimer(y); 
		}

		return y;
	}


	private class ABRIterator implements Iterator<E> {
		private Noeud courant; // Le nœud actuellement parcouru par l'itérateur
		private Noeud derN; // Dernier nœud retourné par la méthode next()

		// Initialise l'itérateur pour commencer au nœud avec la clé minimale
		ABRIterator() {
			courant = (racine == null) ? null : racine.minimum();
			derN = null;
		}

		@Override
		public boolean hasNext() {
			return courant != null;
		}

		@Override
		public E next() {
			if (courant == null) {
				throw new NoSuchElementException(" plus d'éléments à parcourir.");
			}
			derN = courant;
			E value = courant.cle;
			courant = courant.suivant();
			return value;
		}

		@Override
		public void remove() {
			if (derN == null) {
				throw new IllegalStateException("La méthode next() doit être appelé avant");
			}

			Noeud p = supprimer(derN);
			if (p == courant) {
				courant = p.suivant();
			}
			derN = null;
		}
	}

	// Insère un élément dans l'arbre
	private boolean inserer(Noeud node, E element) {
		Noeud courant = node;
		Noeud parent = null;

		while (courant != null) {
			parent = courant;
			int cmpR = comparer(element, courant.cle);
			if (cmpR == 0) {
				return false;
			} else if (cmpR < 0) {
				courant = courant.gauche;
			} else {
				courant = courant.droit;
			}
		}

		// Position d'insertion trouvé
		Noeud newN = new Noeud(element);
		if (parent == null) {
			racine = newN;
		} else if (comparer(element, parent.cle) < 0) {
			parent.gauche = newN;
		} else {
			parent.droit = newN;
		}

		newN.pere = parent;
		taille++;
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return rechercher(o) != null;
	}

	@Override
	public boolean add(E element) {
		if (element == null) {
			throw new IllegalArgumentException("les Valeurs nulles ne sont pas autorisés.");
		}
		if (racine == null) {
			racine = new Noeud(element);
			taille++;
			return true;
		} else {
			return inserer(racine, element);
		}
	}

	//Calcule la longueur maximale des chaînes représentant les clés dans l'arbre.
	private int maxLongeurChaine(Noeud x) {
		return x == null ? 0 : Math.max(x.cle.toString().length(), Math.max(maxLongeurChaine(x.gauche), maxLongeurChaine(x.droit)));
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		toString(racine, buf, "", maxLongeurChaine(racine));
		return buf.toString();
	}

      // Construit une représentation graphique de l'arbre pour l'affichage.

	private void toString(Noeud x, StringBuffer buf, String path, int len) {
		if (x == null)
			return;
		toString(x.droit, buf, path + "D", len); // Parcours du sous-arbre droit
		for (int i = 0; i < path.length(); i++) {
			for (int j = 0; j < len + 6; j++)
				buf.append(' ');
			char c = ' ';
			if (i == path.length() - 1)
				c = '+';
			else if (path.charAt(i) != path.charAt(i + 1))
				c = '|';
			buf.append(c);
		}
		buf.append("-- ").append(x.cle.toString());
		if (x.gauche != null || x.droit != null) {
			buf.append(" --");
			for (int j = x.cle.toString().length(); j < len; j++)
				buf.append('-');
			buf.append('|');
		}
		buf.append("\n");
		toString(x.gauche, buf, path + "G", len); // Parcours du sous-arbre gauche
	}
}