package org.clyze.persistent.model;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * A symbol that has a unique id (for the whole program).
 */
public abstract class SymbolWithId extends Symbol implements Comparable<SymbolWithId> {

    /** The artifact name (e.g. foo-1.2.jar) */
    private String artifactName;

    /** The unique id of this symbol. */
    protected String symbolId;

    /**
     * No-arg constructor, use setters or fromMap() to populate the object.
     */
    public SymbolWithId() {}

    public SymbolWithId(Position position, String sourceFileName, boolean source,
                        String artifactName, String symbolId) {
        super(position, sourceFileName, source);
        this.artifactName = artifactName;
        this.symbolId = symbolId;
    }

    public String getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(String symbolId) {
        this.symbolId = symbolId;
    }

    public String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    @Override
    public boolean equals(Object object) {
        if ((!(object instanceof SymbolWithId)))
            return false;
        SymbolWithId that = (SymbolWithId) object;
        return super.equals(object) && Objects.equals(this.symbolId, that.symbolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), symbolId);
    }

    @Override
    public int compareTo(SymbolWithId that) {
        return Comparator.comparing(SymbolWithId::getSymbolId).compare(this, that);
    }

    @Override
    protected void saveTo(Map<String, Object> map) {
        super.saveTo(map);
        map.put("symbolId", getSymbolId());
        map.put("artifactName", getArtifactName());
    }

    @Override
    public void fromMap(Map<String, Object> map){
        super.fromMap(map);
        setSymbolId((String) map.get("symbolId"));
        setArtifactName((String) map.get("artifactName"));
    }
}
