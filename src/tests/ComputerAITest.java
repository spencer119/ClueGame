package tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ComputerAITest {
    private static Board board;
    private static ComputerPlayer ai1;
    private final Random rand = new Random();

    @BeforeAll
    public static void setUp() {
        // Board is singleton, get the only instance
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load config files
        board.initialize();
        ai1 = new ComputerPlayer("testAI 1", "red", 13, 2);
        board.addPlayer(ai1);
    }

    @Test
    public void testAISuggestion() {
        Room attic = board.getRoom('A');
        ArrayList<Card> boardDeck = board.getDeck();
        Set<Card> unSeenWeapon = new HashSet<>();
        Set<Card> unSeenPeople = new HashSet<>();
        int weapon = 0, person = 0;
        // Randomly add 2 weapon/person cards to seenCards
        for (Card c : boardDeck) { // Add all unseen and possible solutions to unSeenCards
            if (c.getType() == CardType.ROOM) continue;
            switch (c.getType()) {
                case WEAPON:
                    if (weapon < 2) ai1.updateSeen(c);
                    else unSeenWeapon.add(c);
                    weapon++;
                    break;
                case PERSON:
                    if (person < 2) ai1.updateSeen(c);
                    else unSeenPeople.add(c);
                    person++;
                    break;
                default:
                    break;
            }
//            if (c.getType() == CardType.WEAPON && weapon < 2) {
//                ai1.updateSeen(c);
//                unSeenCards.add(c);
//                weapon++;
//            } else if (c.getType() == CardType.PERSON && person < 2) {
//                ai1.updateSeen(c);
//                unSeenCards.add(c);
//                person++;
//            }
        }
        // Testing random pick of both weapon and person cards
        Solution sol = ai1.createSuggestion(attic);
        assertTrue(unSeenPeople.contains(sol.getPerson()));
        assertTrue(unSeenWeapon.contains(sol.getWeapon()));
        assertEquals(sol.getRoom().getCardName(), attic.getName());
        // Testing when only one weapon is left
        ArrayList<Card> wList = new ArrayList<>(unSeenWeapon);
        ArrayList<Card> pList = new ArrayList<>(unSeenPeople);
        while (wList.size() > 1) { // Add all weapons to seen except 1
            ai1.updateSeen(wList.get(0));
            wList.remove(0);
        }
        sol = ai1.createSuggestion(attic);
        assertEquals(sol.getWeapon(), wList.get(0)); // Make sure the only weapon left is in sol
        // Do the same for person
        while (pList.size() > 1) {
            ai1.updateSeen(pList.get(0));
            pList.remove(0);
        }
        sol = ai1.createSuggestion(attic);
        assertEquals(sol.getPerson(), pList.get(0)); // Make sure the only person left is in sol
    }

    @Test
    public void testAITarget() {
        board.calcTargets(board.getCell(2, 0), 1); // Doorawy to attic
        BoardCell target = ai1.selectTarget(board.getTargets());
        assertEquals(board.getCell(3, 3), target); // Attic center
        // Test when room is seen
        ai1.updateSeen(new Card("Attic", CardType.ROOM));
        board.calcTargets(board.getCell(2, 0), 1); // Doorawy to attic
        target = ai1.selectTarget(board.getTargets());
        assertNotEquals(board.getCell(3, 3), target); // Attic center
        // Test no room random target on walkway
        board.calcTargets(board.getCell(13, 2), 1);
        target = ai1.selectTarget(board.getTargets());
        assertEquals('W', target.getChar());
        // Test target is Storage room not Library which is seen
        ai1.updateSeen(new Card("Library", CardType.ROOM));
        board.calcTargets(board.getCell(16, 17), 2);
        target = ai1.selectTarget(board.getTargets());
        assertEquals(target, board.getRoom('S').getCenterCell());
        // Test with only one unseen weapon
    }
}
