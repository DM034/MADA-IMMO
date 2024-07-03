package immobilier.immobilier.tools;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class Tools {

    public static String formatSecondsToHHMMSS(Double seconds) {
        int hours = (int) (seconds.doubleValue() / 3600);
        int minutes = (int) ((seconds.doubleValue() % 3600) / 60);
        int remainingSeconds = (int) (seconds.doubleValue() % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    public String formatSecondsToHHMMSS(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    public static String formatTime(Object tempsFinObj) {
        if (tempsFinObj == null) {
            return null;
        }

        try {
            // Convertir l'Object en String
            String tempsFinStr = tempsFinObj.toString();

            // Définir le format original
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Définir le nouveau format pour obtenir uniquement l'heure
            SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm:ss");

            // Parser la date
            Date date = originalFormat.parse(tempsFinStr);

            // Formater la date pour obtenir uniquement l'heure
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static java.sql.Date convertStringToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate localDateTime = LocalDate.parse(dateStr, formatter);
            return java.sql.Date.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur: Format de date incorrect");
            return null;
        }
    }

    public static Timestamp convertStringToTimestamp(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur: Format de date incorrect");
            return null;
        }
    }

    public static String formatDate(String dateStr, String inputFormat, String outputFormat) throws Exception {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
        try {
            Date parsedDate = inputDateFormat.parse(dateStr);
            return outputDateFormat.format(parsedDate);
        } catch (Exception e) {
            throw e;
        }
    }

    public static java.sql.Date addDays(java.sql.Date date, int days) {
        LocalDate localDate = date.toLocalDate();
        LocalDate newDate = localDate.plusDays(days);
        java.sql.Date outputDate = java.sql.Date.valueOf(newDate);
        return outputDate;
    }

    public static String formatChiffreAffaires(double chiffreAffaires) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(chiffreAffaires);
    }

    public static String formatNumber(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }

    public static String formatThousand(Double number) {
        DecimalFormat format = new DecimalFormat("#,###.##");
        return format.format(number);
    }

    public static String formatThousand(double number) {
        DecimalFormat format = new DecimalFormat("#,###.##");
        return format.format(number);
    }

    public static String formatThousand(float number) {
        DecimalFormat format = new DecimalFormat("#,###.##");
        return format.format(number);
    }

    @SuppressWarnings("deprecation")
    public static String formatDate(Date date) {
        return date.toLocaleString().replaceAll("00:00:00", "");
    }

    public static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString().replaceAll("'", "''"));
                currentValue.setLength(0); // Réinitialiser la chaîne pour la prochaine valeur
            } else {
                currentValue.append(c);
            }
        }

        values.add(currentValue.toString()); // Ajouter la dernière valeur
        return values.toArray(new String[0]);
    }

    public static double convertirEnDouble(String dureeString) {
        String[] elements = dureeString.split(":");
        int heures = Integer.parseInt(elements[0]);
        int minutes = Integer.parseInt(elements[1]);
        int secondes = Integer.parseInt(elements[2]);

        double dureeEnHeures = heures + (double) minutes / 60 + (double) secondes / 3600;
        return dureeEnHeures;
    }

}
