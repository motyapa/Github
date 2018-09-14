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
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);
        

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);


        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }
    

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
   
    
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> webgraph = new ChainedHashDictionary<URI, ISet<URI>>();
        IDictionary<URI, IList<URI>> webInfo =  new ChainedHashDictionary<URI, IList<URI>>();
        
        for (Webpage webpage: webpages) {
        		if (!webInfo.containsKey(webpage.getUri())) {
        			webInfo.put(webpage.getUri(), webpage.getLinks());
        		}
        }
        
        for (KVPair<URI, IList<URI>> pair: webInfo) {
            IList<URI> links = pair.getValue();
            ISet<URI> linkTo = new ChainedHashSet<URI>();
            
            for (URI link : links) {
                if (!link.equals(pair.getKey()) && webInfo.containsKey(link)) {
                    linkTo.add(link);
                }
            }
            
            webgraph.put(pair.getKey(), linkTo);
        }
        
        return webgraph;  
    }
    
    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     *                 
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {

		IDictionary<URI, Double> oldRanks = new ChainedHashDictionary<URI, Double>();
		IDictionary<URI, Double> newRanks = new ChainedHashDictionary<URI, Double>();
		ISet<URI> allLinks = new ChainedHashSet<URI>();
		
		double initRank = 1.0 / graph.size();
		
		for (KVPair<URI, ISet<URI>> links: graph) {
			oldRanks.put(links.getKey(), initRank); 
			newRanks.put(links.getKey(), 0.0);
			allLinks.add(links.getKey());
		}
		
		for (int i = 0; i < limit; i++) {
			
			for (KVPair<URI, ISet<URI>> links: graph) {
				
				if (links.getValue().size() > 0) {			
					for (URI url: links.getValue()) {
						newRanks.put(url, newRanks.get(url) + 
						        oldRanks.get(links.getKey()) * decay / links.getValue().size());
					}				
				} else {
					for (URI url : allLinks) {
						newRanks.put(url, newRanks.get(url) + 
						        decay * oldRanks.get(links.getKey()) / graph.size());
					}
				}
			}
			
			for (URI url : allLinks) {
				newRanks.put(url, newRanks.get(url) + (1 - decay) / graph.size());
			}
			
			boolean flag = true;
			
			for (KVPair<URI, ISet<URI>> links: graph) {
				if (Math.abs(newRanks.get(links.getKey()) - oldRanks.get(links.getKey())) 
				        > epsilon) {				
					flag = false;
				}
				oldRanks.put(links.getKey(), newRanks.get(links.getKey()));
				newRanks.put(links.getKey(), 0.0);
			}
			
			if (flag) {
				return oldRanks;
			}
		}
		
		return oldRanks;          
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        return pageRanks.get(pageUri);
    }
}
