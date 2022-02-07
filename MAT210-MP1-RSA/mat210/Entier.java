package mat210;

/**
 * Fichier distribué dans le cadre du cours MAT210, session automne 2021, à l'ÉTS.
 *
 * Par Xavier Provençal.
 *
 * Modifications par les étudiant·e·s : 
 *  - TODO inscrivez vos noms ici.
 *  - TODO inscrivez vos noms ici.
 *  - TODO inscrivez vos noms ici.
 *  - TODO inscrivez vos noms ici.
 */



import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;


/**
 * Représente un entier positif (0 inclu), de taille arbitrairement grande.
 *
 * Pour être exact, la valeur maximum théorique est 10^Integer.MAX_VALUE - 1,
 * mais en pratique cette valeur est réduite par la taille de la pile (Java
 * heap space).
 *
 * Opérations supportées : addition, soustraction (avec résultat positif),
 * multiplication, puissance et modulo.
 */
public class Entier {

    //
    // DONNEES MEMBRES
    //

    /**
     * Tableau dans lequel sont stockées les décimales de l'entier. La case 0
     * contient le chiffre le moins significatif.
     */
    protected ArrayList<Integer> decimales;


    //
    // CONSTRUCTEURS
    // 

    /**
     * Constructeur vide, non accessible de l'extérieur de la classe.
     */
    private Entier() {
        this.decimales = new ArrayList<Integer>();
    }


    /**
     * Constructeur à partir d'un ``long``
     *
     * @param x  valeur de l'entier
     */
    public Entier(long x) {
        this.decimales = new ArrayList<Integer>();
        // x%10 donne la dernière décimale de x
        // x/10 enlève la dernière décimale de x
        do {
            this.decimales.add((int) x%10);
            x /= 10;
        } while (x > 0);
    }


    /** 
     * Constructeur de copie.
     *
     * @param aCopier  Entier à copier
     */
    public Entier(Entier aCopier) {
        this.decimales = new ArrayList<Integer>(aCopier.decimales);
    }


    /**
     * Constructeur à partir d'une chaîne de caractères représentant un entier
     * écrit en base 10.
     *
     * @param s  Représentation textuelle de l'entier, en base 10
     */
    public Entier(String s) {
        this.decimales = new ArrayList<Integer>();
        int zero_index = Character.getNumericValue('0');
        for( int i=s.length()-1; i>=0; --i) {
            this.decimales.add(Character.getNumericValue(s.charAt(i) - zero_index));
        }
    }



    //
    // OPÉRATIONS ARITHMÉTIQUES
    // 

    /**
     * Additionne l'entier spécifié à l'entier.
     *
     * Cette fonction ne modifie pas l'entier actuel (this), ni celui spécifié en
     * paramètre. Un nouvel entier est retourné.
     *
     * @param autre  valeur à additionner
     * @return  la somme de `this` et `autre`
     */
    public Entier somme(Entier autre) {
        
        // Exercice 1
        //
        // Déboguer cette fonction !
        //
        Entier somme = new Entier();
        int decimale, retenue = 0;
        int lng = Math.max(this.longueur(), autre.longueur());
        for (int i=0; i<lng; ++i) {
            // On utilise le fait que .getDecimale(i) retourne 0 si (i >= this.longueur())
            decimale = this.getDecimale(i) + autre.getDecimale(i) + retenue;   // <--- bug
            retenue = decimale / 10;
            somme.decimales.add(decimale % 10);
        }
        if (retenue != 0) {                      // <--- bug
            somme.decimales.add(retenue);
        }
        return somme;
    }


    public Entier multiplicationSimple(int valeur) {
    	
    	Entier multiplicationSimple = new  Entier();
    	int decimale, retenue = 0;
    	for (int i = 0; i < this.longueur(); i++) {
    		decimale = this.getDecimale(i) * valeur + retenue; 
    		retenue = decimale / 10;
    		multiplicationSimple.decimales.add(decimale % 10);
		}
    	if (retenue != 0) {                      
            multiplicationSimple.decimales.add(retenue);
        }
		return multiplicationSimple;
    }
    
    public Entier ajoutZero(int nbZero) {
    	int ZERO = 0;
    	
    	Entier ajoutZero = new Entier();
    	for (int i = 0; i < nbZero; i++) {
			ajoutZero.decimales.add(ZERO);
		}
    	for(int j=0;j <this.longueur();j++){
            ajoutZero.decimales.add(this.getDecimale(j));
        }
    	return ajoutZero;
    }

