package Twenty_One_Test;

import java.io.IOException;
import java.util.*;

public class ComputerGame extends Main{
    int runningCount;
    ArrayList<Card> cardsPlayed;
    String[] pair2, pair3, pair4, pair5, pair6, pair7, pair8, pair9, pair10, pair11;
    String[] pairA2, pairA3, pairA4, pairA5, pairA6, pairA7, pairA8, pairA9;
    String[] hand5_8, hand9, hand10, hand11, hand12, hand13, hand14, hand15, hand16, hand17_;
    int game = 0;
    
    public ComputerGame() throws IOException{
        createStrategyChart();
        cardsPlayed = new ArrayList<>();
        setup();
        run(player, dealer);
    }
    
    public static void main(String[] args) throws IOException{
        new ComputerGame();
    }
    
    @Override
    public void run(Player player, Dealer dealer){ 
        while(player.chips > 10){
            if(deck.size() < 30){
                deck = new ArrayList<>(fullDeck);
                Collections.shuffle(deck); 
            }
            playerMakesBet(player);
            initialDeal(player, dealer);
            initialPlay(player, dealer);
            cardsPlayed.addAll(player.getFirstHand().hand);
            cardsPlayed.addAll(dealer.hand.hand);
            //game++;
            //System.out.println("game: " + game);
            //if(game%100 == 0){
            //System.out.println(player.chips);}
        }
    }
    
    public void initialPlay(Player player, Dealer dealer){
        //if face up card is ace of ten card, dealer checks facedown card
        if(dealer.getFaceUpCard().ace || dealer.getFaceUpCard().ten){ //method needs redoing maybe
            if(dealer.hand.checkNatural()){
                dealer.natural = true;
                dealer.turnOverFaceDownCard();
            }
        }
        if(player.natural && dealer.natural){ //standoff
            player.chips += player.getFirstHand().bet;
        }
        else if(player.natural && !dealer.natural){ //player wins
            player.chips += player.getFirstHand().bet * 2.5;
        }
        else{
            play(player, dealer);
        }
    }
    
    public void play(Player player, Dealer dealer){
        hand = player.getFirstHand();
        outerloop:
        while(!hand.checkForBust() && !hand.twentyOne()){
            switch(getDecision(hand, dealer.getFaceUpCard())){
                case "hit":
                    hit(hand, true);
                    break;
                case "stand":
                    break outerloop;
                case "double":
                    Double(hand);
                    break outerloop;
                case "split":
                    if(hand.hand.get(0).ace){
                        splitAce = true;
                        split(player);
                        break outerloop;
                    }
                    else{
                        split(player);
                    }
                    break;
            }
        }
        
        if(player.hands.size() == 2 && !splitAce){
            hand = player.getSecondHand();
            outerloop:
            while(!hand.checkForBust() && !hand.twentyOne()){
                switch(getDecision(hand, dealer.getFaceUpCard())){
                    case "hit":
                        hit(hand, true);
                        break;
                    case "stand":
                        break outerloop;
                    case "double":
                        Double(hand);
                        break outerloop;
                    case "split":
                        if(hand.hand.get(0).ace){
                            splitAce = true;
                            split(player);
                            break outerloop;
                        }
                        else{
                            split(player);
                        }
                        break;
                }
            }
        }
        player.turnOverFaceDownCards();
        player.getFirstHand().checkForBust();
        hand.checkForBust();
        for(Hand hand : player.hands){
            if(!hand.checkForBust()){  
                dealersPlay(player, dealer, hand);
            }
        }
    }
    
    public void playerMakesBet(Player player){
        reset();
        int bet = decideBet(player);
        player.newPlayerHand().bet = bet;
        player.chips -= bet;
        
    }
    
    public double trueCount(){
        runningCount = 0;
        for(Card card : cardsPlayed){
            if(card.ten || card.ace){
                runningCount--;
            }
            else if(card.value >= 2 && card.value <= 6){
                runningCount++;
            }
        }
        //System.out.println(runningCount);
        double decksRemaining = (312 - cardsPlayed.size()) / 52;
        return runningCount / decksRemaining;
    }
    
    public int decideBet(Player player){
        int unit = player.chips / 1000;
        double t = trueCount();
        if(t > 4){t = 4;}
        if(t < 0.25){t = 0.25;}
        //System.out.println(t);
        return (int) (t * unit);
    }
    
