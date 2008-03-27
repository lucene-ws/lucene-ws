package net.lucenews3.model.servlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ResourceResolverImpl<R> implements ResourceResolver {

	private Map<PathClass, Object> resourcesByPathClass;
	
	/**
	 * Determines which resources are valid by path.
	 * 
	 * @return
	 */
	public Map<Path, Object> getResourcesByPath(Iterable<String> tokens) {
		final Map<Path, Object> resourcesByPath = new HashMap<Path, Object>();
		
		for (Entry<PathClass, Object> entry : resourcesByPathClass.entrySet()) {
			final PathClass pathClass = entry.getKey();
			final Object resource = entry.getValue();
			
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
	public PathEntry<R> resolveResource(Iterable<String> tokens) throws PathResolutionException {
		final PathEntry<R> result;
		
		final Map<Path, Object> resourcesByPath = getResourcesByPath(tokens);
		
		switch (resourcesByPath.size()) {
		case 0:
			throw new PathResolutionException("No valid path classes for the tokens");
		case 1:
			result = null;
			// TODO
			break;
		default:
			throw new PathResolutionException("Multiple valid path classes for the tokens");
		}
		
		return result;
	}

}
