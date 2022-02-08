package mat210;

/**
 * Fichier distribué dans le cadre du cours MAT210, session automne 2021, à l'ÉTS.
 *
 * Par Xavier Provençal.
 *
 */


/**
 * Exécute les tests contenus dans le fichier passé en argument.
 */
public class Test {

    public static void main(String[] args) {

        // Les lignes suivantes forment une petite démonstration de la classe
        // Entier. Vous pouvez les commenter après les avoir exécutées une
        // fois.
    	Entier x = new Entier("2108");
    	System.out.println("L'entier x est " + x);
        System.out.println("L'entier x est écrit sur sur " + x.longueur() + " chiffres.");
    	System.out.println("La décimale en position 0 est : " + x.getDecimale(0) + " (décimale la plus à droite)");
    	System.out.println("La décimale en position 1 est : " + x.getDecimale(1));
    	System.out.println("La décimale en position 2 est : " + x.getDecimale(2));
    	System.out.println("La décimale en position 3 est : " + x.getDecimale(3) + " (décimale la plus à gauche)");
    	System.out.println("");
        System.out.print("On peut utiliser une boucle pour parcourir toutes les décimales de x :");
        for (int i=0; i<x.longueur(); i++) {
            System.out.print(" " + x.getDecimale(i));
        }
        System.out.println("\n");
    	
    	Entier y = new Entier("34908230498320943209483209483204800927509247509824905709579284328809350138509832459040984569865842790289870398309432049849032240328490832049823094329048093284903848294089408920384902384234940824908329408");
    	System.out.println("Par défaut, l'affichage d'un grand entier est tronqué, par exemple :");
    	System.out.println("  y = " + y);
    	System.out.println("On peut forcer l'affichage de toutes ces décimales avec la fonction .str()");
    	System.out.println("  y = " + y.str());
    	System.out.println("");

        // Si vous ne souhaitez pas utiliser les arguments de la ligne de
        // commande, vous pouvez les simuler en réaffectant ``args`` sur un
        // nouveau tableau de String.
        //

        // ATTENTION : pensez à commenter ces lignes AVANT de rendre votre travail !
        //args = new String[] {"chemin complet vers le fichier de tests à exécuter"};
    	//args = new String[] {"C:\\Users\\rayan\\MAT210\\MAT210-MP1-RSA\\tests\\additions1"};
        //args = new String[] {"C:\\Users\\rayan\\MAT210\\MAT210-MP1-RSA\\tests\\additions2"};
    	args = new String[] {"C:\\Users\\david\\OneDrive\\Bureau\\ETS\\MAT210\\repoLab1\\MAT210-Atelier1\\MAT210-MP1-RSA\\tests\\RSA"};
    	//args = new String[] {"C:\\Users\\rayan\\MAT210\\MAT210-MP1-RSA\\tests\\multiplications2"};
    	//args = new String[] {"C:\\Users\\rayan\\MAT210\\MAT210-MP1-RSA\\tests\\puissances"};
        //
        TestParser.executeFichierDeTests(args[0]);
    }
}
