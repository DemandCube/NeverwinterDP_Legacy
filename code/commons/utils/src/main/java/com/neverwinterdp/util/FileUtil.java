package com.neverwinterdp.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * $Author: Tuan Nguyen$
 **/
public class FileUtil {
  static public int emptyFolder(File folder, boolean ignoreCannotDel) throws IOException {
    int counter = 0;
    if (folder.exists() && folder.isDirectory()) {
      File[] child = folder.listFiles();
      for (int i = 0; i < child.length; i++) {
        File file = child[i];
        if (file.isDirectory()) counter += emptyFolder(file, ignoreCannotDel);
        boolean result = file.delete();
        if (!result && !ignoreCannotDel) {
          if(!ignoreCannotDel) {
            throw new IOException("Cannot delete " + file.getAbsolutePath());
          } else {
            file.deleteOnExit() ;
          }
        } else {
          counter++;
        }
      }
    }
    return counter;
  }

  static public int remove(File file, boolean ignoreCannotDelete) throws Exception {
    int counter = 0;
    if (file.exists()) {
      if (file.isDirectory()) {
        counter += emptyFolder(file, ignoreCannotDelete);
      }
      boolean result = file.delete();
      if (!result && !ignoreCannotDelete) {
        throw new Exception("Cannot delete " + file.getAbsolutePath());
      } else {
        counter++;
      }
    }
    return counter;
  }

  static public void removeIfExist(String path, boolean ignoreCannotDelete) throws Exception {
    File file = new File(path);
    if (file.exists()) remove(file, ignoreCannotDelete);
  }

  static public boolean exist(String path) throws Exception {
    File file = new File(path);
    return file.exists();
  }

  static public void mkdirs(String path) throws IOException {
    File file = new File(path);
    if (!file.exists()) {
      if (!file.mkdirs()) { throw new IOException("Cannot create directory " + path); }
    }
  }

  static public int cp(String src, String dest) throws Exception {
    int counter = 0;
    File srcFolder = new java.io.File(src);
    if (!srcFolder.exists()) throw new Exception(src + " does not exist");

    if (srcFolder.isFile()) {
      File destFolder = new java.io.File(dest);
      if (destFolder.isFile()) {
        dest = destFolder.getParent();
      }
      if (destFolder.exists()) {
        dest = dest + "/" + srcFolder.getName();
      }
      InputStream input = new java.io.FileInputStream(srcFolder);
      OutputStream output = new java.io.FileOutputStream(dest);
      byte[] buff = new byte[8192];
      int len = 0;
      while ((len = input.read(buff)) > 0) {
        output.write(buff, 0, len);
      }
      input.close();
      output.close();
      counter++;
    } else {
      File destFolder = new File(dest);
      if (!destFolder.exists()) {
        destFolder.mkdirs();
      }
      File[] child = srcFolder.listFiles();
      for (int i = 0; i < child.length; i++) {
        File file = child[i];
        if (file.isFile()) {
          counter += cp(file.getAbsolutePath(), destFolder.getAbsolutePath() + "/" + file.getName());
        } else {
          counter += cp(file.getAbsolutePath(), destFolder.getAbsolutePath() + "/" + file.getName());
        }
      }
    }
    return counter;
  }

  static public void copyTo(InputStream src, String dest) throws IOException {
    File destFolder = new java.io.File(dest);
    if (destFolder.isFile()) {
      dest = destFolder.getParent();
    }
    OutputStream output = new java.io.FileOutputStream(dest);
    byte[] buff = new byte[8192];
    int len = 0;
    while ((len = src.read(buff)) > 0) {
      output.write(buff, 0, len);
    }
    src.close();
    output.close();
  }

  static public void writeToFile(String fname, byte[] data, boolean append) throws IOException {
    FileOutputStream os = new FileOutputStream(fname, append);
    os.write(data);
    os.close();
  }

  static public String[] findFiles(String dir, final String exp) throws IOException {
    List<String> holder = new ArrayList<String>();
    final Pattern fpattern = Pattern.compile(exp);
    FileFilter filter = new FileFilter() {
      public boolean accept(File pathname) {
        return fpattern.matcher(pathname.getName()).matches();
      }
    };
    findFiles(holder, new File(dir), filter);
    return holder.toArray(new String[holder.size()]);
  }

  static public <T extends FileFilter> List<String> findFiles(File dir, T filter) throws IOException {
    List<String> holder = new ArrayList<String>();
    findFiles(holder, dir, filter);
    return holder;
  }

  static public <T extends FileFilter> void findFiles(List<String> holder, File file, T filter) throws IOException {
    if (file.isFile() && filter.accept(file)) {
      holder.add(file.getCanonicalPath());
      return;
    } else if (file.isDirectory()) {
      for (File child : file.listFiles()) {
        findFiles(holder, child, filter);
      }
    }
  }
}