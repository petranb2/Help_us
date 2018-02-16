package app.petkoul.help_us;

/**
 * Created by kalogeros on 2/16/2018.
 */

public class Invitation  {

    User user;
    String time_stamp;

    public Invitation(){
    }

    public  Invitation(User user , String time_stamp){
        this.user = user;
        this.time_stamp = time_stamp;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }
}
