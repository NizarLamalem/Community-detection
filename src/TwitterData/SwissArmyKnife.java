package TwitterData;

import java.math.BigInteger;
import java.util.LinkedList;

public class SwissArmyKnife {

    //The Types Of Communities
    public static final int SUBSCRIPTIONS = 1;
    public static final int LIKES = 2;
    public static final int HASHTAGS = 3;
    public static final int COMMENTS = 4;
    public static final int Geolocalisation = 5;
    /*Application parameters*/
    public static int MAX_RESULT_RETURNED = 100;
    public static int MAX_SUBSCRIPTIONS_RETURNED_EACH_TIME = 200;
    public static int MAX_SUBSCRIPTIONS_PER_USER = 500;
    public static int MAX_LIKES_PER_USER = 10000;
    public static int MAX_Users = 3200;
    //The TwitterData.Authenticating Accounts
    public static LinkedList<Authenticating> All_Authenticating_Accounts;

    /*Setting Up The Accounts*/
    public static void startAccounts() {

        All_Authenticating_Accounts = new LinkedList<>();
        //Account 1
        All_Authenticating_Accounts.add(new Authenticating("3AgNnLYh6kmNYt7QUAmoC4SK6", "lGEe19YE2ND5rWaZOSoKG68FSme5jlMZTHKUGFG4cYPGkduZUz",
                "4025396657-oRPuJ6XCkfFRwNlUi3vAJYU6f5tyk0LUxBDTvSU", "22sai4zyRRekVoOtmZkqx1sOk4bSpbt7DHO2W3FopcUix", "ACCOUNT 1"));
        //Account 2
        All_Authenticating_Accounts.add(new Authenticating("HvmpVjdQLDyMLMdY1ffzF5Q16", "6YEBcRb8trBiXYdHJM9Qp02fXFOQ5a0RgQdSbn4aDtaZ2EeeGc",
                "4025396657-urkrXwGDBQNb1rQnflGLuehnxJhH9rXxUvxA2Pw", "psXPYFVHML2MoAta9Yism6t3IA0nc7AhEKbNfISyD4mo6", "ACCOUNT 2"));
        //Account 3
        All_Authenticating_Accounts.add(new Authenticating("V3o4GkXiNsCKMuQBzC5XkAslG", "Xtkl4GFYTJ6zSpNIAlRqzh9d3gfVcggvDAvOEBKVI5eoqqfsF8",
                "4025396657-DpGo8NzCaBmtoUykZYfsvrFwEeAUwStFD4dDCpr", "zslEk3sIuUeflLUXmo4bbPaOqReJcCgkmrNdOru55ydgO", "ACCOUNT 3"));
        //Account 4
        All_Authenticating_Accounts.add(new Authenticating("24DksxHeoEVjmoi4M654iBa3A", "w2xAC7IIpoY6vO0FkE5AVCai08vr0HKuypWJAbgw8CvRSt67l3",
                "4025396657-FrwgPG8yC2WhwLQWY0DwpYk9ObuWdydXsDFqs6w", "LFPZMbSRUzLs2YrAuNG5kaXXJll3XN1gLHnMNG825exUi", "ACCOUNT 4"));
        //Account 5
        All_Authenticating_Accounts.add(new Authenticating("GNbGbKqeJuDs0LxFPQwTKUJNm", "etM581WldPpuxWsPOvm9OhsEPG1LBDvIjV7QbOD52bCy5n8wDY",
                "4025396657-khZU3VfQAjAdClWCnWpw96rtZcD9PEtYmmbPWqr", "aX3EvntTJscfmSj22TKvpn4QNqqa6aLqcxjCH4YxFDxOT", "ACCOUNT 5"));
        All_Authenticating_Accounts.add(new Authenticating("qRdYW9p7NXqRYLX5GMJlxiHRT", "PBCPyqQZYejHdMK3ZEd2sYVwpbuSZlaxXZBSB4KTMaW4GE0NAV",
                "985804949845151744-2E7mvoToWKnn82NvMPeaMGuPhN1AQcF", "2zaXfr6asZGcg1j82XT0D3q5Pxm3zADqcaicw96198yJm", "ACCOUNT 6"));
        All_Authenticating_Accounts.add(new Authenticating("edcVu4N7d0HhIHxZfAk8WIleg", "CyJksb24xJ87w1n7Rhq8SN5EwnNd3LRpkNWUo5rijIdJH6r027",
                "985804949845151744-ZVOkrVewwvK1GLzLV5jIaKcQ1aCc5rj", "sOVjNrjxHvT8MPuvhC9vDWAV6HRzDWYXq3UKXTqDzpRJa", "ACCOUNT 7"));
        All_Authenticating_Accounts.add(new Authenticating("vIB9YsgJl10GFJJugvSOsFbGM", "utXsIjXDS36AJA3x3Bx6oRtvPytHZLTOjAOWtSlzePsiNMj19T",
                "985804949845151744-bha2YUKo7f4q8hyU1DR13JfUF79uVXh", "UqjFflLzMf6tRX0jaI6DgEomGapBFK4btFN0KxFnx3B33", "ACCOUNT 8"));
        All_Authenticating_Accounts.add(new Authenticating("lnmTCafc0oNBlxvBlZKNKjiwr", "HrhZI6met3RNHIC1jaOSF1cbOgk1tyR2iSBj7TbLit9KsDJxWg",
                "4025396657-fCmMTddpCoGLmDMwz127HWDOsfNdujf2kO6PzhF", "QitCAad0DGP7isBVIGGftY6ZkaXr5i6NSTvtMU7cBkfcT", "ACCOUNT 9"));

    }

