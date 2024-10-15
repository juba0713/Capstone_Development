//package capstone.model.service.impl;
//
//import java.io.FileInputStream; // For reading files
//import java.io.IOException; // For handling IOExceptions
//import com.google.api.services.drive.Drive; // Google Drive API
//import com.google.api.services.drive.model.File; // Google Drive File class
//import com.google.api.client.http.InputStreamContent; // For file content
//import java.util.Collections; // For Collections.singletonList
//
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.FileList;
//
//import capstone.model.service.GoogleDriveService;
//import jakarta.annotation.PostConstruct;
//
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.security.GeneralSecurityException;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//public class GoogleDriveServiceImpl  implements GoogleDriveService{
//	
//	@Autowired
//	private final Environment env;
//
//    // Inject properties
//    @Value("${google.credentials.file}")
//    private String CREDENTIALS_FILE_PATH;
//
//    @Value("${google.tokens.directory.path}")
//    private String TOKENS_DIRECTORY_PATH;
//
//    @Value("${google.application.name}")
//    private String APPLICATION_NAME;
//
//    @Value("${google.oauth.redirectUri}")
//    private String REDIRECT_URI;
//
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//
//    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
//
//    private Drive driveService;
//
//    private GoogleAuthorizationCodeFlow flow;
//
//    public GoogleDriveServiceImpl(Environment env) {
//        this.env = env;
//    }
//
//    @PostConstruct
//    public void init() {
//        try {
//            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//
//            // Load client secrets.
//            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(CREDENTIALS_FILE_PATH)));
//
//            // Build flow and trigger user authorization request.
//            flow = new GoogleAuthorizationCodeFlow.Builder(
//                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                    .setAccessType("offline")
//                    .build();
//
//            System.out.println("Google Drive Service initialized.");
//        } catch (Exception e) {
//            System.err.println("Error initializing Google Drive Service: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public String getAuthorizationUrl() throws IOException, GeneralSecurityException {
//        return flow.newAuthorizationUrl()
//                .setRedirectUri(REDIRECT_URI)
//                .build();
//    }
//
//    @Override
//    public void handleCallback(String code) throws IOException, GeneralSecurityException {
//        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
//                .setRedirectUri(REDIRECT_URI)
//                .execute();
//
//        Credential credential = flow.createAndStoreCredential(tokenResponse, "user");
//        driveService = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//
//        System.out.println("Google Drive API authorized and ready.");
//    }
//
//    @Override
//    public Drive getDriveService() {
//        return driveService;
//    }
//
//    @Override
//    public FileList listFiles() throws IOException {
//        if (driveService == null) {
//            throw new IOException("Google Drive service is not initialized.");
//        }
//
//        FileList result = driveService.files().list()
//                .setPageSize(10)
//                .setFields("nextPageToken, files(id, name)")
//                .execute();
//        return result;
//    }
//	  
//	  
//	  
//	  public InputStream  getFileContentByName(String fileName, Boolean isPdf) throws IOException, GeneralSecurityException {
//		  
////		// Retrieve the folder ID from properties
////		    String folderId;
////
////		    // Check if we are dealing with PDFs or certificates
////		    if (isPdf) {
////		        folderId = env.getProperty("pdf.folder.id"); // ID for the pdfs folder
////		    } else {
////		        System.out.println("CERTIFICATE");
////		        folderId = env.getProperty("certificate.folder.id"); // ID for the certificate folder
////		    }
////
////		    Drive service = getInstance();
////
////		    // Search for the file by name within the specified folder
////		    String query = "name = '" + fileName + "' and '" + folderId + "' in parents";
////		    FileList result = service.files().list()
////		            .setQ(query)
////		            .setSpaces("drive")
////		            .setFields("files(id, name)")
////		            .execute();
////
////		    List<File> files = result.getFiles();
////
////		    if (files == null || files.isEmpty()) {
////		        return null; // No files found
////		    } else {
////		        File file = files.get(0); // Get the first matching file
////
////		        // Return the InputStream for the file's content
////		        return service.files().get(file.getId()).executeMediaAsInputStream();
////		    }
//		  
//		  return null;
//		  }
//
//
//	  public void uploadPdfFile(MultipartFile file, String fileName) {
//		  
////		  String folderId = env.getProperty("pdf.folder.id");
////		  
////		    try {
////		        if (file.isEmpty()) {
////		            throw new IllegalArgumentException("File is empty");
////		        }
////
////		        Drive service = getInstance();
////
////		        File fileMetadata = new File();
////		        fileMetadata.setName(fileName);
////		        fileMetadata.setParents(Collections.singletonList(folderId));
////
////		        InputStreamContent mediaContent = new InputStreamContent(
////		                file.getContentType(),
////		                file.getInputStream()
////		        );
////
////		        File uploadedFile = service.files().create(fileMetadata, mediaContent)
////		                .setFields("id, parents")
////		                .execute();
////
////		        System.out.println("Uploaded File ID: " + uploadedFile.getId());
////
////		    } catch (Exception e) {
////		        System.err.println("Error uploading file: " + e.getMessage());
////		        e.printStackTrace();
////		    }
//		}
//
//
//	@Override
//	public void uploadCertificateFile(java.io.File file, String fileName) {
////		String folderId = env.getProperty("certificate.folder.id");
////	    
////	    try {
////	        if (file.length() == 0) {
////	            throw new IllegalArgumentException("File is empty");
////	        }
////
////	        Drive service = getInstance();
////
////	        File fileMetadata = new File();
////	        fileMetadata.setName(fileName);
////	        fileMetadata.setParents(Collections.singletonList(folderId));
////
////	        InputStreamContent mediaContent = new InputStreamContent(
////	                "image/png", // Specify the correct content type
////	                new FileInputStream(file) // Use FileInputStream to read the file
////	        );
////
////	        File uploadedFile = service.files().create(fileMetadata, mediaContent)
////	                .setFields("id, parents")
////	                .execute();
////
////	        System.out.println("Uploaded File ID: " + uploadedFile.getId());
////
////	    } catch (Exception e) {
////	        System.err.println("Error uploading file: " + e.getMessage());
////	        e.printStackTrace();
////	    }
//	}
//	
//	
//}