    /**
     * Multiplie l'entier spécifié à l'entier.
     *
     * Cette fonction ne modifie pas l'entier actuel (this), ni celui spécifié en
     * paramètre. Un nouvel entier est retourné.
     *
     * @param autre  valeur à multiplier
     * @return  le produit de `this` et `autre`
     * @throws CloneNotSupportedException 
     */
    public Entier produit(Entier autre) {
    	
    	long ZERO = 0;
    	Entier entierZero = new Entier(ZERO);
    	Entier produit = new Entier();

    	if(autre.longueur() < this.longueur()) {
    		entierZero = autre.produit(this);
    	} else if(this.estZero()) {
    		return entierZero;
    	} else {
    		for (int i = 0; i < this.longueur(); i++) {
				produit = autre.multiplicationSimple(this.getDecimale(i));
				entierZero = entierZero.somme(produit.ajoutZero(i));
			}
    	}
    	
    	return entierZero;
    }
    

    /**
     * Retourne le modulo l'entier par l'entier spécifié.
     *
     * Cette fonction ne modifie pas l'entier actuel (this), ni celui spécifié en
     * pramètre. Un nouvel entier est retourné.
     *
     * @param p  exposant
     * @return  la valeur de `this` à la puissance `p`
     */
    public Entier puissance(Entier p) {
    	
    	Entier reponse = new Entier(this);

    	if (p.estZero()) {
    		reponse = new Entier(1);
        } else  {
            for (Entier i = new Entier(1); i.plusPetit(p); i = i.somme(new Entier(1))) {
            reponse = this.produit(reponse);
            }
		}
        
        return reponse;
    }


    /**
     * Retourne le modulo l'entier par l'entier spécifié.
     *
     * Pré-condition : l'entier spécifié (m) ne doit pas être 0.
     *
     * Cette fonction ne modifie pas l'entier actuel (this), ni celui spécifié en
     * paramètre. Un nouvel entier est retourné.
     *
     * @param m  valeur du modulo
     * @return  la valeur de `this` modulo `m`
     */
    public Entier moduloNaif(Entier m) {

        // Exercices 4.
        //
        // À compléter.
        //
        return null; // return bidon (pour que ça compile) À RETIRER !
    }

    /**
     * Retourne le modulo l'entier par l'entier spécifié.
     *
     * Pré-condition : l'entier spécifié (m) ne doit pas être 0.
     *
     * Cette fonction ne modifie pas l'entier actuel (this), ni celui spécifié en
     * paramètre. Un nouvel entier est retourné.
     *
     * @param m  valeur du modulo
     * @return  la valeur de `this` modulo `m`
     */
    public Entier moduloOpt(Entier m) {

        // Exercices 5.
        //
        // À compléter.
        //
        return null; // return bidon (pour que ça compile) À RETIRER !
    }

    /**
     * Calcule du modulo
     *
     * @param m  valeur du modulo
     * @return  la valeur de `this` modulo m
     */
    Entier modulo(Entier m) {
        return this.moduloNaif(m);
        //return this.moduloOpt(m);
    }


    /**
     * Retourne (this^p) mod m, soit la p-ième puissance de l'entier, modulo m.
     *
     * Cette fonction ne modifie pas l'entier actuel (this), ni ceux spécifiés
     * en paramètres. Un nouvel entier est retourné.
     *
     * @param p  valeur de l'exposant
     * @param m  valeur du modulo
     * @return  la valeur de `this` puissance `p` modulo `m`
     */
    public Entier puissanceModulaireNaif(Entier p, Entier m) {
        
        // Exercices 6.
        //
        // À réécrire
        //
        return (this.puissance(p)).modulo(m);
    }

    /**
     * Retourne (this^p) mod m, soit la p-ième puissance de l'entier, modulo m.
     *
     * Cette fonction ne modifie pas l'entier actuel (this), ni ceux spécifiés
     * en paramètres. Un nouvel entier est retourné.
     *
     * TODO Exercice 8 : expliquez le fait que cet fonction implémente
     *                   l'algorithme "puissance modulaire" vu en classe.
     *
     *
     * @param p  valeur de l'exposant
     * @param m  valeur du modulo
     * @return  la valeur de `this` puissance `p` modulo `m`
     */
    public Entier puissanceModulaireOpt(Entier p, Entier m) {
        
        // Exercices 7.
        //
        // À réécrire
        //
        return (this.puissance(p)).modulo(m);
    }


