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


import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Exception;
import java.io.FileWriter;
import java.io.IOException;


public class RSA {


    public static String chiffre(Cle cleRSA, String messageOriginal) {
        // On récupère les infos de la clé, soit les entiers n et e.
        Entier n = cleRSA.getModulo();
        Entier e = cleRSA.getExposant();

        // Conversion du message en Entier
        Entier M = Convertisseur.texteVersEntier(messageOriginal);

        // Un peu d'affichage...
        System.out.println("# Message original       : " + messageOriginal);
        System.out.println("# Converti en entier (M) : " + M.str());
        System.out.println("# Clé, exposant (e)      : " + e.str());
        System.out.println("# Clé, modulo (n)        : " + n.str());
        System.out.println("# Chiffrement du Message, calcul de M^e mod n");

        if (M.plusGrandOuEgal(n)) {
            System.out.println("ERREUR. Taille de la clé trop courte pour ce message.");
            System.out.println("Recommencez avec un message plus court ou une clé plus grande.");
            System.exit(1);
        }

        long avant = System.nanoTime();
        ////////////////////////////////////////////
        // Début de l'exercice 7a
        //
        // Le chiffrement RSA demande de calculer ``M^e mod n``
        //
     
        Entier C = (M.puissance(e)).modulo(n);
        
        //
        // Fin de l'exercice 7a
        ////////////////////////////////////////////
        
        long apres = System.nanoTime();
        long duree = (apres-avant)/1000000;
        System.out.println("# (calculé en " + duree + " ms)");

        return C.str();
    }

    public static String dechiffre(Cle cleRSA, String messageChiffre) {
        // On récupère les infos de la clé
        Entier n = cleRSA.getModulo();
        Entier d = cleRSA.getExposant();

        // Le message chiffré n'est rien d'autre que l'écriture en base 10 d'un entier.
        Entier C = new Entier(messageChiffre);

        // Un peu d'affichage...
        System.out.println("# Entier chiffré (C) : " + C.str());
        System.out.println("# Clé, exposant (d)  : " + d.str());
        System.out.println("# Clé, modulo (n)    : " + n.str());
        System.out.println("# Déchiffrement du Message, calcul de C^d mod n");

        long avant = System.nanoTime();
        ////////////////////////////////////////////
        // Début de l'exercice 7b
        // 
        // Le déchiffrement RSA demande de calculer ``C^d mod n``
        //
        
        Entier M = (C.puissance(d)).modulo(n);
        
        //
        // Fin de l'exercice 7b
        ////////////////////////////////////////////
        long apres = System.nanoTime();
        long duree = (apres-avant)/1000000;
        System.out.println("# (calculé en " + duree + " ms)");

        // Conversion de l'entier en texte.
        return Convertisseur.entierVersTexte(M);
    }


    /**
     * Génère une paire de clé publique/privee pour le RSA.
     *
     * La clé est générée à partir de nombre premier probabilistes.
     *
     * @param fichierClePublique  nom du fichier dans le quel est inscrit la clé publique
     * @param fichierClePrivee  nom du fichier dans le quel est inscrit la clé privee
     * @param tailleCle  nombre de chiffre dans l'écriture en base 10 des nombres de la clé
     * @param niveauDeCertitude  nombre de fois qu'un nombre doit réussir le test de Fermat pour être considéré premier
     */
    public static void genererCleAleatoires(String fichierClePublique, String fichierClePrivee, int tailleCle, int niveauDeCertitude) {

        System.out.println("# Génération de deux nombres premiers probabilistes...");

        Entier p = Entier.nbPremierAleatoireProbabiliste(tailleCle/2, niveauDeCertitude);
        System.out.println("#   p=" + p);

        Entier q = Entier.nbPremierAleatoireProbabiliste(tailleCle - tailleCle/2, niveauDeCertitude);
        System.out.println("#   q=" + q);
        System.out.println("#");

        Entier n = p.produit(q);
        System.out.println("# Le produit des deux nombres premiers est n=" + n);
        System.out.println("#");

        System.out.println("# Recherche d'un entier `d` inversible modulo (p-1)*(q-1)...");
        Entier s = p.soustraire(new Entier(1)).produit(q.soustraire(new Entier (1)));
        Entier d = Entier.entierAleatoire(s.longueur()-1);
        System.out.print("#   d=" + d);  
        Entier.ResultatBezout rb = Entier.bezout(d, s);
        while (!rb.d.estUn()) {
            System.out.println(" --> Rejeté,  pgcd(d, (p-1)*(q-1)) != 1");
            d = Entier.entierAleatoire(s.longueur()-1);
            rb = Entier.bezout(d, s);
            System.out.print("#   d=" + d);  
        }
        System.out.println(" --> Accepté, pgcd(d, (p-1)*(q-1)) == 1");
        System.out.println("#");

        Entier e = rb.u;
        // Si rb.uPositif est `false`, c'est que le coefficient `u` représente
        // un nombre négatif. Il faut le ramener du côté positif en effectuant
        // le calcul s-u.
        if (!rb.uPositif) {
            e = s.soustraire(rb.u);
        }
        System.out.println("# L'inverse de d modulo (p-1)*(q-1) est e=" + e);
        System.out.println("#");
        System.out.println("# Écriture des clés dans les fichiers...");
        System.out.println("#");
        System.out.println("#    Clé publique (fichier `" + fichierClePublique + "`)");
        System.out.println("#      n=" + n.str());
        System.out.println("#      d=" + d.str());
        System.out.println("#");
        System.out.println("#    Clé privée (fichier `" + fichierClePrivee + "`)");
        System.out.println("#      n=" + n.str());
        System.out.println("#      e=" + e.str());
        System.out.println("#");
        try {
            FileWriter fw;
            fw = new FileWriter(fichierClePublique);
            fw.write("n " + n.str() + "\n");
            fw.write("d " + d.str() + "\n");
            fw.close();
            fw = new FileWriter(fichierClePrivee);
            fw.write("n " + n.str() + "\n");
            fw.write("e " + e.str() + "\n");
            fw.close();
        } catch (IOException exception) {
            System.out.println("Erreur lors de l'écriture dans les fichiers.");
            exception.printStackTrace();
        }
        System.out.println("# Écriture des fichier complétée avec succès.");

    }




