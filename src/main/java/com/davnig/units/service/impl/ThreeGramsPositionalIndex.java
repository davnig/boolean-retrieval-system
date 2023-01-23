package com.davnig.units.service.impl;

import com.davnig.units.model.PositionalTerm;
import com.davnig.units.service.KGramIndex;
import com.davnig.units.service.PositionalIndex;

import java.util.*;

import static com.davnig.units.util.StringUtils.addLeadingAndTrailingDollarSymbol;
import static com.davnig.units.util.StringUtils.extractThreeGrams;

/**
 * An implementation of both a three-grams and a positional index.
 */
public class ThreeGramsPositionalIndex implements PositionalIndex, KGramIndex<PositionalTerm> {

    private final PositionalIndex positionalIndex;
    private final Map<String, Set<PositionalTerm>> threeGramsIndex;

    public ThreeGramsPositionalIndex() {
        positionalIndex = new BSTPositionalIndex();
        threeGramsIndex = new Hashtable<>();
    }

    @Override
    public Set<PositionalTerm> getTermsByGram(String gram) {
        return threeGramsIndex.get(gram);
    }

    @Override
    public void addTerm(PositionalTerm term) {
        positionalIndex.addTerm(term);
        extractAndAddThreeGrams(term);
    }

    @Override
    public boolean existsTermByWord(String word) {
        return positionalIndex.existsTermByWord(word);
    }

    @Override
    public void addGramWithTerm(String gram, PositionalTerm term) {
        if (existsGramInIndex(gram)) {
            Set<PositionalTerm> termSet = threeGramsIndex.get(gram);
            if (termSet == null) {
                termSet = new HashSet<>();
            } else termSet.remove(term); // the removal method uses equal to check existence, so it just compares docIDs
            termSet.add(term);
            return;
        }
        Set<PositionalTerm> termList = new HashSet<>();
        termList.add(term);
        threeGramsIndex.put(gram, termList);
    }

    @Override
    public Set<Integer> getAllDocIDs() {
        return positionalIndex.getAllDocIDs();
    }

    @Override
    public Iterator<PositionalTerm> invertedIndexIterator() {
        return positionalIndex.invertedIndexIterator();
    }

    @Override
    public Optional<PositionalTerm> findTermByWord(String word) {
        return positionalIndex.findTermByWord(word);
    }

    @Override
    public Optional<PositionalTerm> addTermAndOccurrence(String word, int docID, int position) {
        return positionalIndex.addTermAndOccurrence(word, docID, position);
    }

    @Override
    public Optional<PositionalTerm> addTermAndOccurrences(String word, int docID, int... positions) {
        return positionalIndex.addTermAndOccurrences(word, docID, positions);
    }

    @Override
    public void addOccurrence(PositionalTerm term, int docID, int position) {
        positionalIndex.addOccurrence(term, docID, position);
    }

    @Override
    public void addOccurrencesInDoc(PositionalTerm term, int docID, int[] positions) {
        positionalIndex.addOccurrencesInDoc(term, docID, positions);
    }

    public void addTermOccurrenceAndGramsForEachToken(int docID, String[] tokens) {
        for (int position = 0; position < tokens.length; position++) {
            String token = tokens[position];
            if (isTokenNotInBlackList(token)) {
                addTermOccurrenceAndGrams(token, docID, position);
            }
        }
    }

    public void addTermOccurrencesAndGrams(String word, int docID, int... positions) {
        Optional<PositionalTerm> termOpt = positionalIndex.addTermAndOccurrences(word, docID, positions);
        termOpt.ifPresent(this::extractAndAddThreeGrams);
    }

    public void addTermOccurrenceAndGrams(String word, int docID, int position) {
        Optional<PositionalTerm> termOpt = positionalIndex.addTermAndOccurrence(word, docID, position);
        termOpt.ifPresent(this::extractAndAddThreeGrams);
    }

    public void extractAndAddThreeGrams(PositionalTerm term) {
        String extendedWord = addLeadingAndTrailingDollarSymbol(term.getWord());
        extractThreeGrams(extendedWord).forEach(gram -> addGramWithTerm(gram, term));
    }

    public boolean existsGramInIndex(String gram) {
        return threeGramsIndex.containsKey(gram);
    }

    public int size() {
        return threeGramsIndex.size();
    }

    private boolean isTokenNotInBlackList(String token) {
        String[] blackList = new String[]{"", "-"};
        return Arrays.stream(blackList).noneMatch(el -> el.equals(token) || token.startsWith("--"));
    }
}
