package net.lucenews3.model;

import java.util.ListIterator;

import org.apache.lucene.index.Term;

public interface TermListIterator extends TermIterator, ListIterator<Term> {

}
