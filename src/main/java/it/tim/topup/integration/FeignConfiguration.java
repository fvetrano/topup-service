package it.tim.topup.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.AccessController;
import java.security.KeyStore;
import java.security.PrivilegedExceptionAction;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import feign.Client;
import feign.Feign;
import it.tim.topup.model.configuration.BuiltInConfiguration;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
public class FeignConfiguration {
	
	private ResourceLoader resourceLoader;	
	private BuiltInConfiguration builtInConfig;
	
	 @Autowired
	 public FeignConfiguration(ResourceLoader resourceLoader, BuiltInConfiguration builtInConfig) {
	        this.resourceLoader = resourceLoader;
	        this.builtInConfig = builtInConfig;
	 }
	
	@Bean
	public Feign.Builder feignBuilder() throws Exception{
		
		log.info("****************** FeignConfiguration *********************** ");

		try {
			String keyStoreFileName = builtInConfig.getKeystorepath();		
			File keyStoreFile = new File(keyStoreFileName);
			
			log.info("----------->  keyStoreFileName from resource: " + keyStoreFile.getName());
			log.info("----------->  keyStoreFileName from resource: " + keyStoreFile.getAbsolutePath());
			
			Client trustSSLSockets = new    Client.Default(TrustingSSLSocketFactory.get("tomcat","",keyStoreFile), null);
			
			log.info("****************** FeignConfiguration ******* trustSSLSockets " + trustSSLSockets);
			
			return Feign.builder().client(trustSSLSockets);
		}
		catch(Throwable ex) {
				log.error("ERROR " + ex);
				ex.printStackTrace();
				//throw ex;
				return Feign.builder();
		}	
	}
	
	/**
	 * Gets the {@link SSLSocketFactory} instance for the client communication
	 * using the given truststore file and password.
	 * 
	 * Since the instance is used as client, this is instantiated with empty
	 * keystore and the truststore represented by the given truststore file.
	 * 
	 * 
	 * @param theTrustStoreFile
	 *            The complete file path of the truststore.
	 * @return {@link SSLSocketFactory} instance that internally uses the given
	 *         truststore.
	 * @throws Exception
	 *             When there is an error in the creating the
	 *             {@link SSLSocketFactory} instance.
	 */
	public static SSLSocketFactory getClientSSLSocketFactory(File theTrustStoreFile)
	        throws Exception
	{
	    // This supports TLSv1.2
	    SSLContext sslContext = SSLContext.getInstance("TLS");

	    KeyStore kStore = KeyStore.getInstance(KeyStore.getDefaultType());

	    FileInputStream file = getFileInputStream(theTrustStoreFile);
	    kStore.load(file, null);

	    TrustManagerFactory tmf = TrustManagerFactory
	            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
	    tmf.init(kStore);

	    
	    log.info("****************** sslContext.init *********************** ");
	    
	    sslContext.init(new KeyManager[] {}, tmf.getTrustManagers(), null);

	    log.info("****************** sslContext.init DONE !!!! *********************** ");
	    return sslContext.getSocketFactory();
	}

	/**
	 * Reads the file into {@link FileInputStream} instance.
	 * 
	 * @param file
	 *            The file to be read.
	 * @return {@link FileInputStream} that represents the file content/
	 * @throws Exception
	 *             When there is any error in reading the file.
	 */
	private static FileInputStream getFileInputStream(final File file) throws Exception
	{
	    return AccessController.doPrivileged(new PrivilegedExceptionAction<FileInputStream>()
	    {
	        @Override
	        public FileInputStream run() throws Exception
	        {
	            try
	            {
	                if (file.exists())
	                {
	                    return new FileInputStream(file);
	                } else
	                {
	                    return null;
	                }
	            } catch (FileNotFoundException e)
	            {
	                // couldn't find it, oh well.
	                return null;
	            }
	        }
	    });
	}
}

