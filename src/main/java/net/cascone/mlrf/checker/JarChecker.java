package net.cascone.mlrf.checker;

import java.nio.file.Path;
import java.util.zip.ZipFile;

public class JarChecker {

    public static boolean check(Path pathname){
        
        try {
            ZipFile file = new ZipFile(pathname.toFile());
            file.close();
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
}
