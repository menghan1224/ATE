package cn.edu.buaa.sei.lmf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * <code>LMFResource</code> is used for serialization and deserialization {@link ManagedObject}.
 * Client should implement the concrete resource class to determine the input and ouput stream.
 * @author Gong Zhang
 */
public abstract class LMFResource {
	
	// static fields
	private static final Map<String, LMFResource> uriMap = new HashMap<String, LMFResource>();
	private static final Map<ManagedObject, LMFResource> rootObjectMap = new HashMap<ManagedObject, LMFResource>();
	
	// instance fields
	private ManagedObject rootObject;
	private final String uri;
	private boolean editable;
	private boolean unloaded;
	private boolean loaded;
	
	/**
	 * Creates a model resource.
	 * @param uri resource uri
	 * @param rootObject initial root object or {@code null}
	 */
	public LMFResource(String uri, ManagedObject rootObject) {
		synchronized (LMFResource.class) {
			if (uri == null) throw new IllegalArgumentException();
			if (uriMap.containsKey(uri)) throw new IllegalArgumentException("duplicate resource uri: " + uri);
			this.uri = uri;
			this.rootObject = rootObject;
			uriMap.put(uri, this);
			if (rootObject != null) {
				rootObjectMap.put(rootObject, this);
			}
			editable = true;
			unloaded = false;
			loaded = false;
		}
	}
	
	public final ManagedObject getRootObject() {
		return rootObject;
	}
	
	public final String getURI() {
		return uri;
	}
	
	public synchronized boolean isEditable() {
		return editable;
	}
	
	public synchronized boolean isUnloaded() {
		return unloaded;
	}
	
	public synchronized boolean isLoaded() {
		return loaded;
	}
	
	public synchronized void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	synchronized void notifyModelChanged(ManagedObject target, String key, ManagedObject value) {
		if (loaded && !unloaded) {
			modelChanged(target, key, value);
		}
	}
	
	protected void modelChanged(ManagedObject target, String key, ManagedObject value) {
	}
	
