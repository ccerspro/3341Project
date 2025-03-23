import java.util.HashMap;
import java.util.Map;

class Main {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(args[0]);
		Scanner data = new Scanner(args[1]);

		Map<String, int[]> memory = new HashMap<>();
		Map<String, Function> funcMap = new HashMap<>();

		Procedure procedure = new Procedure();
		procedure.parse(scanner);
		procedure.execute(data, memory, funcMap);
	}
}