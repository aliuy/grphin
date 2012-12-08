package edu.gatech.comp.social.grphin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Parser for Grph.in
 * 
 * @author andrew
 */
public class Grphin {
  // Change this to your dataset (can be a directory or individual file).
  private final static String DATA = "E:\\linkedin_dataset\\A\\large\\";
  // Change this to your output file.
  private final static String OUTPUT = "C:\\Users\\andrew\\Desktop\\jsonOutput.json";
  // The minimum number of employees needed in order to output a company or
  // edge.
  private final static int THRESHOLD = 15;
  // Debug Mode (1 = Suggest normalized company names)
  private final static boolean DEBUG = true;

  // Candidate company names to be normalized.
  private static Set<String> NORMAL_CANDIDATES = new HashSet<String>();
  // The output file for normalized company name suggestions.
  private final static String NORMAL_OUTPUT = "C:\\Users\\andrew\\Desktop\\normal.txt";

  /**
   * The Main Method
   */
  public static void main(String[] args) {
    Grphin driver = new Grphin();
    long timer = System.currentTimeMillis();

    // Parse Input
    System.out.println("Processing " + DATA);
    driver.parse(new File(DATA));

    // TODO: Add in database support for smarter filtering.

    // Output File
    System.out.println("Outputting to " + OUTPUT);
    try {
      FileWriter out = new FileWriter(OUTPUT);
      out.write(driver.toString());
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (DEBUG) {
      System.out.println("Normalized Company Name Suggestions Outputting to " + NORMAL_OUTPUT);
      // Output Normal File
      try {
        FileWriter out = new FileWriter(NORMAL_OUTPUT);
        for (String normal : NORMAL_CANDIDATES) {
          out.write(normal + "\n");
        }
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    timer = (System.currentTimeMillis() - timer) / 1000;
    System.out.println("Finished processing ~" + driver.getPeople().size() + " profiles!");
    System.out.println("Companies Output: " + driver.getSize() + ".");
    System.out.println("Elapsed Time: " + timer + " seconds.");

  }

  // Set<PersonId> people.
  private Set<String> people = new HashSet<String>();

  // Map<CompanyName, HashSet<PersonId>> companies.
  private Map<String, HashSet<String>> nodes = new HashMap<String, HashSet<String>>();

  // Map<Edge, HashSet<PersonId>> edges.
  private Map<Edge, HashSet<String>> edges = new HashMap<Edge, HashSet<String>>();

  // State Date format strings.
  private String[] formatStrings = { "yyyy-MM-dd", "yyyy" };

  // Number of companies output.
  private int size = 0;

  public Set<String> getPeople() {
    return people;
  }

  public int getSize() {
    return size;
  }

  /**
   * Parses a directory or file.
   * 
   * @param filePath the File Path to a file or directory.
   */
  private void parse(File filePath) {
    if (filePath.isDirectory()) {
      for (File file : filePath.listFiles()) {
        parse(file);
      }
    } else {
      parseFile(filePath);
    }
  }

  /**
   * Parses a single file.
   * 
   * @param myFile the File Path to the file.
   */
  private void parseFile(File myFile) {
    try {
      JsonParser parser = new JsonParser();
      JsonElement jsonElement = parser.parse(new FileReader(myFile));
      if (jsonElement.isJsonArray()) {
        for (JsonElement e : jsonElement.getAsJsonArray()) {
          if (e.isJsonObject()) {
            JsonObject jsonObj = e.getAsJsonObject();
            // Employee Id: Use profile-url as unique identifier.
            if (jsonObj.get("public-profile-url") != null
                && jsonObj.get("public-profile-url").isJsonPrimitive()) {
              String employeeId = jsonObj.get("public-profile-url").getAsString();

              // Add people to a HashSet for statistical purposes.
              people.add(employeeId);

              // If Employee ID exists, check for positions.
              PriorityQueue<Company> sortedCompanies = new PriorityQueue<Company>();
              if (jsonObj.get("positions") != null && jsonObj.get("positions").isJsonArray()) {
                for (JsonElement position : jsonObj.get("positions").getAsJsonArray()) {
                  if (position.isJsonObject()) {
                    JsonObject company = position.getAsJsonObject();
                    if (company.get("company-name") != null
                        && company.get("company-name").isJsonPrimitive()
                        && company.get("start-date") != null
                        && company.get("start-date").isJsonPrimitive()) {
                      // Strip formatting to help normalize company names.
                      String companyName = NameUtil.normalize(company.get("company-name")
                          .getAsString());
                      Date startDate = tryParse(company.get("start-date").getAsString());
                      sortedCompanies.add(new Company(companyName, startDate));
                    }

                  }
                }
              }

              if (sortedCompanies.size() > 0) {
                Company c = sortedCompanies.poll();
                // Add to employee to company map
                HashSet<String> companyEmployees = nodes.get(c.name);
                if (companyEmployees == null) {
                  companyEmployees = new HashSet<String>();
                }
                companyEmployees.add(employeeId);
                nodes.put(c.name, companyEmployees);

                // Set oldCompany or newCompany?
                Company oldC;
                while (sortedCompanies.size() > 0) {
                  oldC = c;
                  c = sortedCompanies.poll();

                  // Add to employee to company map
                  companyEmployees = nodes.get(c.name);
                  if (companyEmployees == null) {
                    companyEmployees = new HashSet<String>();
                  }
                  companyEmployees.add(employeeId);
                  nodes.put(c.name, companyEmployees);

                  // Skip if source == destination (most likely a promotion).
                  if (!(oldC.equals(c)) && oldC.name.length() > 0 && c.name.length() > 0) {
                    // Add to employee to edge map
                    Edge key = new Edge();
                    key.source = oldC.name;
                    key.destination = c.name;
                    HashSet<String> edgeEmployees = edges.get(key);
                    if (edgeEmployees == null) {
                      edgeEmployees = new HashSet<String>();
                    }
                    edgeEmployees.add(employeeId);
                    edges.put(key, edgeEmployees);
                  }
                }
              }
            }
          }
        }
        System.out.println("Processed: " + myFile.getName());
      } else {
        // Invalid JSON
        System.out.println("Error: Invalid Json - " + myFile.getName());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // For Andrew's Arbor visualization.
  @Override
  public String toString() {
    JsonObject toRet = new JsonObject();
    JsonArray nodeArr = new JsonArray();
    JsonArray edgeArr = new JsonArray();
    Map<String, Company> companyMap = new HashMap<String, Company>();

    // Build Company Objects
    for (String companyName : nodes.keySet()) {
      Integer companySize = nodes.get(companyName).size();
      // Skip companies that have too few employees (uninteresting).
      if (companySize >= THRESHOLD) {
        companyMap.put(companyName, new Company(companyName, companySize));
      }
    }

    // Build Incoming Edges
    Set<String> relavantCompanies = new HashSet<String>();
    for (Edge e : edges.keySet()) {
      Integer edgeSize = edges.get(e).size();
      // Skip edges that have too few employees (uninteresting).
      if (edgeSize >= THRESHOLD && companyMap.get(e.destination) != null
          && companyMap.containsKey(e.source)) {
        if (DEBUG) {
          // Add normalization candidates as needed.
          if (e.destination.contains(e.source) || e.source.contains(e.destination)) {
            if (e.source.length() < e.destination.length()) {
              NORMAL_CANDIDATES.add("normalizedNames.put(\"" + e.destination + "\", \"" + e.source
                  + "\");");
            } else {
              NORMAL_CANDIDATES.add("normalizedNames.put(\"" + e.source + "\", \"" + e.destination
                  + "\");");
            }
          }
        }
        JsonObject eJson = new JsonObject();
        eJson.addProperty("source", e.source);
        eJson.addProperty("destination", e.destination);
        eJson.addProperty("size", edgeSize);
        edgeArr.add(eJson);

        relavantCompanies.add(e.source);
        relavantCompanies.add(e.destination);
      }
    }

    // Build Json
    for (Company c : companyMap.values()) {
      if (relavantCompanies.contains(c.name)) {
        JsonObject companyJson = new JsonObject();
        companyJson.addProperty("name", c.name);
        companyJson.addProperty("size", c.size);
        nodeArr.add(companyJson);
      }
    }
    size = nodeArr.size();
    toRet.add("nodes", nodeArr);
    toRet.add("edges", edgeArr);

    return toRet.toString();
  }

  private Date tryParse(String dateString) {
    for (String formatString : formatStrings) {
      try {
        return new SimpleDateFormat(formatString).parse(dateString);
      } catch (ParseException e) {
      }
    }

    return null;
  }
}
