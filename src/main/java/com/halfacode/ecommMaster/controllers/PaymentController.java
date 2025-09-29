package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.PaymentResponseDTO;
import com.halfacode.ecommMaster.services.PaymentService;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {


    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/upload-receipt")
    public ResponseEntity<String> uploadReceipt(
            @RequestParam("file") MultipartFile file,
            @RequestParam("senderAccount") String senderAccount,
            @RequestParam("amount") String expectedAmount) {

        try {
            // Save the file temporarily
            File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convertedFile);

            // Extract text using OCR
            String extractedText = extractTextFromImage(convertedFile);
            System.out.println("🔍 Extracted OCR Text: \n" + extractedText);

            // Validate extracted details dynamically
            if (validateTransactionDetails(extractedText, senderAccount, expectedAmount)) {
                return ResponseEntity.ok("✅ Payment Verified Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Verification Failed: Mismatched Data");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("⚠️ Error Processing Receipt");
        }
    }

    private String extractTextFromImage(File file) throws Exception {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata/");
        tesseract.setLanguage("ara+eng"); // Arabic + English support
        return tesseract.doOCR(file);
    }

    private boolean validateTransactionDetails(String extractedText, String senderAccount, String expectedAmount) {
        // Normalize extracted text by removing spaces & special characters
        String normalizedText = extractedText.replaceAll("[^\\p{L}\\p{N}]", "").toLowerCase();

        // Normalize expected values
        senderAccount = senderAccount.replaceAll("[^0-9]", "");
        expectedAmount = expectedAmount.replaceAll("[^0-9.]", "");
        String businessAccount = "0603113497600001"; // Remove spaces from fixed values
        String recipientName = "طارقمحمداحمدصافيبلوله"; // Remove spaces from Arabic name

        return normalizedText.contains(senderAccount)
                && normalizedText.contains(expectedAmount)
                && normalizedText.contains(businessAccount)
                && normalizedText.contains(recipientName);
    }

//    private boolean validateTransactionDetails(String extractedText, String senderAccount, String expectedAmount) {
//        String businessAccount = "0603 1134 9760 0001"; // Your business account (recipient)
//        String recipientName = "طارق محمداحمد صافي بلوله"; // Expected recipient name
//
//        return extractedText.contains(senderAccount)  // Dynamic sender account
//                && extractedText.contains(expectedAmount)  // Dynamic amount
//                && extractedText.contains(businessAccount) // Static business account
//                && extractedText.contains(recipientName); // Static recipient name
//    }
   /* private boolean validateTransactionDetails(String extractedText, String senderAccount, String expectedAmount) {
        String normalizedText = extractedText.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
        return normalizedText.contains(senderAccount.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))
                && normalizedText.contains(expectedAmount.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))
                && normalizedText.contains("0603113497600001")
                && normalizedText.contains("طارقمحمداحمدصافيبلوله");
    }*/

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentResponseDTO paymentResponse) {
        return paymentService.verifyPayment(paymentResponse);
    }


}
