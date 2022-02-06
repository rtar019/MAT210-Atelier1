package mat210;

/**
 * Fichier distribué dans le cadre du cours MAT210, session automne 2021, à l'ÉTS.
 *
 * Par Xavier Provençal.
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Cette classe implémente un parseur de tests.
 *
 * Un fichier de tests est lu, chaque test est exécuté et le résultat est
 * vérifiée.
 */

///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
// Dans le cadre du cours MAT210, le contenu de ce fichier est sans intérêt. //
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

public class TestParser {

    /**
     * Permet de lire un fichier de texte, une ligne à la fois.
     *
     * Chaque appel à la fonction `ligneSuivante` retourne une des lignes du
     * fichier. Les lignes commençant par '#' sont ignorées. 
     *
     * Lorsque la totalité du fichier a été lue, la fonction `ligneSuivante`
     * retourne `null`.
     *
     */
    private static class LecteurLigneParLigne {

        private static class LigneNumerotee {
            public LigneNumerotee(String texte, int n) {
                this.texte = texte;
                this.n = n;
            }
            public int getNum() {
                return n;
            }
            public String getTexte() {
                return texte;
            }
            public int n;        // numéro de la ligne
            public String texte; // contenu de la ligne
        }

        ArrayList<LigneNumerotee> lignes;
        int index; // index de la prochaine a être retournée

        public LecteurLigneParLigne(String fichier) {
            FileReader fr = null;
            BufferedReader br = null;

            lignes = new ArrayList<LigneNumerotee>();
            int n = 0;
            try {
                fr = new FileReader(fichier);
                br = new BufferedReader(fr);

                String s = br.readLine();
                ++n;
                while (s != null) {
                    s = s.toLowerCase().trim().replaceAll("\\s+"," ");
                    if (!s.startsWith("#") && !s.isEmpty()) {
                        lignes.add(new LigneNumerotee(s, n));
                    }
                    s = br.readLine();
                    ++n;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERREUR à la lecture du fichier ``" + fichier + "``");
                System.exit(1);
            } finally {
                try {
                    br.close();
                    fr.close();
                } catch (Exception e) {} 
            }
            index = 0;
        }

        // retourne le numéro de la dernière ligne produite
        public int getNumLigne() {
            // on soustrait 1 car index est incrément immédiatement après 
            // avoir produit une ligne.
            return lignes.get(index-1).getNum(); 
        }

        private String ligneSuivante() {
            if (index < lignes.size()) {
                return lignes.get(index++).getTexte();
            } else {
                return null;
            }
        }
    }

    public static void executeFichierDeTests(String fichier) {
        TestParser tp = new TestParser(fichier);
        tp.run();
    }

    private LecteurLigneParLigne lll;
    private int nbCalculs;
    private int nbTests;
    private int nbTestsOK;
    Map<String, Entier> variables;

    private TestParser(String fichier) {
        lll = new LecteurLigneParLigne(fichier);
        nbCalculs = 0;
        nbTests = 0;
        nbTestsOK = 0;
        variables = new HashMap<String, Entier>();
    }


    private Entier lireVariable(String s) {
        Entier x = variables.get(s);
        if (x == null) {
            System.out.println("ERREUR : ligne " + lll.getNumLigne() + ", variable inconnue ``" + s + "``");
            System.exit(1);
        }
        return x;
    }

    private void runDef(String s) {
        String[] t = s.split(" ");
        String varName = t[1];
        String value = t[2];
        Entier x = new Entier(value);
        variables.put(varName, x);

        // Affichage
        System.out.println("--------------------------------------------------------");
        System.out.println(varName + " := " + x);
    }

    private void runCalcul(String s) {
        nbCalculs += 1;
        CalculParseur c = new CalculParseur(s, lll.getNumLigne());
        System.out.println("--------------------------------------------------------");
        System.out.println("Calcul " + nbCalculs + " (ligne " + lll.getNumLigne() + ")");;
        System.out.println("  Operation : " + c);
        for (String var : c.operandes) {
            Entier x = lireVariable(var);
            System.out.println("  " + var + " = " + x);
            c.setValeur(var, x);
        }
        long avant = System.nanoTime();
        Entier result = c.execute();
        long apres = System.nanoTime();
        // Affichage du résultat
        long duree = (apres-avant)/1000000;
        System.out.println("Résultat = " + result + " (calculé en "+duree+" ms)");
    }

    private boolean commenceParUnZero(Entier e) {
        return e.decimaleDeGauche() == 0 && !e.estZero();
    }

    private void runTest(String s) {
        nbTests += 1;
        CalculParseur c = new CalculParseur(s, lll.getNumLigne());
        System.out.println("--------------------------------------------------------");
        System.out.println("Test " + nbTests + " (ligne " + lll.getNumLigne() + ")");
        System.out.println("  Operation : " + c);
        for (String var : c.operandes) {
            Entier x = lireVariable(var);
            System.out.println("  " + var + " = " + x);
            c.setValeur(var, x);
        }
        Entier resultatAttendu = lireVariable(c.varResultatAttendu);
        System.out.println("  " + c.varResultatAttendu + " = " + resultatAttendu);
        long avant = System.nanoTime();
        Entier result = c.execute();
        long apres = System.nanoTime();
        // Affichage du résultat
        if (commenceParUnZero(result)) {
            System.out.println("---> Echec. Entier non valide car la décimale la plus à gauche est un zéro : " + result);
        } else {
            if (result.egal(resultatAttendu)) {
                System.out.print("---> OK");
                nbTestsOK += 1;
            } else {
                System.out.print("---> Echec (résultat obtenu : " + result + ")");
            }
        }
        long duree = (apres-avant)/1000000;
        System.out.println(" (calculé en "+duree+" ms)");
    }