    public static BigInteger stringToBigInteger(String token) {
// convert to big integer
        BigInteger bigInt = new BigInteger(token.getBytes());

        return bigInt;

    }

    public static String bigIntegerToString(BigInteger bigInt) {
        // convert back
        String token = new String(bigInt.toByteArray());

        return token;
    }


    public static float normalise(float inValue, float min, float max) {
        return (inValue - min) / (max - min);
    }

    public static boolean alreadyLikedOtherPostByTheSameChannel(String user, Interest interest) {
        Users cursor = interest.startOfTheUsersList;
        while (cursor != null) {
            if (user.equals(cursor.getUserString())) {
                return true;
            }
            cursor = cursor.nextUser;
        }
        return false;
    }

    //The Users List
    public static class Users {


        public static long count = 0;


        //The Name Of The Channel
        public BigInteger user;
        //The Next Element
        public Users nextUser;

        // new Channel
        public Users(String user) {
            this.user = stringToBigInteger(user);

        }

        public static long getcount() {
            return count;
        }

        //get the Created Text
        public String getUserString() {
            return bigIntegerToString(this.user);
        }

        public void addUserToList(SwissArmyKnife.Users user) {
            if (this.nextUser != null) {
                user.nextUser = this.nextUser;
                this.nextUser = user;

            } else {
                this.nextUser = user;

            }
            count++;

        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println(getUserString().concat(" is Getting Destroyed"));

        }

        @Override
        public String toString() {
            return getUserString();
        }

        public Users isUsersIn(String name) {
            Users cursor = this;
            BigInteger bigInteger = stringToBigInteger(name);
            while (cursor != null && cursor.nextUser != null) {
                if (cursor.user.equals(bigInteger)) {
                    return cursor;
                }
                cursor = cursor.nextUser;
            }
            return null;
        }

        //Show All The Element Of The List
        public void showListOfUsers() {
            Users cursor = this;
            while (cursor.nextUser != null) {
                System.out.println(cursor);
                cursor = cursor.nextUser;
            }

        }
    }

    //The Subscription List
    public static class Interest {


        public static long count = 0;
        //The Head Of The List
        public static Interest startOfTheList = null;


        //The Name Of The Channel
        public BigInteger interest;
        //The Next Element
        public Interest nextInterest;

        //The Users List
        public Users startOfTheUsersList;

        // new Channel
        public Interest(String interest) {
            this.interest = stringToBigInteger(interest);

        }

        public static void addInterestToList(Interest interest) {
            System.out.println("Add Subscription To List ".concat(interest.toString()));
            if (startOfTheList != null) {
                interest.nextInterest = startOfTheList.nextInterest;
                startOfTheList.nextInterest = interest;

            } else {
                startOfTheList = interest;
            }
            count++;

        }

        public static Interest isInterestIn(String name) {
            Interest cursor = startOfTheList;
            BigInteger bigInteger = stringToBigInteger(name);
            while (cursor != null && cursor.nextInterest != null) {
                if (cursor.interest.equals(bigInteger)) {
                    return cursor;
                }
                cursor = cursor.nextInterest;
            }
            return null;
        }

        //Show All The Element Of The List
        public static void showListOfSubscription() {
            Interest cursor = startOfTheList;
            while (cursor != null && cursor.nextInterest != null) {
                System.out.println(cursor.toString().concat("   :::"));
                cursor.showOfInterestperUser();
                cursor = cursor.nextInterest;
            }

        }

        public static long getcount() {
            return count;
        }

        //get the Created Text
        public String getInterestString() {
            return bigIntegerToString(this.interest);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println(getInterestString().concat(" is Getting Destroyed"));
        }

        @Override
        public String toString() {
            return getInterestString();
        }

        public void showOfInterestperUser() {
            Users cursor = startOfTheUsersList;
            while (cursor != null) {
                System.out.println("\t\t".concat(cursor.toString()));
                cursor = cursor.nextUser;
            }


        }


    }

}
