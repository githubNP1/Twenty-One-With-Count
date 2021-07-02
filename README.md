# Twenty-One-With-Count

In order to run the game you will need to add all the pictures in the Picture Folder to the same package as the java files then run the PlayerGame class.

This is the same as the other Twenty-One repository but has an added feature. The cards are being counted by the program and along with using a strategy table, the program advises the player on how much to bet and what moves to make in order to increase the player's chance of winning.
The ComputerGame class was just used to test code.

The program decides how much the player should bet based on multiplying the true count by the betting unit. The betting unit is calculated by dividing the player's remaining chips by 500. Also, if the true count is above 4, than it is counted as 4 and if it is lower than 0.25, it is counted as 0.25.

# Rules

- 6 decks are shuffled and used
- When there are only 30 cards left, the decks are reshuffled and used    
- The player starts with 10000 chips 
- The program stops when the player has 10 chips or less

- A natural pays 1.5 times the player's bet
- Player can only split once
- Player can split any pair

- Player can only double on first move if first 2 cards add to 9, 10 or 11
- Ace counts as 1 or 11

- Dealer cannot split or double
- Dealer just hits until score is 17 and above

- If the dealers face up card is an ace, the player may make an insurance bet
- Insurance pays 2 to 1

- 5 card charlie rule not being used

- Otherwise standard rules of blackjack apply
