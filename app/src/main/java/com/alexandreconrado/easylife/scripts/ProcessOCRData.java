package com.alexandreconrado.easylife.scripts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessOCRData {
    public ProcessOCRData(){

    }
    public String ProcessData(String ocrOutput){
        double aux = parseTotalAmount(ocrOutput);
        return String.valueOf(aux);
    }
    public static double parseTotalAmount(String ocrOutput) {
        // Patterns for extracting the total amount in various formats
        Pattern[] amountPatterns = {
                Pattern.compile("TOTAL PEDIDO:[\\s\\S]*?(\\d+\\.\\d+) €"),
                Pattern.compile("TOTAL[\\s\\S]*?(\\d+\\.\\d+) €"),
                Pattern.compile("Eur (\\d+,\\d+)"),
                Pattern.compile("TOTAL INCIDENCIAS:[\\s\\S]*?\\n(\\d+,\\d+)\\n(\\d+,\\d+)\\n(\\d+)%\\n(\\d+,\\d+)\\n(\\d+,\\d+)"),
                Pattern.compile("TUAL:[\\s\\S]*?\\n(\\d+,\\d+)\\n(\\d+%)\n(\\d+,\\d+)\\n(\\d+,\\d+)"),
                Pattern.compile("eur (\\d+,\\d+)"),
                Pattern.compile("ur (\\d+,\\d+)"),
                Pattern.compile("iur (\\d+,\\d+)"),
                Pattern.compile("(\\d+\\.\\d+)€")
        };

        for (Pattern amountPattern : amountPatterns) {
            Matcher amountMatcher = amountPattern.matcher(ocrOutput);

            if (amountMatcher.find()) {
                return Double.parseDouble(amountMatcher.group(1).replace(",", "."));
            }
        }

        return 0.0; // Return 0.0 if the total amount is not found
    }
}
