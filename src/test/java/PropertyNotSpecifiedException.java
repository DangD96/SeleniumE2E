public class PropertyNotSpecifiedException extends RuntimeException {
    public PropertyNotSpecifiedException(String property) {
        super("You are missing the following property in the Maven Run Configuration: " + property);
    }
}
