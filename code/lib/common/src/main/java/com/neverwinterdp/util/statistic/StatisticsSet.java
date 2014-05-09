package com.neverwinterdp.util.statistic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class StatisticsSet implements Serializable {
  static DecimalFormat    PFORMATER = new DecimalFormat("####00.00%");

  private String          name;
  Map<String, Statistics> map       = new LinkedHashMap<String, Statistics>();

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void clear() {
    map.clear();
  }

  public void increment(String category, String name, long amount) {
    incr(category, name, amount);
  }

  public void incr(String category, String name, long amount) {
    incr(category, name, null, amount);
  }

  public void incr(String category, String name, String relateTo, long amount) {
    Statistics cat = map.get(category);
    if (cat == null) {
      cat = new Statistics(category);
      map.put(category, cat);
    }
    cat.incr(name, relateTo, amount);
  }

  public void incr(String category, String[] name, long amount) {
    Statistics cat = map.get(category);
    if (cat == null) {
      cat = new Statistics(category);
      map.put(category, cat);
    }
    cat.incr(name, amount);
  }

  public void incr(String category, String[] name, String relateTo, long amount) {
    Statistics cat = map.get(category);
    if (cat == null) {
      cat = new Statistics(category);
      map.put(category, cat);
    }
    cat.incr(name, relateTo, amount);
  }

  public void report(String file, String order) throws Exception {
    PrintStream os = new PrintStream(new FileOutputStream(file));
    report(os, order);
    os.close();
  }

  public void report(Appendable os) {
    report(os, null);
  }

  public void report(Appendable os, String order) {
    try {
      Iterator<Map.Entry<String, Statistics>> i = map.entrySet().iterator();
      while (i.hasNext()) {
        Map.Entry<String, Statistics> entry = i.next();
        os.append("*************************************************************************\n");
        os.append(entry.getKey()).append('\n');
        os.append("*************************************************************************\n");
        for (Map.Entry<String, Statistic> subentry : sort(entry.getValue(), order)) {
          Statistic relateTo = null;
          Statistic value = subentry.getValue();
          if (value.getRelateTo() != null) {
            relateTo = entry.getValue().get(value.getRelateTo());
          }
          print(os, subentry.getKey(), 50, subentry.getValue(), relateTo);
        }
        os.append('\n');
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public Statistics getStatistics(String name) {
    return map.get(name);
  }

  public Map<String, Statistics> getStatistics() {
    return map;
  }

  public String[] getCategories() {
    return map.keySet().toArray(new String[map.size()]);
  }

  public Object[][] getStatisticData(String category, String order) {
    TreeMap<String, Statistic> categoryMap = map.get(category);
    Object[][] data = new Object[categoryMap.size()][];
    int idx = 0;
    for (Map.Entry<String, Statistic> subentry : sort(categoryMap, order)) {
      Statistic value = subentry.getValue();
      String percentage = "";
      if (value.getRelateTo() != null) {
        Statistic relateTo = categoryMap.get(value.getRelateTo());
        percentage = PFORMATER.format(value.getFrequency() / (double) relateTo.getFrequency());
      }
      data[idx++] = new Object[] {
          subentry.getKey(), value.getFrequency(), percentage
      };
    }
    return data;
  }

  public String[] getCategoryKeys(String category) {
    TreeMap<String, Statistic> categoryMap = map.get(category);
    return categoryMap.keySet().toArray(new String[categoryMap.size()]);
  }

  public Object[] getCategoryData(String category, String[] key) {
    TreeMap<String, Statistic> categoryMap = map.get(category);
    Object[] data = new Object[key.length];
    for (int i = 0; i < key.length; i++) {
      Statistic number = categoryMap.get(key[i]);
      if (number != null) {
        data[i] = categoryMap.get(key[i]).getFrequency();
      } else {
        data[i] = 0;
      }
    }
    return data;
  }

  private void print(Appendable os, String name, int nameWidth, Statistic value, Statistic relateValue)
      throws IOException {
    os.append(name);
    for (int i = name.length(); i < nameWidth; i++) {
      os.append(' ');
    }
    String valueString = Long.toString(value.getFrequency());
    for (int i = valueString.length(); i < 15; i++) {
      os.append(' ');
    }
    os.append(valueString);
    os.append("\t");
    if (relateValue != null) {
      os.append(PFORMATER.format(value.getFrequency() / (double) relateValue.getFrequency()));
    }
    os.append('\n');
  }

  public List<Map.Entry<String, Statistic>> sort(TreeMap<String, Statistic> map, String order) {
    List<Map.Entry<String, Statistic>> holder = new ArrayList<Map.Entry<String, Statistic>>();
    holder.addAll(map.entrySet());
    if ("desc".equals(order)) {
      Collections.sort(holder, new EntryComparator(-1));
    } else if ("asc".equals(order)) {
      Collections.sort(holder, new EntryComparator(1));
    }
    return holder;
  }

  static class EntryComparator implements Comparator<Map.Entry<String, Statistic>> {
    private int direction;

    EntryComparator(int direction) {
      this.direction = direction;
    }

    public int compare(Entry<String, Statistic> e0, Entry<String, Statistic> e1) {
      long val = e0.getValue().getFrequency() - e1.getValue().getFrequency();
      int ret = 0;
      if (val > 1) ret = 1;
      else if (val < 0) ret = -1;
      return ret * direction;
    }
  };

  static public void main(String[] args) {
    StatisticsSet map = new StatisticsSet();
    map.increment("category1", "name1", 1);
    map.increment("category1", "name2", 1);

    map.incr("category2", "all", 3);
    map.incr("category2", "name1", "all", 1);
    map.incr("category2", "name2", "all", 1);
    map.incr("category2", "name2", "all", 1);

    map.report(System.out);
  }
}