    public static void main(String[] args) {

        // Si vous ne souhaitez pas utiliser les arguments de la ligne de
        // commande, vous pouvez les simuler en réaffectant ``args`` sur un
        // nouveau tableau de String.
        // ATTENTION : pensez à commenter ces lignes AVANT de rendre votre travail !
        //args = new String[] {"--chiffre", "--cle", "cle", "Boum"};
        //args = new String[] {"--chiffre", "--cle", "cle", "Bonjour toto"};


////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
//// À partir d'ici, tout ce qui suit n'est pas pertinant dans le cadre ////
//// du cours MAT210.                                                   ////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////

        String operation = null;;
        String fichierCle = null;;
        String messageOriginal = null;;
        String fichierClePublique = null;
        String fichierClePrivee = null;
        int tailleCle = -1;
        int niveauCertitude = -1;

        // Lecture des arguments de la ligne de commande
        for (int i=0; i<args.length; ++i) {
            if (args[i].equals("--chiffre")) {
                operation = "chiffre";
            } else if (args[i].equals("--dechiffre")) {
                operation = "dechiffre";
            } else if (args[i].equals("--genCle")) {
                operation = "genCle";
                fichierClePublique = args[i+1];
                fichierClePrivee = args[i+2];
                tailleCle = Integer.parseInt(args[i+3]);
                niveauCertitude = Integer.parseInt(args[i+4]);
                i += 4;
            } else if (args[i].equals("--cle")) {
                fichierCle = args[i+1];
                i += 1;
            } else if (args[i].equals("--demoConversion")) {
                operation = "demo";
            } else if (args[i].equals("--entierVersTexte")) {
                operation = "entierVersTexte";
            } else if (args[i].equals("--texteVersEntier")) {
                operation = "texteVersEntier";
            } else if (args[i].equals("--help")) {
                usage();
                System.exit(0);
            } else {
                messageOriginal = args[i];
            }
        }

        if ((operation == null) 
                || (operation.equals("--chiffre") && (messageOriginal == null))
                || (operation.equals("--dechiffre") && (messageOriginal == null))
                || (operation.equals("--genCle") && ((fichierClePublique == null) || (fichierClePrivee == null) || (tailleCle == -1) || (niveauCertitude == -1)))
           ) {
            usage();
            System.exit(1);
        }

        if (operation.equals("chiffre")) {
            Cle cleRSA = new Cle(fichierCle);
            //Entier x = chiffre(cleRSA, messageOriginal);
            String c = chiffre(cleRSA, messageOriginal);
            System.out.println(c);
        } else if (operation.equals("dechiffre")) {
            Cle cleRSA = new Cle(fichierCle);
            String texte = dechiffre(cleRSA, messageOriginal);
            System.out.println(texte);
        } else if (operation.equals("genCle")) {
            genererCleAleatoires(fichierClePublique, fichierClePrivee, tailleCle, niveauCertitude);
        } else if (operation.equals("demo")) {
            demoConversion();
        } else if (operation.equals("entierVersTexte")) {
            System.out.println(Convertisseur.entierVersTexte(new Entier(messageOriginal)));
        } else if (operation.equals("texteVersEntier")) {
            System.out.println("" + Convertisseur.texteVersEntier(messageOriginal));
        } else {
            System.out.println("Option : " + operation + ", inconnue");
            usage();
            System.exit(1);
        }

    }

