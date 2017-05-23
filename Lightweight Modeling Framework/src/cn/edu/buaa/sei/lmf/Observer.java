package cn.edu.buaa.sei.lmf;

public interface Observer {
	
	public void notifyChanged(ManagedObject target, String key, ManagedObject value);

}
