import TwitterData.Authenticating;
import TwitterData.RetreivingData;
import TwitterData.SwissArmyKnife;
import org.graphstream.stream.GraphParseException;
import twitter4j.TwitterException;
import twitter4j.User;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) throws IOException, GraphParseException, TwitterException, InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        System.setProperty("sun.java2d.opengl", "true");
        // set system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // LingLong Community Detection
        //Algo.LinLogLayout linLogLayout = new Algo.LinLogLayout() ;
        //linLogLayout.findCommunities("/root/IdeaProjects/Graphs/books.gml");

        //Modularity Community Detection
        //Algo.ModularityCommunity Modularity = new Algo.ModularityCommunity() ;
        //Modularity.findCommunities();


        //Setting Up The Accounts
        SwissArmyKnife.startAccounts();

        RetreivingData data = new RetreivingData(Authenticating.bestAccountToUse());

        //Reading The words to query for
        data.readingWords();

        //showing the Words written By User
        //data.showKeyWords();

        //getting The Usres That we found they had and opinion in the research
        LinkedList<User> users = data.getUsers();

        for (int i = 0; i < users.size(); i++) {
            //Getting The User Based On The Index
            User user = users.get(i);
            try {
                //Getting users Subscription
                data.getUsersSpecificInterest(user);

            } catch (TwitterException e) {
                i--;
                //The Guy Being Used
                Authenticating tmp = Authenticating.getBiengUsed();
                //Do The Confirmed Protocol in case of error
                tmp.rateLimiteReached(System.currentTimeMillis(), e.getRateLimitStatus() == null ? 900 : e.getRateLimitStatus().getSecondsUntilReset());
                System.err.println("##########".concat(tmp != null ? tmp.toString() : "Nothing Being Used").concat(" Achieved His Limit ##########"));


                tmp = Authenticating.bestAccountToUse();
                if (tmp == null) {
                    System.err.println("The Best Account To Use Is Null");
                }
                //Setting The New Account
                System.err.println("##########Setting The New Account ====>".concat(tmp.toString().concat("##########")));
                data.setTwitter(tmp);
                while (tmp.isRateLimite()) {
                    TimeUnit.SECONDS.sleep(30);
                }
                //;
            }
        }


        //Destroying The Users and The Data and The Interest AND ACCOUNT
        users = null;
        SwissArmyKnife.Interest.startOfTheList = null;
        SwissArmyKnife.All_Authenticating_Accounts = null;

        //Calling The Garbage Collector
        System.gc();
        //  data.getLinLogLayout().CommunityDetection();
        data.getLeung().communityDetection();
        ;


        //Getting Trough the List
        //TwitterData.SwissArmyKnife.Interest.showListOfSubscription();


    }
}