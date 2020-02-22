package flashcards;
import org.hamcrest.core.IsCollectionContaining;

import javax.print.DocFlavor;
import javax.swing.plaf.IconUIResource;
import java.io.*;
import java.util.*;

class Card{
    private String front;
    private String back;
    private int wrongAnswers=0;

    Card(String front,String back){
        this.back=back;
        this.front=front;
    }
    public void addWrongAnswer(){
        this.wrongAnswers++;
    }
    public int getWrongAnswers(){
        return wrongAnswers;
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
    public void resetWrongAnswers(){
        this.wrongAnswers=0;
    }
    public void setWrongAnswers(int wrongAnswers){
        this.wrongAnswers=wrongAnswers;
    }
}
class Deck{
    private Set<Card> cards=new HashSet<>();
    private ArrayList<String> logs=new ArrayList<>();//sasd
    public String getUserInput(){
        Scanner scanner=new Scanner(System.in);
        String input=scanner.nextLine();
        logs.add(input);
        scanner.close();
        return input;

    }
    public void outputMsg(String message){
        System.out.println(message);
        logs.add(">"+message);
    }
    public void addNewCardFromUser(){
        outputMsg("The card");
        String front=getUserInput();
        for(Card card:cards) {
            if(front.equals(card.getFront())){
                outputMsg("The card \""+front+"\" already exists.");
                return;
            }
        }
        outputMsg("The definition of the card");
        String back=getUserInput();
        for(Card card:cards) {
            if(back.equals(card.getBack())){
                outputMsg("The definition \""+back+"\" already exists.");
                return;
            }
        }
        addNewCard(front,back,0);
        outputMsg("The pair (\""+front+"\":\""+back+"\") has been added.");
    }
    private void addNewCard(String front,String back,int mistakes){
        Card newCard=new Card(front,back);
        boolean unique=true;
        newCard.setWrongAnswers(mistakes);
        for(Card card:cards){
            if(card.getFront().equals(newCard.getFront())){
                unique=false;
                card.setBack(newCard.getBack());
              //  card.resetWrongAnswers();
            }
        }
        if(unique) {
            cards.add(newCard);
        }
    }
    public void importCardsFromFile(){

        outputMsg("File name:");
        String fileName=getUserInput();
        File file = new File(fileName);
        int counter=0;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String front = fileScanner.nextLine();
                String back = fileScanner.nextLine();
                int mistakes=Integer.parseInt(fileScanner.nextLine());
                addNewCard(front,back,mistakes);
                counter++;
            }
            fileScanner.close();
        }
        catch (FileNotFoundException e) {
            outputMsg("not found");
            return;
        }
        outputMsg(counter+" cards have been loaded.");
        return;
    }
    public void importCardsFromFile(String fileName){

        File file = new File(fileName);
        int counter=0;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String front = fileScanner.nextLine();
                String back = fileScanner.nextLine();
                int mistakes=Integer.parseInt(fileScanner.nextLine());
                addNewCard(front,back,mistakes);
                counter++;
            }
            fileScanner.close();
        }
        catch (FileNotFoundException e) {
         //   outputMsg("not found");
            return;
        }
        outputMsg(counter+" cards have been loaded.");
        return;
    }
    public void removeCard(){
        outputMsg("The card:");
        String cardToRemove=getUserInput();
        for(Card card:cards) {
            if(card.getFront().equals(cardToRemove)){
                cards.remove(card);
                outputMsg("The card has been removed.");
                return;
            }
        }
        outputMsg("Can't remove \""+cardToRemove+"\": there is no such card.");
        return;
    }
    private void askRandomQuestion(){

        Card currentCard=randomCard();
        outputMsg("Print the definition of \""+currentCard.getFront()+"\":");
        String answer=getUserInput();
        if(answer.equals(currentCard.getBack())){
            outputMsg("Correct answer");
        }
        else{
            currentCard.addWrongAnswer();
            for (Card card:cards){
                if(card.getBack().equals(answer)){
                    outputMsg("Wrong answer. The correct one is \""+currentCard.getBack()+"\", you've just written the definition of \""+card.getFront()+"\".");
                    return;
                }
            }
            outputMsg("Wrong answer. The correct one is \""+currentCard.getBack()+"\".");
        }
        return;

    }
    public void hardestQuestion(){
        int mostMistakes=0;
        for(Card card:cards){
            if(card.getWrongAnswers()>mostMistakes){
                mostMistakes=card.getWrongAnswers();
            }
        }
        if(mostMistakes==0){
            outputMsg("There are no cards with errors.");
        }
        else{
            int cardsWithMostMistakes=0;
            for (Card card:cards){
                if(card.getWrongAnswers()==mostMistakes){
                    cardsWithMostMistakes++;
                }

            }
            if(cardsWithMostMistakes==1){
                for (Card card:cards){
                    if(card.getWrongAnswers()==mostMistakes){
                        outputMsg("The hardest card is \""+card.getFront()+"\". You have "+mostMistakes+" errors answering it.");
                    }
                }
            }
            else{
                String message="The hardest card are ";
                boolean flag=false;
                for(Card card:cards){
                    if(card.getWrongAnswers()==mostMistakes){
                        message+=(flag?", ":"")+"\""+card.getFront()+"\"";
                        flag=true;
                    }
                }
                message+=". You have "+mostMistakes+" errors answering them.";
                outputMsg(message);
            }
        }
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
        outputMsg("How many times to ask?");
        int numberOfQuestions=Integer.parseInt(getUserInput());
        for(int i=0;i<numberOfQuestions;i++){
            this.askRandomQuestion();
        }
    }
    public void exportDeck(){
        outputMsg("File name:");
        String fileName=getUserInput();
        File file = new File(fileName);
        int counter=0;
        try (PrintWriter writer = new PrintWriter(file)) {
            for(Card card:cards){
                writer.println(card.getFront());
                writer.println(card.getBack());
                writer.println(card.getWrongAnswers());
                counter++;
            }

        } catch (IOException e) {
            outputMsg("An exception occurs "+e.getMessage());
            return;
        }
        outputMsg(counter+(counter==1?" cards":" cards")+" have been saved.");
        return;
    }
    public void exportDeck(String fileName){
        File file = new File(fileName);
        int counter=0;
        try (PrintWriter writer = new PrintWriter(file)) {
            for(Card card:cards){
                writer.println(card.getFront());
                writer.println(card.getBack());
                writer.println(card.getWrongAnswers());
                counter++;
            }

        } catch (IOException e) {
            outputMsg("An exception occurs "+e.getMessage());
            return;
        }
        outputMsg(counter+(counter==1?" cards":" cards")+" have been saved.");
        return;
    }
    public void log(){

        outputMsg("File name:");
        String fileName=getUserInput();
        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(file)) {
            for(String log:logs){
                writer.println(log);
            }
        } catch (IOException e) {
            outputMsg("An exception occurs e.getMessage()");
            return;
        }
        outputMsg("The log has been saved.");
        return;
    }
    public void resetStats(){
        for(Card card:cards){
            card.resetWrongAnswers();
        }
        outputMsg("Card statistics has been reset.");
    }
}
public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        Deck deck=new Deck();
        for(int i=0;i<args.length;i++){
            if(args[i].equals("-import"))deck.importCardsFromFile(args[i+1]);
        }
        boolean app=true;
        String decision;
        while(app){
            deck.outputMsg("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            decision=deck.getUserInput();
            switch (decision){
                case "add":deck.addNewCardFromUser();break;
                case "remove": deck.removeCard();break;
                case "import": deck.importCardsFromFile();break;
                case "export": deck.exportDeck();break;
                case "ask": deck.startTest();break;
                case "exit": app=false;break;
                case "log": deck.log();break;
                case "hardest card": deck.hardestQuestion();break;
                case "reset stats": deck.resetStats();break;
            }

        }
        deck.outputMsg("Bye bye!");
        for(int i=0;i<args.length;i++){
            if(args[i].equals("-export"))deck.exportDeck(args[i+1]);
        }
    }
}