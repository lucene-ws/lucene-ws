// Sample application to create a Lucene index from a PostgreSQL database
//
// You need:
//    java
//    lucene (Debian: liblucene-java)
//    JDBC (Debian: libpgjava)
//
// CLASSPATH=$CLASSPATH:../build/WEB-INF/classes
// CLASSPATH=$CLASSPATH:/usr/share/java/lucene.jar
// CLASSPATH=$CLASSPATH:/usr/share/java/postgresql.jar
//
// It is important that postgres allows TCP/IP access.
//
// Search for TODO in this source code to find spots that you need
// to provide code/SQL in order for this script to run correctly.
//
import java.io.*;
import java.sql.*;
import java.util.regex.Pattern;
import java.lang.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import com.cdbaby.lucene.*;

public class CreateIndex 
{
    public static void printUsage()
    {
        System.out.println("Usage: CreateIndex <analyzer> <index dir> <index name>");
        System.out.println("");
        System.out.println("<analyzer> must be one of:");
        System.out.println("  SimpleAnalyzer -- Lowercaseing only");
        System.out.println("  StopAnalyzer -- Stop word filtering and lowercasing.");
        System.out.println("  StandardAnalyzer -- Stop word filtering, lowercaseing and some " +
                           "punctionation removal");
        System.out.println("  WithStopAnalyzer -- Just like StandardAnalyzer, but stop indexs stop words too");
        System.out.println("\nFor more information on analyzers, check out:");
        System.out.println("  http://www.darksleep.com/lucene/");
    }

    public static void main(String[] args) 
    {
        if (args.length < 3)
        {
            printUsage();
            return;
        }
        String analyzer = args[0];
        if (!analyzer.equals("SimpleAnalyzer") && !analyzer.equals("StopAnalyzer") && 
            !analyzer.equals("StandardAnalyzer") && !analyzer.equals("WithStopAnalyzer"))
        {
            System.out.println("Unknown analyzer " + analyzer + "\n");
            printUsage();
            return;
        }
        try 
        {
            LuceneIndex index = new LuceneIndex(args[2], args[1], 1, 1, 30, 60);
            if (index == null)
            {
                System.out.println("Cannot create LuceneIndex object");
                return;
            }
            IndexWriter writer = index.createIndex(args[0]);
            if (writer == null)
            {
                System.out.println("Cannot create lucene index: " + index.getError());
                return;
            }

            // Adjust this factor according to the data you are indexing. For an excellent disucssion
            // of this parameter, check this page:  
            //     http://www.onjava.com/pub/a/onjava/2003/03/05/lucene.html?page=1
            writer.mergeFactor = 500;
            indexTracks(writer);

            writer.optimize();
            writer.close();
            index.closeIndex();
        }
        catch ( IOException e ) 
        {
            System.out.println("IO-Exception: " + e.getMessage());
        }
    }

    // The problem here is that postgres' JDBC implementation fetches
    // the *whole* result set and tries to store it in a Vector.
    // For our huge result set, that doesn't work of course, so I'm
    // querying the database in smaller chunks.
    public static void indexTracks(IndexWriter writer) 
    {

        try 
        {
            Class.forName("org.postgresql.Driver");

            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql:musicbrainz_db", "musicbrainz_user", "");

            Statement st = connection.createStatement();
            // TODO: The following query determines the number of rows to process.
            // (e.g. SELECT MAX(id) FROM artist
            ResultSet rset = st.executeQuery("SELECT MAX(id) FROM artist");

            int min = 0;
            int max;
            int step = 10000;

            if ( rset.next() )
                max = rset.getInt(1);
            else
                max = min;
            max = 100000;
            int lower = min;
            int upper = lower+step-1;
            while ( lower <= max ) {

                System.out.print("Index " + lower + " to " + upper + ": ");

                // TODO: Write a query to pull the data to be indexed from the database
                // Make sure that the last two arguments are use a BETWEEN clause to
                // pull back only a slice of data at a time.
                PreparedStatement pst = connection.prepareStatement(
                        "SELECT ar.gid, ar.name "
                        + "FROM artist ar "
                        + "WHERE ar.id BETWEEN ? AND ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY
                        );
                pst.setInt(1, lower);
                pst.setInt(2, upper);

                ResultSet rset2 = pst.executeQuery();

                int i = 1;
                while ( rset2.next() ) 
                {
                    //TODO: Pass the columns of data to the makeDocument function
                    writer.addDocument( makeDocument(
                                rset2.getString(1), rset2.getString(2)));
                }

                System.out.println(" Done. (" + (100 * upper) / max +
                        "% complete)");

                lower = upper+1;
                upper = lower+step-1;
            }
        }
        catch ( ClassNotFoundException e ) 
        {
            System.out.println("JDBC driver not found: " + e.getMessage());
        }
        catch ( SQLException e ) 
        {
            System.out.println("Query failed: " + e.getMessage());
        }
        catch ( IOException e ) 
        {
            System.out.println("Error writing index: " + e.getMessage());
        }
    }

    // TODO: Create a Document that describes a row of data and return it
    public static Document makeDocument(String artist_id, String artist)
    {
        Document doc = new Document();

        doc.add(Field.Text("artistId", artist_id));
        doc.add(Field.Text("artist", artist));

        return doc;
    }
}
