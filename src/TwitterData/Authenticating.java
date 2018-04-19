package TwitterData;

import java.util.Timer;
import java.util.TimerTask;

public class Authenticating {

    private final String ACCOUNT_NAME;
    //The Credentials
    private String CONSUMERKEY;
    private String CONSUMERSECRET;
    private String ACCESSTOKEN;
    private String ACCESSTOKENSECRET;
    //The Rate Limite
    private boolean rateLimite;


    //set the rate limite secondes
    private long systemTime = -1;

    private long waitSeconds;

    //Is This Account Being Used
    private boolean beingUsed;
    //The Timer
    private Timer timer;

    public Authenticating(String CONSUMERKEY, String CONSUMERSECRET, String ACCESSTOKEN, String ACCESSTOKENSECRET, String name) {
        this.CONSUMERKEY = CONSUMERKEY;
        this.CONSUMERSECRET = CONSUMERSECRET;
        this.ACCESSTOKEN = ACCESSTOKEN;
        this.ACCESSTOKENSECRET = ACCESSTOKENSECRET;
        this.ACCOUNT_NAME = name;
        this.rateLimite = false;
        this.beingUsed = false;
        this.timer = new Timer();
        this.waitSeconds = -1;
    }

    public static Authenticating getBiengUsed() {
        for (Authenticating beinused : SwissArmyKnife.All_Authenticating_Accounts) {
            if (beinused.isBeingUsed()) {
                return beinused;
            }
        }
        //Nothing Being Used
        return null;
    }

    private static Authenticating getNoRateLimitAccount() {
        for (Authenticating noRateLimit : SwissArmyKnife.All_Authenticating_Accounts) {
            if (!noRateLimit.isRateLimite()) {
                return noRateLimit;
            }
        }
        //All have Acceded Their Rate Limte
        return null;
    }

    //Get the Least User That we will not hav to wait a lot for him to finish
    private static Authenticating getTheLeastRateSeconds() {
        // 2 variable min
        long minWaitSeconds = Long.MAX_VALUE;
        long minSystemSeconds = Long.MAX_VALUE;
        Authenticating theLeastRateSeconds = null;
        Authenticating theLeastSystemSeconds = null;

        for (Authenticating tmp : SwissArmyKnife.All_Authenticating_Accounts) {
            //if all the variable are initialise and it less then the recorded min its the new min
            if (tmp.getWaitSeconds() >= 0 && tmp.getWaitSeconds() < minWaitSeconds) {
                theLeastRateSeconds = tmp;
                minWaitSeconds = tmp.getWaitSeconds();
            }
            if (theLeastRateSeconds == null && tmp.getSystemTime() < minSystemSeconds) {
                theLeastSystemSeconds = tmp;
                minSystemSeconds = tmp.getSystemTime();
            }
        }

        return theLeastRateSeconds != null ? theLeastRateSeconds : theLeastSystemSeconds;
    }

    public static Authenticating bestAccountToUse() {
        Authenticating bestAccount = getNoRateLimitAccount();
        //return the account with no rate limit
        if (bestAccount != null) {
            System.err.println("No Rate Limitation account is ".concat(bestAccount.toString()));
            bestAccount.setBeingUsed(true);
            return bestAccount;
        }

        //Return The Account With Least Rate Limit
        bestAccount = getTheLeastRateSeconds();
        System.err.println("The Least Being Used is ".concat(bestAccount.toString()));
        bestAccount.setBeingUsed(true);
        return bestAccount;
    }

    public long getWaitSeconds() {
        return waitSeconds;
    }

    public String getCONSUMERKEY() {
        return CONSUMERKEY;
    }

    public String getCONSUMERSECRET() {
        return CONSUMERSECRET;
    }

    public String getACCESSTOKEN() {
        return ACCESSTOKEN;
    }

    public String getACCESSTOKENSECRET() {
        return ACCESSTOKENSECRET;
    }

    public boolean isRateLimite() {
        return rateLimite;
    }

    public void setRateLimite(boolean rateLimite) {
        this.rateLimite = rateLimite;
    }

    public boolean isBeingUsed() {
        return beingUsed;
    }

    public void setBeingUsed(boolean beingUsed) {
        this.beingUsed = beingUsed;
    }

    @Override
    public String toString() {
        return ACCOUNT_NAME;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public void setWaitSeconds(long systemTime, int secondsUntilReset) {
        System.out.println(ACCOUNT_NAME.concat(" will Continue After ".concat(Integer.toString(secondsUntilReset / 60))));
        this.systemTime = systemTime;
        this.waitSeconds = secondsUntilReset;
        //Schedue when time arrive min Done Make is reusable again
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(ACCOUNT_NAME.concat(" can request again"));
                rateLimite = false;
                Authenticating.this.systemTime = -1;
                Authenticating.this.waitSeconds = -1;
            }
        }, secondsUntilReset * 1000);

    }

    public void rateLimiteReached(long timeToRetry, int secondsUntilReset) {
        this.setRateLimite(true);
        this.setWaitSeconds(timeToRetry, secondsUntilReset);
        this.setBeingUsed(false);
    }

}
