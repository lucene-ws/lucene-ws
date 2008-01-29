package net.lucenews.opensearch;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OpenSearchQueryTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    /**
     * This test verifies that inputs that should return null do indeed return null 
     */
    public void testGetBoundingIndicesFailures() {
        OpenSearchQuery q = new OpenSearchQuery();
        
        //Both totalresults and count needs to bet set in order for the
        // bounding indices to be set
        
        Integer[] indices = q.getBoundingIndices();
        assertNotNull("Method should not return null", indices);
        assertNull("First bounding index should be null", indices[0]);
        assertNull("Second bounding index should be null", indices[1]);
        
        q = new OpenSearchQuery();
        q.setTotalResults(10);
        q.setCount(null);
        indices = q.getBoundingIndices();
        assertNotNull("Method should not return null", indices);
        assertNull("First bounding index should be null", indices[0]);
        assertNull("Second bounding index should be null", indices[1]);
        
        q = new OpenSearchQuery();
        q.setCount(10);
        q.setTotalResults(null);
        assertNotNull("Method should not return null", indices);
        assertNull("First bounding index should be null", indices[0]);
        assertNull("Second bounding index should be null", indices[1]);
        
        q = new OpenSearchQuery();
        q.setCount(0);
        q.setTotalResults(0);
        assertNotNull("Method should not return null", indices);
        assertNull("First bounding index should be null", indices[0]);
        assertNull("Second bounding index should be null", indices[1]);
        
        
    }
    

    /**
     * This method checks inputs that produce boundaries
     */
    @Test
    public void testGetBoundingIndicesSuccess() {
        
        
        // TotalResults and Count are the same
        OpenSearchQuery q = new OpenSearchQuery();
        q.setTotalResults(10);
        q.setCount(10);
        
        Integer[] indices = q.getBoundingIndices();
        assertEquals(1, indices[0]);
        assertEquals(10, indices[1]);
        
        /**
         *  TotalResult and Count are different
         */
        q = new OpenSearchQuery();
        q.setTotalResults(10);
        q.setCount(2);
        
        indices = q.getBoundingIndices();
        assertEquals(1, indices[0]);
        assertEquals(2, indices[1]);//totalresult has no real effect on the indices
        
        /**
         * startIndex w/totalresults < startIndex
         */
        Integer count = 10;
        Integer startIndex = 11;
        q = new OpenSearchQuery();
        q.setTotalResults(10);
        q.setCount(count);
        q.setStartIndex(startIndex);
        
        indices = q.getBoundingIndices();
        assertNull(indices[0]); // totalResults must be larger than the startIndex
        assertNull(indices[1]);
        
        /**
         * startIndex
         */
        count = 10;
         startIndex = 11;
        q = new OpenSearchQuery();
        q.setTotalResults(100);
        q.setCount(count);
        q.setStartIndex(startIndex);
        
        indices = q.getBoundingIndices();
        assertEquals(startIndex, indices[0]);
        assertEquals( (startIndex + count) - 1, indices[1]);
        
        
        /**
         * startPage
         */
        Integer startPage = 3;
        count = 10; 
        q = new OpenSearchQuery();
        q.setTotalResults(100);
        q.setStartPage(startPage);
        q.setCount(count);
        
        indices = q.getBoundingIndices();
        // Page 3 with pagesize 10 means we should get the 21st result
        assertEquals(1 + count * (startPage -1) , indices[0]);
        // the last result should be 30
        assertEquals(count * startPage, indices[1]);
        
        /**
         * startIndex and startPage
         * If both parameters are specified, only startIndex should be used
         * 
         */
        count = 10;
        startIndex = 11;
        startPage = 5;
        
        q = new OpenSearchQuery();
        q.setTotalResults(100);
        q.setCount(count);
        q.setStartIndex(startIndex);
        q.setStartPage(startPage);
        
        indices = q.getBoundingIndices();
        assertEquals(startIndex, indices[0]);
        assertEquals(startIndex + count - 1, indices[1]); // startIndex is used
        
        
        
        
    }

}
