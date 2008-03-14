package net.lucenews.model;
import java.util.Comparator;
import java.util.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

/**
 * Sorts terms by their boost. If two terms have equal boost, their
 * length of the shared base is compared to the original term. That is,
 * the more consecutive chars equal to the original term, the higher the rank.
 */
public class FuzzyTermScoreComparator
    implements Comparator
{
    private Term term;
    private String t, t1, t2;
    
    public FuzzyTermScoreComparator(Term term)
    {
        this.term = term;
    }
    
    public int compare(Object o1, Object o2)
    {
        // compare boosts
        float f1 = ((TermQuery) o1).getBoost();
        float f2 = ((TermQuery) o2).getBoost();
        if (f1 == f2) {
            // equal score, now compare distance
            t1 = ((TermQuery) o1).getTerm().text();
            t2 = ((TermQuery) o2).getTerm().text();
            t = term.text();
            // double extra safe check
            if (t == null || t1 == null || t2 == null) {
                return 0;
            }
            for (int i = 0; i < t.length(); i++) {
                if (i == t1.length() || i == t2.length()) {
                    // out of bounds in either term
                    return 0;
                }
                if (t1.charAt(i) == t2.charAt(i)) {
                    // equal so far, continue
                    continue;
                }
                // now the terms have different chars
                if (t1.charAt(i) == t.charAt(i)) {
                    return -1;
                } else if (t2.charAt(i) == t.charAt(i)) {
                    return 1;
                }
                // both terms had different char than the original term...try next
            }
            return 0;
        }
        return f1 < f2 ? 1 : -1;
    }
}
