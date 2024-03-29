package org.clyze.persistent.model;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * A processed source file.
 */
public class SourceFile extends ItemImpl implements Comparable<SourceFile> {
    /** The name of the parent artifact. */
    private String artifactName;
    /** The source file path. */
    private String path;
    /** The unique id of the file. */
    private String id;

    /** No-arg constructor, use setters or fromMap() to populate the object. */
    public SourceFile() {}

    public SourceFile(String artifactName, String path, String id) {
        this.artifactName = artifactName;
        this.path = path;
        this.id = id;
    }

    public String getArtifactName() {
        return this.artifactName;
    }

    public String getPath() {
        return this.path;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SourceFile)) return false;
        SourceFile sourceFile = (SourceFile) object;

        return Objects.equals(path, sourceFile.path) && Objects.equals(id, sourceFile.id);
    }

    public int hashCode() {
        return Objects.hash(path, id);
    }

    @Override
    protected void saveTo(Map<String, Object> map) {
        map.put("artifactName", getArtifactName());
        map.put("path", getPath());
        map.put("id", getId());
    }

    @Override
    public void fromMap(Map<String, Object> map) {
        this.artifactName = (String) map.get("artifactName");
        this.path = (String) map.get("path");
        this.id = (String) map.get("id");
    }

    @Override
    public int compareTo(SourceFile that) {
        return Comparator.comparing(SourceFile::getId).compare(this, that);
    }

    @Override
    public String toString() {
        return this.id + ": " + this.path;
    }
}
