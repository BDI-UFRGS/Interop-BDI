/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.log.util;

import interop.log.model.LogType;
import interop.log.model.ParsedLAS;
import interop.log.model.WellLog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a simple LAS Log File parser. The method parseLas is the one responsible
 * to parse a .las file.
 * For now, supports only non-wrapped files from the version 2.0.
 *
 * @author Luan
 */
public class LASParser {
    boolean wrap = true;
    boolean insideVersion = false;
    boolean insideWell = false;
    boolean insideCurve = false;
    boolean insideParameter = false;
    boolean insideOther = false;
    boolean insideData = false;
    private ParsedLAS parsedLAS;
    private List<WellLog> logsList;

    /**
     * Parses a .LAS file, without any kind of validation. Thus, if the .las is
     * not structured exactly according to a LAS file definition, the file could not be
     * parsed.
     *
     * @param pathFile String containing the path to a .las file.
     * @return A ParsedLAS instance, containing the most useful attributes of a LAS.
     */
    public ParsedLAS parseLAS(String pathFile) {
        parsedLAS = new ParsedLAS();
        parsedLAS.setFullPath(pathFile);
        logsList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(pathFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                if(line.startsWith("#")) {
                    continue;
                }

                if (line.startsWith("~V")) {
                    setInsideFalse();
                    insideVersion = true;
                    System.out.println("LAS: V");
                }

                if (line.startsWith("~W")) {
                    setInsideFalse();
                    insideWell = true;
                    System.out.println("LAS: W");
                }

                if (line.startsWith("~C")) {
                    setInsideFalse();
                    insideCurve = true;
                    System.out.println("LAS: C");
                }
                if (line.startsWith("~P")) {
                    setInsideFalse();
                    insideParameter = true;
                    System.out.println("LAS: P");
                }
                if (line.startsWith("~O")) {
                    setInsideFalse();
                    insideOther = true;
                    System.out.println("LAS: O");
                }
                if (line.startsWith("~A")) {
                    setInsideFalse();
                    insideData = true;
                    System.out.println("LAS: A");
                }

                if (!line.startsWith("~") && !line.isEmpty()) {
                    System.out.println("LAS: " + line);

                    if (insideVersion)
                        handleVersion(line);

                    if (insideWell)
                        handleWell(line);

                    if (insideCurve)
                        handleCurve(line);

                    if (insideParameter)
                        handleParameter(line);

                    if (insideOther)
                        handleOther(line);

                    if (insideData)
                        handleData(line);
                }

            }

            bufferedReader.close();

        } catch (IOException ex) {
            Logger.getLogger(LASParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return parsedLAS;
    }

    private void handleVersion(String line) {
        line = line.trim();

        if (line.startsWith("VERS.") || line.startsWith("VERS .")) {
            if (line.contains("2.0")) {
                this.parsedLAS.setVersion("2.0");
            } else {
                System.out.println("Version not supported yet.");
            }
        }

        if (line.startsWith("WRAP.") || line.startsWith("WRAP .")) {
            String[] splitLine = line.split("\\.");
            splitLine = splitLine[1].split(":");
            String wrapped = splitLine[0].trim();

            if (wrapped.equalsIgnoreCase("yes"))
                wrap = true;
            else if (wrapped.equalsIgnoreCase("no"))
                wrap = false;
        }
    }

    private void setInsideFalse() {
        insideVersion = false;
        insideWell = false;
        insideCurve = false;
        insideParameter = false;
        insideOther = false;
        insideData = false;
    }

    private void handleWell(String line) {

        line = line.trim();

        String[] splitLine;

        if (line.startsWith("STRT.") || line.startsWith("STRT .")) {
            splitLine = line.split("\\.", 2);
            String[] temp = splitLine[1].split(" ", 2);
            String measureUnity = temp[0];
            parsedLAS.setStartDepthMeasureUnit(measureUnity);

            splitLine = line.split(" ", 2);
            splitLine = splitLine[1].split(":");
            String startDepthValue = splitLine[0].trim();
            parsedLAS.setStartDepth(Float.parseFloat(startDepthValue));
        }

        if (line.startsWith("STOP.") || line.startsWith("STOP .")) {
            splitLine = line.split("\\.", 2);
            String[] temp = splitLine[1].split(" ", 2);
            String measureUnity = temp[0];
            parsedLAS.setStopDepthMeasureUnit(measureUnity);

            splitLine = line.split(" ", 2);
            splitLine = splitLine[1].split(":");
            String stopDepthValue = splitLine[0].trim();
            parsedLAS.setStopDepth(Float.parseFloat(stopDepthValue));
        }

        if (line.startsWith("STEP.") || line.startsWith("STEP .")) {
            splitLine = line.split("\\.", 2);
            String[] temp = splitLine[1].split(" ", 2);
            String measureUnity = temp[0];
            parsedLAS.setStepValueMeasureUnit(measureUnity);

            splitLine = line.split(" ", 2);
            splitLine = splitLine[1].split(":");
            String step = splitLine[0].trim();

            parsedLAS.setStepValue(Float.parseFloat(step));
        }

        if (line.startsWith("NULL.") || line.startsWith("NULL .")) {
            splitLine = line.split("\\.");
            splitLine = splitLine[1].split(":");
            String nullValue = splitLine[0].trim();
            parsedLAS.setNullValue(Float.parseFloat(nullValue));
        }

        if (line.startsWith("COMP.") || line.startsWith("COMP .")) {
            splitLine = line.split("\\.");
            splitLine = splitLine[1].split(":");
            String company = splitLine[0].trim();
            parsedLAS.setCompany(company);
        }

        if (line.startsWith("WELL.") || line.startsWith("WELL .")) {
            splitLine = line.split("\\.");
            splitLine = splitLine[1].split(":");
            String well = splitLine[0].trim();
            parsedLAS.setWellName(well);
        }
    }

    private void handleCurve(String line) {

        line = line.trim();
        String[] splitLine;

        //DEPTH or TIME informations are not used yet.
        if (line.startsWith("DEPT")) {
            splitLine = line.split("\\.", 2);
            String[] temp = splitLine[1].split(" ", 2);
            String measureUnity = temp[0];
        } else {
            if (line.startsWith("TIME")) {
                splitLine = line.split("\\.", 2);
                String[] temp = splitLine[1].split(" ", 2);
                String measureUnity = temp[0];
            } else {
                splitLine = line.split("\\.", 2);
                String logType = splitLine[0];

                splitLine = splitLine[1].split(":", 2);
                String logMeasureUnit = splitLine[0].trim();
                String logDescription = splitLine[1].trim();

                WellLog newLog = new WellLog();

                newLog.setNullValue(this.parsedLAS.getNullValue());
                newLog.setLogType(new LogType(logType, logDescription, logMeasureUnit));
                logsList.add(newLog);
            }
        }
    }

    private void handleParameter(String line) {

    }

    private void handleOther(String line) {

    }

    private void handleData(String line) {
        if (!wrap) {
            handleUnwrappedData(line);
        }
    }

    private void handleUnwrappedData(String line) {
        line = line.trim();

        String splitLine[];

        while (line.contains("  ")) {
            line = line.replaceAll("  ", " ");
        }

        splitLine = line.split(" ");

        for (int i = 0; i < logsList.size(); i++) {

            float depth = Float.parseFloat(splitLine[0]);
            float logValue = Float.parseFloat(splitLine[i + 1]);
            logsList.get(i).addLogValuePair(depth, logValue);

        }
        this.parsedLAS.setLogsList(this.logsList);
    }

    public void extractData(String line) {
        Pattern data = Pattern.compile("^[ \t]*([a-zA-Z]+)[ ]*" +      // MNEMONIC
                "\\.([^ \\t]+)?[ \\t]+" +                           // DOT (SEPARATOR) + measurement unit (may not exist)
                "([^ \\t](?:.*[^ \\t])?)[ \\t]*" +                  // DATA
                ":(?:[ \\t]*([^ \\t](?:.*[^ \\t])?))?[ \\t]*$");    // COLON + DESCRIPTION (may not exist)
        Matcher matcher = data.matcher(line);

        if()
    }

}