    /**
     * Affiche l'aide pour utiliser ce programme
     */
    private static void usage() {
        System.out.println("\nUsages :\n\n" 
                + "java mat210.RSA --chiffre --cle <fichierCle> \"message\"\n"
                + "    Le paramètre \"message\" est d'abord converti en entier puis chiffré.\n"
                + "    Le nombre résultant du chiffrement est affiché sur la sortie standard.\n"
                + "\n"
                + "java mat210.RSA --dechiffre --cle <fichierCle> N\n"
                + "    Le paramètre N est un nombre, ce nombre est déchiffré puis converti en\n"
                + "    chaîne de caractères. Cette chaîne est affichée sur la sortie standard.\n"
                + "\n"
                + "java mat210.RSA --genCle <fichierClePublique> <fichierClePrivee> <taille> <certitude>\n"
                + "    Génère une paire de clés RSA, les clés sont inscrites dans les fichiers\n"
                + "    dont les noms sont spécifiéez en argument. Le troisième argument <taille>\n"
                + "    est le nombre de chiffres de l'écriture en base 10 des nombres qui forment\n"
                + "    la clé et le quatrième argument <certitude> est un entier désignant le\n"
                + "    niveau de certitude lors du test de primalité.\n"
                + "\n"
                + "java mat210.RSA --texteVersEntier \"texte\"\n"
                + "    Converti la chaîne de caractère \"texte\" en une entier.\n"
                + "    Aucune cryptographie n'est impliquée dans cette conversion.\n"
                + "\n"
                + "java mat210.RSA --entierVersTexte N\n"
                + "    Converti l'entier N en une chaîne de caractères.\n"
                + "    Aucune cryptographie n'est impliquée dans cette conversion.\n"
                + "\n"
                + "java mat210.RSA --demoConversion\n"
                + "    Affiche quelques exemples de conversion String <--> Entier.\n"
                + "\n"
                + "java mat210.RSA --help\n"
                + "    Affiche cet aide.\n"
                );
    }

    /**
     * Affiche des exemples de conversion du texte en entier et vice-versa.
     */
    private static void demoConversion() {
        String s;
        Entier e;

        s = "Bonjour";
        e = Convertisseur.texteVersEntier(s);
        System.out.println("\"" + s + "\" --> " + e);
        s = Convertisseur.entierVersTexte(e);
        System.out.println(e + " --> \'" + s + "\'\n");

        s = "Message secret";
        e = Convertisseur.texteVersEntier(s);
        System.out.println("\"" + s + "\" --> " + e);
        s = Convertisseur.entierVersTexte(e);
        System.out.println(e + " --> \'" + s + "\'\n");

        s = "Ceci est une texte pas mal long, vraiment long!";
        e = Convertisseur.texteVersEntier(s);
        System.out.println("\"" + s + "\" --> " + e);
        s = Convertisseur.entierVersTexte(e);
        System.out.println(e + " --> \'" + s + "\'\n");

        s = "Support Unicode via UTF-8 :😀 😁 😅 😇 ";
        e = Convertisseur.texteVersEntier(s);
        System.out.println("\"" + s + "\" --> " + e);
        s = Convertisseur.entierVersTexte(e);
        System.out.println(e + " --> \'" + s + "\'\n");

    }


    /**
     * Une clé RSA est formée de deux entiers. Il peut s'agit autant d'une clé
     * de chiffrement que de déchiffrement.
     *
     * Le calcul du chiffrement/déchiffrement RSA est : 
     *
     * M^e mod n
     *
     * où M est le message
     *    e est l'exposant
     *    n est le modulo
     *
     * La paire (n, e) forme la clé RSA.
     */
    private static class Cle {

        /**
         * Constructeur par défaut.
         *
         * Ne fait rien.
         */
        public Cle() {
            this.modulo = null;
            this.exposant = null;
        }

        /**
         * Une clé est construite à partir d'un fichier où les trois nombres n, d et e sont lus.
         *
         * @param fichier  fichier texte contenant la clé RSA
         */
        public Cle(String fichier) {
            FileReader fr = null;
            BufferedReader br = null;
            try {
                fr = new FileReader(fichier);
                br = new BufferedReader(fr);
                String s = br.readLine();
                while (s != null) {
                    if (!(s.isEmpty() || s.startsWith("#"))) {
                        s = s.toLowerCase().trim().replaceAll("\\s+"," ");
                        String[] t = s.split(" ");
                        if (t[0].equals("n")) {
                            modulo = new Entier(t[1]);
                        } else if (t[0].equals("d")) {
                            exposant = new Entier(t[1]);
                        } else if (t[0].equals("e")) {
                            exposant = new Entier(t[1]);
                        } else {
                            throw new Exception();
                        }
                    }
                    s = br.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERREUR à la lecture de la clé dans le fichier ``" + fichier + "``");
                System.exit(1);
            } finally {
                try {
                    br.close();
                    fr.close();
                } catch (Exception e) {}
            }
        }

        /**
         * Retourne le modulo.
         */
        public Entier getModulo() {
            return modulo;
        }

        /**
         * Retourne l'exposant.
         */
        public Entier getExposant() {
            return exposant;
        }

        private Entier modulo;
        private Entier exposant;
    }



}
