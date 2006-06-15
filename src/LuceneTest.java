import java.io.*;
import net.lucenews.model.*;
import org.apache.log4j.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import org.apache.lucene.wordnet.*;

class LuceneTest {
    
    public static void main (String[] args) throws Exception {
        PropertyConfigurator.configureAndWatch("C:/dev/LuceneWebService/log4j.conf");
        
        IndexSearcher searcher = new IndexSearcher("c:/dev/indices/research");
        
        Analyzer analyzer = new StandardAnalyzer();
        
        
        QueryParser normalParser = new QueryParser( "all", analyzer );
        LuceneQueryParser expandedParser = new LuceneQueryParser( "all", analyzer );
        expandedParser.setSearcher(new IndexSearcher("c:/dev/indices/wordnet/"));
        
        String search = "foo";
        if (args.length > 0) {
            search = args[0];
        }
        Logger.getRootLogger().debug("TESTING");
        
        
        Query normalQuery = normalParser.parse(search);
        Hits  normal      = searcher.search( normalQuery );
        
        Query expandedQuery = expandedParser.parse(search);
        Hits  expanded      = searcher.search( expandedQuery );
        
        System.out.println("SEARCHING FOR '" + search + "'");
        System.out.println(normal.length() + " hits using standard analyzer");
        System.out.println(expanded.length() + " hits using expanded standard analyzer");
        System.out.println("Normal query: " + normalQuery);
        System.out.println("Expanded query: " + expandedQuery);
    }
    
}
