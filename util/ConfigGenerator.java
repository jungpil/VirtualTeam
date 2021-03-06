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
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Jiao
 */
public class ConfigGenerator {

    public static void main(String[] args) {
        int n = 16;
        String username = args[0];
        String parent_path = "";
        String path = "";
        int SpatialType=0;  //0:00 no loss, no distortion,  1:01 no loss, with distortion, 2:10 with loss, with distortion,  3:11 with loss, with distortion
        int TemporalType;//0: 0.2 no temp diff  1: 0.2 with temp diff no overlap  22: 2 with temp diff some overlap,   3: 0.8 no temp diff  4: 0.8 with temp diff no overlap  5: 0.8 with temp diff some overlap   

        if (username.equals("anqing")) {
            parent_path = "/home/a/anqing/VirtualTeam/";
            path = "/home/a/anqing/VirtualTeam/conf/";
            SpatialType = 0;
            // set spatialType here - loss = y, distortion = y (type 1)
        } else if (username.equals("jungpil")) {
            parent_path = "/home/j/jungpil/VirtualTeam/";
            path = "/home/j/jungpil/VirtualTeam/conf/";
            SpatialType = 1;
            // set spatialType here - loss = n, distortion = y (type 2)
        } else if (username.equals("yuewu")) {
            parent_path = "/home/y/yuewu/VirtualTeam/";
            path = "/home/y/yuewu/VirtualTeam/conf/";
            SpatialType = 2;
            // set spatialType here - loss = y, distortion = n (type 3)
        } else if (username.equals("kristina")) {
            parent_path = "/home/k/kristina/VirtualTeam/";
            path = "/home/k/kristina/VirtualTeam/conf/";
            SpatialType = 3;
            // set spatialType here - loss = n, distortion = n (type 0)
        }
        int numInf = 10;
        for (int k = 0; k < 16; k++) {
            if (k == 0 || k == 15) {
                numInf = 1;
            }else{
                numInf =10;
            }
            for (int num = 1; num <= numInf; num++) {
                int numOrgs = 100;
                for (TemporalType = 0; TemporalType <= 5; TemporalType++) { 
                        double lossPossibility=0;
                        double distortionPossibility=0;                        
                        int temporalType=0;
                        int spatialType=0;
                        double alignment=0;
                        
                        if(SpatialType==0){
                            spatialType=0;
                            lossPossibility=0;
                            distortionPossibility=0;
                        }else if(SpatialType==1){
                            spatialType=1;
                            lossPossibility=0;
                            distortionPossibility=0.25;
                        }else if(SpatialType==2){
                            spatialType=1;
                            lossPossibility=0.25;
                            distortionPossibility=0;
                        }else{
                            spatialType=1;
                            lossPossibility=0.25;
                            distortionPossibility=0.25;
                        }
                        
                        
                        if(TemporalType==0){
                            alignment=0.2;
                            temporalType=0;
                        }else if(TemporalType==1){
                            alignment=0.2;
                            temporalType=1;
                        }else if(TemporalType==2){
                            alignment=0.2;
                            temporalType=2;
                        }else if(TemporalType==3){
                            alignment=0.8;
                            temporalType=0;
                        }else if(TemporalType==4){
                            alignment=0.8;
                            temporalType=1;
                        }else{
                            alignment=0.8;
                            temporalType=2;
                        }
                        
                        // String parent_path="/home/a/anqing/VirtualTeam/";
                        // String path="C:\\Users\\Jiao\\Documents\\NetBeansProjects\\VirtualTeam\\src\\conf\\";
                        String file_name = "n" + n + "k" + k + "_" + num + "_TemporalType" + TemporalType + "_SpatialType" + SpatialType;

                        String text = "";

                        text = text + "periods=100\n";
                        text = text + "runs=100\n";
                        text = text + "outfile=" + parent_path + "results/" + file_name + ".txt\n";
                        text = text + "influenceMatrix=" + parent_path + "inf/" + "n" + n + "k" + k + "_" + num + ".txt\n";
                        text = text + "numOrgs=" + numOrgs + "\n";
                        text = text + "N=" + n + "\n";
                        text = text + "numSubOrgs=2\n";
                        text = text + "reportLevel=summary\ndebug=false\n";
                        text = text + "temporalType=" + temporalType + "\n";
                        text = text + "spatialType=" + spatialType + "\n";
                        text = text + "lossPossibility=" + lossPossibility + "\n";
                        text = text + "distortionPossibility=" + distortionPossibility + "\n";
                        text = text + "alignment=" + alignment + "\n";

                        try {
                            File file = new File(path + file_name + ".conf");
                            BufferedWriter output = new BufferedWriter(new FileWriter(file));
                            output.write(text);
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                }

            }
        }

    }
}
