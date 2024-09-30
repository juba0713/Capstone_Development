package capstone.model.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import capstone.model.dao.entity.AdminDashboardEntity;
import capstone.model.dao.entity.UserDetailsEntity;
import capstone.model.dao.entity.UserInformationEntity;

@Transactional
public interface UserInformationDao extends JpaRepository<UserInformationEntity, Integer>{

	public final String GET_USER_BY_USERNAME = "SELECT e"
			+ " FROM UserInformationEntity e"
			+ " WHERE e.email = :email"
			+ " AND e.deleteFlg = false";
	
	public final String GET_USER_BY_ID_PK = "SELECT e"
			+ " FROM UserInformationEntity e"
			+ " WHERE e.idPk = :idPk"
			+ " AND e.deleteFlg = false";
	
	public final String GET_USER_BY_EVALUATED_TOKEN = "SELECT u "
			+ "FROM EvaluatedApplicantEntity e "
			+ "JOIN ApplicantEntity a ON a.idPk = e.applicantIdPk "
			+ "JOIN UserInformationEntity u ON u.idPk = a.createdBy "
			+ "WHERE e.token = :token "
			+ "AND e.deleteFlg = false";
	
	public final String GET_USER_BY_REJECTED_TOKEN = "SELECT u "
			+ "FROM RejectedApplicantEntity e "
			+ "JOIN ApplicantEntity a ON a.idPk = e.applicantIdPk "
			+ "JOIN UserInformationEntity u ON u.idPk = a.createdBy "
			+ "WHERE e.token = :token "
			+ "AND e.deleteFlg = false";
	
	public final String GET_USER_BY_APPLICANT_ID_PK = "SELECT u "
			+ "FROM ApplicantEntity a "
			+ "JOIN UserInformationEntity u ON u.idPk = a.createdBy AND u.deleteFlg = false "
			+ "WHERE a.idPk = :applicantIdPk "
			+ "AND a.deleteFlg = false";
	
	public final String GET_DETAILS_FOR_ADMIN = "SELECT CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM t_evaluated_applicant e	 "
			+ "  LEFT JOIN t_evaluation_details ed ON ed.evaluated_applicant_id_pk = e.id_pk AND ed.delete_flg = false "
			+ "  WHERE ed.total >= 60 "
			+ "  AND e.delete_flg = false "
			+ ") AS INTEGER) AS application_passed_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM t_evaluated_applicant e "
			+ "  LEFT JOIN t_evaluation_details ed ON ed.evaluated_applicant_id_pk = e.id_pk AND ed.delete_flg = false "
			+ "  WHERE ed.total < 60 "
			+ "  AND e.delete_flg = false "
			+ ") AS INTEGER) AS application_failed_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM t_accepted_applicant e "
			+ ") AS INTEGER) AS application_accepted_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM t_rejected_applicant e "
			+ "  WHERE e.resubmit_flg = false "
			+ ") AS INTEGER) AS application_rejected_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM m_applicant e "
			+ "  WHERE e.status IN (0) "
			+ "  AND e.delete_flg = false "
			+ ") AS INTEGER) AS in_officer_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM m_applicant e "
			+ "  WHERE e.status IN (4) "
			+ "  AND e.delete_flg = false "
			+ ") AS INTEGER) AS in_tbiboard_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM m_applicant e "
			+ "  WHERE e.status IN (3,4) "
			+ "  AND e.delete_flg = false "
			+ ") AS INTEGER) AS in_manager_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM m_user_information e "
			+ "  WHERE e.role = 'APPLICANT' "
			+ "  AND e.delete_flg = false "
			+ ") AS INTEGER) AS applicant_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM m_user_information e "
			+ "  WHERE e.role = 'OFFICER' "
			+ "  AND e.delete_flg = false "
			+ ") AS INTEGER) AS officer_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM m_user_information e "
			+ "  WHERE e.role = 'TBIBOARD' "
			+ "  AND e.delete_flg = false "
			+ ") AS INTEGER) AS tbiboard_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM m_user_information e "
			+ "  WHERE e.role = 'MANAGER' "
			+ "  AND e.delete_flg = false "
			+ ") AS INTEGER) AS manager_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM m_user_information e "
			+ "  WHERE e.delete_flg = false "
			+ ") AS INTEGER) AS activated_account_count, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM t_rejected_applicant e "
			+ "  WHERE e.resubmit_flg = true "
			+ ") AS INTEGER) AS reapplication_rejected, "
			+ "CAST( ( "
			+ "  SELECT COUNT(e) "
			+ "  FROM t_evaluated_applicant e "
			+ "  WHERE e.resubmit_flg = true "
			+ ") AS INTEGER) AS reapplication_failed,"
			+ "CAST( ( "
			+ "        SELECT COUNT(e) "
			+ "        FROM m_applicant e "
			+ "        WHERE e.delete_flg = false "
			+ "		AND e.certificate_name IS NOT NULL "
			+ "    ) AS INTEGER) AS issued_certifate;";	
	
