package com.rs.cache;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.rs.Settings;
import com.rs.io.OutputStream;



public final class Cache {

	private static CacheFileManager[] cacheFileManagers;
	private static CacheFile containersInformCacheFile;
	
	private Cache() {
		
	}
	public static final void init() throws IOException {
		byte[] cacheFileBuffer = new byte[520];
		RandomAccessFile containersInformFile = new RandomAccessFile(Settings.CACHE_PATH + "/main_file_cache.idx255", "r");
		RandomAccessFile dataFile =	new RandomAccessFile(Settings.CACHE_PATH + "/main_file_cache.dat2", "r");
		containersInformCacheFile = new CacheFile(255, containersInformFile, dataFile, 500000, cacheFileBuffer);
		int length = (int) (containersInformFile.length() / 6);
		cacheFileManagers = new CacheFileManager[length];
		for(int i = 0; i < length; i++) {
			File f = new File(Settings.CACHE_PATH + "/main_file_cache.idx" + i);
			if(f.exists() && f.length() > 0) {
				cacheFileManagers[i] = new CacheFileManager(new CacheFile(i, new RandomAccessFile(f, "r"), dataFile, 1000000, cacheFileBuffer), true);
				if(cacheFileManagers[i].getInformation() == null)
					cacheFileManagers[i] = null;
			}
		}
		
	}
	
	public static final byte[] generateUkeysContainer() {
		OutputStream stream = new OutputStream(cacheFileManagers.length * 8);
		for(int index = 0; index < cacheFileManagers.length; index++) {
			if(cacheFileManagers[index] == null) {
				stream.writeInt(0);
				stream.writeInt(0);
				continue;
			}
			stream.writeInt(cacheFileManagers[index].getInformation().getInformationContainer().getCrc());
			stream.writeInt(cacheFileManagers[index].getInformation().getRevision());
		}
		byte[] ukeysContainer = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(ukeysContainer, 0, ukeysContainer.length);
		return ukeysContainer;
	}
	
	public static final CacheFileManager[] getCacheFileManagers() {
		return cacheFileManagers;
	}
	
	public static final CacheFile getConstainersInformCacheFile() {
		return containersInformCacheFile;
	}
	
}
