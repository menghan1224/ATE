package cn.edu.buaa.sei.lmf;

public interface OwnerObserver {
	
	public void ownerChanged(ManagedObject target, ManagedObject owner);

}
