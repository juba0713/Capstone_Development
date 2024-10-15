package capstone.model.service;


import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

@Service
public interface GoogleDriveService {
	
	public String getAuthorizationUrl() throws IOException, GeneralSecurityException;
	public void handleCallback(String code) throws IOException, GeneralSecurityException;
    public Drive getDriveService();
    public FileList listFiles() throws IOException;
    
	public void uploadPdfFile(MultipartFile file, String fileName);
	
	public void uploadCertificateFile(java.io.File file, String fileName);
	}
