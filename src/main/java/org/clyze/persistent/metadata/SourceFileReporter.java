package org.clyze.persistent.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceFileReporter extends FileReporter {

    final SourceMetadata metadata;

    /**
     * Creates a new file reporter to use for generating metadata.
     *
     * @param configuration the output configuration to use
     */
    public SourceFileReporter(Configuration configuration, SourceMetadata metadata) {
        super(configuration);
        this.metadata = metadata;
    }

    @Override
    public void printReportStats() {
        configuration.printer.println("Types: " + metadata.types.size());
    }

    @Override
    protected Map<String, List<?>> createJsonReport() {
        Map<String, List<?>> jsonReport = new HashMap<>();
        metadata.sort();
        jsonReport.put("Type", metadata.types);
        return jsonReport;
    }
}