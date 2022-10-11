package util;

/* 
 * TPCW_Util.java - Some random utility functions needed by the servlets.
 *
 ************************************************************************
 *
 * This is part of the the Java TPC-W distribution,
 * written by Harold Cain, Tim Heil, Milo Martin, Eric Weglarz, and Todd
 * Bezenek.  University of Wisconsin - Madison, Computer Sciences
 * Dept. and Dept. of Electrical and Computer Engineering, as a part of
 * Prof. Mikko Lipasti's Fall 1999 ECE 902 course.
 *
 * Copyright (C) 1999, 2000 by Harold Cain, Timothy Heil, Milo Martin, 
 *                             Eric Weglarz, Todd Bezenek.
 * Copyright © 2008 Gustavo Maciel Dias Vieira
 *
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 * Everyone is granted permission to copy, modify and redistribute
 * this code under the following conditions:
 *
 * This code is distributed for non-commercial use only.
 * Please contact the maintainer for restrictions applying to 
 * commercial use of these tools.
 *
 * Permission is granted to anyone to make or distribute copies
 * of this code, either as received or modified, in any
 * medium, provided that all copyright notices, permission and
 * nonwarranty notices are preserved, and that the distributor
 * grants the recipient permission for further redistribution as
 * permitted by this document.
 *
 * Permission is granted to distribute this code in compiled
 * or executable form under the same conditions that apply for
 * source code, provided that either:
 *
 * A. it is accompanied by the corresponding machine-readable
 *    source code,
 * B. it is accompanied by a written offer, with no time limit,
 *    to give anyone a machine-readable copy of the corresponding
 *    source code in return for reimbursement of the cost of
 *    distribution.  This written offer must permit verbatim
 *    duplication by anyone, or
 * C. it is distributed by someone who received only the
 *    executable form, and is accompanied by a copy of the
 *    written offer of source code that they received concurrently.
 *
 * In other words, you are welcome to use, share and improve this codes.
 * You are forbidden to forbid anyone else to use, share and improve what
 * you give them.
 *
 ************************************************************************/
import java.util.*;

/**
 * *<img src="./doc-files/TPCW_Util.png" alt="TPCW_Util">
 */
public class TPCW_Util {

    private static final String[] arrayFnames = new String[]{
        "Alice", "Miguel", "Sophia", "Arthur", "Helena", "Bernardo", "Valentina", "Heitor", "Laura", "Davi", "Isabella", "Lorenzo", "Manuela", "Théo", "Júlia", "Pedro", "Heloísa", "Gabriel", "Luiza", "Enzo", "Maria", "Luiza", "Matheus", "Lorena", "Lucas", "Lívia", "Benjamin", "Giovanna", "Nicolas", "Maria", "Eduarda", "Guilherme", "Beatriz", "Rafael", "Maria", "Clara", "Joaquim", "Cecília", "Samuel", "Eloá", "Enzo", "Gabriel", "Lara", "João", "Miguel", "Maria", "Júlia", "Henrique", "Isadora", "Gustavo", "Mariana", "Murilo", "Emanuelly", "Pedro", "Henrique", "Ana", "Júlia", "Pietro", "Ana", "Luiza", "Lucca", "Ana", "Clara", "Felipe", "Melissa", "João", "Pedro", "Yasmin", "Isaac", "Maria", "Alice", "Benício", "Isabelly", "Daniel", "Lavínia", "Anthony", "Esther", "Leonardo", "Sarah", "Davi", "Lucca", "Sandro", "Elisa", "Bryan", "Antonella", "Eduardo", "Rafaela", "João", "Lucas", "Maria", "Cecília", "Victor", "Liz", "João", "Marina", "Cauã", "Nicole", "Antônio", "Maitê", "Vicente", "Isis", "Caleb", "Alícia", "Gael", "Luna", "Bento", "Rebeca", "Caio", "Agatha", "Emanuel", "Letícia", "Vinícius", "Maria-", "João", "Guilherme", "Gabriela", "Davi", "Lucas", "Ana", "Laura", "Noah", "Catarina", "João", "Gabriel", "Clara", "João", "Victor", "Ana", "Beatriz", "Luiz", "Miguel", "Vitória", "Francisco", "Olívia", "Kaique", "Maria", "Fernanda", "Otávio", "Emilly", "Augusto", "Maria", "Valentina", "Levi", "Milena", "Yuri", "Maria", "Helena", "Enrico", "Bianca", "Thiago", "Larissa", "Ian", "Mirella", "Victor", "Hugo", "Maria", "Flor", "Thomas", "Allana", "Henry", "Ana", "Sophia", "Luiz", "Felipe", "Clarice", "Ryan", "Pietra", "Arthur", "Miguel", "Maria", "Vitória", "Davi", "Luiz", "Maya", "Nathan", "Laís", "Pedro", "Lucas", "Ayla", "Davi", "Miguel", "Ana", "Lívia", "Raul", "Eduarda", "Pedro", "Miguel", "Mariah", "Luiz", "Henrique", "Stella", "Luan", "Ana", "Erick", "Gabrielly", "Martin", "Sophie", "Bruno", "Carolina", "Rodrigo", "Maria", "Laura", "Luiz", "Gustavo", "Maria", "Heloísa", "Arthur", "Gabriel", "Maria", "Sophia", "Breno", "Fernanda", "Kauê", "Malu", "Enzo", "Miguel", "Analu", "Fernando", "Amanda", "Arthur", "Henrique", "Aurora", "Luiz", "Otávio", "Maria", "Isis", "Carlos", "Eduardo", "Louise", "Tomás", "Heloise", "Lucas", "Gabriel", "Ana", "Vitória", "André", "Ana", "Cecília", "José", "Ana", "Liz", "Yago", "Joana", "Danilo", "Luana", "Anthony", "Gabriel", "Antônia", "Ruan", "Isabel", "Miguel", "Henrique", "Bruna", "Oliver"
    };
    private static final String[] arrayLnames = new String[]{
        "Ferreira",
        "Braga",
        "da Silva",
        "Della Coletta",
        "Zampirolli",
        "Fernandes",
        "Alves",
        "Costalonga",
        "Botteon",
        "Caliman",
        "de Oliveira",
        "Zanette",
        "Salvador",
        "Silva",
        "Zandonadi",
        "Pesca",
        "Falqueto",
        "Tosi",
        "da Costa",
        "de Souza",
        "Gomes",
        "Calmon",
        "Pereira",
        "Sossai detto Pegorer",
        "de Almeida",
        "de Jesus",
        "Martins",
        "Balarini",
        "Rodrigues",
        "Gonçalves",
        "Pizzol",
        "Moreira",
        "Vieira",
        "Venturim",
        "Bazoni",
        "Filete",
        "Almeida",
        "Oliveira",
        "dos Santos",
        "Falchetto",
        "Barbosa",
        "Breda",
        "Scaramussa",
        "de Barros",
        "Marques"};

