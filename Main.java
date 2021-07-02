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
                //display.setText("Deck reshuffled"); 
            }
            playerMakesBet(player);
            initialDeal(player, dealer);
            initialPlay(player, dealer);
        }
    }
    
    public void pause(int time){
        try{Thread.sleep(time/100);} catch (InterruptedException ex) {}
    }
    
    public void playerMakesBet(Player player){
        reset();
        //changeButtonVisibility(chipButtons, true);
        //display.setText("Enter how much you would like to bet \n You must enter a valid amount");
        calculatedBet = 0;
        do{
            bet = 0;
            pause(200);
        } while(bet <= 0 || bet > player.chips);
        //display.setText(""); 
        //changeButtonVisibility(chipButtons, false);
        player.newPlayerHand().bet = bet;
        player.chips -= bet;
        //changeButtonVisibility(chipButtons, false);
        //labelBet.setText("BET: " + player.getFirstHand().bet);    
        //labelChips.setText("CHIPS: " + player.chips);
    }
    
    public void reset(){
        player.hands.clear();
        dealer.hand.clear();
        playerFirstCards.clear();
        playerSecondCards.clear();
        dealerCards.clear();
        //repaint();
        dealer.hand.bust = false;
        player.natural = false;
        dealer.natural = false;
        splitAce = false;
        //labelSecondBet.setVisible(false);
        //dealerScore.setText("DEALER SCORE: ");
        //firstScore.setText("FIRST HAND SCORE: ");
        //secondScore.setText("SECOND HAND SCORE: ");
        //secondScore.setVisible(false); 
        revertAces();
    }
    
    public void initialDeal(Player player, Dealer dealer){
        player.getFirstHand().add(deck.get(0), true); 
        dealer.hand.add(deck.get(1), true); 
        player.getFirstHand().add(deck.get(2), true);  
        dealer.hand.add(deck.get(3), false); 
        deck.removeIf(n -> (deck.indexOf(n) >= 0 && deck.indexOf(n) <= 3)); 
        playerFirstCards = player.getFirstHand().hand; 
        //repaint();
        //firstScore.setVisible(true);
        //firstScore.setText("FIRST HAND SCORE: " + player.getFirstHand().getVisibleScore());
        if(player.getFirstHand().checkNatural()){player.natural = true; /*display.setText("You have a natural");*/}
        dealerCards = dealer.hand.hand; 
        //repaint();
        //dealerScore.setVisible(true);
        //dealerScore.setText("DEALER SCORE: " + dealer.hand.getVisibleScore()); 
        player.checkIfFirstHandCanBeSplit();
        pause(2000);
    }
    
    public void initialPlay(Player player, Dealer dealer){
        //first see if dealers faceup card is ace, if so, player can make an insurance bet
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
        
        //if face up card is ace of ten card, dealer checks facedown card
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
        //display.setText("First hand");
        while(!hand.checkForBust() && !hand.twentyOne()){
            //firstScore.setText("FIRST HAND SCORE: " + hand.getVisibleScore());
            hitBo = false; standBo = false; doubleBo = false; splitBo = false;
            //moveButtonVisibility(hand.moveChoices());
            while(!hitBo && !standBo && !doubleBo && !splitBo){
                pause(200);
            }
            //changeButtonVisibility(moveButtons, false);
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
        //firstScore.setText("FIRST HAND SCORE: " + hand.getVisibleScore());
        if(player.hands.size() == 2 && !splitAce){
            //display.setText("Second hand");
            hand = player.getSecondHand();
            while(!hand.checkForBust() && !hand.twentyOne()){
                //secondScore.setText("SECOND HAND SCORE: " + hand.getVisibleScore()); 
                hitBo = false; standBo = false; doubleBo = false; splitBo = false;
                //moveButtonVisibility(hand.moveChoices());
                while(!hitBo && !standBo && !doubleBo && !splitBo){
                    pause(200);
                }
                //changeButtonVisibility(moveButtons, false);
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
        //secondScore.setText("SECOND HAND SCORE: " + hand.getVisibleScore()); 
        player.turnOverFaceDownCards();
        player.getFirstHand().checkForBust();
        hand.checkForBust();
        //firstScore.setText("FIRST HAND SCORE: " + player.getFirstHand().getVisibleScore());
        //secondScore.setText("SECOND HAND SCORE: " + hand.getVisibleScore()); 
        for(Hand hand : player.hands){
            pause(2000);
            if(hand.checkForBust()){  //cannot use check for bust in if statement - meant for while loop or on its own. necessary for player since player can double, not needed for dealer
                //display.setText("You are bust, dealer wins");
            }
            else{
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
        //labelChips.setText("CHIPS: " + player.chips);
        if(hand.equals(player.getFirstHand())){
            //labelBet.setText("BET: " + hand.bet);
        }
        else if(hand.equals(player.getSecondHand())){
            //labelSecondBet.setText("SECOND BET: " + hand.bet);
        }
    }
    
    public void split(Player player){ 
        player.split();
        playerSecondCards = player.getSecondHand().hand; 
        player.chips -= player.getFirstHand().bet;
        player.getSecondHand().bet = player.getFirstHand().bet;
        //labelSecondBet.setVisible(true);
        //labelSecondBet.setText("SECOND BET: " + player.getSecondHand().bet);
        //labelChips.setText("CHIPS: " + player.chips);
        hit(player.getFirstHand(), true);
        hit(player.getSecondHand(), true);
        //secondScore.setText("SECOND HAND SCORE: " + player.getSecondHand().getVisibleScore()); 
        //secondScore.setVisible(true);
    }
    
    public void hit(Hand hand, Boolean faceUp){
        hand.add(deck.get(0), faceUp); 
        //repaint();
        deck.remove(0);
    }
    
    public void dealersPlay(Player player, Dealer dealer, Hand playerHand){
        dealer.turnOverFaceDownCard();
        //repaint();
        while(!dealer.hand.checkForBust() && !dealer.hand.twentyOne()){
            //dealerScore.setText("DEALER SCORE: " + dealer.hand.getVisibleScore());
            pause(2000);
            if(dealer.hand.getTotalScore() >= 17){
                break;
            }
            else{
                hit(dealer.hand, true);
                //display.setText("Dealer hits"); 
            }
        }
        //dealerScore.setText("DEALER SCORE: " + dealer.hand.getVisibleScore());
        pause(2000);
        if(dealer.hand.checkForBust()){
            //display.setText("Dealer is bust, you win");
            player.chips += 2 * playerHand.bet;
        }
        //settlement
        else if(dealer.hand.getTotalScore() > playerHand.getTotalScore()){
            //display.setText("Dealer wins");
        }
        else if(dealer.hand.getTotalScore() < playerHand.getTotalScore()){
            //display.setText("You win");
            player.chips += 2 * playerHand.bet;
        }
        else if(dealer.hand.getTotalScore() == playerHand.getTotalScore()){
            //display.setText("Standoff");
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
    
    
    
    public void revertAces(){ //explain why this is needed
        for(Card card : fullDeck){
            if(card.value == 1){card.value = 11;}
        }
    }            
}
// need to make aces have variable value - done
// player hitting 21 should be auto move on, still asks whether to hit, stand etc. - done
// insurance bet needs own label - done
// second hand bet needs own label, plus needs to be clear which hand is currently being played - done
// add scores for hands as its being played - done
// should score labels be in paint method
// after splitting aces - still goes to second hand move when shouldnt - done
// can only split same card type, not value like jack and queen
// maybe allow multiple splitting
// can add 5 card charlie rule