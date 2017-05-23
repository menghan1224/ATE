package cn.edu.buaa.sei.lmf;


public interface ListObserver {
	
	public void listChanged(ManagedObject target, String key, ManagedObject[] added, ManagedObject[] removed);

}
