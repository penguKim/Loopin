package com.itwillbs.c4d2412t3p1.controller;

import java.io.OutputStream;

import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itwillbs.c4d2412t3p1.repository.ApprovalRepository;
import com.itwillbs.c4d2412t3p1.service.ApprovalService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class PdfController {

	private final ApprovalRepository approvalRepository;
	
	private final ApprovalService approvalService;

	private final SpringTemplateEngine templateEngine;

	@GetMapping("/generatePdf")
    public void generatePdf(@RequestParam("approval_cd") String approval_cd,
                            HttpServletResponse response) throws Exception {
        // 1. ApprovalService의 getApprovalDataForPdf 메소드를 통해 PDF에 필요한 데이터를 가져옵니다.
        Map<String, Object> pdfData = approvalService.getApprovalDataForPdf(approval_cd);

        // 2. Thymeleaf Context에 데이터를 세팅하고 템플릿을 처리합니다.
        Context context = new Context();
        context.setVariables(pdfData);
        String htmlContent = templateEngine.process("approval/sample/pdfTemplate", context);

        // 3. HTTP 응답 헤더 설정 (PDF 출력)
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=approval_" + approval_cd + ".pdf");

        // 4. FlyingSaucer(ITextRenderer)를 이용하여 HTML을 PDF로 변환 (한글 지원: NanumGothic 폰트 등록)
        try (OutputStream outputStream = response.getOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            // 폰트 파일 위치: src/main/resources/static/assets/fonts/NanumGothic.ttf
            URL fontUrl = getClass().getResource("/static/assets/fonts/NanumGothic.ttf");
            if (fontUrl != null) {
                renderer.getFontResolver().addFont(
                        fontUrl.toString(),
                        com.lowagie.text.pdf.BaseFont.IDENTITY_H,
                        com.lowagie.text.pdf.BaseFont.EMBEDDED);
            } else {
                throw new RuntimeException("폰트 파일을 찾을 수 없습니다.");
            }

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
        }
    }

}
