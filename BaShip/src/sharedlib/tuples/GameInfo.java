package sharedlib.tuples;

import java.util.Date;

public class GameInfo {

    // TODO: status? currrent move number?
    public final Long id;
    public final Long player1ID;
    public final Long player2ID;
    public final String player1Username;
    public final String player2Username;
    public final Date startDate;
    public final Date endDate;

    public GameInfo(Long id, Long player1ID, Long player2ID, String player1Username, String player2Username, Date startDate, Date endDate) {
        this.id = id;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.player1Username = player1Username;
        this.player2Username = player2Username;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
