package Twenty_One_Test;

import java.util.*;

public class Hand {
    ArrayList<Card> hand = new ArrayList<>();
    Boolean bust = false;
    Boolean doubled = false;
    Boolean splittable;
    int bet;
    
    public Hand(){}
    
    public Hand(Boolean splittable){
        this.splittable = splittable;
    }
    
    public int getTotalScore(){
        int score = 0;
        for (Card card : hand){
            score += card.value;
        }
        return score;
    }
    
    public void add(Card card, Boolean faceUp){
        hand.add(card);
        card.faceUp = faceUp;
    }
    
    public Boolean checkNatural(){  //returns Boolean, maybe no need for Boolean natural
        if(hand.get(0).ace && hand.get(1).ten || hand.get(0).ten && hand.get(1).ace){
            return true;
        }
        return false;
    }
    
    public Boolean twentyOne(){
        if(getTotalScore() == 21){
            return true;
        }
        return false;
    }
    
    public Boolean checkBust(){
        if(getTotalScore() > 21){
            return true;
        }
        return false;
    }
    
    public Boolean elevenToOne(){  //changes ace card value from 11 to 1
        for(Card card : hand){
            if(card.ace && card.value == 11){
                card.value = 1;
                return true;
            }
        }
        return false;
    }
    
    public Boolean checkForBust(){ //either bust or changes ace from 11 to 1 so no longer bust.
        if(getTotalScore() <= 21){
            return false;
        }
        else{
            if(elevenToOne()){
                return false;
            }
            return true;
        }
    }
    
    public void clear(){
        hand.clear();
    }
    
    public Boolean checkIfDoubleable(){ //currently not used - still deciding
        if(!doubled && hand.size() == 2 && getTotalScore() >= 9 && getTotalScore() <= 11){
            return true;
        }
        return false;
    }
    
    public String makeMove(){
        String moves = "Do you want to 1) stand, 2) hit";
        checkIfDoubleable();
        if(!doubled){moves += ", 3) double";}
        if(splittable){moves += " or 4) split";}
        return moves;
    }
    
    public ArrayList moveChoices(){
        ArrayList<String> choices = new ArrayList<>();
        choices.add("stand");
        choices.add("hit"); 
        checkIfDoubleable();
        if(!doubled){choices.add("double");}
        if(splittable){choices.add("split");} 
        return choices;
    }
    
    public Integer getVisibleScore(){
        int score = 0;
        for (Card card : hand){
            if(card.faceUp){
                score += card.value;
            }
        }
        return score;
    }
    
    public Boolean aceHand(int value){ //checks if a hand contains an ace plus another card of value equal to parameter - not being used
        if(hand.size() == 2 && (hand.get(0).ace || hand.get(1).ace)){
            if(hand.get(0).value == value || hand.get(1).value == value){
                return true;
            }
        }
        return false;
    }
    
    public String acePartner(){ //if one card is an ace, returns other card
        if(hand.get(0).ace){
            return hand.get(1).number;
        }
        else if(hand.get(1).ace){
            return hand.get(0).number;
        }
        return null;
    }
    
    public Boolean acePresent(){
        if(hand.get(0).ace || hand.get(1).ace){
            return true;
        }
        return false;
    }
}