    /**
     *
     * @param rand
     * @return
     */
    public static String getRandomLname(Random rand) {
        final int index = getRandomInt(rand, 0, arrayLnames.length);
        return arrayLnames[0];
    }

    /**
     *
     * @param rand
     * @param min
     * @param max
     * @return
     */
    public static String getRandomString(Random rand, int min, int max) {
        StringBuilder newstring = new StringBuilder();
        final char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '@', '#',
            '$', '%', '^', '&', '*', '(', ')', '_', '-', '=', '+',
            '{', '}', '[', ']', '|', ':', ';', ',', '.', '?', '/',
            '~', ' '}; //79 characters
        int strlen = getRandomInt(rand, min, max);
        for (int i = 0; i < strlen; i++) {
            newstring.append(chars[rand.nextInt(chars.length)]);
        }
        return newstring.toString();
    }

    /**
     *
     * @param rand
     * @param lower
     * @param upper
     * @return
     */
    public static int getRandomInt(Random rand, int lower, int upper) {
        return rand.nextInt(upper - lower + 1) + lower;
    }

    /**
     *
     * @param rand
     * @param lower
     * @param upper
     * @return
     */
    public static long getRandomLong(Random rand, long lower, long upper) {
        return rand.nextLong() * (upper - lower + 1) + lower;
    }
    
    /**
     * 
     * @param rand
     * @param lower
     * @param upper
     * @return 
     */
    public static double getRandomDouble(Random rand, double lower, double upper) {
        return rand.nextDouble() * (upper - lower + 1) + lower;
    }

    /**
     *
     * @param rand
     * @return
     */
    public static Date getRandomBirthdate(Random rand) {
        return new GregorianCalendar(
                TPCW_Util.getRandomInt(rand, 1880, 2000),
                TPCW_Util.getRandomInt(rand, 0, 11),
                TPCW_Util.getRandomInt(rand, 1, 30)).getTime();
    }

    /**
     *
     * @param rand
     * @return
     */
    public static Date getRandomPublishdate(Random rand) {
        return new GregorianCalendar(
                TPCW_Util.getRandomInt(rand, 1930, 2000),
                TPCW_Util.getRandomInt(rand, 0, 11),
                TPCW_Util.getRandomInt(rand, 1, 30)).getTime();
    }

    /**
     *
     * @param d
     * @param n
     * @return
     */
    public static String DigSyl(int d, int n) {
        StringBuilder resultString = new StringBuilder();
        String digits = Integer.toString(d);

        if (n > digits.length()) {
            int padding = n - digits.length();
            for (int i = 0; i < padding; i++) {
                resultString = resultString.append("BA");
            }
        }

        for (int i = 0; i < digits.length(); i++) {
            switch (digits.charAt(i)) {
                case '0':
                    resultString = resultString.append("BA");
                    break;
                case '1':
                    resultString = resultString.append("OG");
                    break;
                case '2':
                    resultString = resultString.append("AL");
                    break;
                case '3':
                    resultString = resultString.append("RI");
                    break;
                case '4':
                    resultString = resultString.append("RE");
                    break;
                case '5':
                    resultString = resultString.append("SE");
                    break;
                case '6':
                    resultString = resultString.append("AT");
                    break;
                case '7':
                    resultString = resultString.append("UL");
                    break;
                case '8':
                    resultString = resultString.append("IN");
                    break;
                case '9':
                    resultString = resultString.append("NG");
                    break;
                default:
                    break;
            }
        }

        return resultString.toString();
    }

}
