package capstone.model.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import capstone.model.dao.entity.ApplicantEntity;
import capstone.model.dao.entity.JoinApplicantProject;
import capstone.model.dao.entity.RejectedApplicantEntity;
import capstone.model.dto.OfficerInOutDto;
import capstone.model.logic.ApplicantLogic;
import capstone.model.object.ApplicantObj;
import capstone.model.service.OfficerService;

@Service
public class OfficerServiceImpl implements OfficerService{
	
	@Autowired
	private ApplicantLogic applicantLogic;

	@Override
	public OfficerInOutDto getAllApplicants() {
		
		OfficerInOutDto outDto = new OfficerInOutDto();
		
		List<JoinApplicantProject> listOfApplicant = applicantLogic.getAllApplicant();
		
		List<ApplicantObj> listOfAppObj = new ArrayList<>();
		
		for(JoinApplicantProject app : listOfApplicant) {
			
			ApplicantObj obj = new ApplicantObj();
			
			obj.setApplicantIdPk(app.getApplicantIdPk());
			
			obj.setEmail(app.getEmail());
			
			obj.setProjectTitle(app.getProjectTitle());
			
			obj.setDescription(app.getDescription());
			
			obj.setConsent(app.getConsent());
			
			obj.setTeam(convertToList(app.getTeam()));
			
			listOfAppObj.add(obj);
			
		}
		
		outDto.setListOfApplicants(listOfAppObj);
		
		return outDto;
	}
	
	public List<String> convertToList(String[] val){
		
		List<String> newList = new ArrayList<>();
		
		for(String v : val) {
			String[] splitV = v.split("-");
			
			newList.add(splitV[0]+"("+splitV[1]+")");
			
		}
		
		return newList;
	}

	@Override
	public OfficerInOutDto rejectApplicant(OfficerInOutDto inDto) {
		
		OfficerInOutDto outDto = new OfficerInOutDto();
		
		Timestamp timeNow = new Timestamp(System.currentTimeMillis());
		
		ApplicantEntity applicantEntity = applicantLogic.getApplicantByIdPk(inDto.getApplicantIdPk());
		
		/*
		 * 0 - pending
		 * 1 - accept
		 * 2 - reject
		 * 3 - evaluated
		 */
		applicantEntity.setStatus(2);
		
		applicantLogic.saveApplicantEntity(applicantEntity);
		
		RejectedApplicantEntity rejectedApplicantEntity = new RejectedApplicantEntity();
		
		rejectedApplicantEntity.setApplicantIdPk(inDto.getApplicantIdPk());
		
		rejectedApplicantEntity.setFeedback(inDto.getFeedback());
		
		rejectedApplicantEntity.setResubmitFlg(inDto.getResubmitFlg());
		
		rejectedApplicantEntity.setCreateDate(timeNow);
		
		rejectedApplicantEntity.setDeleteFlg(false);
		
		applicantLogic.saveRejectedApplicantEntity(rejectedApplicantEntity);
		
		return outDto;
	}

}