	public final String GET_USERS_BY_APPLICANT_ID_PKS = "SELECT u "
			+ "FROM ApplicantEntity a "
			+ "JOIN UserInformationEntity u ON u.idPk = a.createdBy AND u.deleteFlg = false "
			+ "WHERE a.idPk IN (:applicantIdPks) "
			+ "AND a.deleteFlg = false";
	
	public final String GET_ALL_USERS = "SELECT e.id_pk,  "
			+ "e.email, "
			+ "CONCAT(e.first_name, ' ', e.last_name) AS fullName, "
			+ "e.mobile_number, "
			+ "e.role, "
			+ "e.created_date, "
			+ "e.updated_date, "
			+ "NOT ((SELECT COUNT(a.created_by)>0  "
			+ " FROM t_accepted_applicant a  "
			+ " WHERE a.created_by = e.id_pk  "
			+ " AND e.delete_flg = false) "
			+ " OR "
			+ " (SELECT COUNT(a.created_by)>0  "
			+ " FROM t_rejected_applicant a  "
			+ " WHERE a.created_by = e.id_pk  "
			+ " AND e.delete_flg = false) "
			+ " OR "
			+ " (SELECT COUNT(a.created_by)>0  "
			+ " FROM t_evaluated_applicant a 	 "
			+ " WHERE a.created_by = e.id_pk  "
			+ " AND e.delete_flg = false) "
			+ " OR "
			+ " (SELECT COUNT(u)>0  "
			+ "  FROM m_user_information u "
			+ "  WHERE u.role IN ('APPLICANT', 'ADMIN') "
			+ "  AND u.id_pk = e.id_pk) "
			+ " ) "
			+ " AS deletable "
			+ "FROM m_user_information e "
			+ "WHERE e.delete_flg = false";
	
	public final String DELETE_USER = "UPDATE m_user_information  "
			+ "SET "
			+ "	delete_flg = true "
			+ "WHERE "
			+ "	id_pk = :userIdPk ";
	
	public final String UPDATE_USER = "UPDATE m_user_information "
	        + "SET "
	        + "    first_name = :firstName, "
	        + "    last_name = :lastName, "
	        + "    mobile_number = :mobileNumber, "
	        + "    role = :role, "
	        + "    updated_date = :updatedDate "
	        + "WHERE "
	        + "    id_pk = :userIdPk";

	
	@Modifying
	@Query(value=DELETE_USER, nativeQuery=true)
	public void deleteUser(@Param("userIdPk") int userIdPk) throws DataAccessException;
	
	@Modifying
	@Query(value=UPDATE_USER, nativeQuery=true) 
	public int updateUser(@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("mobileNumber") String mobileNumber,
			@Param("role") String role,
			@Param("updatedDate") Timestamp updatedDate,
			@Param("userIdPk") int userIdPk) throws DataAccessException;
	
	@Query(value=GET_USER_BY_APPLICANT_ID_PK)
	public UserInformationEntity getUserByApplicantIdPk(int applicantIdPk) throws DataAccessException;
	
	@Query(value=GET_USERS_BY_APPLICANT_ID_PKS)
	public List<UserInformationEntity> getUsersByApplicantIdPks(List<Integer> applicantIdPks) throws DataAccessException;

	@Query(value=GET_USER_BY_EVALUATED_TOKEN)
	public UserInformationEntity getUserByEvaluatedtoken(String token) throws DataAccessException;
	
	@Query(value=GET_USER_BY_REJECTED_TOKEN)
	public UserInformationEntity getUserByRejectedtoken(String token) throws DataAccessException;
	
	@Query(value=GET_USER_BY_USERNAME)
	public UserInformationEntity getUserByUsername(String email) throws DataAccessException;
	
	@Query(value=GET_USER_BY_ID_PK)
	public UserInformationEntity getUserByIdPk(int idPk) throws DataAccessException;
	
	@Query(value=GET_ALL_USERS, nativeQuery=true)
	public List<Object[]> getAllUsersRaw() throws DataAccessException;
	
	default List<UserDetailsEntity> getAllUsers() {
		
		List<Object[]> rawResults = getAllUsersRaw();
	    List<UserDetailsEntity> users = new ArrayList<>();

	    for (Object[] objects : rawResults) {
	    	UserDetailsEntity applicant = new UserDetailsEntity(objects);  
	        users.add(applicant);
	    }

	    return users;
	};
	
	@Query(value=GET_DETAILS_FOR_ADMIN, nativeQuery=true)
	public List<Object[]> getDetailsForAdminRaw() throws DataAccessException;
	
	default AdminDashboardEntity getDetailsForAdmin() {
		
		Object[] data = getDetailsForAdminRaw().get(0);
		
		AdminDashboardEntity entity = new AdminDashboardEntity(data);
		
		return entity;
	};
}
