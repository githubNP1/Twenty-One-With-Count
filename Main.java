package Twenty_One_Test;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class Main extends JPanel{ 
    ArrayList<Card> deck, fullDeck, dealerCards, playerFirstCards, playerSecondCards;
    Boolean makingInsuranceBet = false;
    int calculatedBet, bet;
    Hand hand;
    Player player;
    Dealer dealer;
    Boolean hitBo, standBo, doubleBo, splitBo, y, n, splitAce;
    
    public void setup() throws IOException{
        deck = new ArrayList<>();
        fullDeck = new ArrayList<>();
        dealerCards = new ArrayList<>();
        playerFirstCards = new ArrayList<>();
        playerSecondCards = new ArrayList<>();
        player = new Player(10000);
        dealer = new Dealer();
        getDeck();
    }
    
    public static void main(String[] args) throws IOException{ 
        Main game = new Main();
    }
    
    public void run(Player player, Dealer dealer){
        while(player.chips > 10){
            if(deck.size() < 30){
                deck = new ArrayList<>(fullDeck);
                Collections.shuffle(deck); 
            }
            playerMakesBet(player);
            initialDeal(player, dealer);
            initialPlay(player, dealer);
        }
    }
    
    public void pause(int time){
        try{Thread.sleep(time);} catch (InterruptedException ex) {}
    }
    
    public void playerMakesBet(Player player){
        reset();
        calculatedBet = 0;
        do{
            bet = 0;
            pause(200);
        } while(bet <= 0 || bet > player.chips);
        player.newPlayerHand().bet = bet;
        player.chips -= bet;
    }
    
    public void reset(){
        player.hands.clear();
        dealer.hand.clear();
        playerFirstCards.clear();
        playerSecondCards.clear();
        dealerCards.clear();
        dealer.hand.bust = false;
        player.natural = false;
        dealer.natural = false;
        splitAce = false;
        revertAces();
    }
    
    public void initialDeal(Player player, Dealer dealer){
        player.getFirstHand().add(deck.get(0), true); 
        dealer.hand.add(deck.get(1), true); 
        player.getFirstHand().add(deck.get(2), true);  
        dealer.hand.add(deck.get(3), false); 
        deck.removeIf(n -> (deck.indexOf(n) >= 0 && deck.indexOf(n) <= 3)); 
        playerFirstCards = player.getFirstHand().hand; 
        if(player.getFirstHand().checkNatural()){player.natural = true; /*display.setText("You have a natural");*/}
        dealerCards = dealer.hand.hand; 
        player.checkIfFirstHandCanBeSplit();
        pause(2000);
    }
    
    public void initialPlay(Player player, Dealer dealer){
        if(dealer.getFaceUpCard().ace){
            y = false;
            n = false;
            while(!y && !n){
                pause(200);
            }
            if(y){
                makingInsuranceBet = true;
                calculatedBet = 0;
                do{
                    bet = 0;
                    pause(200);
                } while(bet <= 0 || bet > player.getFirstHand().bet / 2);
                makingInsuranceBet = false;
                player.insuranceBet = bet;
                player.chips -= player.insuranceBet;
                player.insurance = true;
            }
        }
        pause(2000);
        
        if(dealer.getFaceUpCard().ace || dealer.getFaceUpCard().ten){
            pause(2000);
            if(dealer.hand.checkNatural()){
                dealer.natural = true;
                dealer.turnOverFaceDownCard();
                pause(2000);
                if(player.insurance){
                    player.chips += player.insuranceBet * 3;
                    player.insurance = false;
                }
            }
            else{
                pause(2000);
                if(player.insurance){
                    player.insurance = false;
                }
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
        while(!hand.checkForBust() && !hand.twentyOne()){
            hitBo = false; standBo = false; doubleBo = false; splitBo = false;
            while(!hitBo && !standBo && !doubleBo && !splitBo){
                pause(200);
            }
            pause(2000);
            if(hitBo){
                hit(hand, true);
            }
            else if(standBo){
                break;
            }
            else if(doubleBo){
                Double(hand);
                break;
            }
            else if(splitBo){
                if(hand.hand.get(0).ace){
                    splitAce = true;
                    split(player);
                    break;
                }
                else{
                    split(player);
                }
            }
            hitBo = false; standBo = false; doubleBo = false; splitBo = false;
        }
        if(player.hands.size() == 2 && !splitAce){
            hand = player.getSecondHand();
            while(!hand.checkForBust() && !hand.twentyOne()){
                hitBo = false; standBo = false; doubleBo = false; splitBo = false;
                while(!hitBo && !standBo && !doubleBo && !splitBo){
                    pause(200);
                }
                pause(2000);
                if(hitBo){
                    hit(hand, true);
                }
                else if(standBo){
                    break;
                }
                else if(doubleBo){
                    Double(hand);
                    break;
                }
                else if(splitBo){
                    if(hand.hand.get(0).ace){
                        split(player);
                        break;
                    }
                    else{
                        split(player);
                    }
                }
                hitBo = false; standBo = false; doubleBo = false; splitBo = false;
            }
        }
        player.turnOverFaceDownCards();
        player.getFirstHand().checkForBust();
        hand.checkForBust();
        for(Hand hand : player.hands){
            pause(2000);
            if(!hand.checkForBust()){  
                dealersPlay(player, dealer, hand);
            }
        }
    }
    
    public void Double(Hand hand){
        player.chips -= hand.bet;
        hand.bet *= 2;
        hit(hand, false);
        hand.doubled = true;
        hand.checkForBust();
    }
    
    public void split(Player player){ 
        player.split();
        playerSecondCards = player.getSecondHand().hand; 
        player.chips -= player.getFirstHand().bet;
        player.getSecondHand().bet = player.getFirstHand().bet;
        hit(player.getFirstHand(), true);
        hit(player.getSecondHand(), true);
    }
    
    public void hit(Hand hand, Boolean faceUp){
        hand.add(deck.get(0), faceUp); 
        deck.remove(0);
    }
    
    public void dealersPlay(Player player, Dealer dealer, Hand playerHand){
        dealer.turnOverFaceDownCard();
        while(!dealer.hand.checkForBust() && !dealer.hand.twentyOne()){
            pause(2000);
            if(dealer.hand.getTotalScore() >= 17){
                break;
            }
            else{
                hit(dealer.hand, true);
            }
        }
        pause(2000);
        if(dealer.hand.checkForBust()){
            player.chips += 2 * playerHand.bet;
        }
        //settlement
        else if(dealer.hand.getTotalScore() < playerHand.getTotalScore()){
            player.chips += 2 * playerHand.bet;
        }
        else if(dealer.hand.getTotalScore() == playerHand.getTotalScore()){
            player.chips += playerHand.bet;
        }
    }
    
    public void getDeck() throws FileNotFoundException, IOException{  //shuffles 6 decks of cards to be used
        for(int i = 0; i < 6; i++){
            BufferedReader reader = new BufferedReader(new FileReader("Deck2.txt"));
            for (int j = 0; j < 52; j++){                                                          
                String[] line = reader.readLine().split("[:]");   
                Card one = new Card(line[0], line[1], Integer.valueOf(line[2]), Integer.valueOf(line[3]), Integer.valueOf(line[4]), Integer.valueOf(line[5]), Integer.valueOf(line[6]), Integer.valueOf(line[7]));                                     
                fullDeck.add(one);
            }
            reader.close();
        }
    }
    
    public void revertAces(){ 
        for(Card card : fullDeck){
            if(card.value == 1){card.value = 11;}
        }
    }            
}
