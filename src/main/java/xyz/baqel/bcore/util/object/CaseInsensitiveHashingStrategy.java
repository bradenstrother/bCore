package xyz.baqel.bcore.util.object;

import gnu.trove.strategy.HashingStrategy;

public class CaseInsensitiveHashingStrategy implements HashingStrategy {
	
	private static final long serialVersionUID = -1115271651225792216L;
	
	public static CaseInsensitiveHashingStrategy INSTANCE;
    
    static {
        INSTANCE = new CaseInsensitiveHashingStrategy();
    }
    
    @Override
    public int computeHashCode(final Object object) {
        return ((String)object).toLowerCase().hashCode();
    }
    
    @Override
    public boolean equals(Object o1, Object o2) {
        return o1.equals(o2) || (o1 instanceof String && o2 instanceof String && ((String)o1).toLowerCase().equals(((String)o2).toLowerCase()));
    }
}
