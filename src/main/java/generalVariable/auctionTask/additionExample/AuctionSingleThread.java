package generalVariable.auctionTask.additionExample;

public class AuctionSingleThread {
    public static class Bid {
        // учебный пример без private модификаторов и get методов
        Long id; // ID заявки
        Long participantId; // ID участника
        Long price; // предложенная цена

        public Bid(Long id, Long participantId, Long price) {
            this.id = id;
            this.participantId = participantId;
            this.price = price;
        }
    }

    public static class Notifier {
        public void sendOutdatedMessage(Bid bid) { /*...*/ }
    }

    private Notifier notifier = new Notifier();

    private Bid latestBid=new Bid(1L, 1L, 1L);

    public boolean propose(Bid bid) {
        if (bid.price > latestBid.price) {
            notifier.sendOutdatedMessage(latestBid);
            latestBid = bid;
            return true;
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }
}
