package com.example.denis.loginui;

public class CheckInput {

    public static final int DRAWABLE_LEFT = 0;
    public static final int DRAWABLE_TOP = 1;
    public static final int DRAWABLE_RIGHT = 2;
    public static final int DRAWABLE_BOTTOM = 3;

    public static final int PASSWORD_LENGTH = 8;

    //controllo se username è valido
    public static boolean is_Valid_Usrrname(String username) {

        if (username.length() == 0) return false;

        boolean notSpecial= true ;

        int i=0;

        while(notSpecial && i < username.length()) {

            char ch = username.charAt(i);

            if (is_Special(ch)) notSpecial=false;

            i++;
        }


        return (notSpecial);
    }


    //controllo se password è valida
    public static boolean is_Valid_Password(String password) {

        if (password.length() < PASSWORD_LENGTH) return false;

        int charCount = 0;
        int numCount = 0;

        int i=0;

        while ((charCount == 0 || numCount <2) && i < password.length()) {

            char ch = password.charAt(i);

            if (is_Numeric(ch)) numCount++;
            else if (is_Letter(ch)) charCount++;

            i++;

        }

        return (charCount >= 1 && numCount >= 2);
    }



    //controllo se è lettera in maiuscolo
    public static boolean is_Letter(char ch) {
        return (ch >= 'A' && ch <= 'Z');
    }

    //controllo se è un numero
    public static boolean is_Numeric(char ch) {

        return (ch >= '0' && ch <= '9');
    }

    //controllo se è un carattere speciale
    public static boolean is_Special(char ch){
        String aux = Character.toString(ch);
        return (aux.matches("[^A-Za-z0-9 ]"));
    }

    public static boolean is_Valid_ISBN(String isbn){

        if ( isbn == null )
        {
            return false;
        }

        //remove any hyphens
        isbn = isbn.replaceAll( "-", "" );

        //must be a 13 digit ISBN
        if ( isbn.length() == 13 )
        {
            try
            {
                int tot = 0;
                for ( int i = 0; i < 12; i++ )
                {
                    int digit = Integer.parseInt( isbn.substring( i, i + 1 ) );
                    tot += (i % 2 == 0) ? digit * 1 : digit * 3;
                }

                //checksum must be 0-9. If calculated as 10 then = 0
                int checksum = 10 - (tot % 10);
                if ( checksum == 10 )
                {
                    checksum = 0;
                }

                return checksum == Integer.parseInt( isbn.substring( 12 ) );
            }
            catch ( NumberFormatException nfe )
            {
                //to catch invalid ISBNs that have non-numeric characters in them
                return false;
            }
        }
        if ( isbn.length() == 10 ) {
            try {
                int tot = 0;
                for (int i = 0; i < 9; i++) {
                    int digit = Integer.parseInt(isbn.substring(i, i + 1));
                    tot += ((10 - i) * digit);
                }

                String checksum = Integer.toString((11 - (tot % 11)) % 11);
                if ("10".equals(checksum)) {
                    checksum = "X";
                }

                return checksum.equals(isbn.substring(9));
            } catch (NumberFormatException nfe) {
                //to catch invalid ISBNs that have non-numeric characters in them
                return false;
            }
        }
        return false;





    }

    //controllo se username è valido
    public static boolean is_Valid_Name(String x) {

        if (x.length() == 0) return false;

        boolean notSpecial= true ;
        boolean notNumber= true;

        int i=0;

        while(notSpecial && notNumber && i < x.length()) {

            char ch = x.charAt(i);

            if (is_Special(ch)) notSpecial=false;
            if(is_Numeric(ch)) notNumber=false;
            i++;
        }


        return (notSpecial && notNumber);
    }
}
