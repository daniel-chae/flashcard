import java.io.*;
import java.util.*;

public class Main {
    static ArrayList<String> log = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static TreeMap <String, String> flashcards = new TreeMap<>();
    static TreeMap <String, String> reverseFlashcards = new TreeMap<>();
    static TreeMap <String, Integer> hardestCard = new TreeMap<>();
    static String importFile = null;
    static String exportFile = null;

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++){
            switch (args[i]){
                case "-import":
                    importFile = args[i+1];
                    break;
                case "-export":
                    exportFile = args[i+1];
                    break;
                default:
                    break;
            }
        }
        if (importFile != null){
            initialImport();
        }
        printMenu();
    }

    static public void printMenu(){
        String menuInstruction = "Input the action(add, remove, import, export, ask, exit, log, hardest card, reset stats):";
        System.out.println(menuInstruction);
        log.add(menuInstruction);
        String userInput = scanner.nextLine();
        log.add(userInput);

        switch (userInput){
            case "add":
                addCard();
                break;
            case "remove":
                removeCard();
                break;
            case "import":
                importCards();
                break;
            case "export":
                exportCards();
                break;
            case "ask":
                askCards();
                break;
            case "exit":
                if (exportFile != null){
                    commandExport();
                } else {
                    System.out.println("Bye bye!");
                }
                break;
            case "log":
                generateLog();
                break;
            case "hardest card":
                hardestCard();
                break;
            case "reset stats":
                resetCard();
                break;
            default:
                String defaultInstruction1 = "There is no such a command. Try another command.";
                System.out.println(defaultInstruction1);
                log.add(defaultInstruction1);
                printMenu();
                break;
        }
    }

    //Method to add a new card into flashcard map
    static public void addCard(){
        String addInstruction1 = "The card:";
        System.out.println(addInstruction1);
        log.add(addInstruction1);
        String card = scanner.nextLine();
        log.add(card);
        if (flashcards.containsKey(card)){
            String addInstruction2 = "The card \"" + card + "\" already exists.";
            System.out.println(addInstruction2);
            log.add(addInstruction2);
        } else {
            String addInstruction3 = "The definition of the card:";
            System.out.println(addInstruction3);
            log.add(addInstruction3);
            String definition = scanner.nextLine();
            log.add(definition);
            if (flashcards.containsValue(definition)) {
                String addInstruction4 = "The definition \"" + definition + "\" already exists.";
                System.out.println(addInstruction4);
            } else {
                flashcards.put(card, definition);
                reverseFlashcards.put(definition, card);
                hardestCard.put(card, 0);
                String addInstruction5 = String.format("The pair (\"%s\":\"%s\") has been added.\n", card, definition);
                System.out.printf(addInstruction5);
                log.add(addInstruction5);
            }
        }
        printMenu();
    }

    //Method to remove a card from the flashcard map
    static public void removeCard(){
        String removeInstruction1 = "The card:";
        System.out.println(removeInstruction1);
        log.add(removeInstruction1);
        String cardToRemove = scanner.nextLine();
        log.add(cardToRemove);
        if (flashcards.containsKey(cardToRemove)){
            reverseFlashcards.remove(flashcards.get(cardToRemove));
            flashcards.remove(cardToRemove);
            hardestCard.remove(cardToRemove);
            String removeInstructio2 = "The card has been removed.";
            System.out.println(removeInstructio2);
            log.add(removeInstructio2);
        } else {
            String removeInstruction3 = "Can't remove \"" + cardToRemove + "\": there is no such card.";
            System.out.println(removeInstruction3);
            log.add(removeInstruction3);
        }
        printMenu();
    }

    //Method to import cards from a file.
    static public void importCards(){
        String importInstruction1 = "File name:";
        System.out.println(importInstruction1);
        log.add(importInstruction1);
        String fileName = scanner.nextLine();
        log.add(fileName);
        File file = new File("./" + fileName);
        String card;
        String definition;
        int error;
        int count = 0;
        try (Scanner readScanner = new Scanner(file)){
            while (readScanner.hasNext()){
                card = readScanner.nextLine();
                definition = readScanner.nextLine();
                error = Integer.parseInt(readScanner.nextLine());
                count ++;
                if (flashcards.containsKey(card)){
                    flashcards.replace(card, definition);
                    hardestCard.replace(card, error);
                } else {
                    flashcards.put(card, definition);
                    hardestCard.put(card, error);
                }
            }
            String importInstruction2 = count + " cards have been loaded.";
            System.out.println(importInstruction2);
            log.add(importInstruction2);
        } catch (FileNotFoundException e){
            String importInstruction3 = "File not found.";
            System.out.println(importInstruction3);
            log.add(importInstruction3);
        }
        printMenu();
    }
    //Method to export the flashcards.
    static public void exportCards(){
        String exportInstruction1 = "File name:";
        System.out.println(exportInstruction1);
        String filePath = scanner.nextLine();
        log.add(filePath);
        File file = new File("./" + filePath);
        int count = 0;
        try (PrintWriter printWriter = new PrintWriter(file)){
            for (Map.Entry<String, String> card: flashcards.entrySet()){
                printWriter.println(card.getKey());
                printWriter.println(card.getValue());
                printWriter.println(hardestCard.get(card.getKey()));
                count++;
            }
            String exportInstruction2 = count + " cards have been saved.";
            System.out.println(exportInstruction2);
            log.add(exportInstruction2);
        } catch (FileNotFoundException e){
            String exportInstruction3 = "Random";
            System.out.println(exportInstruction3);
        }
        printMenu();
    }
    //Method to ask cards.
    static public void askCards(){
        String askInstruction1 = "How many times to ask?";
        System.out.println(askInstruction1);
        log.add(askInstruction1);
        int timeToAsk = scanner.nextInt();
        log.add(String.valueOf(timeToAsk));
        scanner.nextLine();
        for (int i = 0; i < timeToAsk; i++){
            Random random = new Random();
            String randomKey = (String)flashcards.keySet().toArray()[random.nextInt(flashcards.size())];
            String askInstruction2 = "Print the definition of \"" + randomKey + "\":";
            System.out.println(askInstruction2);
            log.add(askInstruction2);
            String userInput = scanner.nextLine();
            log.add(userInput);
            if (flashcards.get(randomKey).equals(userInput)){
                String askInstruction3 = "Correct answer.";
                System.out.println(askInstruction3);
                log.add(askInstruction3);
            } else if (flashcards.containsValue(userInput)){
                String askInstruction4 = "Wrong answer. The correct one is \""
                        + flashcards.get(randomKey)
                        + "\", you've just written the definition of \""
                        + reverseFlashcards.get(userInput) + "\".";
                System.out.println(askInstruction4);
                log.add(askInstruction4);
                hardestCard.replace(randomKey, (hardestCard.get(randomKey) + 1));
            } else {
                String askInstruction5 = "Wrong answer. The correct one is \"" + flashcards.get(randomKey) + "\".";
                System.out.println(askInstruction5);
                log.add(askInstruction5);
                hardestCard.replace(randomKey, (hardestCard.get(randomKey) + 1));
            }
        }
        printMenu();
    }
    //Method to generate a log file.
    static public void generateLog(){
        String generateInstruction1 = "File name:";
        System.out.println(generateInstruction1);
        log.add(generateInstruction1);
        String fileName = scanner.nextLine();
        log.add(fileName);
        File logFile = new File("./" + fileName);
        try (PrintWriter printWriter = new PrintWriter(logFile)){
            for (String entry: log){
                printWriter.println(entry);
            }
            printWriter.flush();
            String generateInstruction2 = "The log has been saved.";
            System.out.println(generateInstruction2);
            log.add(generateInstruction2);
            printMenu();
        } catch (IOException e){
            String generateInstruction3 = "file not found";
            System.out.println(generateInstruction3);
            log.add(generateInstruction3);
        }
    }
    //Show the hardest card.
    static int max = 0;
    static public void hardestCard(){
        max = 0;
        for (Map.Entry<String, Integer> entry : hardestCard.entrySet()){
            if (entry.getValue() > max){
                max = entry.getValue();
            }
        }
        ArrayList<String> hardest = new ArrayList<>();

        if (max == 0){
            String hardestInstruction1 = "There are no cards with errors.";
            System.out.println(hardestInstruction1);
            log.add(hardestInstruction1);
        } else {
            for (Map.Entry<String, Integer> entry : hardestCard.entrySet()){
                if (entry.getValue() == max){
                    hardest.add("\"" + entry.getKey() + "\"");
                }
            }
            String hardestInstruction2 = "The hardest card is "
                    + String.join(",", hardest)
                    + ". You have " + max + " errors answering it.";
            System.out.println(hardestInstruction2);
            log.add(hardestInstruction2);
        }
        printMenu();
    }
    //Reset the hardest card count.
    static public void resetCard(){
        for (Map.Entry<String, Integer> entry : hardestCard.entrySet()){
            entry.setValue(0);
        }
        String resetInsturction1 = "Card statistics has been reset.";
        System.out.println(resetInsturction1);
        printMenu();
    }
    //Initial import
    static public void initialImport(){
        File file = new File("./" + importFile);
        String card;
        String definition;
        int error;
        int count = 0;
        try (Scanner readScanner = new Scanner(file)){
            while (readScanner.hasNext()){
                card = readScanner.nextLine();
                definition = readScanner.nextLine();
                error = Integer.parseInt(readScanner.nextLine());
                count ++;
                if (flashcards.containsKey(card)){
                    flashcards.replace(card, definition);
                    hardestCard.replace(card, error);
                } else {
                    flashcards.put(card, definition);
                    reverseFlashcards.put(definition, card);
                    hardestCard.put(card, error);
                }
            }
            String importInstruction2 = count + " cards have been loaded.";
            System.out.println(importInstruction2);
            log.add(importInstruction2);
        } catch (FileNotFoundException e){
            String importInstruction3 = "File not found.";
            System.out.println(importInstruction3);
            log.add(importInstruction3);
        }
    }

    static public void commandExport(){
        File file = new File("./" + exportFile);
        int count = 0;
        try (PrintWriter printWriter = new PrintWriter(file)){
            for (Map.Entry<String, String> card: flashcards.entrySet()){
                printWriter.println(card.getKey());
                printWriter.println(card.getValue());
                printWriter.println(hardestCard.get(card.getKey()));
                count++;
            }
            String exportInstruction2 = "Bye bye!\n" + count + " cards have been saved.";
            System.out.println(exportInstruction2);
        } catch (FileNotFoundException e){
            String exportInstruction3 = "Random";
            System.out.println(exportInstruction3);
        }
    }
}
