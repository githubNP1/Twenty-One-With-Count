package Twenty_One_Test;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class PlayerGame extends Main implements ActionListener{
    BufferedImage cards, table;
    JButton hit, stand, Double, split, Bet, Chip1, Chip5, Chip10, Chip25, Chip50, Chip100, Chip500, Chip1000, yes, no;
    JTextArea display, secondDisplay;
    JLabel labelBet, labelSecondBet, labelChips, labelInsuranceBet, dealerScore, firstScore, secondScore, labelRunningCount;
    ArrayList<JButton> moveButtons, chipButtons, ynButtons;
    
    int runningCount;
    int runCalc = 0;
    
    ArrayList<Card> cardsPlayed;
    String[] pair2, pair3, pair4, pair5, pair6, pair7, pair8, pair9, pair10, pair11;
    String[] pairA2, pairA3, pairA4, pairA5, pairA6, pairA7, pairA8, pairA9;
    String[] hand5_8, hand9, hand10, hand11, hand12, hand13, hand14, hand15, hand16, hand17_;
    
    public PlayerGame() throws IOException{
        JFrame frame = new JFrame();
        frame.add(this);
        frame.setSize(1020, 750); 

        createStrategyChart();
        cardsPlayed = new ArrayList<>();
        setup();
        getCardImages();
        setupScreen();
        frame.setVisible(true);
        
        run(player, dealer);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) throws IOException{
        new PlayerGame();
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
            cardsPlayed.addAll(player.getFirstHand().hand);
            cardsPlayed.addAll(dealer.hand.hand);
            //labelRunningCount.setText("RUNNING COUNT: " + runningCount); 
        }
    }
    
    public double calcRunningCount(){
        double decksRemaining = (312 - cardsPlayed.size()) / 52;
        return runCalc / decksRemaining;
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
        int unit = player.chips / 500;
        double t = calcRunningCount();
        if(t > 4){t = 4;}
        if(t < 0.25){t = 0.25;}
        //System.out.println(t);
        return (int) (t * unit);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g); 
        g.drawImage(table, 1, 1, null);
        try{
            int i = 0;
            for(Card card : dealerCards){
                if(card.faceUp){
                    g.drawImage(cards, 400 + i*72, 70, 472 + i*72, 166, card.x1, card.y1, card.x2, card.y2, null);
                }
                i++;
            }
            int j = 0;
            for(Card card : playerFirstCards){
                if(card.faceUp){
                    g.drawImage(cards, 80 + j*72, 400, 152 + j*72, 496, card.x1, card.y1, card.x2, card.y2, null);
                }
                j++;
            }
            int k = 0;
            for(Card card : playerSecondCards){
                if(card.faceUp){
                    g.drawImage(cards, 400 + k*72, 400, 472 + k*72, 496, card.x1, card.y1, card.x2, card.y2, null);
                }
                k++;
            }
        }catch(Exception e){}
    }
    
    public void getCardImages(){
        try {
            File input = new File("cards.jpg");
            cards = ImageIO.read(input);
            File input2 = new File("blackjack table.jpg");
            table = ImageIO.read(input2);
        }
        catch (Exception e) {System.out.println(e);}
    }
    
    public void setupScreen(){ //could be condensed?
        this.setLayout(null); 
        moveButtons = new ArrayList<>();
        chipButtons = new ArrayList<>();      
        ynButtons = new ArrayList<>();      
        
        hit = createButtons(hit, "hit.jpg", 80, 570, 70, 40, moveButtons);
        stand = createButtons(stand, "stand.jpg", 200, 570, 137, 38,  moveButtons);
        Double = createButtons(Double, "double.jpg", 380, 570, 170, 40, moveButtons);
        split = createButtons(split, "split.jpg", 600, 570, 114, 40, moveButtons);
        yes = createButtons(yes, "yes.jpg", 300, 500, 80, 70, ynButtons);
        no = createButtons(no, "no.jpg", 500, 500, 80, 70, ynButtons);
        Bet = createButtons(Bet, "bet.jpg", 120, 80, 132, 68, chipButtons);
        Chip1 = createButtons(Chip1, "1 chip.jpg", 30, 570, 114, 114, chipButtons);
        Chip5 = createButtons(Chip5, "5 chip.jpg", 150, 570, 113, 116, chipButtons);
        Chip10 = createButtons(Chip10, "10 chip.jpg", 270, 570, 114, 116, chipButtons);
        Chip25 = createButtons(Chip25, "25 chip.jpg", 390, 570, 113, 114, chipButtons);
        Chip50 = createButtons(Chip50, "50 chip.jpg", 510, 570, 114, 114, chipButtons);
        Chip100 = createButtons(Chip100, "100 chip.jpg", 630, 570, 114, 114, chipButtons);
        Chip500 = createButtons(Chip500, "500 chip.jpg", 750, 570, 116, 116, chipButtons);
        Chip1000 = createButtons(Chip1000, "1000 chip.jpg", 870, 570, 114, 116, chipButtons);
        
        labelBet = createLabels(labelBet, "BET: ", bet, 880, 520, 60, 20);
        labelChips = createLabels(labelChips, "CHIPS: ", player.chips, 880, 540, 100, 20);
        labelInsuranceBet = createLabels(labelInsuranceBet, "INSURANCE: ", player.insuranceBet, 880, 500, 200, 20);
        labelSecondBet = createLabels(labelSecondBet, "SECOND BET: ", bet, 880, 480, 200, 20);
        dealerScore = createScoreLabels(dealerScore, "DEALER SCORE: ", 400, 180, 250, 20);
        firstScore = createScoreLabels(firstScore, "FIRST HAND SCORE: ", 80, 370, 300, 20);
        secondScore = createScoreLabels(secondScore, "SECOND HAND SCORE: ", 400, 370, 300, 20);
        labelRunningCount = createLabels(labelRunningCount, "RUNNING COUNT: ", runCalc, 880, 460, 250, 20);
        
        display = createDisplay(display, 730, 270, 250, 50);
        secondDisplay = createDisplay(secondDisplay, 730, 350, 250, 50);
    }
    
    public JTextArea createDisplay(JTextArea display, int x, int y, int width, int height){
        display = new JTextArea();
        display.setLineWrap(true); 
        display.setWrapStyleWord(true);
        display.setBounds(x, y, width, height); 
        this.add(display); 
        return display;
    }
    
    public JButton createButtons(JButton button, String file, int x, int y, int width, int height, ArrayList<JButton> group){ //creates buttons, uses file as imageicon, set bounds
        ImageIcon image = new ImageIcon(file);
        button = new JButton(image);
        button.setBounds(x, y, width, height); 
        button.setVisible(false);
        group.add(button);
        this.add(button);
        button.addActionListener(this);
        return button;
    }
    
    public JLabel createLabels(JLabel label, String text, int text2, int x, int y, int width, int height){
        label = new JLabel(text + text2); 
        label.setBounds(x, y, width, height); 
        this.add(label);
        return label;
    }
    
    public JLabel createScoreLabels(JLabel label, String text, int x, int y, int width, int height){
        label = new JLabel(text); 
        label.setBounds(x, y, width, height); 
        this.add(label);
        label.setVisible(false); 
        return label;
    }
    
    public void changeButtonVisibility(ArrayList<JButton> list, Boolean visibile){
        for(JButton button : list){
            button.setVisible(visibile);
        }
    }
    
    public void moveButtonVisibility(ArrayList<String> list){
        if(list.contains("hit")){
            hit.setVisible(true);
        }
        if(list.contains("stand")){
            stand.setVisible(true);
        }
        if(list.contains("double")){
            Double.setVisible(true);
        }
        if(list.contains("split")){
            split.setVisible(true);
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == hit){
            hitBo = true;
        }
        else if(e.getSource() == stand){
            standBo = true;
        }
        else if (e.getSource() == Double){
            doubleBo = true;
        }
        else if(e.getSource() == split){
            splitBo = true;
        }
        else if(e.getSource() == yes){
            y = true;
        }
        else if(e.getSource() == no){
            n = true;
        }
        else if (e.getSource() == Bet){
            bet = calculatedBet;
            calculatedBet = 0;
        }
        else if (e.getSource() == Chip1){
            calculatedBet += 1;
            if(makingInsuranceBet){
                labelInsuranceBet.setText("INSURANCE: " + calculatedBet);
            }
            else{
                labelBet.setText("BET: " + calculatedBet);
            }
        }
        else if (e.getSource() == Chip5){
            calculatedBet += 5;
            if(makingInsuranceBet){
                labelInsuranceBet.setText("INSURANCE: " + calculatedBet);
            }
            else{
                labelBet.setText("BET: " + calculatedBet);
            }
        }
        else if (e.getSource() == Chip10){
            calculatedBet += 10;
            if(makingInsuranceBet){
                labelInsuranceBet.setText("INSURANCE: " + calculatedBet);
            }
            else{
                labelBet.setText("BET: " + calculatedBet);
            }
        }
        else if (e.getSource() == Chip25){
            calculatedBet += 25;
            if(makingInsuranceBet){
                labelInsuranceBet.setText("INSURANCE: " + calculatedBet);
            }
            else{
                labelBet.setText("BET: " + calculatedBet);
            }
        }
        else if (e.getSource() == Chip50){
            calculatedBet += 50;
            if(makingInsuranceBet){
                labelInsuranceBet.setText("INSURANCE: " + calculatedBet);
            }
            else{
                labelBet.setText("BET: " + calculatedBet);
            }
        }
        else if (e.getSource() == Chip100){
            calculatedBet += 100;
            if(makingInsuranceBet){
                labelInsuranceBet.setText("INSURANCE: " + calculatedBet);
            }
            else{
                labelBet.setText("BET: " + calculatedBet);
            }
        }
        else if (e.getSource() == Chip500){
            calculatedBet += 500;
            if(makingInsuranceBet){
                labelInsuranceBet.setText("INSURANCE: " + calculatedBet);
            }
            else{
                labelBet.setText("BET: " + calculatedBet);
            }
        }
        else if (e.getSource() == Chip1000){
            calculatedBet += 1000;
            if(makingInsuranceBet){
                labelInsuranceBet.setText("INSURANCE: " + calculatedBet);
            }
            else{
                labelBet.setText("BET: " + calculatedBet);
            }
        }
    }
    
    public void nextRoundKey(){
        changeButtonVisibility(moveButtons, false);
        y = false;
        yes.setVisible(true); 
        while(!y){
            pause(200);
        }
        yes.setVisible(false);
        y = false;
    }
    
    @Override
    public void playerMakesBet(Player player){
        reset();
        changeButtonVisibility(chipButtons, true);
        display.setText("Enter how much you would like to bet \n You must enter a valid amount");
        secondDisplay.setText("Recommended bet: " + decideBet(player)); 
        calculatedBet = 0;
        do{
            bet = 0;
            pause(200);
        } while(bet <= 0 || bet > player.chips);
        display.setText(""); 
        changeButtonVisibility(chipButtons, false);
        player.newPlayerHand().bet = bet;
        player.chips -= bet;
        changeButtonVisibility(chipButtons, false);
        labelBet.setText("BET: " + player.getFirstHand().bet);    
        labelChips.setText("CHIPS: " + player.chips);
    }
    
    @Override
    public void reset(){
        player.hands.clear();
        dealer.hand.clear();
        playerFirstCards.clear();
        playerSecondCards.clear();
        dealerCards.clear();
        repaint();
        dealer.hand.bust = false;
        player.natural = false;
        dealer.natural = false;
        splitAce = false;
        labelSecondBet.setVisible(false);
        dealerScore.setText("DEALER SCORE: ");
        firstScore.setText("FIRST HAND SCORE: ");
        secondScore.setText("SECOND HAND SCORE: ");
        secondScore.setVisible(false); 
        revertAces();
    }
    
    public void initialDeal(Player player, Dealer dealer){
        hit(player.getFirstHand(), true);
        hit(dealer.hand, true);
        hit(player.getFirstHand(), true);
        hit(dealer.hand, false);
        //player.getFirstHand().add(deck.get(0), true); 
        //dealer.hand.add(deck.get(1), true); 
        //player.getFirstHand().add(deck.get(2), true);  
        //add 3 cards to count
        //dealer.hand.add(deck.get(3), false); 
        //deck.removeIf(n -> (deck.indexOf(n) >= 0 && deck.indexOf(n) <= 3)); 
        playerFirstCards = player.getFirstHand().hand; 
        repaint();
        firstScore.setVisible(true);
        firstScore.setText("FIRST HAND SCORE: " + player.getFirstHand().getVisibleScore());
        if(player.getFirstHand().checkNatural()){player.natural = true; /*display.setText("You have a natural");*/}
        dealerCards = dealer.hand.hand; 
        repaint();
        dealerScore.setVisible(true);
        dealerScore.setText("DEALER SCORE: " + dealer.hand.getVisibleScore()); 
        player.checkIfFirstHandCanBeSplit();
        pause(2000);
    }
    
    public void initialPlay(Player player, Dealer dealer){
        //first see if dealers faceup card is ace, if so, player can make an insurance bet
        if(dealer.getFaceUpCard().ace){
            y = false;
            n = false;
            changeButtonVisibility(moveButtons, false);
            changeButtonVisibility(ynButtons, true);
            display.setText("Would you like to make an insurance bet?");
            while(!y && !n){
                pause(200);
            }
            display.setText(""); 
            changeButtonVisibility(ynButtons, false);
            if(y){
                changeButtonVisibility(chipButtons, true);
                display.setText("Enter how much you would like to bet /n You can bet up to half your original bet");
                makingInsuranceBet = true;
                calculatedBet = 0;
                do{
                    bet = 0;
                    pause(200);
                } while(bet <= 0 || bet > player.getFirstHand().bet / 2);
                makingInsuranceBet = false;
                changeButtonVisibility(chipButtons, false);
                player.insuranceBet = bet;
                display.setText(""); 
                player.chips -= player.insuranceBet;
                player.insurance = true;
                labelChips.setText("CHIPS: " + player.chips);
            }
        }
        pause(2000);
        
        //if face up card is ace of ten card, dealer checks facedown card
        if(dealer.getFaceUpCard().ace || dealer.getFaceUpCard().ten){
            display.setText("The dealer checks the hole card"); 
            pause(2000);
            if(dealer.hand.checkNatural()){
                dealer.natural = true;
                dealerTurnOverCard(dealer);;
                dealerScore.setText("DEALER SCORE: " + dealer.hand.getVisibleScore()); 
                display.setText("The dealer has a natural");
                pause(2000);
                if(player.insurance){
                    display.setText("You win twice your insurance bet");
                    player.chips += player.insuranceBet * 3;
                    labelChips.setText("CHIPS: " + player.chips);
                    player.insurance = false;
                }
            }
            else{
                display.setText("The dealer does not have a natural");
                pause(2000);
                if(player.insurance){
                    display.setText("You lose your insurance bet");
                    player.insurance = false;
                }
            }
        }
        if(player.natural && dealer.natural){ //standoff
            display.setText("Standoff: Your bet is returned to you");
            player.chips += player.getFirstHand().bet;
            labelChips.setText("CHIPS: " + player.chips);
        }
        else if(player.natural && !dealer.natural){ //player wins
            display.setText("You receive 1.5 times your bet in winnings");
            player.chips += player.getFirstHand().bet * 2.5;
            labelChips.setText("CHIPS: " + player.chips);
        }
        else if(!player.natural && dealer.natural){ //dealer wins
            display.setText("Dealer wins");
        }
        else{
            play(player, dealer);
        }
        labelChips.setText("CHIPS: " + player.chips);
        labelBet.setText("BET: ");
        labelSecondBet.setText("SECOND BET: ");
        labelInsuranceBet.setText("INSURANCE: "); 
        nextRoundKey();
    }
    
    public void decideMove(Hand hand, Dealer dealer){
        switch(getDecision(hand, dealer.getFaceUpCard())){
            case "hit":
                secondDisplay.setText("Recommended move: hit");
                break;
            case "stand":
                secondDisplay.setText("Recommended move: stand");
                break;
            case "double":
                secondDisplay.setText("Recommended move: double");
                break;
            case "split":
                secondDisplay.setText("Recommended move: split");
                break;
        }
    }
    
    public void play(Player player, Dealer dealer){
        hand = player.getFirstHand();
        display.setText("First hand");
        while(!hand.checkForBust() && !hand.twentyOne()){
            firstScore.setText("FIRST HAND SCORE: " + hand.getVisibleScore());
            hitBo = false; standBo = false; doubleBo = false; splitBo = false;
            moveButtonVisibility(hand.moveChoices());
            decideMove(hand, dealer);
            while(!hitBo && !standBo && !doubleBo && !splitBo){
                pause(200);
            }
            changeButtonVisibility(moveButtons, false);
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
        firstScore.setText("FIRST HAND SCORE: " + hand.getVisibleScore());
        if(player.hands.size() == 2 && !splitAce){
            display.setText("Second hand");
            hand = player.getSecondHand();
            while(!hand.checkForBust() && !hand.twentyOne()){
                secondScore.setText("SECOND HAND SCORE: " + hand.getVisibleScore()); 
                hitBo = false; standBo = false; doubleBo = false; splitBo = false;
                moveButtonVisibility(hand.moveChoices());
                decideMove(hand, dealer);
                while(!hitBo && !standBo && !doubleBo && !splitBo){
                    pause(200);
                }
                changeButtonVisibility(moveButtons, false);
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
        secondScore.setText("SECOND HAND SCORE: " + hand.getVisibleScore()); 
        playerTurnOverCards(player);
        player.getFirstHand().checkForBust();
        hand.checkForBust();
        firstScore.setText("FIRST HAND SCORE: " + player.getFirstHand().getVisibleScore());
        secondScore.setText("SECOND HAND SCORE: " + hand.getVisibleScore()); 
        for(Hand hand : player.hands){
            pause(2000);
            if(hand.checkForBust()){  //cannot use check for bust in if statement - meant for while loop or on its own. necessary for player since player can double, not needed for dealer
                display.setText("You are bust, dealer wins");
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
        labelChips.setText("CHIPS: " + player.chips);
        if(hand.equals(player.getFirstHand())){
            labelBet.setText("BET: " + hand.bet);
        }
        else if(hand.equals(player.getSecondHand())){
            labelSecondBet.setText("SECOND BET: " + hand.bet);
        }
    }
    
    public void split(Player player){ 
        player.split();
        playerSecondCards = player.getSecondHand().hand; 
        player.chips -= player.getFirstHand().bet;
        player.getSecondHand().bet = player.getFirstHand().bet;
        labelSecondBet.setVisible(true);
        labelSecondBet.setText("SECOND BET: " + player.getSecondHand().bet);
        labelChips.setText("CHIPS: " + player.chips);
        hit(player.getFirstHand(), true);
        hit(player.getSecondHand(), true);
        secondScore.setText("SECOND HAND SCORE: " + player.getSecondHand().getVisibleScore()); 
        secondScore.setVisible(true);
    }
    
    public void hit(Hand hand, Boolean faceUp){
        hand.add(deck.get(0), faceUp); 
        repaint();
        if(faceUp){
            runCalc += deck.get(0).count;
            labelRunningCount.setText("RUNNING COUNT: " + runCalc); 
        }
        deck.remove(0);
    }
    
    public void playerTurnOverCards(Player player){
        try{
            for(Card card : player.turnOverFaceDownCards()){ 
                runCalc += card.count;
                labelRunningCount.setText("RUNNING COUNT: " + runCalc); 
            }
        }catch(Exception e){System.out.println("null");} 
        repaint();
    }
    
    public void dealerTurnOverCard(Dealer dealer){
        try{
            runCalc += dealer.turnOverFaceDownCard().count;
            labelRunningCount.setText("RUNNING COUNT: " + runCalc); 
        }catch(Exception e){System.out.println("null");} 
        repaint();
    }
    
    public void dealersPlay(Player player, Dealer dealer, Hand playerHand){
        dealerTurnOverCard(dealer);
        while(!dealer.hand.checkForBust() && !dealer.hand.twentyOne()){
            dealerScore.setText("DEALER SCORE: " + dealer.hand.getVisibleScore());
            pause(2000);
            if(dealer.hand.getTotalScore() >= 17){
                break;
            }
            else{
                hit(dealer.hand, true);
                display.setText("Dealer hits"); 
            }
        }
        dealerScore.setText("DEALER SCORE: " + dealer.hand.getVisibleScore());
        pause(2000);
        if(dealer.hand.checkForBust()){
            display.setText("Dealer is bust, you win");
            player.chips += 2 * playerHand.bet;
        }
        else if(dealer.hand.getTotalScore() > playerHand.getTotalScore()){
            display.setText("Dealer wins");
        }
        else if(dealer.hand.getTotalScore() < playerHand.getTotalScore()){
            display.setText("You win");
            player.chips += 2 * playerHand.bet;
        }
        else if(dealer.hand.getTotalScore() == playerHand.getTotalScore()){
            display.setText("Standoff");
            player.chips += playerHand.bet;
        }
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
}
//running count is not live - only done after round is finished