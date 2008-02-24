package net.lucenews3.model;

import net.lucenews3.Transformer;

public class ResultToOpenSearchResultTransformer implements Transformer<Result, net.lucenews3.opensearch.Result> {

	@Override
	public net.lucenews3.opensearch.Result transform(Result result) {
		final net.lucenews3.opensearch.Result openSearchResult = new net.lucenews3.opensearch.ResultImpl();
		transform(result, openSearchResult);
		return openSearchResult;
	}

	@Override
	public void transform(Result result, net.lucenews3.opensearch.Result openSearchResult) {
		final Document document = result.getDocument();
		final FieldList fields = document.getFields();
		
		String stave = fields.byName("stave").first().stringValue();
		String paragraph = fields.byName("paragraph").first().stringValue();
		
		openSearchResult.setRelevance(new Double(result.getScore()));
		openSearchResult.setTitle("Stave " + stave + ", paragraph " + paragraph);
		final net.lucenews3.atom.InlineXhtmlContent content = new net.lucenews3.atom.InlineXhtmlContentImpl();
		content.getContentNodes().add(new FieldListToXoxoTransformer().transform(fields));
		openSearchResult.setContent(content);
	}

}
