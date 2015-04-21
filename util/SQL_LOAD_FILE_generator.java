/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Jiao
 */
public class SQL_LOAD_FILE_generator {

    public static void main(String[] args) {
        int n = 16;
        String text = "";

        for (int num = 1; num <= 20; num++) {
            for (int k = 1; k < 15; k++) {

                for (int temporalType = 0; temporalType <= 2; temporalType++) {
                    for (int spatialType = 0; spatialType <= 1; spatialType++) {

                        String file_name = "n" + n + "k" + k + "_" + num + "_temporalType" + temporalType + "_spatialType" + spatialType;

                        text = text + "LOAD DATA LOCAL INFILE\n";

                        text = text + "'C:/Users/Jiao/Desktop/results/" + file_name + ".txt'\n";

                        text = text + "INTO TABLE results\n";

                        text = text + "FIELDS TERMINATED BY '\\t' ESCAPED BY ''\n";

                        text = text + "LINES TERMINATED BY '\\n'\n";

                        text = text + "(landscapeID, period, complete, avgFitness, stdFitness)\n";

                        text = text + "SET K=" + k + ", temporalType=" + temporalType + ", spatialType=" + spatialType + ", INF='n" + n + "k"+ k + "_" + num + "'\n;\n\n\n";
                    }
                }

            }
        }
        
        for (int num = 1; num <= 1; num++) {
            for (int k = 0; k <=0; k++) {

                for (int temporalType = 0; temporalType <= 2; temporalType++) {
                    for (int spatialType = 0; spatialType <= 1; spatialType++) {

                        String file_name = "n" + n + "k" + k + "_" + num + "_temporalType" + temporalType + "_spatialType" + spatialType;

                        text = text + "LOAD DATA LOCAL INFILE\n";

                        text = text + "'C:/Users/Jiao/Desktop/results/" + file_name + ".txt'\n";

                        text = text + "INTO TABLE results\n";

                        text = text + "FIELDS TERMINATED BY '\\t' ESCAPED BY ''\n";

                        text = text + "LINES TERMINATED BY '\\n'\n";

                        text = text + "(landscapeID, period, complete, avgFitness, stdFitness)\n";

                        text = text + "SET K=" + k + ", temporalType=" + temporalType + ", spatialType=" + spatialType + ", INF='n" + n + "k"+ k + "_" + num + "'\n;\n\n\n";
                    }
                }

            }
        }
        
        for (int num = 1; num <= 1; num++) {
            for (int k = 15; k <=15; k++) {

                for (int temporalType = 0; temporalType <= 2; temporalType++) {
                    for (int spatialType = 0; spatialType <= 1; spatialType++) {

                        String file_name = "n" + n + "k" + k + "_" + num + "_temporalType" + temporalType + "_spatialType" + spatialType;

                        text = text + "LOAD DATA LOCAL INFILE\n";

                        text = text + "'C:/Users/Jiao/Desktop/results/" + file_name + ".txt'\n";

                        text = text + "INTO TABLE results\n";

                        text = text + "FIELDS TERMINATED BY '\\t' ESCAPED BY ''\n";

                        text = text + "LINES TERMINATED BY '\\n'\n";

                        text = text + "(landscapeID, period, complete, avgFitness, stdFitness)\n";

                        text = text + "SET K=" + k + ", temporalType=" + temporalType + ", spatialType=" + spatialType + ", INF='n" + n + "k"+ k + "_" + num + "'\n;\n\n\n";
                    }
                }

            }
        }

        try {
            File file = new File("C:\\Users\\Jiao\\Documents\\NetBeansProjects\\VirtualTeam\\SQL.sh");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(text);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
