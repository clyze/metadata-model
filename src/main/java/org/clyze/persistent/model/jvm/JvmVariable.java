package org.clyze.persistent.model.jvm;

import java.util.Map;
import org.clyze.persistent.model.Position;
import org.clyze.persistent.model.SymbolWithId;

/**
 * This class models local variables in JVM methods. In bytecode, these
 * "variables" come from appropriate analysis of bytecode (such as Soot
 * and its Jimple IR). In the Dalvik Executable format (dex), these
 * "variables" come from the "registers" in the IR.
 */
public class JvmVariable extends SymbolWithId {

	private String name;

	private String type;

	private boolean isLocal;

	private boolean isParameter;

	private String declaringMethodId;

	/** True if this variable is inside an instance initializer block. */
	private boolean inIIB = false;

	public JvmVariable() {}

    public JvmVariable(String id) {
        this.id = id;
    }

	public JvmVariable(Position position,
					   String sourceFileName,
					   String name,
					   String symbolId,
					   String type,
					   String declaringMethodId,
					   boolean isLocal,
					   boolean isParameter,
					   boolean inIIB) {
		super(position, sourceFileName, symbolId);
		this.name = name;
		this.type = type;
		this.declaringMethodId = declaringMethodId;
		this.isLocal = isLocal;
		this.isParameter = isParameter;
		this.inIIB = inIIB;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public boolean isParameter() {
        return isParameter;
    }

    public void setParameter(boolean parameter) {
        isParameter = parameter;
    }

    public String getDeclaringMethodId() {
        return declaringMethodId;
    }

    public void setDeclaringMethodId(String declaringMethodId) {
        this.declaringMethodId = declaringMethodId;
    }

    public boolean isInIIB() {
        return inIIB;
    }

    public void setInIIB(boolean inIIB) {
        this.inIIB = inIIB;
    }

    protected void saveTo(Map<String, Object> map) {
		super.saveTo(map);
		map.put("name", this.name);
		map.put("type", this.type);
		map.put("isLocal", this.isLocal);
		map.put("isParameter", this.isParameter);
		map.put("declaringMethodId", this.declaringMethodId);
		map.put("inIIB", this.inIIB);
	}

	public void fromMap(Map<String, Object> map){
		super.fromMap(map);
		this.name              = (String) map.get("name");
		this.type              = (String) map.get("type");
		this.isLocal           = (Boolean) map.get("isLocal");
		this.isParameter       = (Boolean) map.get("isParameter");
		this.declaringMethodId = (String) map.get("declaringMethodId");
		this.inIIB             = (Boolean) map.get("inIIB");
	}

	public String getSymbolId() {
		return symbolId;
	}

	public void setType(String type) {
		this.type = type;
	}
}