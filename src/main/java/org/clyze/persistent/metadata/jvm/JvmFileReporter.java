package org.clyze.persistent.metadata.jvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.clyze.persistent.metadata.Configuration;
import org.clyze.persistent.metadata.FileInfo;
import org.clyze.persistent.metadata.FileReporter;

/**
 * The reporter for JVM-specific metadata.
 */
public class JvmFileReporter extends FileReporter {
    /** The file information object containing the metadata. */
    private final FileInfo fInfo;

    /**
     * Creates a new file reporter to use for generating metadata.
     *
     * @param configuration  the output configuration to use
     * @param fInfo          the object containing the metadata
     */
    public JvmFileReporter(Configuration configuration, FileInfo fInfo) {
        super(configuration);
        this.fInfo = fInfo;
    }

    /**
     * Prints a metadata report in the standard output.
     */
    @Override
    public void printReportStats() {
        JvmMetadata metadata = fInfo.getElements();
        configuration.printer.println("Classes: " + metadata.jvmClasses.size());
        configuration.printer.println("Fields: " + metadata.jvmFields.size());
        configuration.printer.println("Methods: " + metadata.jvmMethods.size());
        configuration.printer.println("Variables: " + metadata.jvmVariables.size());
        configuration.printer.println("HeapAllocations: " + metadata.jvmHeapAllocations.size());
        configuration.printer.println("MethodInvocations: " + metadata.jvmInvocations.size());
        configuration.printer.println("Usages: " + metadata.usages.size());
        configuration.printer.println("Aliases: " + metadata.aliases.size());
        configuration.printer.println("StringConstants: " + metadata.jvmStringConstants.size());
    }

    @Override
    protected Map<String, List<?>> createJsonReport() {
        JvmMetadata metadata = fInfo.getElements();
        Map<String, List<?>> jsonReport = new HashMap<>();
        // Sort sets (by id) so that output order is predictable.
        jsonReport.put("JvmClass", JvmMetadata.getSortedBySymbolId(metadata.jvmClasses));
        jsonReport.put("JvmField", JvmMetadata.getSortedBySymbolId(metadata.jvmFields));
        jsonReport.put("JvmMethod", JvmMetadata.getSortedBySymbolId(metadata.jvmMethods));
        jsonReport.put("JvmVariable", JvmMetadata.getSortedBySymbolId(metadata.jvmVariables));
        jsonReport.put("JvmHeapAllocation", JvmMetadata.getSortedBySymbolId(metadata.jvmHeapAllocations));
        jsonReport.put("JvmMethodInvocation", JvmMetadata.getSortedBySymbolId(metadata.jvmInvocations));
        jsonReport.put("JvmStringConstant", new ArrayList<>(metadata.jvmStringConstants));
        jsonReport.put("Usage", JvmMetadata.getSortedBySymbolId(metadata.usages));
        jsonReport.put("SymbolAlias", JvmMetadata.getSortedBySymbolId(metadata.aliases));
        return jsonReport;
    }
}