    public void run() {
        String s = lll.ligneSuivante();
        while (s != null) {
            if (s.startsWith("def")) {
                runDef(s);
            } else if (s.startsWith("calcul")) {
                runCalcul(s);
            } else if (s.startsWith("test")) {
                runTest(s);
            } else {
                System.out.println("ERREUR, opération inconnu à la ligne " + lll.getNumLigne());
                System.exit(1);
            }
            s = lll.ligneSuivante();
        }
        if (nbTests > 0) {
            System.out.println("--------------------------------------------------------");
            System.out.println("" + nbTestsOK + " tests reussis sur " + nbTests);
            System.out.println("--------------------------------------------------------");
        }
    }


    private class CalculParseur {
        public CalculParseur (String s, int numeroDeLigne) {
            this.operandes = new ArrayList<String>();
            this.valeurs = new HashMap<String, Entier>();
            String[] t = s.split(" ");
            if (t[1].equals("pwrmod")) {
                if (t.length != 5 && t.length != 7) {
                    this.erreurSyntaxe(s, numeroDeLigne);
                }
                this.operation = Operation.PuissanceModulaire;
                this.operandes.add(t[2]);
                this.operandes.add(t[3]);
                this.operandes.add(t[4]);
                if (t.length == 5) {
                    varResultatAttendu = null;
                } else {
                    if (!t[5].equals("=")) {
                        this.erreurSyntaxe(s, numeroDeLigne);
                    }
                    varResultatAttendu = t[6];
                }
            } else {
                // Operation +, *, ^ ou %
                if (t.length != 4 && t.length != 6) {
                    this.erreurSyntaxe(s, numeroDeLigne);
                }
                if (t[2].equals("+")) {
                    this.operation = Operation.Somme;
                } else if (t[2].equals("*")) {
                    this.operation = Operation.Produit;
                } else if (t[2].equals("^")) {
                    this.operation = Operation.Puissance;
                } else if (t[2].equals("%")) {
                    this.operation = Operation.Modulo;
                } else {
                    System.out.println("Ici");
                    this.erreurSyntaxe(s, numeroDeLigne);
                }
                this.operandes.add(t[1]);
                this.operandes.add(t[3]);
                if (t.length == 4) {
                    this.varResultatAttendu = null;
                } else {
                    if (!t[4].equals("=")) {
                        this.erreurSyntaxe(s, numeroDeLigne);
                    }
                    this.varResultatAttendu = t[5];
                }
            }
        }

        private void erreurSyntaxe(String s, int numeroDeLigne) {
            System.out.println("ERREUR de syntaxe à la ligne " + numeroDeLigne);
            System.exit(1);
        }

        public void setValeur(String variable, Entier valeur) {
            this.valeurs.put(variable, valeur);
        }

        public Entier execute() {
            Entier resultat = null;
            Entier x = this.valeurs.get(this.operandes.get(0));
            Entier y = this.valeurs.get(this.operandes.get(1));
            if (this.operation == Operation.Somme) {
                resultat = x.somme(y);
            } else if (this.operation == Operation.Produit) {
                resultat = x.produit(y);
            } else if (this.operation == Operation.Puissance) {
                resultat = x.puissance(y);
            } else if (this.operation == Operation.Modulo) {
                resultat = x.modulo(y);
            } else if (this.operation == Operation.PuissanceModulaire) {
                Entier z = this.valeurs.get(this.operandes.get(2));
                resultat = x.puissanceModulaire(y, z);
            }
            return resultat;
        }

        public String toString() {
            String s = null;
            if (this.operation == Operation.Somme) {
                s = this.operandes.get(0) + " + " + this.operandes.get(1);
            } else if (this.operation == Operation.Produit) {
                s = this.operandes.get(0) + " * " + this.operandes.get(1);
            } else if (this.operation == Operation.Puissance) {
                s = this.operandes.get(0) + "^" + this.operandes.get(1);
            } else if (this.operation == Operation.Modulo) {
                s = this.operandes.get(0) + " % " + this.operandes.get(1);
            }
            if (this.operation == Operation.PuissanceModulaire) {
                s = "(" + this.operandes.get(0) + "^" + this.operandes.get(1) 
                    + ") % " + this.operandes.get(2);
            }
            if (varResultatAttendu != null) {
                s += " = " + varResultatAttendu;
            }
            return s;
        }

        Operation operation;
        String varResultatAttendu;
        ArrayList<String> operandes;
        Map<String, Entier> valeurs;
    }

    private enum Operation {
        Somme("Somme"), Produit("Produit"), Puissance("Puissance"), Modulo("Modulo"), PuissanceModulaire("Puissance modulaire");
        Operation(String s) {}
    }


}
