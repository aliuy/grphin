package edu.gatech.comp.social.grphin;

import java.util.Date;

/**
 * Helper class for sorting companies by start date and outputing Json.
 */
public class Company implements Comparable<Company> {
  public String name;
  public Date startDate;
  public Integer size;
  // public HashMap<String, Integer> incomingEdges;
  public boolean sortBySize;

  public Company(String name, Date startDate) {
    super();
    this.name = name;
    this.startDate = startDate;
    sortBySize = false;
  }

  public Company(String name, Integer size) {
    super();
    this.name = name;
    this.size = size;
    // incomingEdges = new HashMap<String, Integer>();
    sortBySize = true;
  }

  @Override
  public int compareTo(Company comp) {
    if (sortBySize) {
      return size.compareTo(comp.size);
    } else {
      return startDate.compareTo(comp.startDate);
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    } else if (other instanceof Company) {
      return this.name.equals(((Company) other).name);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}