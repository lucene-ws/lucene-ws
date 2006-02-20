package net.lucenews.model.event;

import net.lucenews.model.*;

public class LuceneIndexEvent
{
	private LuceneIndex m_index;
	
	public LuceneIndexEvent (LuceneIndex index)
	{
		m_index = index;
	}
	
	public LuceneIndex getIndex ()
	{
		return m_index;
	}
}