    /**
     * Calcul de la puissance modulaire
     *
     * @param p  valeur de l'exposant
     * @param m  valeur du modulo
     * @return  la valeur de `this` puissance `p` modulo `m`
     */
    public Entier puissanceModulaire(Entier p, Entier m) {
        return this.puissanceModulaireNaif(p, m);
        //return this.puissanceModulaireOpt(p, m);
    }


    ///////////////////////////    ///////////////////////////    ///////////////////////////
    ///////////////////////////    ///////////////////////////    ///////////////////////////
    //                       //    //                       //    //                       //
    //     Section BONUS     //    //     Section BONUS     //    //     Section BONUS     //
    //                       //    //                       //    //                       //
    ///////////////////////////    ///////////////////////////    ///////////////////////////
    ///////////////////////////    ///////////////////////////    ///////////////////////////


   /**
     * Retourne un entier aléatoire formée de `n` chiffres.
     *
     * @param n  le nombre de chiffre dans l'écriture en base 10 de l'entier
     * @return  un entier choisi pseudo-aléatoirement sur n chiffres.
     */
    public static Entier entierAleatoire(int n) {

        Random generateur = new Random(); // générateur pseudo-aléatoire

        // Pour des fins de débogage, il peut être utile d'utiliser toujours
        // les mêmes nombres. Dans ce cas, utilisez la ligne suivante afin pour
        // initialiser le générateur pseudo-aléatoire.
        //Random generateur = new Random(0);


        Entier nbAleat = new Entier();
        for (int i = 0; i < n-1; i++) {
           nbAleat.decimales.add(generateur.nextInt(10));
        }
        nbAleat.decimales.add(1 + generateur.nextInt(9)); // La dernière décimale ne doit pas être zéro
        return nbAleat;
    }

    /**
     * Exercice B1 : le test de Fermat !
     *
     * Cette fonction se base sur le petit théorème de Fermat (Théorème 2.11 à
     * la page 98 des notes de cours) afin de déterminer si l'entier `p` est
     * peut-être premier. Complétez la documentation qui suit.
     *
     *
     * Si la fonction retourne `true`, cela signifie que 
     *
     *   TODO identifiez la bonne réponse en supprimant les autres
     *    - il est certain que p est premier
     *    - il est certain que p n'est pas premier
     *    - p est peut-être premier, peut-être pas.
     *
     *   TODO Justifiez votre réponse en vous basant sur le petit théorème de Fermat
     *
     *
     *
     * Si la fonction retourne `false`, cela signifie que 
     *
     *   TODO identifiez la bonne réponse en supprimant les autres
     *    - il est certain que p est premier
     *    - il est certain que p n'est pas premier
     *    - p est peut-être premier, peut-être pas.
     *
     *   TODO Justifiez votre réponse en vous basant sur le petit théorème de Fermat
     *
     * @param p  nombre à tester
     * @return true si ???, false sinon
     */
    public static boolean testDeFermat(Entier p) {
        Entier a = entierAleatoire(p.longueur() - 1); // a est plus petit que p
        Entier x = a.puissanceModulaire(p.soustraire(new Entier(1)), p);
        return x.estUn();
    }


    /**
     * Utilise le test de Fermat afin de déterminer si l'entier `p` est
     * peut-être premier avec un niveau de certitude donné par `niveauDeCertitude`.
     *
     * Le niveau de certitude est le nombre de fois que l'entier a passé 
     * le test de Fermat avec succès.
     *
     * Cette fonction effectue le test de Fermat sur l'entier `p` et retourne
     * `true` si tous les tests sont positifs. La valeur `false` est retournée
     * dès qu'un test est a échoué.
     *
     * @param  p  l'entier à tester
     * @param  niveauDeCertitude le nombre de fois que le test de Fermat est utilisé
     * @return  true si l'entier p a réussi tous les tests, false sinon.
     */
    public static boolean estPeutEtrePremier(Entier p, int niveauDeCertitude) {
        //
        // Exercice B2
        //
        // À compléter
        //
        return false; // return bidon à retirer
    }



