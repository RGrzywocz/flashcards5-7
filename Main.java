package flashcards;
import javax.print.DocFlavor;
import javax.swing.plaf.IconUIResource;
import java.io.*;
import java.util.*;

class Card{
    private String front;
    private String back;
    Card(String front,String back){
        this.back=back;
        this.front=front;
    }
    public void setFront(String front){
        this.front=front;
    }
    public void setBack(String back){
        this.back=back;
    }
    public String getFront(){
        return front;
    }
    public String getBack(){
        return back;
    }
}
class Deck{
    private Set<Card> cards=new HashSet<>();
    public void addNewCardFromUser(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("The card");
        String front=scanner.nextLine();

        for(Card card:cards) {
        if(front.equals(card.getFront())){
            System.out.println("The card \""+front+"\" already exists.");
            scanner.close();

            return;
        }
        }
        System.out.println("The definition of the card");
        String back=scanner.nextLine();
        for(Card card:cards) {
            if(back.equals(card.getBack())){
                System.out.println("The definition \""+back+"\" already exists.");
                scanner.close();

                return;
            }
        }
        addNewCard(front,back);
        System.out.println("The pair (\""+front+"\":\""+back+"\") has been added.");
        scanner.close();
    }
    private void addNewCard(String front,String back){
        Card newCard=new Card(front,back);
        boolean unique=true;
        for(Card card:cards){
            if(card.getFront().equals(newCard.getFront())){
                unique=false;
                card.setBack(newCard.getBack());
            }
        }
        if(unique) {
            cards.add(newCard);
        }
    }
    public void importCardsFromFile(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("File name:");
        String fileName=scanner.nextLine();
        File file = new File(fileName);
        int counter=0;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String front = fileScanner.nextLine();
                String back = fileScanner.nextLine();
                addNewCard(front,back);
                counter++;
            }
            fileScanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("not found");
            scanner.close();
            return;
        }
        System.out.println(counter+" cards have been loaded.");
        scanner.close();
        return;
    }
    public void removeCard(){
    Scanner scanner=new Scanner(System.in);
    System.out.println("The card:");
    String cardToRemove=scanner.nextLine();
    for(Card card:cards) {
    if(card.getFront().equals(cardToRemove)){
        cards.remove(card);
        System.out.println("The card has been removed.");
        scanner.close();
        return;
    }
    }
        System.out.println("Can't remove \""+cardToRemove+"\": there is no such card.");
        scanner.close();
        return;
    }
    private void askRandomQuestion(){
        Scanner scanner=new Scanner(System.in);
        Card currentCard=randomCard();
        System.out.println("Print the definition of \""+currentCard.getFront()+"\":");
        String answer=scanner.nextLine();
        if(answer.equals(currentCard.getBack())){
            System.out.println("Correct answer");
        }
        else{
            System.out.print("Wrong answer. The correct one is \""+currentCard.getBack()+"\"");
            for (Card card:cards){
                if(card.getBack().equals(answer)){
                    System.out.println(", you've just written the definition of \""+card.getFront()+"\".");
                    scanner.close();
                    return;
                }
            }
            System.out.println(".");
        }
        scanner.close();
        return;

    }
    private Card randomCard(){
        int size = cards.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(Card card:cards)
        {
            if (i == item)
                return card;
            i++;
        }
        return new Card("X","d");
    }
    public void startTest(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("How many times to ask?");
        int numberOfQuestions=scanner.nextInt();
        for(int i=0;i<numberOfQuestions;i++){
            this.askRandomQuestion();
        }
    }
    public void exportDeck(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("File name:");
        String fileName=scanner.nextLine();
        File file = new File(fileName);
        int counter=0;
        try (PrintWriter writer = new PrintWriter(file)) {
            for(Card card:cards){
                writer.println(card.getFront());
                writer.println(card.getBack());
                counter++;
            }

        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
            scanner.close();
            return;
        }
        System.out.println(counter+(counter==1?" cards":" cards")+" have been saved.");
        scanner.close();
        return;
    }
}
public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        Deck deck=new Deck();
        boolean app=true;
        String decision;
        while(app){
        System.out.println("Input the action (add, remove, import, export, ask, exit):");
        decision=scanner.nextLine();
        switch (decision){
            case "add":deck.addNewCardFromUser();break;
            case "remove": deck.removeCard();break;
            case "import": deck.importCardsFromFile();break;
            case "export": deck.exportDeck();break;
            case "ask": deck.startTest();break;
            case "exit": app=false;break;
        }

    }
        System.out.println("Bye bye!");

    }
}
