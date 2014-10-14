package tools;

import eu.celar.tosca.DocumentRoot;
import eu.celar.tosca.ToscaPackage;
import eu.celar.tosca.elasticity.Tosca_Elasticity_ExtensionsPackage;
import eu.celar.tosca.util.ToscaResourceFactoryImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
/**
 *
 * @author cmantas
 */
public class Tools {
    
    
    static Logger logger = Logger.getLogger( Tools.class.getName() );
    
    /**
     * Loads a tosca xml from a file
   * @param toscaFile
   * @return The TOSCA document root or NULL if empty
   */
    public static DocumentRoot loadFromFile(final String toscaFile) throws IOException {
        logger.debug("Loading from file: "+toscaFile);
        // Create a ResourceSet
        ResourceSet resourceSet = new ResourceSetImpl();
        XMLMapImpl baseToscaMap = new XMLMapImpl();
        //baseToscaMap.setNoNamespacePackage(ToscaPackage.eINSTANCE);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(XMLResource.OPTION_XML_MAP, baseToscaMap);
        options.put(XMLResource.OPTION_ENCODING, "UTF-8");//$NON-NLS-1$
        options.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
        // register XMIRegistryResourceFactoryIml
        resourceSet.getResourceFactoryRegistry()
                .getExtensionToFactoryMap()
                .put("tosca", new ToscaResourceFactoryImpl()); //$NON-NLS-1$

        resourceSet.getPackageRegistry().put(ToscaPackage.eNS_URI, ToscaPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put(Tosca_Elasticity_ExtensionsPackage.eNS_URI, Tosca_Elasticity_ExtensionsPackage.eINSTANCE);
        Resource resource = resourceSet.createResource(URI.createFileURI(toscaFile));
        if(resource==null){
            logger.error("Could not load file: "+toscaFile);
            throw new IOException("Could not load file: "+toscaFile);
        }
        resource.load(options);

        DocumentRoot toscaRoot = (DocumentRoot) resource.getContents().get(0);

        return toscaRoot;
    }
  
  
    /**
     * Extracts the .csar archive in the given folder
     * @param csar
     * @param outputFolder
     */
    public static void extractCsar(File csar, final String outputFolder ){    
    byte[] buffer = new byte[ 1024 ];
    try {
      // create output directory is not exists
      File folder = new File( outputFolder );
      if( !folder.exists() ) {
        folder.mkdir();
      }

      // get the zip file content
      ZipInputStream zis = new ZipInputStream( new FileInputStream( csar ) );
      // get the zipped file list entry
      ZipEntry ze = zis.getNextEntry();
      
      while( ze != null ) {
        //directories are created only for the necessary files
        if (!ze.isDirectory()){
            String fileName = ze.getName().replace("\\", "/");
            File newFile = new File( outputFolder + File.separator + fileName );
            logger.debug("Extracting file: " + newFile.getAbsoluteFile() ); //$NON-NLS-1$
            //this is where dirs are actually created
            new File( newFile.getParent() ).mkdirs();
            FileOutputStream fos = new FileOutputStream( newFile );
            int len;
            while( ( len = zis.read( buffer ) ) > 0 ) {
              fos.write( buffer, 0, len );
            }
            fos.close();
        }
        ze = zis.getNextEntry();
      }
      zis.closeEntry();
      zis.close();
    } catch( IOException ex ) {
      ex.printStackTrace();
    }
  }
    
    public static Path extractCsar(String csarFileName) throws IOException {
        File csar = new File(csarFileName);
        if(!csar.isFile()){
            logger.error("File '"+ csarFileName +"' does not exist!");
            throw new IOException("File "+ csarFileName +" does not exist!");
        }
        //Path basedir = FileSystems.getDefault().getPath("C:/tutorial/tmp/");
        String tmp_dir_prefix = "extracted_csar_";
        Path tmp = Files.createTempDirectory(tmp_dir_prefix);
        logger.debug("Temp csar extract dir: "+tmp);
        extractCsar(csar, tmp.toString());
        return tmp;
    }

  
  static void recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists()) return;
        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                //call recursively
                recursiveDelete(f);
            }
        }
        file.delete();
    }

    static Map<String, String> getToscaMeta(Path tempDir) throws IOException {
        Map<String, String> props = new java.util.TreeMap<>();
        File meta = new File (tempDir.toString()+File.separator+"TOSCA-Metadata"+File.separator+"TOSCA.meta");
        
        if(!meta.isFile()){
            logger.error("missing 'TOSCA.meta' file ");
            throw new IOException("could not find TOSCA.meta file");
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(meta))) {
            for (String line; (line = br.readLine()) != null;) {
                line = line.trim();
                int c = line.indexOf(':');
                if(c==-1) continue;
                String key = line.substring(0, c).trim();
                String value = line.substring(c + 1).trim().replace("\\", "/");
                props.put(key, value);
            }
        }
        return props;
    }
    
    public static String readFromFile(String filename) throws IOException{
        filename = filename.replace("\\", "/");
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return content;
    }
    
  
  public static void main(String args[]) throws Exception{
      CSARParser tc = new CSARParser("aaa");
  }
}
