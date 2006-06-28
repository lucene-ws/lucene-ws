package net.lucenews;

public class LuceneKeys {
    
    /**
     * Analysis
     */
    
    public static final int    ANALYZER = 200;
    public static final String ANALYSIS_NAMESPACE = "org.apache.lucene.analysis";
    
    
    
    /**
     * Index
     */
    
    public static final int INDEX_NAME    = 500;
    public static final int MIN_SEARCHERS = 501;
    public static final int MAX_SEARCHERS = 502;
    
    
    
    /**
     * Index Manager
     */
    
    public static final int INDICES_DIRECTORIES       = 600;
    public static final int STALE                     = 601;
    public static final int AUTO_STALE                = 602;
    public static final int STALE_PERIOD              = 603;
    public static final int CREATED_INDICES_DIRECTORY = 604;
    
    
    
    /**
     * Search
     */
    
    public static final int SEARCH_STRING    = 100;
    public static final int SORT             = 101;
    public static final int MAXIMUM_HITS     = 102;
    public static final int DEFAULT_FIELD    = 103;
    public static final int DEFAULT_OPERATOR = 104;
    public static final int FILTER           = 105;
    
    
    
    /**
     * Miscellaneous
     */
    
    public static final int LOCALE = 300;
    public static final int LIMIT  = 301;
    public static final int OFFSET = 302;
    
    
    
    /**
     * Paging
     */
    
    public static final int PAGE             = 400;
    public static final int CURRENT_PAGE     = 400;
    public static final int ENTRIES_PER_PAGE = 401;
    public static final int TOTAL_ENTRIES    = 402;
    
    
    public static final int DATE  = 701;
    public static final int TITLE = 702;
    public static final int DESCRIPTION = 703;
    
}
