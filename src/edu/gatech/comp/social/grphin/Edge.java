package edu.gatech.comp.social.grphin;

/**
 * Helper class for keying edges for a HashMap.
 */
public class Edge {
  public String source;
  public String destination;

  public Edge() {
    super();
  }

  public Edge(String source, String destination) {
    super();
    this.source = source;
    this.destination = destination;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    } else if (other instanceof Edge) {
      return (this.source.equals(((Edge) other).source) && this.destination
          .equals(((Edge) other).destination));
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return (3 * source.hashCode()) ^ (7 * destination.hashCode());
  }
}