    /**
     * Retourne un nombre à `n` chiffres qu'on pense être un nombre premier.
     *
     * Cette fonction choisit un nombre impair aléatoirement et utilise le test
     * de Fermat afin de déterminer si le nombre est possiblement premier ou
     * non.
     *
     * Le paramètre `niveauDeCertitude` indique le nombre de fois qu'un nombre
     * doit réussir le test de Fermat pour être accepté comme nombre premier.
     *
     * Si le nombre échoue le test de Fermat, alors on lui additionne 2 et on
     * recommence avec ce nouvel entier.
     *
     * Si le nombre passe avec succès le nombre requis de tests de Fermat,
     * alors on considère qu'il est premier, même si nous ne pouvons pas en
     * être absolument certain.
     *
     * @param  n le nombre chiffre dans l'écriture en base 10 de l'entier.
     * @param  niveauDeCertitude  le nombre de fois que le nombre a passé avec succès le test de Fermat.
     * @return  un nombre premier (probabiliste) à n chiffres.
     */
    public static Entier nbPremierAleatoireProbabiliste(int n, int niveauDeCertitude) {
        Entier p = entierAleatoire(n);
        if (p.estPair()) {
            p = p.somme(new Entier(1));
        }
        //System.out.println("p est " + p);
        while (!estPeutEtrePremier(p, niveauDeCertitude)) {
            p = p.somme(new Entier(2));
            //System.out.println("p n'était pas premier, nouveau p " + p);
        }
        return p;
    }


    /**
     * Représente le résultat d'une division entière, c'est à dire un quotient
     * et un reste.
     *
     * Par exemple, pour la division entière de 14 par 3 
     * 14 = 4*3 + 2
     *
     * Le quotient est 4 et le reste est 2. L'objet ResultatDivisionEntiere
     * correspondant serait donc construit de la manière suivante :
     *
     * ResultatDivisionEntiere resultat = new ResultatDivisionEntiere(new Entier(4), new Entier(2));
     */
    public static class ResultatDivisionEntiere {
        public Entier quotient;
        public Entier reste;
        public ResultatDivisionEntiere(Entier quotient, Entier reste) {
            this.quotient = quotient;
            this.reste = reste;
        }
    }


    /**
     * Division entière
     *
     * Retourne le résultat de la division entière de `this` par `m`.
     *
     * Précondition : m n'est pas 0.
     *
     * @param m  le diviseur
     * @return  le quotient et le reste
     */
    public ResultatDivisionEntiere divisionEntiere(Entier m) {
        //
        // Exercice B3
        //
        // À compléter
        //
        return null; // return bidon (pour que ça compile) À RETIRER !
    }

    /**
     * Représente de résultat de l'algorithme de Bézout.
     *
     * Le thérorème de Bézout spécifie que pour toute paire d'entiers `a` et
     * `b`, il existe deux entier `u` et `v` tels que 
     *
     *   a*u + b*v = d = pgcd(a,b)
     *
     * L'objet ResultatBezout est formé des nombres u, v et d.
     *
     * Étant donné que `a` et `b` sont tous les deux positifs, les nombres u et
     * v doivent être de signes opposés. Cependant, la classe Entier ne
     * représente que des entiers positifs. Pour contrer ce problème, le
     * booléen `uPositif` indique si l'entier `u` doit être considérer comme un
     * nombre positif ou non.
     *
     * Concrètement : 
     *
     *    Si `uPositif` est `true`  alors `u` est positif et `v` est négatif.
     *        Autrement dit, a*u + b*(-v) = d = pgcd(a,b)
     *
     *    Si `uPositif` est `false` alors `u` est négatif et `v` est positif.
     *        Autrement dit, a*(-u) + b*v = d = pgcd(a,b)
     */
    public static class ResultatBezout {
        public Entier u; // multiplicateurs de u
        public Entier v; // multiplicateurs de v
        public Entier d; // pgcd(a,b)
        public boolean uPositif;

        ResultatBezout(Entier u, Entier v, Entier d, boolean uPositif) {
            this.u = u;
            this.v = v;
            this.d = d;
            this.uPositif = uPositif;
        }
    }

    public static class EtapeEuclide {
        public Entier a;
        public Entier b;
        public Entier quotient;
        public Entier reste;
        public EtapeEuclide(Entier a, Entier b, Entier quotient, Entier reste) {
            this.a = a;
            this.b = b;
            this.quotient = quotient;
            this.reste = reste;
        }
    }

