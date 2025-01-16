package com.itwillbs.c4d2412t3p1.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.PRCalDTO;
import com.itwillbs.c4d2412t3p1.domain.PRDTO;
import com.itwillbs.c4d2412t3p1.domain.PR_calculationMDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.PR;
import com.itwillbs.c4d2412t3p1.entity.PRCode;
import com.itwillbs.c4d2412t3p1.entity.PRDetail;
import com.itwillbs.c4d2412t3p1.mapper.PRMapper;
import com.itwillbs.c4d2412t3p1.repository.PRCodeRepository;
import com.itwillbs.c4d2412t3p1.repository.PRDetailRepository;
import com.itwillbs.c4d2412t3p1.repository.PRRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class PRService {
	
	private final PRMapper prM;
	private final PRCodeRepository prcRep;
	private final PRRepository prRep;
	private final PRDetailRepository prdRep;
//	private final EmployeeRepository empRep;
	
	public List<Map<String, Object>> selectpr(String employee_cd) {
		
		List<Map<String, Object>> list = prM.selectpr(employee_cd);
		
		return list;
	}
	
	public List<Map<String, Object>> selectprmodal(Long pr_id, String employee_cd) {
		
		List<Map<String, Object>> list = prM.checkprmodal(pr_id, employee_cd);
		
		return list;
	}
	
	public List<PRCode> getprcode() {

		List<PRCode> list = prcRep.findAll();
		
		return list;
	}

	public List<Map<String, Object>> selectpradmin() {
		List<Map<String, Object>> list = prM.selectpradmin();
		
		return list;
	}
	public List<Map<String, Object>> selectpradminfirstmodal(Long pr_id) {
		List<Map<String, Object>> list = prM.selectpradminfirstmodal(pr_id);
		
		return list;
	}
	public List<Map<String, Object>> selectpradminfirstmodal2(Long prdetail_id) {
		
		List<Map<String, Object>> list = prM.selectpradminfirstmodal2(prdetail_id);
		
		return list;
	}

	public List<PRDTO> calculatingMachine(String employee_cd, String employee_nm, String bS2, String workingtime2, String overworkingtime2, String nightworkingtime2, 
			String weekendworkingtime2, String holydayworkingtime2, String remainleave2, String bonus2) throws ScriptException {
		log.info("======================================================================bs2====================="+bS2);
		String B = "B";
		String D = "D";
		
//		List<PRCode> formulas = prcRep.findAll();
		List<PRCode> Bformulas = prcRep.findByPrcodeIdStartingWith(B);
		List<PRCode> Dformulas = prcRep.findByPrcodeIdStartingWith(D);
		List<PRDTO> calculatedSalary = new ArrayList<>();
		List<PRCalDTO> calculated = new ArrayList<>();

		BigDecimal BS = new BigDecimal(bS2);
		BigDecimal overworkingtime = new BigDecimal(overworkingtime2);
		BigDecimal nightworkingtime = new BigDecimal(nightworkingtime2);
		BigDecimal weekendworkingtime = new BigDecimal(weekendworkingtime2);
		BigDecimal holydayworkingtime =new BigDecimal(holydayworkingtime2);
		BigDecimal remainleave = (remainleave2 == null)?BigDecimal.ZERO:new BigDecimal(remainleave2);
		BigDecimal bonus = (bonus2 == null)?BigDecimal.ZERO:new BigDecimal(bonus2);
		BigDecimal workingtime = new BigDecimal(workingtime2);
		BigDecimal D_GG = BigDecimal.ZERO;
		BigDecimal totalSalary = BigDecimal.ZERO; //총지급액 
		BigDecimal totalDeduction = BigDecimal.ZERO; //총공제액
//		BigDecimal totalNetsalary = BigDecimal.ZERO; //실지급액
		BigDecimal totalNonTax = BigDecimal.ZERO; //총비과세
		log.info("======================================================================BS====================="+BS);
		
		// 전체계산
//		for (PRCode formula : formulas) {
//			log.info("계산확인"+formula);
//			BigDecimal calculatedAmount = calculateSalaryWithSpEL(formula.getPrcode_fl(), BS,overworkingtime,nightworkingtime,weekendworkingtime,holydayworkingtime,remainleave,bonus,workingtime );
//			
//			calculatedAmount = calculatedAmount.setScale(0, RoundingMode.HALF_UP); // 소수점 반올림
//			
//			calculated.add(new PRCalDTO(formula.getPrcode_id(),calculatedAmount));
//			
//			if(formula.getPrcode_id().startsWith(B)) {
//				totalSalary = totalSalary.add(calculatedAmount);
//			}
//			if(formula.getPrcode_id().startsWith(D)) {
//				totalDeduction =totalDeduction.add(calculatedAmount);
//			}
//			if(formula.isPrcode_nt()) {
//				totalNonTax = totalNonTax.add(calculatedAmount);
//			}
//		}
		
		//수당계산
		for (PRCode Bformula : Bformulas) {
			log.info("계산확인"+Bformula);
//			BigDecimal calculatedAmount = calculateSalaryWithSpEL(Bformula.getPrcode_fl(), data, workingtime );
			BigDecimal calculatedAmount = calculateSalaryWithSpEL(Bformula.getPrcode_fl(), BS,overworkingtime,nightworkingtime,weekendworkingtime,holydayworkingtime,remainleave,bonus,workingtime );
			calculatedAmount = calculatedAmount.setScale(0, RoundingMode.HALF_UP);
			calculated.add(new PRCalDTO(Bformula.getPrcode_id(),calculatedAmount));
			
			totalSalary = totalSalary.add(calculatedAmount);
			if(Bformula.isPrcode_nt()) {
				totalNonTax = totalNonTax.add(calculatedAmount);
			}
		}
		
		log.info("======================================================================totalSalary====================="+totalSalary);
		log.info("======================================================================totalDeduction====================="+totalDeduction);
		
		//공제계산
		for (PRCode Dformula : Dformulas) {
			BigDecimal calculatedD = calculateSalaryWithSpEL(Dformula.getPrcode_fl(), totalSalary, totalNonTax, D_GG);
			calculatedD = calculatedD.setScale(0, RoundingMode.HALF_UP);
			calculated.add(new PRCalDTO(Dformula.getPrcode_id(),calculatedD));
			if(Dformula.getPrcode_id().contains("D_GG")) {
				D_GG = D_GG.add(calculatedD);
			}
			totalDeduction =totalDeduction.add(calculatedD);
		}
		
		BigDecimal netSalary = totalSalary.subtract(totalDeduction);
		
		PRDTO prDTO = new PRDTO();
		prDTO.setEmployee_cd(employee_cd);
		prDTO.setEmployee_nm(employee_nm);
		prDTO.setRS(netSalary);
		prDTO.setTA(totalSalary);
		prDTO.setTD(totalDeduction);
		prDTO.setCalculated(calculated);
		
		calculatedSalary.add(prDTO);
		
		return calculatedSalary;
	}

	private BigDecimal calculateSalaryWithSpEL(String prcode_fl, BigDecimal BS, BigDecimal overworkingtime,
			BigDecimal nightworkingtime, BigDecimal weekendworkingtime, BigDecimal holydayworkingtime,
			BigDecimal remainleave,BigDecimal bonus , BigDecimal workingtime) {
		log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~BS"+BS);
		log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~BFORMULA : "+prcode_fl);
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		
		
	    // 수식에서 사용할 변수를 context에 등록
		context.setVariable("BS", BS);
	    context.setVariable("workingtime", workingtime);
	    context.setVariable("overworkingtime", overworkingtime);
	    context.setVariable("nightworkingtime", nightworkingtime);
	    context.setVariable("weekendworkingtime", weekendworkingtime);
	    context.setVariable("holydayworkingtime", holydayworkingtime);
	    context.setVariable("remainleave", remainleave);
	    context.setVariable("BN", bonus);
		log.info("context variables: " + context.lookupVariable("BS"));
		log.info("context variables: " + context.lookupVariable("workingtime"));
		log.info("context variables: " + context.lookupVariable("overworkingtime"));
		log.info("context variables: " + context.lookupVariable("nightworkingtime"));
		log.info("context variables: " + context.lookupVariable("weekendworkingtime"));
		log.info("context variables: " + context.lookupVariable("holydayworkingtime"));
		log.info("context variables: " + context.lookupVariable("remainleave"));
		log.info("context variables: " + context.lookupVariable("BN"));

	    
		try {
	        
			// 변수명이 정확히 치환되도록 정규 표현식으로 처리
	        prcode_fl = prcode_fl.replaceAll("\\bBS\\b", "#BS");
	        prcode_fl = prcode_fl.replaceAll("\\bworkingtime\\b", "#workingtime");
	        prcode_fl = prcode_fl.replaceAll("\\boverworkingtime\\b", "#overworkingtime");
	        prcode_fl = prcode_fl.replaceAll("\\bnightworkingtime\\b", "#nightworkingtime");
	        prcode_fl = prcode_fl.replaceAll("\\bweekendworkingtime\\b", "#weekendworkingtime");
	        prcode_fl = prcode_fl.replaceAll("\\bholydayworkingtime\\b", "#holydayworkingtime");
	        prcode_fl = prcode_fl.replaceAll("\\bremainleave\\b", "#remainleave");
	        prcode_fl = prcode_fl.replaceAll("\\bBN\\b", "#BN");

			
	        BigDecimal result = parser.parseExpression(prcode_fl).getValue(context, BigDecimal.class);
	        log.info("SpEL evaluated result: " + result);
	        return result;
	    } catch (SpelEvaluationException e) {
	        log.info("SpEL Evaluation Error: " + e.getMessage());
	        throw e;
	    }
	}

	private BigDecimal calculateSalaryWithSpEL(String prcode_fl, BigDecimal totalSalary, BigDecimal totalNonTax, BigDecimal D_GG) {
		
		log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~DFORMULA : "+prcode_fl);
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();

		// 수식에서 사용할 변수를 context에 등록
		context.setVariable("totalSalary", totalSalary);
		context.setVariable("totalNonTax", totalNonTax);
		context.setVariable("D_GG", D_GG);
		log.info("context variables: " + context.lookupVariable("totalSalary"));
		log.info("context variables: " + context.lookupVariable("totalNonTax"));
		log.info("context variables: " + context.lookupVariable("D_GG"));
		
		try {
	        
			// 변수명이 정확히 치환되도록 정규 표현식으로 처리
	        prcode_fl = prcode_fl.replaceAll("\\btotalSalary\\b", "#totalSalary");
	        prcode_fl = prcode_fl.replaceAll("\\btotalNonTax\\b", "#totalNonTax");
	        prcode_fl = prcode_fl.replaceAll("\\bD_GG\\b", "#D_GG");

	        BigDecimal result = parser.parseExpression(prcode_fl).getValue(context, BigDecimal.class);
	        log.info("SpEL evaluated result: " + result);
	        return result;
	    } catch (SpelEvaluationException e) {
	        log.info("SpEL Evaluation Error: " + e.getMessage());
	        throw e;
	    }
	}

	public List<Employee> select_empworklastmth() {
		List<Employee> list = prM.select_empworklastmth();

		return list;
	}

	public List<Map<String, Object>> select_worktimelastmth(List<String> emp_cdlist, String premth) {
		
		String iscal = prM.isCal(premth);
		if(iscal.equals(premth)) {
			return prM.select_spes(premth);
		}else {
			String[] presep = premth.split("-");
			String year = presep[0];
			
			if(premth.contains("-12") ) {
				Map<String, Object> premth_cds = new HashMap<>();
				premth_cds.put("year", year);
				premth_cds.put("employee_cdList", emp_cdlist);
				
				List<Map<String, Object>> wtlist = prM.select_wokringtimeformth(emp_cdlist);
				System.out.println("!!!!!!!!!!!!!!!!!!!wtlist: "+wtlist);
				List<Map<String,Object>> rllist = prM.select_remainleave(premth_cds);
				
				Map<String, Map<String,Object>> list = new HashMap<>();
				
				for(Map<String, Object> wt : wtlist) {
					String empcd = (String) wt.get("EMPLOYEE_CD");
					System.out.println("!!!!!!!!!!!!!!!!!!!empcd1: "+empcd);
					list.put(empcd, new HashMap<>(wt));
				}
				
				for(Map<String,Object> rl : rllist) {
					String empcd = (String) rl.get("EMPLOYEE_CD");
					System.out.println("!!!!!!!!!!!!!!!!!!!empcd2: "+empcd);
					Map<String,Object> isexist = list.get(empcd);
					if(isexist != null) {
						isexist.putAll(rl);
					}else {
						list.put(empcd, new HashMap<>(rl));
					}
				}
				
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!list: "+list.values());
				
				return new ArrayList<>(list.values());
			}else {
				return prM.select_wokringtimeformth(emp_cdlist);
			}
		}
		
	}

	public int update_commutepr() {
		
		int result = prM.update_commutepr();
		
		if (result == 0) {
	        return 0; 
	    }
		return 1;
	}

	public List<Map<String, Object>> calculatesalAllemp(List<PR_calculationMDTO> wtdata) throws ScriptException {
		
		List<Map<String, Object>> result = new ArrayList<>();
		
		BigDecimal Allempta = BigDecimal.ZERO;
		BigDecimal Allemptd = BigDecimal.ZERO;
		BigDecimal Allempns = BigDecimal.ZERO;
		BigDecimal Allemptp = BigDecimal.ZERO;
		
		PR pr = new PR();
		pr.setPr_gm(LocalDate.now().getYear() + "-" + String.format("%02d", LocalDate.now().getMonthValue()));
		pr.setPr_wd(Timestamp.valueOf(LocalDateTime.now()));
		prRep.save(pr);
		Long prid = pr.getPr_id();
		String wm = null;
		String wr = null;
		
		for (PR_calculationMDTO emp : wtdata) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 확인 : "+ emp.getBonus());
			String employee_cd = emp.getEmployee_cd();
			String employee_nm = emp.getEmployee_nm();
			String BS = emp.getBS();
			String workingtime = emp.getWorkingtime();
			String overworkingtime = emp.getOverworkingtime();
			String nightworkingtime = emp.getNightworkingtime();
			String weekendworkingtime = emp.getWeekendworkingtime();
			String holydayworkingtime = emp.getHolydayworkingtime();
			String remainleave = emp.getRemainleave();
			String bonus = emp.getBonus();
			wm = emp.getWm();
			wr = emp.getWr();
			
	        List<PRDTO> calculatedSalary = calculatingMachine(employee_cd, employee_nm, BS, workingtime, overworkingtime, nightworkingtime, weekendworkingtime, holydayworkingtime, remainleave, bonus);
	        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 확인 : "+calculatedSalary.get(0).getRS());
	        Allempta = Allempta.add(calculatedSalary.get(0).getTA());
	        Allemptd = Allemptd.add(calculatedSalary.get(0).getTD());
	        Allempns = Allempns.add(calculatedSalary.get(0).getRS());
	        Allemptp = Allemptp.add(BigDecimal.ONE);
	        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 확인 : "+Allemptp);
	        
	        PRDetail prdetail = new PRDetail();
	        prdetail.setEmployee_cd(employee_cd);
	        prdetail.setEmployee_nm(employee_nm);
	        prdetail.setPr_id(prid);
	        prdetail.setPredetail_ch(false);
	        prdetail.setPredetail_ta(calculatedSalary.get(0).getTA());
	        prdetail.setPredetail_td(calculatedSalary.get(0).getTD());
	        prdetail.setPredetail_rs(calculatedSalary.get(0).getRS());
	        
	        for(PRCalDTO pd : calculatedSalary.get(0).getCalculated()) {
	        	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ 제발"+pd.getPrdetail_nm());
	        	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ 제발"+pd.getAmount());
	        	if(pd.getPrdetail_nm().equals("BS")) {
	        		prdetail.setPredetail_bs(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_BN")) {
	        		prdetail.setPredetail_bn(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_HA")) {
	        		prdetail.setPredetail_ha(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_MT")) {
	        		prdetail.setPredetail_mt(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_NA")) {
	        		prdetail.setPredetail_na(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_OT")) {
	        		prdetail.setPredetail_ot(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_RL")) {
	        		prdetail.setPredetail_rl(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_WA")) {
	        		prdetail.setPredetail_wa(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("D_GG")) {
	        		prdetail.setPredetail_gg(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("D_GM")) {
	        		prdetail.setPredetail_gm(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("D_GY")) {
	        		prdetail.setPredetail_gy(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("D_LG")) {
	        		prdetail.setPredetail_lg(pd.getAmount());
	        	}
	        }
	        
	        prdRep.save(prdetail);
	        
	        Map<String, Object> calculatedMap = new HashMap<>();
	        calculatedMap.put("employee_cd", employee_cd);
	        calculatedMap.put("employee_nm", employee_nm);
	        calculatedMap.put("ta", calculatedSalary.get(0).getTA());
	        calculatedMap.put("td", calculatedSalary.get(0).getTD());
	        calculatedMap.put("rs", calculatedSalary.get(0).getRS());
	        calculatedMap.put("calculated", calculatedSalary.get(0).getCalculated());
	        
	        result.add(calculatedMap);
	    }
		
		pr.setPr_ns(Allempns);
		pr.setPr_ta(Allempta);
		pr.setPr_td(Allemptd);
		pr.setPr_tp(Allemptp);
		pr.setPr_wm(wm);
		pr.setPr_wr(wr);
		prRep.save(pr);
		
//		Map<String, Object> total = new HashMap<>();
//		total.put("TA", Allempta);
//		total.put("TD", Allemptd);
//		total.put("NS", Allempns);
//		total.put("TP", Allemptp);
//		result.add(total);
		
		return result;
	}

}
