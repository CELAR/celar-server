/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

import eu.celar.tosca.DocumentRoot;
import eu.celar.tosca.ToscaPackage;
import eu.celar.tosca.elasticity.Tosca_Elasticity_ExtensionsPackage;
import eu.celar.tosca.util.ToscaResourceFactoryImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Level;
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
    
    
    public static DocumentRoot loadFromFile(final String toscaFile) {
        logger.setLevel(Level.OFF);
        // Create a ResourceSet
        ResourceSet resourceSet = new ResourceSetImpl();
        XMLMapImpl baseToscaMap = new XMLMapImpl();
        baseToscaMap.setNoNamespacePackage(ToscaPackage.eINSTANCE);
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
        try {
            resource.load(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DocumentRoot toscaRoot = (DocumentRoot) resource.getContents().get(0);

        return toscaRoot;
    }
  
  
    /**
     * Extracts the .csar archive in the given folder
     * @param csar
     * @param outputFolder
     * @throws IOException 
     */
    public static void extractCsar(File csar, final String outputFolder ) throws IOException {    
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
        String fileName = ze.getName();
        File newFile = new File( outputFolder + File.separator + fileName );
        logger.info("Extracting file: " + newFile.getAbsoluteFile() ); //$NON-NLS-1$
        new File( newFile.getParent() ).mkdirs();
        FileOutputStream fos = new FileOutputStream( newFile );
        int len;
        while( ( len = zis.read( buffer ) ) > 0 ) {
          fos.write( buffer, 0, len );
        }
        fos.close();
        ze = zis.getNextEntry();
      }
      zis.closeEntry();
      zis.close();
      logger.info( "Done" ); //$NON-NLS-1$
    } catch( IOException ex ) {
      ex.printStackTrace();
    }
  }
    
    public String readFromFile(String filename) throws IOException{
        String content = new String(Files.readAllBytes(Paths.get("duke.java")));
        return content;
    }
    
  
  public static void main(String args[]){
      
  }
}
