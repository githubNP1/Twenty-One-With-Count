package Twenty_One_Test;

public class Dealer {
    Hand hand = new Hand();
    Boolean natural = false;
    
    public Boolean dealerCheckFaceUpCardForAce(){
        if(hand.hand.get(0).ace){
            System.out.println("The dealer has an ace");
            return true;
        }
        return false;
    }
    
    public Boolean dealerCheckFaceUpCardForTen(){
        if(hand.hand.get(0).ten){
            System.out.println("The dealer has a ten card");
            return true;
        }
        return false;
    }
    
    public Card getFaceUpCard(){
        if(hand.hand.get(0).faceUp){
            return hand.hand.get(0);
        }
        else if(hand.hand.get(1).faceUp){
            return hand.hand.get(1); 
        }
        return null;
    }
    
    public Card getFaceDownCard(){
        if(!hand.hand.get(0).faceUp){
            return hand.hand.get(0);
        }
        else if(!hand.hand.get(1).faceUp){
            return hand.hand.get(1); 
        }
        return null;
    }
    
    public Card turnOverFaceDownCard(){
        if(!hand.hand.get(0).faceUp){
            hand.hand.get(0).faceUp = true;
            return hand.hand.get(0);
        }
        else if(!hand.hand.get(1).faceUp){
            hand.hand.get(1).faceUp = true;
            return hand.hand.get(1); 
        }
        return null;
    }
}
