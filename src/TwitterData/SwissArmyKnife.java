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
        All_Authenticating_Accounts.add(new Authenticating("aze", "aze",
                "aze-aze", "aze", "ACCOUNT 1"));
        //Account 2
        All_Authenticating_Accounts.add(new Authenticating("aze", "aze",
                "aze-aze", "aze", "ACCOUNT 2"));
        //Account 3
        All_Authenticating_Accounts.add(new Authenticating("aze", "aze",
                "aez-aze", "aez", "ACCOUNT 3"));
        //Account 4
        All_Authenticating_Accounts.add(new Authenticating("aez", "aze",
                "aez-aez", "aez", "ACCOUNT 4"));
        //Account 5
        All_Authenticating_Accounts.add(new Authenticating("ae", "ae",
                "aez-aze", "aez", "ACCOUNT 5"));
        All_Authenticating_Accounts.add(new Authenticating("aez", "aze",
                "aze-aze", "aze", "ACCOUNT 6"));
        All_Authenticating_Accounts.add(new Authenticating("aze", "zeaze",
                "aze-aez", "aze", "ACCOUNT 7"));
        All_Authenticating_Accounts.add(new Authenticating("aze", "aze",
                "ze-aze", "aze", "ACCOUNT 8"));
        All_Authenticating_Accounts.add(new Authenticating("aze", "aze",
                "aze-aze", "aze", "ACCOUNT 9"));

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
