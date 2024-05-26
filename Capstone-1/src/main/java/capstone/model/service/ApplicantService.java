package capstone.model.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import capstone.model.dto.ApplicantInOutDto;


@Service
public interface ApplicantService {
	
	/**
	 * To valdiate applicant's input
	 * @param inDto
	 * @return ApplicantInOutDto
	 */
	public ApplicantInOutDto validateApplication(ApplicantInOutDto inDto);

	/**
	 * To save applicant
	 * @param inDto
	 * @return ApplicantInOutDto
	 */
	public ApplicantInOutDto saveApplication(ApplicantInOutDto inDto) throws IOException;
	
	/**
	 * To validate the apssword
	 * @param inDto
	 * @return ApplicantInOutDto
	 */
	public ApplicantInOutDto validatePassword(ApplicantInOutDto inDto);
	
	/**
	 * To change password for the first time
	 * @param inDto
	 * @return ApplicantInOutDto
	 */
	public void changePassword(ApplicantInOutDto inDto);
	
	/**
	 * To get the details of the application by id pk
	 * @param inDto
	 * @return ApplicantInOutDto
	 */
	public ApplicantInOutDto getApplicantDetails(); 
}