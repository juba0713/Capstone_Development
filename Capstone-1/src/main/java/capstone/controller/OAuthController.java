package capstone.controller;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Controller
public class OAuthController {
	
	

    private static final String CREDENTIALS_FILE_PATH = "/etc/secrets/credential.json";
    private static final String REDIRECT_URI = "https://capstone-development-vbli.onrender.com/oauth2callback";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);

    private GoogleClientSecrets loadClientSecrets() throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(CREDENTIALS_FILE_PATH)) {
            return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(fileInputStream));
        }
    }

    @GetMapping("/authorize")
    public String authorize() throws Exception {
        GoogleClientSecrets clientSecrets = loadClientSecrets();
        
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
                .setAccessType("offline")
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .build();

        GoogleAuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI);
        return "redirect:" + authorizationUrl.build();
    }

    @GetMapping("/Callback")
    public String oauth2Callback(@RequestParam("code") String code, HttpSession session) throws Exception {
        GoogleClientSecrets clientSecrets = loadClientSecrets();
        
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
                .setAccessType("offline")
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .build();

        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        Credential credential = flow.createAndStoreCredential(tokenResponse, "user");

        // Store credential in session or database as needed
        session.setAttribute("credential", credential);

        // Initialize Drive service with the authorized credential
        Drive driveService = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName("TestProject")
                .build();

        // Store driveService as needed
        session.setAttribute("driveService", driveService);

        return "redirect:/success";
    }
}
