public class InvalidXmlSuiteNameException extends RuntimeException {
    public InvalidXmlSuiteNameException() {
        super("Your XML suite's name attribute is either blank or has a \"/\" character in it");
    }
}
