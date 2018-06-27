package com.meritatech.myrewardzpos.utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.meritatech.myrewardzpos.data.LogRecord;
import com.meritatech.myrewardzpos.database.SchemaGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import org.apache.commons.io.FilenameUtils;

import dalvik.system.DexFile;

/**
 * Created by Waithera on 12/2/2017.
 */

public class Utilities {

    public  static  void LogException(Exception ex)
    {
        LogRecord lr = new LogRecord();
        lr.EventDate = new Date();
        lr.ErrorMessage = ex.getMessage();
        lr.save();
    }

    public static void LogError(String error)
    {

    }
    static SQLiteDatabase sqLiteDatabase;

    static boolean mIsInitialized = false;

    public static void InitializeDB(Context context) {
        ArrayList<String> classNames = Utilities.getClassesOfPackage(context, "com.meritatech.com.myrewardzpos.data");
        if (mIsInitialized == false) {
            doInitializeDB(classNames, context);
            mIsInitialized = true;
        }
    }

    static void doInitializeDB(ArrayList<String> classNames, Context context) {
        generateDBTables(classNames, context);
    }


    static void generateDBTables(ArrayList<String> classNames, Context context) {
        SchemaGenerator schemaGenerator = new SchemaGenerator(context);

        schemaGenerator.createTables(classNames);

    }


    static ArrayList<String> allPackageClassNames;

    public static ArrayList<String> getClassesOfPackage(Context context, String searchPackageName) {

        if (allPackageClassNames == null) {
            String packageName = Utilities.class.getPackage().getName();
            allPackageClassNames = new ArrayList<>();
            try {
                String directory = FilenameUtils.getPath(context.getPackageCodePath());
                File fdir = new File(directory);
                File[] allFiles = fdir.listFiles();

                if (allFiles == null) {
                    return allPackageClassNames;
                }
                for (File file : allFiles) {
                    String fileName = file.getAbsolutePath();
                    if (fileName.endsWith(".apk")) {
                        try {
                            DexFile dex = new DexFile(fileName);
                            Enumeration<String> entries = dex.entries();
                            while (entries.hasMoreElements()) {
                                String entry = entries.nextElement();

                                if (entry.startsWith(packageName)) {
                                    allPackageClassNames.add(entry);
                                }
                                Log.d("MyRewardzPOS", entry);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        ArrayList<String> resultList = new ArrayList<>();

        for (String className : allPackageClassNames) {
        }
        return allPackageClassNames;
    }




}
