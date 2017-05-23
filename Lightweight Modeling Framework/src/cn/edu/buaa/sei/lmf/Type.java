package cn.edu.buaa.sei.lmf;

import java.util.Collection;
import java.util.Set;

/**
 * <code>Type</code> is the meta-class presentation in LMF. Different <code>Type</code>s are distinguished by
 * their name ({@link #getName()}). To retrieve {@link Attribute}s of the <code>Type</code>, see {@link #getAttribute(String)} and {@link #getAttributes()}.
 * <p>
 * Primitives are special <code>Type</code>s. They are {@link Primitives#BOOL}, {@link Primitives#INT}, {@link Primitives#LONG},
 * {@link Primitives#FLOAT}, {@link Primitives#DOUBLE}, {@link Primitives#STRING}, {@link Primitives#LIST} and {@link Primitives#ENUM}.
 * <p>
 * <code>Type</code> objects can be compared by "==" operator. Use {@link #isSubtypeOf(Type)} to determine inheritance relationship. 
 * <p>
 * Note: This interface does not intend to be implemented by clients. Clients should retrieve a <code>Type</code> by {@link LMFContext#typeForName(String)}.
 * @author Gong Zhang
 */
public interface Type {
	
	public String getPackageName();
	public String getExtensionID();
	public String getName();
	
	public Set<Type> getSuperTypes();
	
	public Collection<Attribute> getAttributes();
	
	public Attribute getAttribute(String name);
	
	public boolean isOrIsSubtypeOf(Type type);
	public boolean isSubtypeOf(Type type);
	public boolean isPrimitiveType();
	public boolean isAbstract();
	public boolean isFinal();
	
	public String[] getEnumValues();
	
	public String description();
	
}