    /**
     * Utilise l'algorithme d'Euclide afin de calculer le pgcd de `a` et `b`.
     *
     * Les coefficients de Bézout sont calculés au fur et à mesure de
     * l'exécution de l'algorithme d'Euclide.
     *
     * @param a le premier Entier
     * @param b le deuxième Entier
     * @return  le résultat du Bézout de `a` et `b`
     */
    public static ResultatBezout bezout(Entier a, Entier b) {
        //
        // Exercice B4
        //
        // À compléter
      
        //  Aide pour le débogage: 
        //
        //if (uPositif) {
        //    System.out.println("" + u + "*" + a + " - " + v + "*" + b + " = " + d);
        //} else {
        //    System.out.println("-" + u + "*" + a + " + " + v + "*" + b + " = " + d);
        //}
        
        return null; // return bidon (pour que ça compile) À RETIRER !

    }

    /**
     * Plus grand commun diviseur via l'algorithme d'Euclide.
     *
     * Utilise la fonction `bezout` afin d'obtenir le pgcd des deux entiers.
     *
     * @param a  premier entier
     * @param b  deuxième entier
     * @return   le ggcd(a, b)
     */
    public static Entier pgcd(Entier a, Entier b) {
        ResultatBezout r = bezout(a, b);
        return r.d;
    }

    




////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
//// À partir d'ici, tout ce qui suit n'est pas pertinant dans le cadre ////
//// du cours MAT210.                                                   ////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////



    //
    //AUTRES SERVICES
    //
    
    /**
     * Retourne ``vrai`` si l'entier est 0, ``faux`` sinon.
     */
    public boolean estZero() {
        return longueur() == 1 && this.decimales.get(0) == 0;
    }

    /**
     * Retourne ``vrai`` si l'entier est 1, ``faux`` sinon.
     */
    public boolean estUn() {
        return longueur() == 1 && this.decimales.get(0) == 1;
    }


    /**
     * Retourne ``vrai`` si l'entier est pair, ``faux`` sinon.
     */
    public boolean estPair() {
        return this.getDecimale(0) % 2 == 0;
    }

    /**
     * Retourne ``vrai`` si l'entier est impair, ``faux`` sinon.
     */
    public boolean estImpair() {
        return this.getDecimale(0) % 2 == 1;
    }

    
    /**
     * Retourne la moitié arrondie à l'entier inférieur.
     */
    public Entier divParDeux() {
        // On initialise un entier même lng avec des 0 partout.
        Entier m = new Entier();
        int n = this.longueur();
        for (int i=0; i<n; ++i) {
            m.decimales.add(0);
        }
        // Division par 2 comme on ferait à la main
        int reste = 0;
        for (int i=n-1; i>=0; --i) {
            int d = this.getDecimale(i);
            m.decimales.set(i, (reste*10+d)/2);
            reste = d%2;
        }
        // Il est possible que le nombre commence par un 0, dans ce cas, on le retire.
        m.retireZerosEnTete();
        return m;
    }


    /**
     * Retire les zéros à gauche.
     *
     * Si l'écriture en base 10 du nombre commence par des zéros, ceux-ci sont retirés.
     * Ex : 00843 --> 843
     * Exception : le nombre 0 est représenté par 0.
     */
    protected void retireZerosEnTete() {
        int n = this.longueur();
        while (n>1 && this.decimales.get(n-1) == 0) {
            this.decimales.remove(n-1);
            --n;
        }
    }



    /**
     * Retourne le nombre de décimales de l'entier. Il s'agit du nombre de
     * chiffres nécessaire pour l'écrire en base 10.
     */
    public int longueur() {
        return this.decimales.size();
    }


    /**
     * Retourne la décimale à l'index spécifié. L'index 0 étant le chiffre le
     * moins significatif de l'entier. Si l'index demandé est supérieur au
     * nombre de décimales, retourne 0.
     */
    public int getDecimale(int k) {
        if (k < this.longueur()) {
            return this.decimales.get(k);
        } else {
            return 0;
        }
    }

    /**
     * Retourne la décimale la plus significative.
     */
    public int decimaleDeGauche() {
        return this.getDecimale(this.longueur()-1);
    }


