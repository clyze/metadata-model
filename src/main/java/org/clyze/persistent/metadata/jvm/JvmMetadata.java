package org.clyze.persistent.metadata.jvm;

import java.util.*;

import org.clyze.persistent.metadata.Metadata;
import org.clyze.persistent.metadata.Printer;
import org.clyze.persistent.metadata.TokenLocator;
import org.clyze.persistent.model.*;
import org.clyze.persistent.model.jvm.*;

/**
 * A class providing a common container of basic Doop metadata.
 *
 * The metadata is generated by processing some form of a
 * syntactic representation (e.g. Java source file, Jimple IR).
 */
public class JvmMetadata extends Metadata implements TokenLocator {
    /** The program classes/interfaces. */
    public final Set<JvmClass> jvmClasses = new HashSet<>();
    /** The class/interface fields. */
    public final Set<JvmField> jvmFields = new HashSet<>();
    /** The class/interface methods. */
    public final Set<JvmMethod> jvmMethods = new HashSet<>();
    /** The local variables inside methods. */
    public final Set<JvmVariable> jvmVariables = new HashSet<>();
    /** The method invocation sites inside methods. */
    public final Set<JvmMethodInvocation> jvmInvocations = new HashSet<>();
    /** The heap allocation sites inside methods. */
    public final Set<JvmHeapAllocation> jvmHeapAllocations = new HashSet<>();
    /** Usage information that connects code elements with their use. */
    public final Set<Usage> usages = new HashSet<>();
    /** Alias information to account for elements appearing with many ids in some context. */
    public final Set<SymbolAlias> aliases = new HashSet<>();
    /** String constants. */
    public final Set<JvmStringConstant> jvmStringConstants = new HashSet<>();

    /**
     * Return a metadata collection sorted by id, to make output canonical.
     * @param set    the collection of elements (that have unique ids)
     * @param <T>    the actual type of the set elements
     * @return       the sorted output list
     */
    public static <T extends SymbolWithId> List<T> getSortedBySymbolId(Set<T> set) {
        List<T> ret = new ArrayList<>(set);
        ret.sort(Comparator.comparing(SymbolWithId::getSymbolId));
        return ret;
    }

    /**
     * Create a JVM metadata object from a map representation for JSON data.
     * @param  map the map to use
     * @return the deserialized JVM metadata object
     */
    @SuppressWarnings("unchecked")
    public static JvmMetadata fromMap(Map<String, Object> map) {
        JvmMetadata metadata = new JvmMetadata();
        Metadata.fillFromMap(metadata, map);
        metadata.jvmClasses.addAll((List<JvmClass>) map.get(JvmClass.class.getSimpleName()));
        metadata.jvmFields.addAll((List<JvmField>) map.get(JvmField.class.getSimpleName()));
        metadata.jvmMethods.addAll((List<JvmMethod>) map.get(JvmMethod.class.getSimpleName()));
        metadata.jvmVariables.addAll((List<JvmVariable>) map.get(JvmVariable.class.getSimpleName()));
        metadata.jvmInvocations.addAll((List<JvmMethodInvocation>) map.get(JvmMethodInvocation.class.getSimpleName()));
        metadata.jvmHeapAllocations.addAll((List<JvmHeapAllocation>) map.get(JvmHeapAllocation.class.getSimpleName()));
        metadata.usages.addAll((List<Usage>) map.get(Usage.class.getSimpleName()));
        metadata.jvmStringConstants.addAll((List<JvmStringConstant>) map.get(JvmStringConstant.class.getSimpleName()));
        metadata.aliases.addAll((List<SymbolAlias>) map.get(SymbolAlias.class.getSimpleName()));
        return metadata;
    }

    @Override
    public Map<String, Collection<Position>> getTokenLocations() {
        Map<String, Collection<Position>> res = new HashMap<>();
        // For classes, add both fully-qualified and simple names.
        for (JvmClass jvmClass : jvmClasses) {
            Position pos = jvmClass.getPosition();
            addTokenWithLocation(res, jvmClass.getName(), pos);
            addTokenWithLocation(res, jvmClass.getId(), pos);
        }
        for (JvmField jvmField : jvmFields)
            addTokenWithLocation(res, jvmField.getName(), jvmField.getPosition());
        for (JvmMethod jvmMethod : jvmMethods)
            addTokenWithLocation(res, jvmMethod.getName(), jvmMethod.getPosition());
        for (JvmVariable jvmVariable : jvmVariables)
            addTokenWithLocation(res, jvmVariable.getName(), jvmVariable.getPosition());
        return res;
    }

    /**
     * Prints a metadata report in the standard output.
     */
    @Override
    public void printReportStats(Printer printer) {
        super.printReportStats(printer);
        printer.println("Classes: " + jvmClasses.size());
        printer.println("Fields: " + jvmFields.size());
        printer.println("Methods: " + jvmMethods.size());
        printer.println("Variables: " + jvmVariables.size());
        printer.println("HeapAllocations: " + jvmHeapAllocations.size());
        printer.println("MethodInvocations: " + jvmInvocations.size());
        printer.println("Usages: " + usages.size());
        printer.println("Aliases: " + aliases.size());
        printer.println("StringConstants: " + jvmStringConstants.size());
    }

    @Override
    public void populateJsonReport(Map<String, List<?>> jsonReport) {
        super.populateJsonReport(jsonReport);
        // Sort sets (by id) so that output order is predictable.
        jsonReport.put("JvmClass", JvmMetadata.getSortedBySymbolId(jvmClasses));
        jsonReport.put("JvmField", JvmMetadata.getSortedBySymbolId(jvmFields));
        jsonReport.put("JvmMethod", JvmMetadata.getSortedBySymbolId(jvmMethods));
        jsonReport.put("JvmVariable", JvmMetadata.getSortedBySymbolId(jvmVariables));
        jsonReport.put("JvmHeapAllocation", JvmMetadata.getSortedBySymbolId(jvmHeapAllocations));
        jsonReport.put("JvmMethodInvocation", JvmMetadata.getSortedBySymbolId(jvmInvocations));
        jsonReport.put("JvmStringConstant", new ArrayList<>(jvmStringConstants));
        jsonReport.put("Usage", JvmMetadata.getSortedBySymbolId(usages));
        jsonReport.put("SymbolAlias", JvmMetadata.getSortedBySymbolId(aliases));
    }
}