	@Override
	public final int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public final boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return String.format("%s[\"%s\"]@%s", getClass().getSimpleName(), uri, Integer.toHexString(super.hashCode()));
	}
	
	protected abstract InputStream getInputStream() throws LMFResourceException;
	protected abstract OutputStream getOutputStream() throws LMFResourceException;
	protected abstract LMFResource deriveResource(String uri) throws LMFResourceException;
	
	//// Static Resource Methods ////
	
	static LMFResource getResource(ManagedObject rootObject) {
		// called by ManagedObjectImpl.resource()
		synchronized (LMFResource.class) {
			return rootObjectMap.get(rootObject);
		}
	}
	
	public static LMFResource getResource(String uri) {
		synchronized (LMFResource.class) {
			return uriMap.get(uri);
		}
	}
	
	//// Serializing Method ////
	
	public static final String VERSION = "1.1";
	
	private static final String JSON_VALUE_VERSION_1_0 = "1.0";
	private static final String JSON_VALUE_VERSION_1_1 = "1.1";
	@Deprecated
	private static final String JSON_KEY_VERSION_OLD = "momf.version";
	private static final String JSON_KEY_VERSION = "lmf.version";
	private static final String JSON_KEY_ROOT = "root";
	private static final String JSON_KEY_REF = "reference";
	private static final String JSON_KEY_TYPE = "type";
	private static final String JSON_KEY_CONTENT = "content";
	private static final String JSON_KEY_URI = "uri";
	private static final String JSON_KEY_PATH = "path";
	
	public synchronized final void load() throws LMFResourceException {
		synchronized (LMFResource.class) {
			if (rootObject != null) throw new LMFResourceException("resource is already loaded.");
			
			DeserializingContext context = new DeserializingContext();
			
			// determine the resources that need to load
			parseReferencedResources(this, context);
			
			// build object trunk
			for (Entry<LMFResource, JSONObject> entry : context.jsonMap.entrySet()) {
				try {
					JSONObject jo = entry.getValue().getJSONObject(JSON_KEY_ROOT);
					ManagedObject root = buildObject(jo, context);
					entry.getKey().rootObject = root;
					rootObjectMap.put(root, entry.getKey());
				} catch (JSONException ex) {
					throw new LMFResourceException(ex);
				}
			}
			
			try {
				
				// dereference
				JSONArray array = context.jsonMap.get(this).getJSONArray(JSON_KEY_REF);
				for (int i = 0; i < array.length(); i++) {
					JSONObject ref = array.getJSONObject(i);
					context.refList.add(dereference(ref));
				}
				
				// set association
				for (Entry<ManagedObject, JSONObject> entry : context.objMap.entrySet()) {
					buildAssociation(entry.getKey(), entry.getValue(), context);
				}
				
			} catch (JSONException ex) {
				throw new LMFResourceException(ex);
			}
			
			loaded = true;
			loaded();
		}
	}
	
	protected synchronized void loaded() throws LMFResourceException {
	}
	
	private static class DeserializingContext {
		// the resources that need to be load
		private final Map<LMFResource, JSONObject> jsonMap = new HashMap<LMFResource, JSONObject>();
		// the managed object need to be set property
		private final Map<ManagedObject, JSONObject> objMap = new HashMap<ManagedObject, JSONObject>();
		// reference list
		private final List<ManagedObject> refList = new ArrayList<ManagedObject>();
	}
	
	private static void buildAssociation(ManagedObject obj, JSONObject jo, DeserializingContext context) throws JSONException, LMFResourceException {
		JSONObject content = jo.getJSONObject(JSON_KEY_CONTENT);
		for (Attribute attr : obj.type().getAttributes()) {
			if (!attr.isContainment()) {
				if (!content.has(attr.getName())) continue; // skip missing keys
				
				if (attr.getValueType() == Primitives.LIST) {
					// list
					JSONArray array = content.getJSONArray(attr.getName());
					ManagedList list = obj.get(attr.getName()).listContent();
					for (int i = 0; i < array.length(); i++) {
						String ref = array.getString(i);
						list.add(dereference(ref, context));
					}
				} else {
					// object
					if (!content.isNull(attr.getName())) {
						String ref = content.getString(attr.getName());
						obj.set(attr.getName(), dereference(ref, context));
					}
				}
			}
		}
	}
	
	private static ManagedObject dereference(String ref, DeserializingContext context) throws LMFResourceException {
		if (ref.length() >= 2 && ref.charAt(0) == '&') {
			try {
				int index = Integer.parseInt(ref.substring(1));
				return context.refList.get(index);
			} catch (Exception ex) {
				throw new LMFResourceException("can not resolve reference: " + ref);
			}
		} else {
			throw new LMFResourceException("can not resolve reference: " + ref);
		}
	}
	
	private ManagedObject dereference(JSONObject jo) throws JSONException, LMFResourceException {
		String uri = jo.optString(JSON_KEY_URI);
		String path = jo.getString(JSON_KEY_PATH);
		LMFResource res = uri.isEmpty() ? this : LMFResource.getResource(uri);
		if (res == null || res.getRootObject() == null) throw new IllegalStateException();
		String[] segs = path.split("\\.");
		if (!segs[0].equals("<root>")) throw new LMFResourceException("can not resolve referenced object: " + path);
		ManagedObject obj = res.getRootObject();
		for (int i = 1; i < segs.length; i++) {
			final int index = segs[i].indexOf('[');
			if (index != -1) {
				// attribute[index]
				String indexStr = segs[i].substring(index + 1);
				if (indexStr.length() >= 2 && indexStr.charAt(indexStr.length() - 1) == ']') {
					try {
						obj = obj.get(segs[i].substring(0, index)).listContent().get(Integer.parseInt(indexStr.substring(0, indexStr.length() - 1)));
					} catch (Exception ex) {
						throw new LMFResourceException("can not resolve referenced object: " + path);
					}
				} else {
					throw new LMFResourceException("can not resolve referenced object: " + path);
				}
			} else {
				// attribute
				try {
					obj = obj.get(segs[i]);
				} catch (Exception ex) {
					throw new LMFResourceException("can not resolve referenced object: " + path);
				}
			}
		}
		return obj;
	}
	
	private static ManagedObject buildObject(JSONObject jo, DeserializingContext context) throws JSONException, LMFResourceException {
		String typeName = jo.getString(JSON_KEY_TYPE);
		Type type = LMFContext.typeForName(typeName);
		if (type == null) throw new LMFResourceException("unknown type: " + typeName);
		if (type.isPrimitiveType()) {
			// primitive type
			ManagedObject obj = null;
			if (type == Primitives.BOOL) {
				obj = Primitives.newInstance(jo.getBoolean(JSON_KEY_CONTENT));
			} else if (type == Primitives.INT) {
				obj = Primitives.newInstance(jo.getInt(JSON_KEY_CONTENT));
			} else if (type == Primitives.LONG) {
				obj = Primitives.newInstance(jo.getLong(JSON_KEY_CONTENT));
			} else if (type == Primitives.FLOAT) {
				obj = Primitives.newInstance((float) jo.getDouble(JSON_KEY_CONTENT));
			} else if (type == Primitives.DOUBLE) {
				obj = Primitives.newInstance(jo.getDouble(JSON_KEY_CONTENT));
			} else if (type == Primitives.STRING) {
				obj = Primitives.newInstance(jo.getString(JSON_KEY_CONTENT));
			} else if (type == Primitives.LIST) {
				obj = Primitives.newListInstance(Primitives.EMPTY_OBJECT_COLLECTION);
				ManagedList list = obj.listContent();
				JSONArray array = jo.getJSONArray(JSON_KEY_CONTENT);
				for (int i = 0; i < array.length(); i++) {
					ManagedObject el = buildObject(array.getJSONObject(i), context);
					list.add(el);
				}
			} else if (type == Primitives.ENUM) {
				obj = Primitives.newInstance(jo.getInt(JSON_KEY_CONTENT));
			} else {
				throw new IllegalStateException();
			}
			return obj;
		} else {
			// non-primitive type
			Class<? extends ManagedObjectImpl> implClass = LMFContext.implementationClasses.get(typeName);
			if (implClass == null) throw new LMFResourceException("missing implementation class: " + typeName);
			try {
				ManagedObject obj = implClass.newInstance();
				JSONObject content = jo.getJSONObject(JSON_KEY_CONTENT);
				for (Attribute attr : type.getAttributes()) {
					if (attr.isContainment()) {
						if (!content.has(attr.getName())) continue; // skip missing keys
						
						if (attr.getValueType() == Primitives.LIST) {
							// list
							JSONArray array = content.getJSONArray(attr.getName());
							ManagedList list = obj.get(attr.getName()).listContent();
							for (int i = 0; i < array.length(); i++) {
								ManagedObject el = buildObject(array.getJSONObject(i), context);
								list.add(el);
							}
						} else {
							// object
							if (!content.isNull(attr.getName())) {
								ManagedObject el = buildObject(content.getJSONObject(attr.getName()), context);
								obj.set(attr.getName(), el);
							}
						}
					}
				}
				context.objMap.put(obj, jo);
				return obj;
			} catch (InstantiationException ex) {
				throw new LMFResourceException(ex);
			} catch (IllegalAccessException ex) {
				throw new LMFResourceException(ex);
			}
		}
	}
	
	private static void parseReferencedResources(LMFResource res, DeserializingContext context) throws LMFResourceException {
		JSONObject object = loadJSONObject(res, context);
		context.jsonMap.put(res, object);
		try {
			JSONArray array = object.getJSONArray(JSON_KEY_REF);
			for (int i = 0; i < array.length(); i++) {
				JSONObject ref = array.getJSONObject(i);
				String uri = ref.optString(JSON_KEY_URI);
				if (!uri.isEmpty()) {
					LMFResource r = getResource(uri);
					if (r == null) r = res.deriveResource(uri); // TODO: build resource dependency
					if (r == null) throw new LMFResourceException("can not derive resource: " + uri);
					if (r.getRootObject() == null && !context.jsonMap.containsKey(r)) {
						parseReferencedResources(r, context);
					}
				}
			}
		} catch (JSONException ex) {
			throw new LMFResourceException(ex);
		}
	}
	
	private static JSONObject loadJSONObject(LMFResource res, DeserializingContext context) throws LMFResourceException {
		if (context.jsonMap.containsKey(res)) {
			return context.jsonMap.get(res);
		} else {
			InputStream is = res.getInputStream();
			if (is == null) throw new LMFResourceException("invalid resource uri: " + res.uri);
			JSONTokener tokener = new JSONTokener(is);
			JSONObject obj = null;
			try {
				obj = (JSONObject) tokener.nextValue();
				res.closeInputStream(is);
			} catch (JSONException ex) {
				throw new LMFResourceException(ex);
			} catch (ClassCastException ex) {
				throw new LMFResourceException(ex);
			}
			if (obj == null) throw new IllegalStateException();
			try {
				if (obj.has(JSON_KEY_VERSION) && JSON_VALUE_VERSION_1_1.equals(obj.getString(JSON_KEY_VERSION))) {
					// 1.1
				} else if (obj.has(JSON_KEY_VERSION_OLD) && JSON_VALUE_VERSION_1_0.equals(obj.getString(JSON_KEY_VERSION_OLD))) {
					// 1.0
				} else {
					throw new LMFResourceException("LMF version incompatible");
				}
			} catch (JSONException ex) {
				throw new LMFResourceException(ex);
			}
			context.jsonMap.put(res, obj);
			return obj;
		}
	}
	
	protected void closeInputStream(InputStream is) {
	}
	
	public synchronized final void save() throws LMFResourceException {
		synchronized (LMFResource.class) {
			try {
				JSONObject jo = toJSONObject();
				OutputStream os = getOutputStream();
				if (os == null) throw new LMFResourceException("invalid resource uri: " + uri);
				os.write(jo.toString(2).getBytes(Charset.forName("utf-8")));
				closeOutputStream(os);
			} catch (JSONException ex) {
				throw new LMFResourceException(ex);
			} catch (IOException ex) {
				throw new LMFResourceException(ex);
			}
			loaded = true;
			saved();
		}
	}
	
	protected synchronized void saved() throws LMFResourceException {
	}
	
	protected void closeOutputStream(OutputStream os) {
	}
	
	private static class SerializingContext {
		private final Map<ManagedObject, String> referenceMap = new HashMap<ManagedObject, String>();
		private final List<ManagedObject> references = new ArrayList<ManagedObject>();
	}
	
	private final JSONObject toJSONObject() throws JSONException, LMFResourceException {
		if (rootObject == null) throw new LMFResourceException("the resource has not been registered");
		JSONObject object = new JSONObject();
		object.put(JSON_KEY_VERSION, VERSION);
		SerializingContext context = new SerializingContext();
		object.put(JSON_KEY_ROOT, toJSONObject(rootObject, context));
		// references
		JSONArray array = new JSONArray();
		for (ManagedObject o : context.references) {
			JSONObject ref = new JSONObject();
			LMFResource res = o.resource();
			if (res != this) {
				ref.put(JSON_KEY_URI, res.getURI());
			}
			ref.put(JSON_KEY_PATH, context.referenceMap.get(o));
			array.put(ref);
		}
		object.put(JSON_KEY_REF, array);
		return object;
	}
	
	private static JSONObject toJSONObject(ManagedObject obj, SerializingContext context) throws LMFResourceException {
		// NOTE: for contained object only
		JSONObject object = new JSONObject();
		object.put(JSON_KEY_TYPE, obj.type().getName());
		if (obj.type().isPrimitiveType()) {
			if (obj.type() == Primitives.BOOL) {
				object.put(JSON_KEY_CONTENT, obj.boolValue());
			} else if (obj.type() == Primitives.INT || obj.type() == Primitives.ENUM) {
				object.put(JSON_KEY_CONTENT, obj.intValue());
			} else if (obj.type() == Primitives.LONG) {
				object.put(JSON_KEY_CONTENT, obj.longValue());
			} else if (obj.type() == Primitives.FLOAT) {
				object.put(JSON_KEY_CONTENT, obj.floatValue());
			} else if (obj.type() == Primitives.DOUBLE) {
				object.put(JSON_KEY_CONTENT, obj.doubleValue());
			} else if (obj.type() == Primitives.STRING) {
				object.put(JSON_KEY_CONTENT, obj.stringValue());
			} else if (obj.type() == Primitives.LIST) {
				JSONArray array = new JSONArray();
				for (ManagedObject o : obj.listContent()) {
					array.put(toJSONObject(o, context));
				}
				object.put(JSON_KEY_CONTENT, array);
			} else {
				throw new IllegalStateException();
			}
		} else {
			Map<String, Object> kvstore = new HashMap<String, Object>();
			for (Attribute attr : obj.type().getAttributes()) {
				if (attr.isContainment()) {
					// contained attribute
					if (attr.getValueType() == Primitives.LIST) {
						JSONArray array = new JSONArray();
						for (ManagedObject o : obj.get(attr.getName()).listContent()) {
							if (o == null) array.put(JSONObject.NULL);
							else array.put(toJSONObject(o, context));
						}
						if (array.length() > 0) kvstore.put(attr.getName(), array);
					} else {
						if (obj.get(attr.getName()) != null) {
							kvstore.put(attr.getName(), toJSONObject(obj.get(attr.getName()), context));
						}
					}
				} else {
					// referenced attribute
					if (attr.getValueType() == Primitives.LIST) {
						JSONArray array = new JSONArray();
						for (ManagedObject o : obj.get(attr.getName()).listContent()) {
							if (o == null) array.put(JSONObject.NULL);
							else array.put(toJSONReference(o, context));
						}
						if (array.length() > 0) kvstore.put(attr.getName(), array);
					} else {
						if (obj.get(attr.getName()) != null) {
							kvstore.put(attr.getName(), toJSONReference(obj.get(attr.getName()), context));
						}
					}
				}
			}
			object.put(JSON_KEY_CONTENT, kvstore);
		}
		return object;
	}
	
	private static String toJSONReference(ManagedObject obj, SerializingContext context) throws LMFResourceException {
		if (obj.resource() == null) {
			throw new LMFResourceException(String.format("%s does not belong to any resources", obj));
		}
		if (!context.referenceMap.containsKey(obj)) {
			StringBuilder sb = new StringBuilder();
			buildJSONReferencePath(obj, sb);
			context.references.add(obj);
			context.referenceMap.put(obj, sb.toString());
		}
		return String.format("&%d", context.references.indexOf(obj));
	}
	
	private static void buildJSONReferencePath(ManagedObject obj, StringBuilder buf) {
		if (obj.owner() == null) {
			if (buf.length() > 0) buf.insert(0, ".");
			buf.insert(0, "<root>");
		} else {
			// looking for containment attribute
			for (Attribute attr : obj.owner().type().getAttributes()) {
				if (attr.isContainment()) {
					if (attr.getValueType() == Primitives.LIST) {
						// find in the list
						ManagedList list = obj.owner().get(attr.getName()).listContent();
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i) == obj) {
								if (buf.length() > 0) buf.insert(0, ".");
								buf.insert(0, String.format("%s[%d]", attr.getName(), i));
								buildJSONReferencePath(obj.owner(), buf);
								return;
							}
						}
					} else if (obj.owner().get(attr.getName()) == obj) {
						if (buf.length() > 0) buf.insert(0, ".");
						buf.insert(0, attr.getName());
						buildJSONReferencePath(obj.owner(), buf);
						return;
					}
				}
			}
			throw new IllegalStateException();
		}
	}
	
	public synchronized final void unload() {
		synchronized (LMFResource.class) {
			if (rootObject != null) {
				rootObjectMap.remove(rootObject);
			}
			uriMap.remove(uri);
			unloaded = true;
			unloaded();
		}
	}
	
	protected synchronized void unloaded() {
	}
	
	public static String encodeReference(ManagedObject obj) {
		StringBuilder sb = new StringBuilder();
		buildJSONReferencePath(obj, sb);
		return sb.toString();
	}
	
	public static ManagedObject decodeReference(String ref, ManagedObject root) {
		String[] segs = ref.split("\\.");
		if (!segs[0].equals("<root>")) throw new IllegalArgumentException();
		ManagedObject obj = root;
		for (int i = 1; i < segs.length; i++) {
			final int index = segs[i].indexOf('[');
			if (index != -1) {
				// attribute[index]
				String indexStr = segs[i].substring(index + 1);
				if (indexStr.length() >= 2 && indexStr.charAt(indexStr.length() - 1) == ']') {
					try {
						obj = obj.get(segs[i].substring(0, index)).listContent().get(Integer.parseInt(indexStr.substring(0, indexStr.length() - 1)));
					} catch (Exception ex) {
						throw new IllegalArgumentException();
					}
				} else {
					throw new IllegalArgumentException();
				}
			} else {
				// attribute
				try {
					obj = obj.get(segs[i]);
				} catch (Exception ex) {
					throw new IllegalArgumentException();
				}
			}
		}
		return obj;
	}
	
}
