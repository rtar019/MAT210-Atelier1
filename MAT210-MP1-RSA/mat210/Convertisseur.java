package mat210;

/**
 * Fichier distribué dans le cadre du cours MAT210, session automne 2021, à l'ÉTS.
 *
 * Par Xavier Provençal.
 */

import java.util.Arrays;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.BufferUnderflowException;
import java.io.ByteArrayOutputStream;


/**
 * Converti un message de l'une des formes suivantes vers n'importe quelle autre.
 *
 *  - chaîne de caractères (String)
 *  - tableau d'octets (byte[])
 *  - grand entier (Entier)
 *
 * Aucune forme de cryptographie n'est employée ici. Il s'agit d'une simple conversion.
 *
 *  Ces services de conversions sont disponible via les fonctions publiques : 
 *
 *   - texteVersEntier
 *   - entierVersTexte
 *
 *   - texteVersOctets
 *   - octetsVersTexte
 *
 *   - EntierVersOctets
 *   - octetsVersEntier
 *
 */
public class Convertisseur
{

    /**
     * Converti un tableau d'octet en un objet de la classe Entier.
     *
     * @param  octets le tableau d'octets
     * @return l'entier correspondant
     */
    public static Entier octetsVersEntier(byte[] octets) {
        ByteBuffer bf = ByteBuffer.wrap(octets);
        StringBuilder sb = new StringBuilder();
        sb.append("1");
        try {
            while (true) {
                String s = "" + getUnsignedInt(bf.getInt());
                //tous les nombres doivent être sur 10 chiffres
                while (s.length() < 10) {
                    s = "0"+s;
                }
                //System.out.println("s="+s);
                sb.append(s);
            }
        } catch (BufferUnderflowException e) {
            // on a fini de lire tous les octets.
        }
        return new Entier(sb.toString());
    }

    /**
     * Retourne la représentation d'une chaîne de caractère sous la forme d'un
     * tableau d'octets.
     *
     * @param  texte la chaîne de caractères
     * @return le tableau d'octets correspondant
     */
    public static byte[] texteVersOctets(String texte) {
        byte[] octets = texte.getBytes(StandardCharsets.UTF_8);
        // On traîte les octets par blocs de 4, ainsi si la taille du tableau
        // n'est pas un multiple de 4, on ajoute des octets à 0 pour compléter.
        if (octets.length % 4 != 0) {
            int nbCasesAAJouter = 4 - (octets.length % 4);
            octets = Arrays.copyOf(octets, octets.length + nbCasesAAJouter); // les nouvelles cases vallent 0.
        }
        return octets;
    }


    /**
     * Converti un entier obtenu à partir d'une chaîne de caractère en un
     * tableau d'octets représentant cette même chaîne de caractères 
     *
     * @param entier  l'entier à convertir
     * @return  la représentation en octets
     */
    public static byte[] entierVersOctets(Entier entier) {
        String s = entier.str();
        // on commence à i=1 car le premier chiffre est un '1' ajouté
        // artificiellement.
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        for (int i=1; i+10<=s.length(); i+=10) {
            long x = Long.parseLong(s.substring(i,i+10));
            byte b0 = (byte)((x & 0x00000000000000ffL) >>  0);
            byte b1 = (byte)((x & 0x000000000000ff00L) >>  8);
            byte b2 = (byte)((x & 0x0000000000ff0000L) >> 16);
            byte b3 = (byte)((x & 0x00000000ff000000L) >> 24);
            b.write(b3);
            b.write(b2);
            b.write(b1);
            b.write(b0);
        }
        return b.toByteArray();
    }


    /**
     * Converti une chaîne de caractère en un Entier.
     *
     * @param   texte la chaîne de caractères
     * @return  l'entier correspondant
     */
    public static Entier texteVersEntier(String texte) {
        return octetsVersEntier(texteVersOctets(texte));
    }

    /**
     * Converti un Entier en une chaîne de caractères.
     *
     * @param   entier l'entier
     * @return  la chaîne de caractères correspondante
     */
    public static String entierVersTexte(Entier entier) {
        return octetsVersTexte(entierVersOctets(entier));
    }

    /**
     * Converti un tableau d'octets en une chaine de caractères.
     *
     * Note : l'encodage utilisé est UTF-8.
     *
     * @param   octets le tableau d'octets
     * @return  la chaîne de caractères correspondante
     */
    public static String octetsVersTexte(byte[] octets) {
        return new String(octets);
    }
        



    /**
     * Converti un entier 32 bits signé en l'entier 64 bit positif correspondant.
     *
     * Lorsqu'un entier supérieur à 2^31 est représenté sur 32 bits, celui-ci
     * est considéré comme étant négatif. Cette fonction retourne la valeur de
     * l'entier positif dont le codage sur 32 bits commence par un 1.
     */
    private static long getUnsignedInt(int x) {
            return x & 0x00000000ffffffffL;
    }




}