    public String getDecision(Hand hand, Card dealerCard){  //always avoid insurance
        if(hand.splittable){
            switch(hand.hand.get(0).number){
                case "2":
                    return pair2[dealerCard.value - 2];
                case "3":
                    return pair3[dealerCard.value - 2];
                case "4":
                    return pair4[dealerCard.value - 2];
                case "5":
                    return pair5[dealerCard.value - 2];
                case "6":
                    return pair6[dealerCard.value - 2];
                case "7":
                    return pair7[dealerCard.value - 2];
                case "8":
                    return pair8[dealerCard.value - 2];
                case "9":
                    return pair9[dealerCard.value - 2];
                case "10":
                    return pair10[dealerCard.value - 2];
                case "Jack":
                    return pair10[dealerCard.value - 2];
                case "Queen":
                    return pair10[dealerCard.value - 2];
                case "King":
                    return pair10[dealerCard.value - 2];
                case "Ace":
                    return pair11[dealerCard.value - 2];
            }
        }
        else if(hand.acePresent()){
            switch(hand.acePartner()){
                case "2":
                    return pairA2[dealerCard.value - 2];
                case "3":
                    return pairA3[dealerCard.value - 2];
                case "4":
                    return pairA4[dealerCard.value - 2];
                case "5":
                    return pairA5[dealerCard.value - 2];
                case "6":
                    return pairA6[dealerCard.value - 2];
                case "7":
                    return pairA7[dealerCard.value - 2];
                case "8":
                    return pairA8[dealerCard.value - 2];
                case "9":
                    return pairA9[dealerCard.value - 2];
            }
        }
        else if(hand.getVisibleScore() >= 5 && hand.getVisibleScore() <= 8){
            return hand5_8[dealerCard.value - 2];
        }
        else if(hand.getVisibleScore() >= 9 && hand.getVisibleScore() <= 16){
            switch(hand.getVisibleScore()){
                case 9:
                    return hand9[dealerCard.value - 2];
                case 10:
                    return hand10[dealerCard.value - 2];
                case 11:
                    return hand11[dealerCard.value - 2];
                case 12:
                    return hand12[dealerCard.value - 2];
                case 13:
                    return hand13[dealerCard.value - 2];
                case 14:
                    return hand14[dealerCard.value - 2];
                case 15:
                    return hand15[dealerCard.value - 2];
                case 16:
                    return hand16[dealerCard.value - 2];
            }
        }
        else if(hand.getVisibleScore() >= 17){
            return hand17_[dealerCard.value - 2];
        }
        return null;
    }
    
    public void createStrategyChart(){
        pair2 = new String[]{"split", "split", "split", "split", "split", "split", "hit", "hit", "hit", "hit"};
        pair3 = new String[]{"split", "split", "split", "split", "split", "split", "hit", "hit", "hit", "hit"};
        pair4 = new String[]{"hit", "hit", "hit", "split", "split", "hit", "hit", "hit", "hit", "hit"};
        pair5 = new String[]{"double", "double", "double", "double", "double", "double", "double", "double", "hit", "hit"};
        pair6 = new String[]{"split", "split", "split", "split", "split", "hit", "hit", "hit", "hit", "hit"};
        pair7 = new String[]{"split", "split", "split", "split", "split", "split", "hit", "hit", "hit", "hit"};
        pair8 = new String[]{"split", "split", "split", "split", "split", "split", "split", "split", "split", "split"};
        pair9 = new String[]{"split", "split", "split", "split", "split", "stand", "split", "split", "stand", "stand"};
        pair10 = new String[]{"stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand"};
        pair11 = new String[]{"split", "split", "split", "split", "split", "split", "split", "split", "split", "split"};
        
        pairA2 = new String[]{"hit", "hit", "hit", "double", "double", "hit", "hit", "hit", "hit", "hit"};
        pairA3 = new String[]{"hit", "hit", "hit", "double", "double", "hit", "hit", "hit", "hit", "hit"};
        pairA4 = new String[]{"hit", "hit", "double", "double", "double", "hit", "hit", "hit", "hit", "hit"};
        pairA5 = new String[]{"hit", "hit", "double", "double", "double", "hit", "hit", "hit", "hit", "hit"};
        pairA6 = new String[]{"hit", "double", "double", "double", "double", "hit", "hit", "hit", "hit", "hit"};
        pairA7 = new String[]{"stand", "double", "double", "double", "double", "stand", "stand", "hit", "hit", "hit"};
        pairA8 = new String[]{"stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand"};
        pairA9 = new String[]{"stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand"};
        
        hand5_8 = new String[]{"hit", "hit", "hit", "hit", "hit", "hit", "hit", "hit", "hit", "hit"};
        hand9 = new String[]{"hit", "double", "double", "double", "double", "hit", "hit", "hit", "hit", "hit"};
        hand10 = new String[]{"double", "double", "double", "double", "double", "double", "double", "double", "hit", "hit"};
        hand11 = new String[]{"double", "double", "double", "double", "double", "double", "double", "double", "double", "hit"};
        hand12 = new String[]{"hit", "hit", "stand", "stand", "stand", "hit", "hit", "hit", "hit", "hit"};
        hand13 = new String[]{"stand", "stand", "stand", "stand", "stand", "hit", "hit", "hit", "hit", "hit"};
        hand14 = new String[]{"stand", "stand", "stand", "stand", "stand", "hit", "hit", "hit", "hit", "hit"};
        hand15 = new String[]{"stand", "stand", "stand", "stand", "stand", "hit", "hit", "hit", "hit", "hit"};
        hand16 = new String[]{"stand", "stand", "stand", "stand", "stand", "hit", "hit", "hit", "stand", "hit"};
        hand17_ = new String[]{"stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand", "stand"};
    }
}
