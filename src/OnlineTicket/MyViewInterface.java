package OnlineTicket;

public interface MyViewInterface {
    boolean isReady();
    int nextView();
    boolean isAboutBeingNew();
    boolean saveIfNew();
}
