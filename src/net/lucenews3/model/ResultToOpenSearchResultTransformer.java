package net.lucenews3.model;

import net.lucenews3.Transformer;

public class ResultToOpenSearchResultTransformer implements Transformer<Result, net.lucenews3.opensearch.Result> {

	@Override
	public net.lucenews3.opensearch.Result transform(Result result) {
		net.lucenews3.opensearch.Result openSearchResult = new net.lucenews3.opensearch.ResultImpl();
		transform(result, openSearchResult);
		return openSearchResult;
	}

	@Override
	public void transform(Result result, net.lucenews3.opensearch.Result openSearchResult) {
		Document document = result.getDocument();
		FieldList fields = document.getFields();
		
		//String stave = fields.byName("stave").first().stringValue();
		//String paragraph = fields.byName("paragraph").first().stringValue();
		
		openSearchResult.setRelevance(new Double(result.getScore()));
		openSearchResult.setTitle(fields.first().stringValue());
		net.lucenews3.atom.InlineXhtmlContent content = new net.lucenews3.atom.InlineXhtmlContentImpl();
		content.getContentNodes().add(new FieldListToXoxoTransformer().transform(fields));
		openSearchResult.setContent(content);
	}

}
