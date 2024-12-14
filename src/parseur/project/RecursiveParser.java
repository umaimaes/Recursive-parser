package parseur.project;

import java.util.*;
  
public class RecursiveParser {
    private List<String> tokens;
    private int currentIndex = 0;

    private static final Set<String> ARTICLES = Set.of("le", "la", "les", "une", "un", "des","mon","mes","ton");
    private static final Set<String> PRONOUNS = Set.of("je", "tu", "il", "elle", "nous", "vous", "ils", "elles");
    private static final Set<String> VERBS = Set.of("mange", "mangent", "voit", "voient", "prend", "prennent", "lit", "lisent", "fais", "fait", "a mangé");
    private static final Set<String> NOUNS = Set.of("souris", "fromage", "livre", "chat", "chien", "voiture", "enfant", "maison", "projet");
    private static final Set<String> TIME_ADVERBS = Set.of("hier", "aujourd'hui", "demain", "le matin", "la matinée", "tous les jours", "à 6 heures");

    public RecursiveParser(String sentence) {
        // Modified to keep commas as separate tokens
        tokens = Arrays.asList(sentence.toLowerCase().split("\\s*([,])\\s*|\\s+"));
    }

    public boolean parse() {
        currentIndex = 0;  // Reset index at start of parsing
        return parsePhrase() && currentIndex == tokens.size();
    }

    private boolean parsePhrase() {
        int savedIndex = currentIndex;
        
        // Try all possible phrase structures
        if (parseTimeFirst()) return true;
        currentIndex = savedIndex;
        if (parseTimeMiddle()) return true;
        currentIndex = savedIndex;
        if (parseTimeLast()) return true;
        
        return false;
    }
    
    
    private boolean parseTimeFirst() {
        return parseTimeComplement() && parseComma() && parseSubjectVerbComplement();
    }

    private boolean parseTimeMiddle() {
        return (parseSubject()||parsePronoun()) && parseVerb() && parseTimeComplement() && parseComplement();
    }

    private boolean parseTimeLast() {
        return (parseSubject() || parsePronoun()) && parseVerb() && parseComplement() && 
               (currentIndex == tokens.size() || parseTimeComplement());
    }

    private boolean parseSubjectVerbComplement() {
        return (parseSubject() || parsePronoun()) && parseVerb() && parseComplement();
    }

    private boolean parseSubject() {
        return parseArticle() && parseNoun();
    }

    private boolean parsePronoun() {
        if (currentIndex < tokens.size() && PRONOUNS.contains(tokens.get(currentIndex))) {
            currentIndex++;
            return true;
        }
        return false;
    }

    private boolean parseComplement() {
        return parseArticle() && parseNoun();
    }

    private boolean parseTimeComplement() {
        if (currentIndex < tokens.size() && TIME_ADVERBS.contains(tokens.get(currentIndex))) {
            currentIndex++;
            return true;
        }
        return false;
    }
    
    private boolean parseComma() {
        if (currentIndex < tokens.size() && tokens.get(currentIndex).equals(",")) {
            currentIndex++;
            return true;
        }
        return true;  // Optional, so return true even if no comma is found
    }

    private boolean parseArticle() {
        if (currentIndex < tokens.size() && ARTICLES.contains(tokens.get(currentIndex))) {
            currentIndex++;
            return true;
        }
        return false;
    }

    private boolean parseVerb() {
        if (currentIndex < tokens.size() && VERBS.contains(tokens.get(currentIndex))) {
            currentIndex++;
            return true;
        }
        return false;
    }

    private boolean parseNoun() {
        if (currentIndex < tokens.size() && NOUNS.contains(tokens.get(currentIndex))) {
            currentIndex++;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        List<String> testSentences = Arrays.asList(
            "je fais le projet aujourd'hui",
            "le chat mange le fromage",
            "un enfant lit un livre",
            "nous voyons la voiture",
            "elle prend une maison",
            "je fait le projet",
            "le chat mange hier le fromage",
            "je mange le fromage hier",
            "hier je mange le fromage",
            "aujourd'hui, je fais le projet"
        );

        for (String sentence : testSentences) {
            RecursiveParser parser = new RecursiveParser(sentence);
            if (parser.parse()) {
                System.out.println("Phrase valide : " + sentence);
            } else {
                System.out.println("Phrase invalide : " + sentence);
            }
        }
    }
}