package TwitterData;

import Algo.CommunityDetection;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class RetreivingData {


    private static Twitter twitter;
    private LinkedList<String> wordsLookingFor;


    private CommunityDetection leung;

    private int CommunityType = 1;

    public RetreivingData(Authenticating account) {
        if (twitter == null) {
            RetreivingData.twitter = authToTwitter(account);
        }
        //Prfeparing the list oif words we ae looking for
        this.wordsLookingFor = new LinkedList<>();
        // this.linLogLayout = new LinLogLayout("Twitter Social Analysis") ;
        // this.linLogLayout.autoCreate(true);
        this.leung = new CommunityDetection("Twitter Social Analysis", "weight");
        this.leung.autoCreate(true);
    }

    // getting The Instance Of Leung Algorithm
    public CommunityDetection getLeung() {
        return leung;
    }

    public void setTwitter(Authenticating account) {
        RetreivingData.twitter = authToTwitter(account);
    }

    //Authentication to twitter user Application api
    public Twitter authToTwitter(Authenticating account) {
        System.out.println("##########TwitterData.Authenticating##########");
        ConfigurationBuilder conf_Build = new ConfigurationBuilder();
        conf_Build.setOAuthConsumerKey(account.getCONSUMERKEY())
                .setOAuthConsumerSecret(account.getCONSUMERSECRET())
                .setOAuthAccessToken(account.getACCESSTOKEN())
                .setOAuthAccessTokenSecret(account.getACCESSTOKENSECRET());


        return new TwitterFactory(conf_Build.build()).getInstance();
    }


    private List<Status> searchTweets(String tweet) throws TwitterException {
        System.out.println("##########Searching Tweets##########");
        Query query = new Query(tweet);
        query.setCount(SwissArmyKnife.MAX_RESULT_RETURNED);
        QueryResult result = RetreivingData.twitter.search(query);

        return result.getTweets().stream()
                .collect(Collectors.toList());
    }

    //Here we get The User Intrest
    public void getInterest(List<Status> statusList) {
        System.out.println("##########Getting The Tweeters##########");

    }


    public void showKeyWords() {
        System.out.println("Here Are The Words That You Looked For ::");
        for (String words : wordsLookingFor) {
            System.out.println(words);
        }
    }


    //reading User Input
    public void readingWords() {
        //Initializing the List of possible words and an argument to get the strs
        String input = "";
        Scanner scan = new Scanner(System.in);
        wordsLookingFor.clear();
        System.out.println("##########Reading The Inputs##########");
        //Untile The User Type EOF keep getting the value
        while (!input.toUpperCase().equals("EOF")) {
            if (!input.equals(""))
                wordsLookingFor.add(input);

            System.out.print("Word =>\t");
            input = scan.next();
        }
        if (wordsLookingFor.size() > 0) {
            System.out.print("##########Based On What You Wanna Detect Communities ???########## \n1--Subscriptions\n2--Likes\n3--Hashtags\n4--Comments\n5--Geolocation\nNumber=>\t ");
            CommunityType = Integer.valueOf(scan.next());

            this.leung.findCommunities();

        }

    }

    //getting users from tweets
    public LinkedList<User> getUsers() throws TwitterException {
        System.out.println("##########Getting The Tweets##########");
        LinkedList<User> users = new LinkedList<>();
        for (String keyword : this.wordsLookingFor) {
            //Search for tweets
            for (Status A : this.searchTweets(keyword)) {
                //System.out.println(A.getText());
                getUserFromStatus(users, A);
            }
        }

        System.out.println("Users Number is ".concat(Integer.toString(users.size())));
        return users;
    }

    private void getUserFromStatus(LinkedList<User> users, Status A) {


        //Make Sure That The User Doesn't already exist
        boolean does_Exist = false;
        //getting the user of the status
        User user = A.getUser();
        //if a User Has More The 200 Interest we are better of
        if (isUserSatisfyParameters(user)) {
            //Search if The User pre exist
            for (User B : users) {
                if (B.getScreenName().equals(user.getScreenName())) {
                    does_Exist = true;
                    break;
                }
            }

            if (!does_Exist)
                users.add(user);
        }
    }

    //get users I
    public void getUsersSubscriptions(String user) throws TwitterException {
        //Starting The Getting Of The Interest
        System.out.println("##########Getting ".concat(user).concat(" Subscriptions##########"));

        PagableResponseList<User> users;
        long cursor = -1;
        //Getting Thre Data
        while (cursor != 0) {
            users = twitter.getFriendsList(user, cursor, SwissArmyKnife.MAX_SUBSCRIPTIONS_RETURNED_EACH_TIME);
            cursor = users.getNextCursor();
            addEdgesToGraph(user, users);
        }
    }

    private void getUsersFavorites(String user) throws TwitterException {

        //Starting The Getting Of The Interest
        System.out.println("##########Getting ".concat(user).concat(" Likes##########"));
        List<Status> tweets = twitter.getFavorites(user);
        List<User> topics = new LinkedList<>();
        for (Status status : tweets) {

            User userLike = status.getUser();
            if (!user.equals(userLike.getScreenName()))
                topics.add(status.getUser());

        }
        addEdgesToGraph(user, topics);
    }

    private void getUsersGeolocalisation(User user) {
        //Starting The Getting Of The Interest
        System.out.println("##########Getting ".concat(user.getScreenName()).concat(" Location ##########"));
        String[] place = user.getLocation().split(",");
        if (place != null && place.length != 0) {

            addEdgesToGraph(user.getScreenName(), place);
        }


    }

    public void addEdgesToGraph(String user, List<User> topics) {

        for (User tmp : topics) {
            //getting the old interest if existed
            SwissArmyKnife.Interest existing_Interest = SwissArmyKnife.Interest.isInterestIn(tmp.getScreenName());
            if (existing_Interest == null) {
                //Creation new Interest
                existing_Interest = new SwissArmyKnife.Interest(tmp.getScreenName());
                //Adding The User To The list
                SwissArmyKnife.Interest.addInterestToList(existing_Interest);
            }
            if (existing_Interest.startOfTheUsersList != null && !SwissArmyKnife.alreadyLikedOtherPostByTheSameChannel(user, existing_Interest)) {
                existing_Interest.startOfTheUsersList.addUserToList(new SwissArmyKnife.Users(user));
                leung.addNewLinkBetweenEdges(existing_Interest.getInterestString(), user, existing_Interest.startOfTheUsersList);
            } else {
                existing_Interest.startOfTheUsersList = new SwissArmyKnife.Users(user);
            }
        }
    }

    public void addEdgesToGraph(String user, String[] places) {
        for (String place : places) {
            if (!place.isEmpty()) {
                SwissArmyKnife.Interest existing_Interest = SwissArmyKnife.Interest.isInterestIn(place);
                if (existing_Interest == null) {
                    //Creation new Interest
                    existing_Interest = new SwissArmyKnife.Interest(place);
                    //Adding The User To The list
                    SwissArmyKnife.Interest.addInterestToList(existing_Interest);
                }
                if (existing_Interest.startOfTheUsersList != null && !SwissArmyKnife.alreadyLikedOtherPostByTheSameChannel(user, existing_Interest)) {
                    existing_Interest.startOfTheUsersList.addUserToList(new SwissArmyKnife.Users(user));
                    leung.addNewLinkBetweenEdges(existing_Interest.getInterestString(), user, existing_Interest.startOfTheUsersList);
                } else {
                    existing_Interest.startOfTheUsersList = new SwissArmyKnife.Users(user);
                }
            }
        }
    }

    public void getUsersSpecificInterest(User user) throws TwitterException {
        switch (this.CommunityType) {
            case SwissArmyKnife.SUBSCRIPTIONS:
                getUsersSubscriptions(user.getScreenName());
                break;
            case SwissArmyKnife.LIKES:
                getUsersFavorites(user.getScreenName());
                break;
            case SwissArmyKnife.HASHTAGS:
                break;
            case SwissArmyKnife.COMMENTS:
                break;
            case SwissArmyKnife.Geolocalisation:
                getUsersGeolocalisation(user);
                break;
        }
    }

    private boolean isUserSatisfyParameters(User user) {
        //Starting The Getting Of The Interest
        //System.out.println("##########Seeing If User ".concat(user.getScreenName()).concat(" Satisfy Conditions##########"));
        switch (this.CommunityType) {
            case SwissArmyKnife.SUBSCRIPTIONS:
                return SwissArmyKnife.MAX_SUBSCRIPTIONS_PER_USER > user.getFriendsCount();

            case SwissArmyKnife.LIKES:
                return SwissArmyKnife.MAX_LIKES_PER_USER > user.getFavouritesCount();

            case SwissArmyKnife.HASHTAGS:
                return false;

            case SwissArmyKnife.COMMENTS:
                return false;

            case SwissArmyKnife.Geolocalisation:
                return user.isGeoEnabled();
        }

        return false;
    }


}
