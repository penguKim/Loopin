package com.itwillbs.c4d2412t3p1.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.PRCalDTO;
import com.itwillbs.c4d2412t3p1.domain.PRDTO;
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

	public List<PRDTO> calculatingMachine(BigDecimal BS, BigDecimal overworkingtime, BigDecimal nightworkingtime, BigDecimal weekendworkingtime, BigDecimal holydayworkingtime, BigDecimal leastannual, String B, String D) throws ScriptException {

		List<PRCode> Bformulas = prcRep.findByPrcodeIdStartingWith(B);
		List<PRCode> Dformulas = prcRep.findByPrcodeIdStartingWith(D);
		List<PRDTO> calculatedSalary = new ArrayList<>();
		List<PRCalDTO> calculated = new ArrayList<>();
		
		BigDecimal workingtime = new BigDecimal(209);
		BigDecimal totalSalary = BigDecimal.ZERO; //총지급액 
		BigDecimal totalDeduction = BigDecimal.ZERO; //총공제액
		BigDecimal totalNonTax = BigDecimal.ZERO; //총비과세
		
		//수당계산
		for (PRCode Bformula : Bformulas) {
			log.info("계산확인"+Bformula);
			BigDecimal calculatedAmount = calculateSalaryWithSpEL(Bformula.getPrcode_fl(), BS,overworkingtime,nightworkingtime,weekendworkingtime,holydayworkingtime,leastannual, workingtime );
			
			calculated.add(new PRCalDTO(Bformula.getPrcode_nm(),calculatedAmount));
			
			totalSalary = totalSalary.add(calculatedAmount);
			if(Bformula.isPrcode_nt()) {
				totalNonTax = totalNonTax.add(calculatedAmount);
			}
		}
		
		//공제계산
		for (PRCode Dformula : Dformulas) {
			BigDecimal calculatedAmount = calculateSalaryWithSpEL(Dformula.getPrcode_fl(), totalSalary, totalNonTax );
			
			calculated.add(new PRCalDTO(Dformula.getPrcode_nm(),calculatedAmount));
			totalDeduction =totalDeduction.add(calculatedAmount);
		}
		
		BigDecimal netSalary = totalSalary.subtract(totalDeduction);
		
		PRDTO prDTO = new PRDTO();
		prDTO.setNetSalary(netSalary);
		prDTO.setCalculated(calculated);
		
		calculatedSalary.add(prDTO);
		
		return calculatedSalary;
	}

	private BigDecimal calculateSalaryWithSpEL(String prcode_fl, BigDecimal BS, BigDecimal overworkingtime,
			BigDecimal nightworkingtime, BigDecimal weekendworkingtime, BigDecimal holydayworkingtime,
			BigDecimal leastannual, BigDecimal workingtime) {

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
		
		// SpEL을 사용하여 수식을 평가
		return parser.parseExpression(prcode_fl).getValue(context, BigDecimal.class);
	}

	private BigDecimal calculateSalaryWithSpEL(String prcode_fl, BigDecimal totalSalary, BigDecimal totalNonTax) {
		
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();

		// 수식에서 사용할 변수를 context에 등록
		context.setVariable("totalSalary", totalSalary);
		context.setVariable("totalNonTax", totalNonTax);
		return parser.parseExpression(prcode_fl).getValue(context, BigDecimal.class);
	}

}
