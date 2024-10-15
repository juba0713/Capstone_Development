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

import capstone.model.service.GoogleDriveService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@RestController
public class OAuthController {

    @Autowired
    private GoogleDriveService googleDriveService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {
        try {
            googleDriveService.handleCallback(code);
            return "Authorization successful! You can now use Google Drive features.";
        } catch (Exception e) {
            return "Authorization failed: " + e.getMessage();
        }
    }
    
    @GetMapping("/authorize")
    public String authorize() throws IOException, GeneralSecurityException {
        // Redirect the user to the OAuth authorization page
        return "redirect:" + googleDriveService.getAuthorizationUrl();
    }
}
