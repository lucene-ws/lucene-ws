package net.lucenews3.atom;

public class ServiceImpl extends CommonImpl implements Service {

	private WorkspaceList workspaces;

	public ServiceImpl() {
		this(new WorkspaceListImpl());
	}
	
	public ServiceImpl(WorkspaceList workspaces) {
		this.workspaces = workspaces;
	}
	
	public WorkspaceList getWorkspaces() {
		return workspaces;
	}

	public void setWorkspaces(WorkspaceList workspaces) {
		this.workspaces = workspaces;
	}
	
}
