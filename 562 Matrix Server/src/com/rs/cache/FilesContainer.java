package com.rs.cache;

public final class FilesContainer extends Container {
	
	private int[] filesIndexes;
	private Container[] files;
	
	public FilesContainer() {
	
	}

	public void setFiles(Container[] containers) {
		this.files = containers;
	}

	public Container[] getFiles() {
		return files;
	}

	public void setFilesIndexes(int[] containersIndexes) {
		this.filesIndexes = containersIndexes;
	}

	public int[] getFilesIndexes() {
		return filesIndexes;
	}
	
}
