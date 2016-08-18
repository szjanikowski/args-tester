package argstester;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class NullChecker {
	
	private final String name; 
	private final Consumer<String> failureCallback;
	
	public NullChecker(String name, Consumer<String> failureCallback) {
		this.name = name;
		this.failureCallback = failureCallback;
	}
	
	public NullChecker(String name) {
		this(name, System.out::println);
	}
	
	private Map<String, Object> arguments = new LinkedHashMap<String, Object>();
	
	public void addArgumentAndValue(String name, Object argumentValue) {
		arguments.put(name, argumentValue);		
	}
	
	public void runWithNullForEveryArgument(Consumer< Map<String, Object> > invocation) {
		getNullForEveryArgument().forEach( args -> {
    		try {
    			invocation.accept(args);    			
    			failureCallback.accept(name + ": NPE should have been thrown for arguments:\n" + args.toString());
    		} catch (NullPointerException expectedNPE) {}
    	});
	}
	
	private Stream< Map<String, Object> > getNullForEveryArgument() {
		return arguments.entrySet().stream().map((entry) -> {
			Map<String, Object> mapCopy = new LinkedHashMap<String, Object>(arguments);
			mapCopy.put(entry.getKey(), null);
			return mapCopy;
		});		
	}
	
}