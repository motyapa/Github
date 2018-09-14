package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
    
    private IDictionary<URI, Double> documentScores;

    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.documentScores = this.computeDocScore(documentTfIdfVectors);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.
    
    // helper method to calculate the score for each uri.
    private IDictionary<URI, Double> computeDocScore(IDictionary<URI, IDictionary<String, Double>> documentVectors){
        IDictionary<URI, Double> score = new ChainedHashDictionary<URI, Double>();
        
        for (KVPair<URI, IDictionary<String, Double>> pageVector : documentVectors) {
            double output = 0.0;
            for (KVPair<String, Double> vector : pageVector.getValue()) {
                output += vector.getValue() * vector.getValue();
            }
            score.put(pageVector.getKey(), Math.sqrt(output));
        }
        return score;
    }

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Integer> temp = new ChainedHashDictionary<String, Integer>();
        IDictionary<String, Double> idf = new ChainedHashDictionary<String, Double>();
        
        for (Webpage page : pages) {
            ISet<String> allwords = new ChainedHashSet<String>();
            for (String word : page.getWords()) {
                allwords.add(word);
            }
            for (String word: allwords) {
                if (!temp.containsKey(word)) {
                    temp.put(word, 1);
                } else {
                    temp.put(word, temp.get(word) + 1);
                }
            }
        }
        
        for (KVPair<String, Integer> pair : temp) {
           idf.put(pair.getKey(), (Math.log((double) pages.size() / pair.getValue())));
        }
        return idf;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Integer> tempdic = new ChainedHashDictionary<String, Integer>();
        ISet<String> allwords = new ChainedHashSet<String>();
        IDictionary<String, Double> dic = new ChainedHashDictionary<String, Double>();
        
        for (String word : words) {
            if (!tempdic.containsKey(word)) {
                tempdic.put(word, 1);
                allwords.add(word);
            } else {
                tempdic.put(word, tempdic.get(word) + 1);
            }
        }
        
        for (String unique : allwords) {
            dic.put(unique, ((double) tempdic.get(unique)/words.size()));
        }
        return dic;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> tfidf = 
                new ChainedHashDictionary<URI, IDictionary<String, Double>>();
        
        for (Webpage page : pages) {
            URI uri = page.getUri();
            IDictionary<String, Double> tf = computeTfScores(page.getWords());
            IDictionary<String, Double> data = new ChainedHashDictionary<String, Double>();
            for (KVPair<String, Double> pair : tf) {
                Double score = pair.getValue() * idfScores.get(pair.getKey());
                data.put(pair.getKey(), score);
            }
            tfidf.put(uri, data);
        }
        return tfidf;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        
        IDictionary<String, Double> queryTf = computeTfScores(query);
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<String, Double>();
        IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
        for (String queryWord : query) {
           if (!idfScores.containsKey(queryWord)) {
               queryVector.put(queryWord, (double) 0);
           } else {
               queryVector.put(queryWord, queryTf.get(queryWord) * idfScores.get(queryWord));
           }
        }
        
        double numerator = 0.0; 
        double output = 0.0;
        double relevance = 0.0;
        for (KVPair<String, Double> words : queryVector) {
            String word = words.getKey();
            if (documentVector.containsKey(word)) {
                numerator += documentVector.get(word) * queryVector.get(word);
            }
            output += queryVector.get(word) * queryVector.get(word);
        }
        double denominator = documentScores.get(pageUri) * Math.sqrt(output);
        
        if (denominator != 0) {
            relevance = numerator / denominator;
        } else {
            relevance = 0.0;
        }
        
        return relevance;
    }
}
