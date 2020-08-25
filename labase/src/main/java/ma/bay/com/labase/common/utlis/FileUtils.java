package ma.bay.com.labase.common.utlis;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class FileUtils {

	private static AtomicInteger sSequenceGenerator = new AtomicInteger();

	public static synchronized String getTmpFileName() {
		return System.currentTimeMillis() + "_" + sSequenceGenerator.incrementAndGet() + ".tmp";
	}

	public static synchronized boolean rename(File oriFile, File tgtFile) {
		return oriFile.renameTo(tgtFile);
	}

	public static boolean deleteDirectory(File directory) {
		if(directory.exists()){
			File[] files = directory.listFiles();
			if(null!=files){
				for(int i=0; i<files.length; i++) {
					if(files[i].isDirectory()) {
						deleteDirectory(files[i]);
					}
					else {
						files[i].delete();
					}
				}
			}
		}
		return(directory.delete());
	}
}
