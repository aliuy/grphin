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
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
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
  private final static String DATA = "E:\\linkedin_dataset\\Extracted\\";
  // Change this to your output file.
  private final static String OUTPUT = "C:\\Users\\andrew\\Documents\\GitHub\\grphin\\www\\assets\\jsonOutput\\jsonOutput.json";
  // The minimum number of employees needed in order to output a company or
  // edge.
  private static int THRESHOLD = 10;
  // Debug Mode (1 = Suggest normalized company names)
  private final static boolean DEBUG = true;

  // Candidate company names to be normalized.
  private static Set<String> NORMAL_CANDIDATES = new HashSet<String>();
  // The output file for normalized company name suggestions.
  private final static String NORMAL_OUTPUT = "C:\\Users\\andrew\\Documents\\GitHub\\grphin\\src\\edu\\gatech\\comp\\social\\grphin\\normal.txt";

  private final static HashSet<String> WHITELIST = new HashSet<String>();

  /**
   * The Main Method
   */
  public static void main(String[] args) {
    Grphin driver = new Grphin();
    long bigTimer = System.currentTimeMillis();
    long smallTimer = System.currentTimeMillis();

    // Hard Code Whitelist for Now...
    WHITELIST.add("Google");
    WHITELIST.add("Facebook");
    WHITELIST.add("Amazon");
    WHITELIST.add("LinkedIn");
    WHITELIST.add("Yahoo");
    WHITELIST.add("Apple");
    WHITELIST.add("Microsoft");
    // WHITELIST.add("Oracle");
    // WHITELIST.add("SAP");
    // WHITELIST.add("VMware");
    // WHITELIST.add("Adobe");
    // WHITELIST.add("Dell");
    // WHITELIST.add("Intel");
    // WHITELIST.add("IBM");

    // Parse Input
    System.out.println("Processing " + DATA);
    driver.parse(new File(DATA));

    System.out.println("Parsing Elapsed Time: "
        + ((System.currentTimeMillis() - smallTimer) / 1000) + " seconds.");
    smallTimer = System.currentTimeMillis();

    // Statistics
    int people = driver.getPeople().size();
    int nodes = driver.getNodes().size();
    int edges = driver.getEdges().size();

    // TODO: Add in database support for smarter filtering.

    // Output File
    try {
      for (int i = 10; i <= 50; i += 5) {
        THRESHOLD = i;
        System.out.println("Outputting to " + OUTPUT + "." + i);
        FileWriter out = new FileWriter(OUTPUT + "." + i);
        out.write(driver.toString());
        out.close();

        FileWriter stats = new FileWriter(OUTPUT + ".stats." + i);
        stats.write("Finished processing " + people + " profiles!\n");
        stats.write("Companies Found: " + nodes + " \n");
        stats.write("Edges Found: " + edges + " \n");
        stats.write("Companies Output: " + driver.getCompanySize() + "\n");
        stats.write("Edges Output: " + driver.getEdgeSize() + "\n");
        stats.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (DEBUG) {
      System.out.println("Normalized Company Name Suggestions Outputting to " + NORMAL_OUTPUT);
      // Output Normal File
      try {
        FileWriter out = new FileWriter(NORMAL_OUTPUT);
        out.write("package edu.gatech.comp.social.grphin;\n");
        out.write("import java.util.HashMap;\n");
        out.write("import java.util.Map;\n");
        out.write("public class NameUtil {\n");
        out.write("  private static Map<String, String> normalizedNames;\n");
        out.write("  public static String normalize(String name) {\n");
        out.write("    if (normalizedNames == null) {\n");
        out.write("      normalizedNames = new HashMap<String, String>();\n");

        for (String normal : NORMAL_CANDIDATES) {
          out.write(normal + "\n");
        }

        out.write("    }\n");
        out.write("    name = name.replaceAll(\"[^a-zA-Z0-9]\", \"\");\n");
        out.write("    if (normalizedNames.containsKey(name)) {\n");
        out.write("      name = normalizedNames.get(name);\n");
        out.write("    }\n");
        out.write("    return name;\n");
        out.write("  }\n");
        out.write("}\n");
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Root node (largest company):" + driver.getLargestCompany());
    System.out.println("Output Elapsed Time: " + ((System.currentTimeMillis() - smallTimer) / 1000)
        + " seconds.");
    System.out.println("Total Elapsed Time: " + ((System.currentTimeMillis() - bigTimer) / 1000)
        + " seconds.");
  }

  // Set<PersonId> people.
  private Set<String> people = new HashSet<String>();

  // Map<CompanyName, HashSet<PersonId>> companies.
  private Map<String, Integer> nodes = new HashMap<String, Integer>();

  // Map<Edge, HashSet<PersonId>> edges.
  private Map<Edge, Integer> edges = new HashMap<Edge, Integer>();

  // Map<CompanyName, HashSet<Edges>>.
  private Map<String, HashSet<String>> nodeEdges = new HashMap<String, HashSet<String>>();

  // State Date format strings.
  private String[] formatStrings = { "yyyy-MM-dd", "yyyy" };

  // Number of companies output.
  private int companySize = 0;
  private int edgeSize = 0;

  private String largestCompany = null;

  public int getCompanySize() {
    return companySize;
  }

  public Map<Edge, Integer> getEdges() {
    return edges;
  }

  public int getEdgeSize() {
    return edgeSize;
  }

  public String getLargestCompany() {
    if (largestCompany == null || largestCompany.isEmpty()) {
      int largestSize = 0;
      for (String company : nodes.keySet()) {
        if (nodes.get(company) > largestSize) {
          largestSize = nodes.get(company);
          largestCompany = company;
        }
      }
    }
    return largestCompany;
  }

  public Map<String, Integer> getNodes() {
    return nodes;
  }

  public Set<String> getPeople() {
    return people;
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
      if (filePath.getName().endsWith(".json")) {
        parseFile(filePath);
      }
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

              if (!people.contains(employeeId)) {
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
                          && !company.get("company-name").getAsString().isEmpty()
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
                  Integer companyEmployees = nodes.get(c.name);
                  if (companyEmployees == null) {
                    companyEmployees = new Integer(0);
                  }
                  companyEmployees++;
                  nodes.put(c.name, companyEmployees);

                  // Set oldCompany or newCompany?
                  Company oldC;
                  while (sortedCompanies.size() > 0) {
                    oldC = c;
                    c = sortedCompanies.poll();

                    // Add to employee to company map
                    companyEmployees = nodes.get(c.name);
                    if (companyEmployees == null) {
                      companyEmployees = new Integer(0);
                    }
                    companyEmployees++;
                    nodes.put(c.name, companyEmployees);

                    // Skip if source == destination (most likely a promotion).
                    if (!(oldC.equals(c)) && oldC.name.length() > 0 && c.name.length() > 0) {
                      // Add to employee to edge map
                      Edge key = new Edge();
                      key.source = oldC.name;
                      key.destination = c.name;
                      Integer edgeEmployees = edges.get(key);
                      if (edgeEmployees == null) {
                        edgeEmployees = new Integer(0);
                      }
                      edgeEmployees++;

                      edges.put(key, edgeEmployees);

                      HashSet<String> nEdges = nodeEdges.get(c.name);
                      if (nEdges == null) {
                        nEdges = new HashSet<String>();
                      }
                      nEdges.add(oldC.name);
                      nodeEdges.put(c.name, nEdges);

                      nEdges = nodeEdges.get(oldC.name);
                      if (nEdges == null) {
                        nEdges = new HashSet<String>();
                      }
                      nEdges.add(c.name);
                      nodeEdges.put(oldC.name, nEdges);

                    }
                  }
                }
              }
            }
          }
        }
        // System.out.println("Processed: " + myFile.getName());
      } else {
        // Invalid JSON
        System.out.println("Error: Invalid Json - " + myFile.getName());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setEdges(Map<Edge, Integer> edges) {
    this.edges = edges;
  }

  public void setNodes(Map<String, Integer> nodes) {
    this.nodes = nodes;
  }

  // For Andrew's Arbor visualization.
  @Override
  public String toString() {
    JsonObject toRet = new JsonObject();
    JsonArray nodeArr = new JsonArray();
    JsonArray edgeArr = new JsonArray();

    // Build Incoming Edges
    Set<String> relevantCompanies = new HashSet<String>();
    Set<JsonObject> relevantEdges = new HashSet<JsonObject>();

    // Add WHITELIST to relevant companies.
    relevantCompanies.addAll(WHITELIST);

    for (Edge e : edges.keySet()) {
      Integer edgeSize = edges.get(e);
      // Skip edges that have too few employees (uninteresting).
      if (nodes.containsKey(e.destination)
          && nodes.containsKey(e.source)
          && (edgeSize >= THRESHOLD / 2 || (edgeSize > 1 && WHITELIST.contains(e.destination) && WHITELIST
              .contains(e.source)))) {
        if (DEBUG) {
          // Add normalization candidates as needed.
          if (e.destination.toLowerCase().contains(e.source.toLowerCase())
              || e.source.toLowerCase().contains(e.destination.toLowerCase())) {
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
        eJson.addProperty("toSize", edges.get(e));
        // Only add the greater edge between two nodes.
        Edge ePrime = new Edge(e.destination, e.source);
        if (edges.get(ePrime) == null) {
          eJson.addProperty("fromSize", 0);
          relevantEdges.add(eJson);
        } else if (edges.get(ePrime) <= edges.get(e)) {
          eJson.addProperty("fromSize", edges.get(ePrime));
          relevantEdges.add(eJson);
        }

        // Whitelist both companies.
        relevantCompanies.add(e.source);
        relevantCompanies.add(e.destination);
      }
    }

    // Get the largest company
    largestCompany = getLargestCompany();

    // Filter on a single connected graph.
    Set<String> filteredCompanies = new HashSet<String>();
    Queue<String> q = new LinkedList<String>();
    q.add(largestCompany);
    while (!q.isEmpty()) {
      String c = q.poll();
      if (relevantCompanies.contains(c) && !filteredCompanies.contains(c)) {
        filteredCompanies.add(c);
        // System.out.println(c);
        for (String e : nodeEdges.get(c)) {
          if (relevantCompanies.contains(e) && !filteredCompanies.contains(e) && !q.contains(e)) {
            Edge aKey = new Edge();
            aKey.source = c;
            aKey.destination = e;
            Integer a = edges.get(aKey);
            if (a == null) {
              a = 0;
            }
            Edge bKey = new Edge();
            bKey.source = e;
            bKey.destination = c;
            Integer b = edges.get(bKey);
            if (b == null) {
              b = 0;
            }
            if (a + b >= THRESHOLD || WHITELIST.contains(e)) {
              q.add(e);
            }
          }
        }
      }
    }

    // Build Json
    for (String c : filteredCompanies) {
      JsonObject companyJson = new JsonObject();
      companyJson.addProperty("name", c);
      companyJson.addProperty("size", nodes.get(c));
      nodeArr.add(companyJson);
    }
    for (JsonObject e : relevantEdges) {
      if (filteredCompanies.contains(e.get("source").getAsString())
          && filteredCompanies.contains(e.get("destination").getAsString())) {
        edgeArr.add(e);
      }
    }
    companySize = nodeArr.size();
    edgeSize = edgeArr.size();
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