    /**
     * Compare deux Entier.
     * 
     * Retourne :
     *  -1 si ``this`` < y
     *   0 si ``this`` == y
     *   1 si ``this`` > y
     */
    protected int compareTo(Entier y) {
        // On utilise le fait que la fonction ``decimale(i)`` retourne 0
        // lorsque ``i>=this.longueur()``
        int n = Math.max(this.longueur(), y.longueur());
        for (int i=n-1; i>=0; --i) {
            int a = this.getDecimale(i);
            int b = y.getDecimale(i);
            if (a<b) {
                return -1;
            } else if (b<a) {
                return 1;
            }
        }
        // Si on est rendu ici, c'est que les deux sont égaux.
        return 0;
    }


    /**
     * Retourne ``vrai`` si et seulement si les deux entiers sont égaux.
     */
    public boolean egal(Entier y) {
        return this.compareTo(y) == 0;
    }


    /**
     * Retourne ``vrai`` si et seulement si l'entier est plus petit ou égal à
     * celui spécifié.
     */
    public boolean plusPetitOuEgal(Entier y) {
        return compareTo(y) <= 0;
    }


    /**
     * Retourne ``vrai`` si et seulement si l'entier est strictement plus petit
     * que celui spécifié.
     */
    public boolean plusPetit(Entier y) {
        return compareTo(y) < 0;
    }


    /**
     * Retourne ``vrai`` si et seulement si l'entier est strictement plus grand
     * que celui spécifié.
     */
    public boolean plusGrand(Entier y) {
        return compareTo(y) > 0;
    }


    /**
     * Retourne ``vrai`` si et seulement si l'entier est plus grand ou égal à
     * celui spécifié.
     */
    public boolean plusGrandOuEgal(Entier y) {
        return compareTo(y) >= 0;
    }


    /**
     * Retourne un nouvel entier égal au complément à dix de l'entier.
     */
    protected Entier complementADix(int taille) {
        Entier c = new Entier();
        int retenue = 1;
        for (int i=0; i<taille; ++i) {
            int decimale = 9-this.getDecimale(i)+retenue;
            c.decimales.add(decimale%10);
            retenue = decimale/10;
        }
        if (retenue>0) {
            c.decimales.add(retenue);
        }
        return c;
    }


    /**
     * Retire la décimale la plus à gauche de l'écriture en base 10 de
     * l'entier.
     * (sert uniquement lors de la soustraction via le complément à 10)
     */
    protected void retireDecimaleDeGauche() {
        this.decimales.remove(this.longueur()-1);
    }


    /**
     * Soustrait l'entier spécifié à l'entier.
     *
     * Pré-condition : l'entier actuel (this) doit absolument être plus grand
     * que celui spécifié (y). (rappel : la classe Entier ne représente pas les
     * négatifs)
     *
     * Cette fonction ne modifie pas l'entier actuel (this), ni celui spécifié en
     * paramètre. Un nouvel entier est retourné.
     *
     */
    public Entier soustraire(Entier y) {
        if (this.plusPetit(y)) {
            return null;
        }
        // On additionne le complément à 10
        Entier diff = y.complementADix(this.longueur()).somme(this);
        // On retire la retenue
        diff.retireDecimaleDeGauche();
        // On retire les zéro inutiles en début 
        while (diff.decimaleDeGauche() == 0 && !diff.estZero()) {
            diff.retireDecimaleDeGauche();
        }
        return diff;
    }


    /**
     * Retourne une chaîne de caractère représentant la valeur de l'entier en
     * base 10. 
     *
     * Dans le cas où l'écriture de l'entier fait plus de 80 chiffres, seuls
     * les premières et les dernières décimales sont affichées. Utilisez la
     * fonction ``str`` pour obtenir toutes les décimales de l'entier.
     */

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int n = this.longueur();
        if (n > 80) {
            for (int i=0; i<30; ++i ) {
                sb.append(getDecimale(n-i-1));
            }
            sb.append("...(" + n + " décimales)...");
            for (int i=29; i>=0; --i ) {
                sb.append(getDecimale(i));
            }
            return sb.toString();
        } else {
            return str();
        }
    }


    /**
     * Retourne une chaîne de caractère représentant la valeur de l'entier en
     * base 10. 
     */
    public String str() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<longueur(); ++i) {
            sb.append(getDecimale(i));
        }
        return sb.reverse().toString();
    }


}
