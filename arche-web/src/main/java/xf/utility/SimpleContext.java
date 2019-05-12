package xf.utility;

import java.util.HashMap;
import java.util.Map;

public class SimpleContext {
    
    private static final ThreadLocal<HashMap<Object,Object>> bsContext = new ThreadLocal<HashMap<Object,Object>>() {
        @Override
        protected HashMap<Object,Object> initialValue() {
            return new HashMap<Object,Object>();
        }
    };

    public static Object getValue(Object key) {
        if (bsContext.get() == null) {
            return null;
        }
        return bsContext.get().get(key);
    }

    public static Object setValue(Object key, Object value) {
        HashMap<Object, Object> cacheMap = bsContext.get();
        if (cacheMap == null) {
            cacheMap = new HashMap<Object, Object>();
            bsContext.set(cacheMap);
        }
        return cacheMap.put(key, value);
    }

    public static void removeValue(Object key) {
        Map<Object, Object> cacheMap = bsContext.get();
        if (cacheMap != null) {
            cacheMap.remove(key);
        }
    }

    public static void reset() {
        if (bsContext.get() != null) {
            bsContext.get().clear();
        }
    }
}
