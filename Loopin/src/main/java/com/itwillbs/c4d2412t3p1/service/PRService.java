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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

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

	public List<Map<String, Object>> select_empworklastmth(String premth) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@premth: "+premth);
		String iscal = prM.isCal(premth);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@iscal: "+iscal);
		if(iscal != null && iscal.equals(premth)) {
			return prM.select_spes(premth);
		}else {
			List<Map<String, Object>> list = prM.select_empworklastmth();
			return list;
		}
	}

	public List<Map<String, Object>> select_worktimelastmth(List<String> emp_cdlist, String premth) {
		
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
	        prdetail.setPrdetail_ch(false);
	        prdetail.setPrdetail_ta(calculatedSalary.get(0).getTA());
	        prdetail.setPrdetail_td(calculatedSalary.get(0).getTD());
	        prdetail.setPrdetail_rs(calculatedSalary.get(0).getRS());
	        
	        for(PRCalDTO pd : calculatedSalary.get(0).getCalculated()) {
	        	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ 제발"+pd.getPrdetail_nm());
	        	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ 제발"+pd.getAmount());
	        	if(pd.getPrdetail_nm().equals("BS")) {
	        		prdetail.setPrdetail_bs(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_BN")) {
	        		prdetail.setPrdetail_bn(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_HA")) {
	        		prdetail.setPrdetail_ha(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_MT")) {
	        		prdetail.setPrdetail_mt(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_NA")) {
	        		prdetail.setPrdetail_na(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_OT")) {
	        		prdetail.setPrdetail_ot(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_RL")) {
	        		prdetail.setPrdetail_rl(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("B_WA")) {
	        		prdetail.setPrdetail_wa(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("D_GG")) {
	        		prdetail.setPrdetail_gg(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("D_GM")) {
	        		prdetail.setPrdetail_gm(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("D_GY")) {
	        		prdetail.setPrdetail_gy(pd.getAmount());
	        	}
	        	if(pd.getPrdetail_nm().equals("D_LG")) {
	        		prdetail.setPrdetail_lg(pd.getAmount());
	        	}
	        }
	        
	        prdRep.save(prdetail);
	        Long prdetailId = prdetail.getPrdetail_id();
	        
	        Map<String, Object> calculatedMap = new HashMap<>();
	        calculatedMap.put("employee_cd", employee_cd);
	        calculatedMap.put("employee_nm", employee_nm);
	        calculatedMap.put("pr_id", prid);
	        calculatedMap.put("prdetail_id", prdetailId);
	        calculatedMap.put("ta", calculatedSalary.get(0).getTA());
	        calculatedMap.put("td", calculatedSalary.get(0).getTD());
	        calculatedMap.put("rs", calculatedSalary.get(0).getRS());
	        calculatedMap.put("calculated", calculatedSalary.get(0).getCalculated());
//	        calculatedMap.put("calculated", prdetail.getPrdetail_bn());     // 이렇게 풀어서 보내주면 프론트에서 js로 데이터 전처리가 필요없다. > 묶어서 계산한거에 익숙해져서 풀어보내줄수있다는걸 생각하지 못함,, 나중에 시간되면 풀어서 보내주고 js고치기.
	        
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

	public String isCal(String premth) {
		String iscal = prM.isCal(premth);
		return iscal;
	}

	public List<Map<String, Object>> select_worktimeforbn(List<PR_calculationMDTO> calbndata) throws ScriptException {
		
		PR updatepr = new PR();
		PRDetail updatepd = new PRDetail();
		
		Long prid = calbndata.get(0).getPrid();
		List<String> pdids = calbndata.get(0).getPdid();
		String bn = calbndata.get(0).getBonus();
		
		List<Map<String, Object>> result = new ArrayList<>();
		
		if(Objects.nonNull(prid) && !pdids.isEmpty()) { // 상여계산으로 prid pdid 들고왔을때
			
			Optional<PR> prlist = prRep.findById(prid);
			BigDecimal totalempta =  BigDecimal.ZERO;
			BigDecimal totalemptd =  BigDecimal.ZERO;
			BigDecimal totalempns =  BigDecimal.ZERO;
			
			String[] prwms = prlist.get().getPr_wm().split("-");
			String prwmyear = prwms[0];
			String premth = prwms[1];
			
			for(String pd : pdids) {
				Long pdid = Long.parseLong(pd);
				System.out.println("~~~~pdid : "+pdid);
				Optional<PRDetail> pdlist = prdRep.findById(pdid);
				if(pdids.size() == 1) {
					totalempta = prlist.get().getPr_ta().subtract(pdlist.get().getPrdetail_ta());
					totalemptd = prlist.get().getPr_td().subtract(pdlist.get().getPrdetail_td());
					totalempns = prlist.get().getPr_ns().subtract(pdlist.get().getPrdetail_rs());
				}
				System.out.println("~~~~pdlist : "+pdlist);
				List<String> employee_cdList = new ArrayList<>();
				employee_cdList.add(pdlist.get().getEmployee_cd());
				Map<String, Object> getwtemp = prM.getwt(employee_cdList, premth, prwmyear);
				System.out.println("@@@@@@getwtemp : "+getwtemp);
				System.out.println("@@@@@@getwtemp : "+getwtemp.get("NIGHTWORKINGTIME").getClass().getName());
				
				String employee_cd = pdlist.get().getEmployee_cd();
				String employee_nm = pdlist.get().getEmployee_nm();
				String BS = pdlist.get().getPrdetail_bs().multiply(new BigDecimal(12)).toString();
				String workingtime = getwtemp.get("WORKINGTIME").toString();
				String overworkingtime = getwtemp.get("OVERWORKINGTIME").toString();
				String nightworkingtime = getwtemp.get("NIGHTWORKINGTIME").toString();
				String weekendworkingtime = getwtemp.get("WEEKENDWORKINGTIME").toString();
				String holydayworkingtime = getwtemp.get("HOLYDAYWORKINGTIME").toString();
				String remainleave = getwtemp.get("ANNUAL_RA") == null ? "0" : getwtemp.get("ANNUAL_RA").toString();
				String bonus = null;
				
				if(pdids.size() ==1) {
					bonus = bn;
				}else {
					bonus = pdlist.get().getPrdetail_bn() == null ? bn : pdlist.get().getPrdetail_bn().add(new BigDecimal(bn)).toString();
				}
				
				Map<String, Consumer<BigDecimal>> prDetailMap = new HashMap<>();
				prDetailMap.put("BS", updatepd::setPrdetail_bs);
				prDetailMap.put("B_BN", updatepd::setPrdetail_bn);
				prDetailMap.put("B_HA", updatepd::setPrdetail_ha);
				prDetailMap.put("B_MT", updatepd::setPrdetail_mt);
				prDetailMap.put("B_NA", updatepd::setPrdetail_na);
				prDetailMap.put("B_OT", updatepd::setPrdetail_ot);
				prDetailMap.put("B_RL", updatepd::setPrdetail_rl);
				prDetailMap.put("B_WA", updatepd::setPrdetail_wa);
				prDetailMap.put("D_GG", updatepd::setPrdetail_gg);
				prDetailMap.put("D_GM", updatepd::setPrdetail_gm);
				prDetailMap.put("D_GY", updatepd::setPrdetail_gy);
				prDetailMap.put("D_LG", updatepd::setPrdetail_lg);
				
				List<PRDTO> cal = calculatingMachine(employee_cd, employee_nm, BS, workingtime, overworkingtime, nightworkingtime, weekendworkingtime, holydayworkingtime, remainleave, bonus);
				totalempta = totalempta.add(cal.get(0).getTA());
				totalemptd = totalemptd.add(cal.get(0).getTD());
				totalempns = totalempns.add(cal.get(0).getRS());
				
				updatepd.setEmployee_cd(cal.get(0).getEmployee_cd());
				updatepd.setEmployee_nm(cal.get(0).getEmployee_nm());
				updatepd.setPr_id(prid);
				updatepd.setPrdetail_id(pdid);
				updatepd.setPrdetail_ta(cal.get(0).getTA());
				updatepd.setPrdetail_td(cal.get(0).getTD());
				updatepd.setPrdetail_rs(cal.get(0).getRS());
				
				for( PRCalDTO pc : cal.get(0).getCalculated()) {
					Consumer<BigDecimal> setter = prDetailMap.get(pc.getPrdetail_nm());
				    if (setter != null) {
				        setter.accept(pc.getAmount());
				    }
				}
				
				prdRep.save(updatepd);
				
				Map<String, Object> calculatedMap = new HashMap<>();
		        calculatedMap.put("EMPLOYEE_CD", employee_cd);
		        calculatedMap.put("EMPLOYEE_NM", employee_nm);
		        calculatedMap.put("PR_ID", prid);
		        calculatedMap.put("PRDETAIL_ID", pdid);
		        calculatedMap.put("PRDETAIL_TA", updatepd.getPrdetail_ta());
		        calculatedMap.put("PRDETAIL_TD", updatepd.getPrdetail_td());
		        calculatedMap.put("PRDETAIL_RS", updatepd.getPrdetail_rs());
		        calculatedMap.put("PRDETAIL_BS", updatepd.getPrdetail_bs());
		        calculatedMap.put("PRDETAIL_MT", updatepd.getPrdetail_mt());
		        calculatedMap.put("PRDETAIL_OT", updatepd.getPrdetail_ot());
		        calculatedMap.put("PRDETAIL_NA", updatepd.getPrdetail_na());
		        calculatedMap.put("PRDETAIL_WA", updatepd.getPrdetail_wa());
		        calculatedMap.put("PRDETAIL_HA", updatepd.getPrdetail_ha());
		        calculatedMap.put("PRDETAIL_RL", updatepd.getPrdetail_rl());
		        calculatedMap.put("PRDETAIL_BN", updatepd.getPrdetail_bn());
		        calculatedMap.put("PRDETAIL_GM", updatepd.getPrdetail_gm());
		        calculatedMap.put("PRDETAIL_GY", updatepd.getPrdetail_gy());
		        calculatedMap.put("PRDETAIL_GG", updatepd.getPrdetail_gg());
		        calculatedMap.put("PRDETAIL_LG", updatepd.getPrdetail_lg());
		        
		        result.add(calculatedMap);
			}
		
			
			updatepr.setPr_gm(prlist.get().getPr_gm());
			updatepr.setPr_id(prid);
			updatepr.setPr_ns(totalempns);
			updatepr.setPr_ta(totalempta);
			updatepr.setPr_td(totalemptd);
			updatepr.setPr_tp(prlist.get().getPr_tp());
			updatepr.setPr_wd(prlist.get().getPr_wd());
			updatepr.setPr_wr(prlist.get().getPr_wr());
			updatepr.setPr_wm(prlist.get().getPr_wm());
			prRep.save(updatepr);
			if(pdids.size() != 1) {
				System.out.println("----------- 전체상여계산");
				return result;
			}else {
				System.out.println("----------- 개인상여계산");
				return prM.select_spes(prlist.get().getPr_wm()); 
			}
		}else { // 추가 코드 수정 > 근태마감할때 
			
			return null;
		}
	}

	public List<Map<String, Object>> select_givebnone(List<PR_calculationMDTO> calbndata) {

//		 사원 한명 값 계산 다시 해서 업데이트하고 그 후 모든 데이터 넘겨주기
		PR updatepr = new PR();
		PRDetail updatepd = new PRDetail();
		
		Long prid = calbndata.get(0).getPrid();
		Long pdid = Long.parseLong(calbndata.get(0).getPdid().get(0));
		String bn = calbndata.get(0).getBonus();
		
//		1. 사원 한명의 근태&pd값 다 들고오기 & 해당월의 pr값도 들고오기
		Optional<PR> prlist = prRep.findById(prid);
		Optional<PRDetail> pdlist = prdRep.findById(pdid);
		List<String> employee_cdList = new ArrayList<>();
		employee_cdList.add(pdlist.get().getEmployee_cd());
		String[] prwms = prlist.get().getPr_wm().split("-");
		String prwmyear = prwms[0];
		String prwm = prwms[1];
		Map<String, Object> getwtemp = prM.getwt(employee_cdList, prwm, prwmyear);
//		2. 기존 pr값의 ta td ns 다 사원의 값을 빼기
		BigDecimal beforeta = prlist.get().getPr_ta().subtract(pdlist.get().getPrdetail_ta());
		BigDecimal beforetd = prlist.get().getPr_td().subtract(pdlist.get().getPrdetail_td());
		BigDecimal beforens = prlist.get().getPr_ns().subtract(pdlist.get().getPrdetail_rs());
		
//		3. 기존 bn값에 추가로 주는 bn추가해서 급여계산 다시하고 pd업데이트
		String employee_cd = pdlist.get().getEmployee_cd();
		String employee_nm = pdlist.get().getEmployee_nm();
		String BS = pdlist.get().getPrdetail_bs().multiply(new BigDecimal(12)).toString();
		String workingtime = getwtemp.get("WORKINGTIME").toString();
		String overworkingtime = getwtemp.get("OVERWORKINGTIME").toString();
		String nightworkingtime = getwtemp.get("NIGHTWORKINGTIME").toString();
		String weekendworkingtime = getwtemp.get("WEEKENDWORKINGTIME").toString();
		String holydayworkingtime = getwtemp.get("HOLYDAYWORKINGTIME").toString();
		String remainleave = getwtemp.get("ANNUAL_RA") == null ? "0" : getwtemp.get("ANNUAL_RA").toString();
		String bonus = pdlist.get().getPrdetail_bn() == null ? bn : pdlist.get().getPrdetail_bn().add(new BigDecimal(bn)).toString();
		
//		4. 2의 pr값에 새로 계산된 ta,td,ns 더해서 pr업데이트
		
		return null;
	}


	public List<Map<String, Object>> select_prmodaldata(String empcd, Long prid) {

		return prM.select_prmodaldata(empcd,prid);
	}

}
