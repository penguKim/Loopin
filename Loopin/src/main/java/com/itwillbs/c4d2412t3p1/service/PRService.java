package com.itwillbs.c4d2412t3p1.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.PRCode;
import com.itwillbs.c4d2412t3p1.mapper.PRMapper;
import com.itwillbs.c4d2412t3p1.repository.PRCodeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class PRService {
	
	private final PRMapper prM;
	private final PRCodeRepository prcRep;
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

	public List<PRDTO> calculatingMachine(String bS2, String overworkingtime2, String nightworkingtime2, String weekendworkingtime2, String holydayworkingtime2, String leastannual2, String bonus2) throws ScriptException {
		log.info("======================================================================bs2====================="+bS2);
		String B = "B";
		String D = "D";
		
		List<PRCode> formulas = prcRep.findAll();
		List<PRCode> Bformulas = prcRep.findByPrcodeIdStartingWith(B);
		List<PRCode> Dformulas = prcRep.findByPrcodeIdStartingWith(D);
		List<PRDTO> calculatedSalary = new ArrayList<>();
		List<PRCalDTO> calculated = new ArrayList<>();

		BigDecimal BS = new BigDecimal(bS2);
		BigDecimal overworkingtime = new BigDecimal(overworkingtime2);
		BigDecimal nightworkingtime = new BigDecimal(nightworkingtime2);
		BigDecimal weekendworkingtime = new BigDecimal(weekendworkingtime2);
		BigDecimal holydayworkingtime =new BigDecimal(holydayworkingtime2);
		BigDecimal leastannual = (leastannual2 == null)?BigDecimal.ZERO:new BigDecimal(leastannual2);
		BigDecimal bonus = (bonus2 == null)?BigDecimal.ZERO:new BigDecimal(bonus2);
		BigDecimal workingtime = new BigDecimal(209);
		BigDecimal D_GG = BigDecimal.ZERO;
		BigDecimal totalSalary = BigDecimal.ZERO; //총지급액 
		BigDecimal totalDeduction = BigDecimal.ZERO; //총공제액
//		BigDecimal totalNetsalary = BigDecimal.ZERO; //실지급액
		BigDecimal totalNonTax = BigDecimal.ZERO; //총비과세
		log.info("======================================================================BS====================="+BS);
		
		// 전체계산
		for (PRCode formula : formulas) {
			log.info("계산확인"+formula);
			BigDecimal calculatedAmount = calculateSalaryWithSpEL(formula.getPrcode_fl(), BS,overworkingtime,nightworkingtime,weekendworkingtime,holydayworkingtime,leastannual,bonus,workingtime );
			
			calculatedAmount = calculatedAmount.setScale(0, RoundingMode.HALF_UP); // 소수점 반올림
			
			calculated.add(new PRCalDTO(formula.getPrcode_id(),calculatedAmount));
			
			if(formula.getPrcode_id().startsWith(B)) {
				totalSalary = totalSalary.add(calculatedAmount);
			}
			if(formula.getPrcode_id().startsWith(D)) {
				totalDeduction =totalDeduction.add(calculatedAmount);
			}
			if(formula.isPrcode_nt()) {
				totalNonTax = totalNonTax.add(calculatedAmount);
			}
		}
		
		//수당계산
		for (PRCode Bformula : Bformulas) {
			log.info("계산확인"+Bformula);
//			BigDecimal calculatedAmount = calculateSalaryWithSpEL(Bformula.getPrcode_fl(), data, workingtime );
			BigDecimal calculatedAmount = calculateSalaryWithSpEL(Bformula.getPrcode_fl(), BS,overworkingtime,nightworkingtime,weekendworkingtime,holydayworkingtime,leastannual,bonus,workingtime );
			
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
			BigDecimal calculatedD = calculateSalaryWithSpEL(Dformula.getPrcode_fl(), totalSalary, totalNonTax );
			
			calculated.add(new PRCalDTO(Dformula.getPrcode_id(),calculatedD));
			if(Dformula.getPrcode_id().contains("D_GG")) {
				D_GG = D_GG.add(calculatedD);
			}
			totalDeduction =totalDeduction.add(calculatedD);
		}
		
		BigDecimal netSalary = totalSalary.subtract(totalDeduction);
		
		PRDTO prDTO = new PRDTO();
		prDTO.setRS(netSalary);
		prDTO.setTA(totalSalary);
		prDTO.setTD(totalDeduction);
		prDTO.setCalculated(calculated);
		
		calculatedSalary.add(prDTO);
		
		return calculatedSalary;
	}

	private BigDecimal calculateSalaryWithSpEL(String prcode_fl, BigDecimal BS, BigDecimal overworkingtime,
			BigDecimal nightworkingtime, BigDecimal weekendworkingtime, BigDecimal holydayworkingtime,
			BigDecimal leastannual,BigDecimal bonus , BigDecimal workingtime) {
		log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~BS"+BS);
		log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FORMULA : "+prcode_fl);
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		
		
	    // 수식에서 사용할 변수를 context에 등록
		context.setVariable("BS", BS);
	    context.setVariable("workingtime", workingtime);
	    context.setVariable("overworkingtime", overworkingtime);
	    context.setVariable("nightworkingtime", nightworkingtime);
	    context.setVariable("weekendworkingtime", weekendworkingtime);
	    context.setVariable("holydayworkingtime", holydayworkingtime);
	    context.setVariable("leastannual", leastannual);
	    context.setVariable("BN", bonus);
		log.info("context variables: " + context.lookupVariable("BS"));
		log.info("context variables: " + context.lookupVariable("workingtime"));
		log.info("context variables: " + context.lookupVariable("overworkingtime"));
		log.info("context variables: " + context.lookupVariable("nightworkingtime"));
		log.info("context variables: " + context.lookupVariable("weekendworkingtime"));
		log.info("context variables: " + context.lookupVariable("holydayworkingtime"));
		log.info("context variables: " + context.lookupVariable("leastannual"));
		log.info("context variables: " + context.lookupVariable("BN"));

	    
		try {
//	        if ("BS".equals(prcode_fl)) {
//	            log.info("Returning BS directly: " + BS);
//	            return BS;  // BS 값을 직접 반환
//	        }
	        
			// 변수명이 정확히 치환되도록 정규 표현식으로 처리
	        prcode_fl = prcode_fl.replaceAll("\\bBS\\b", "#BS");
	        prcode_fl = prcode_fl.replaceAll("\\bworkingtime\\b", "#workingtime");
	        prcode_fl = prcode_fl.replaceAll("\\boverworkingtime\\b", "#overworkingtime");
	        prcode_fl = prcode_fl.replaceAll("\\bnightworkingtime\\b", "#nightworkingtime");
	        prcode_fl = prcode_fl.replaceAll("\\bweekendworkingtime\\b", "#weekendworkingtime");
	        prcode_fl = prcode_fl.replaceAll("\\bholydayworkingtime\\b", "#holydayworkingtime");
	        prcode_fl = prcode_fl.replaceAll("\\bleastannual\\b", "#leastannual");
	        prcode_fl = prcode_fl.replaceAll("\\bBN\\b", "#BN");

			
	        BigDecimal result = parser.parseExpression(prcode_fl).getValue(context, BigDecimal.class);
	        log.info("SpEL evaluated result: " + result);
	        return result;
//	        return parser.parseExpression(prcode_fl).getValue(context, BigDecimal.class);
	    } catch (SpelEvaluationException e) {
	        log.info("SpEL Evaluation Error: " + e.getMessage());
	        throw e;  // 예외를 다시 던져서 문제를 상위에서 처리하도록 할 수 있습니다.
	    }
	}

	private BigDecimal calculateSalaryWithSpEL(String prcode_fl, BigDecimal totalSalary, BigDecimal totalNonTax) {
		
		log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FORMULA : "+prcode_fl);
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();

		// 수식에서 사용할 변수를 context에 등록
		context.setVariable("totalSalary", totalSalary);
		context.setVariable("totalNonTax", totalNonTax);
		log.info("context variables: " + context.lookupVariable("totalSalary"));
		log.info("context variables: " + context.lookupVariable("totalNonTax"));
		
		try {
	        
			// 변수명이 정확히 치환되도록 정규 표현식으로 처리
	        prcode_fl = prcode_fl.replaceAll("\\btotalSalary\\b", "#totalSalary");
	        prcode_fl = prcode_fl.replaceAll("\\btotalNonTax\\b", "#totalNonTax");

	        BigDecimal result = parser.parseExpression(prcode_fl).getValue(context, BigDecimal.class);
	        log.info("SpEL evaluated result: " + result);
//		return parser.parseExpression(prcode_fl).getValue(context, BigDecimal.class);
	        return result;
//	        return parser.parseExpression(prcode_fl).getValue(context, BigDecimal.class);
	    } catch (SpelEvaluationException e) {
	        log.info("SpEL Evaluation Error: " + e.getMessage());
	        throw e;  // 예외를 다시 던져서 문제를 상위에서 처리하도록 할 수 있습니다.
	    }
	}

	public List<Employee> select_empworklastmth() {
		List<Employee> list = prM.select_empworklastmth();

		return list;
	}

	public List<Map<String, Object>> select_worktimelastmth(List<String> emp_cdlist) {
		return prM.select_wokringtimeformth(emp_cdlist);
	}

}
