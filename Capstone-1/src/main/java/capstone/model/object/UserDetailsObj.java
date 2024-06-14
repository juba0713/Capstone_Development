package capstone.model.object;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class UserDetailsObj {
	
	private int id;
	
	private String email;
	
	private String number;
	
	private String role;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
}
