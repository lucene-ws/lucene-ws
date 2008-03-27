package net.lucenews3.model.servlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ResourceResolverImpl<R> implements ResourceResolver {

	private Map<PathClass, R> resourcesByPathClass;
	
	/**
	 * Determines which resources are valid by path.
	 * 
	 * @return
	 */
	public Map<Path, R> getResourcesByPath(Iterable<String> tokens) {
		final Map<Path, R> resourcesByPath = new HashMap<Path, R>();
		
		for (Entry<PathClass, R> entry : resourcesByPathClass.entrySet()) {
			final PathClass pathClass = entry.getKey();
			final R resource = entry.getValue();
			
			final Path path;
			
			try {
				path = pathClass.parse(tokens);
			} catch (PathParseException e) {
				continue;
			}
			
			resourcesByPath.put(path, resource);
		}
		
		return resourcesByPath;
	}
	
	@Override
	public QualifiedResource<R> resolveResource(Iterable<String> tokens) throws PathResolutionException {
		final QualifiedResource<R> result;
		
		final Map<Path, R> resourcesByPath = getResourcesByPath(tokens);
		
		switch (resourcesByPath.size()) {
		case 0:
			throw new PathResolutionException("No valid path classes for the tokens");
		case 1:
			Entry<Path, R> entry = resourcesByPath.entrySet().iterator().next();
			result = new QualifiedResourceImpl<R>(entry.getKey(), entry.getValue());
			break;
		default:
			throw new PathResolutionException("Multiple valid path classes for the tokens");
		}
		
		return result;
	}

}
