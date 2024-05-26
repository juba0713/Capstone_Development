package capstone.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import capstone.common.constant.CommonConstant;
import capstone.controller.webdto.ApplicantWebDto;
import capstone.model.dto.ApplicantInOutDto;
import capstone.model.service.ApplicantService;

@Controller
@RequestMapping("/applicant")
public class ApplicantController {
	
	@Autowired
	private ApplicantService applicantService;
	
	@Autowired
	private Environment env;

	/**
	 * To show the Applicant Form
	 * @return String
	 */
	@GetMapping("/form")
	public String showApplicantForm(@ModelAttribute ApplicantWebDto webDto) {
		
		return "applicant/form";
	}
	
	/**
	 * To process the Applicant Form
	 * @return String
	 */
	@PostMapping("/form")
	public String processApplicantForm(@ModelAttribute ApplicantWebDto webDto, RedirectAttributes ra, @RequestParam("vitaeFile") MultipartFile file)  throws IOException{
		
		
		if(file != null && !file.isEmpty()) {
		
            Path uploadPath = Paths.get(env.getProperty("file.path"));
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy( file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		}
		
		ApplicantInOutDto inDto = new ApplicantInOutDto();
		
		inDto.setEmail(webDto.getEmail());
		
		inDto.setAgreeFlg(webDto.getAgreeFlg());
		
		inDto.setProjectTitle(webDto.getProjectTitle());
		
		inDto.setProjectDescription(webDto.getProjectDescription());
		
		inDto.setTeams(webDto.getTeams());
		
		inDto.setProblemStatement(webDto.getProblemStatement());
		
		inDto.setTargetMarket(webDto.getTargetMarket());
		
		inDto.setSolutionDescription(webDto.getSolutionDescription());
		
		inDto.setHistoricalTimeline(webDto.getHistoricalTimeline());
		
		inDto.setProductServiceOffering(webDto.getProductServiceOffering());
		
		inDto.setPricingStrategy(webDto.getPricingStrategy());
		
		inDto.setIntPropertyStatus(webDto.getIntPropertyStatus());
		
		inDto.setObjectives(webDto.getObjectives());
		
		inDto.setScopeProposal(webDto.getScopeProposal());
		
		inDto.setMethodology(webDto.getMethodology());
		
		inDto.setVitaeFile(file);
		
		inDto.setSupportLink(webDto.getSupportLink());
		
		inDto.setGroupName(webDto.getGroupName());
		
		inDto.setGroupLeader(webDto.getGroupLeader());
		
		inDto.setLeaderNumber(webDto.getLeaderNumber());
		
		inDto.setLeaderEmail(webDto.getLeaderEmail());
		
		inDto.setMembers(webDto.getMembers());
		
		inDto.setUniversity(webDto.getUniversity());
		
		inDto.setTechnologyAns(webDto.getTechnologyAns());
		
		inDto.setProductDevelopmentAns(webDto.getProductDevelopmentAns());
		
		inDto.setCompetitiveLandscapeAns(webDto.getCompetitiveLandscapeAns());
		
		inDto.setProductDesignAns(webDto.getProductDesignAns());
		
		inDto.setTeamAns(webDto.getTeamAns());
		
		inDto.setGoToMarketAns(webDto.getGoToMarketAns());
		
		inDto.setManufacturingAns(webDto.getManufacturingAns());
		
		inDto.setEligibilityAgreeFlg(webDto.getEligibilityAgreeFlg());
		
		inDto.setCommitmentOneFlg(webDto.getCommitmentOneFlg());
		
		inDto.setCommitmentTwoFlg(webDto.getCommitmentTwoFlg());
		
		inDto.setCommitmentThreeFlg(webDto.getCommitmentThreeFlg());
		
		inDto.setCommitmentFourFlg(webDto.getCommitmentFourFlg());
		
		ApplicantInOutDto outDto = applicantService.validateApplication(inDto);
		
		System.out.println(webDto.getAgreeFlg());
		
		if(CommonConstant.INVALID.equals(outDto.getResult())) {
			
			webDto.setError(outDto.getError());
			
			ra.addFlashAttribute("applicantWebDto", webDto);
			
			return "redirect:/applicant/form";
		}
		
		applicantService.saveApplication(inDto);
		
		ra.addFlashAttribute("success", "You have sucessfully registered! Wait for the email that will be sent to you!");
		
		return "redirect:/";
	}
	
	
	@GetMapping("/home")
	public String showApplicantHome(@ModelAttribute ApplicantWebDto webDto) {
		
		ApplicantInOutDto outDto = applicantService.getApplicantDetails();
		
		webDto.setApplicantDetailsObj(outDto.getApplicantDetailsObj());
		
		return "applicant/home";
	}
	
	@PostMapping("/change-password")
	public String processChangePassword(@ModelAttribute ApplicantWebDto webDto, RedirectAttributes ra) {
		
		ApplicantInOutDto inDto = new ApplicantInOutDto();
		
		inDto.setCurrentPassword(webDto.getCurrentPassword());
		
		inDto.setNewPassword(webDto.getNewPassword());
		
		inDto.setConfirmPassword(webDto.getConfirmPassword());
		
		ApplicantInOutDto outDto = applicantService.validatePassword(inDto);
		
		if(CommonConstant.INVALID.equals(outDto.getResult())) {
			
			ra.addFlashAttribute("error", outDto.getError());
					
			return "redirect:/applicant/home";
		}
			
		applicantService.changePassword(inDto);
		
		return "redirect:/applicant/home";
	}
	
}
