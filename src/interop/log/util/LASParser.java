/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.log.util;

import interop.log.model.LogType;
import interop.log.model.ParsedLAS;
import interop.log.model.WellLog;
import interop.log.util.exceptions.WrongFormatException;
import interop.log.util.exceptions.WrongVersionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a simple LAS Log File parser. The method parseLas is the one responsible
 * to parse a .las file.
 * For now, supports only non-wrapped files from the version 2.0.
 *
 * @author Luan
 *
 * Updated by Lucas Hagen on 14/08/2017
 */
public class LASParser {

    private String filePath;
    private boolean parsed = false;

    private boolean wrap = true;
    private LASSection section;
    private ParsedLAS parsedLAS;
    private List<WellLog> logsList;

    private LASRetriever retriever;

    public LASParser(String path) {
        this.filePath = path;
    }

    /**
     * Parses a .LAS file, without any kind of validation. Thus, if the .las is
     * not structured exactly according to a LAS file definition, the file could not be
     * parsed.
     *
     * @return A ParsedLAS instance, containing the most useful attributes of a LAS.
     */
    public ParsedLAS getParsedLAS() throws WrongVersionException, WrongFormatException {
        if(parsed)
            return parsedLAS;

        parsedLAS = new ParsedLAS();
        parsedLAS.setFullPath(filePath);
        logsList = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            retriever = new LASRetriever();

            while ((line = bufferedReader.readLine()) != null) {
                if (retriever.isSection(line)) {
                    section = retriever.getSection(line);

                } else if (!retriever.isComment(line) && section != LASSection.OTHER) {
                    handleLine(line);

                }
            }

            bufferedReader.close();
        } catch (IOException ex) {
            Logger.getLogger(LASParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return parsedLAS;
    }

    private void handleLine(String line) throws WrongVersionException, WrongFormatException {
        switch (section) {
            case VERSION:
                handleVersion(line);
                break;
            case WELL:
                handleWell(line);
                break;
            case CURVE:
                handleCurve(line);
                break;
            case PARAMETER:
                handleParameter(line);
                break;
            case OTHER:
                handleOther(line);
                break;
            case ASCII:
                if (wrap)
                    handleData(line);
                else
                    handleUnwrappedData(line);
                break;
        }
    }

    private void handleVersion(String line) throws WrongVersionException, WrongFormatException {
        if (!retriever.isInfo(line))
            throw new WrongFormatException("Unexpected line in Version Section: '" + line + "'");

        String[] data = retriever.getInfo(line);

        if (data[0].equalsIgnoreCase("VERS")) {
            if (!data[2].equalsIgnoreCase("2.0"))
                throw new WrongVersionException("Wrong LAS Version! Expected: 2.0, Found: " + data[2]);

        } else if (data[0].equalsIgnoreCase("WRAP")) {
            if (data[2].equalsIgnoreCase("NO")) {
                wrap = false;
            } else {
                throw new WrongFormatException("LAS (WRAP: YES) not yet supported!");
            }
        }
    }

    private void handleWell(String line) throws WrongFormatException {
        if (!retriever.isInfo(line))
            throw new WrongFormatException("Unexpected line in Well Section: '" + line + "'");

        String[] data = retriever.getInfo(line);

        if (data[0].equalsIgnoreCase("STRT")) {
            parsedLAS.setStartDepthMeasureUnit(data[1]);
            parsedLAS.setStartDepth(Float.parseFloat(data[2]));

        } else if (data[0].equalsIgnoreCase("STOP")) {
            parsedLAS.setStopDepthMeasureUnit(data[1]);
            parsedLAS.setStopDepth(Float.parseFloat(data[2]));

        } else if (data[0].equalsIgnoreCase("STEP")) {
            parsedLAS.setStepValueMeasureUnit(data[1]);
            parsedLAS.setStepValue(Float.parseFloat(data[2]));

        } else if (data[0].equalsIgnoreCase("NULL")) {
            parsedLAS.setNullValue(Float.parseFloat(data[2]));

        } else if (data[0].equalsIgnoreCase("COMP")) {
            parsedLAS.setCompany(data[2]);

        } else if (data[0].equalsIgnoreCase("WELL")) {
            parsedLAS.setWellName(data[2]);

        }
    }

    private void handleCurve(String line) throws WrongFormatException {
        if (!retriever.isInfo(line))
            throw new WrongFormatException("Unexpected line in Curve Section: '" + line + "'");

        String[] data = retriever.getInfo(line);

        //DEPTH or TIME informations are not used yet.
        if (data[0].equalsIgnoreCase("DEPT")) {
            String measureUnity = data[1];

        } else if (data[0].equalsIgnoreCase("TIME")) {
            String measureUnity = data[1];

        } else {
            String logType = data[0];
            String logMeasureUnit = data[1];
            String logDescription = data[3];

            WellLog newLog = new WellLog();

            newLog.setNullValue(this.parsedLAS.getNullValue());
            newLog.setLogType(new LogType(logType, logDescription, logMeasureUnit));
            logsList.add(newLog);
        }
    }

    private void handleParameter(String line) {
        // NOTHING
    }

    private void handleOther(String line) {
        // NOTHING
    }

    private void handleData(String line) {
        if (!wrap) {
            handleUnwrappedData(line);
        }
    }

    private void handleUnwrappedData(String line) {
        retriever.isData(line);

        List<Float> data = retriever.getData(line);

        for (int i = 0; i < logsList.size(); i++) {
            float depth = data.get(0);
            float logValue = data.get(i + 1);
            logsList.get(i).addLogValuePair(depth, logValue);
        }

        this.parsedLAS.setLogsList(this.logsList);
    }

}
