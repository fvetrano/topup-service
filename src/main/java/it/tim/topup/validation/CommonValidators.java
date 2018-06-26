package it.tim.topup.validation;

import org.springframework.util.StringUtils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Created by alongo on 11/05/18.
 */
public class CommonValidators {

    private CommonValidators() {}
    
    private static final Pattern SCRATCH_CARD = Pattern.compile("^[0-9]{16}");
    private static final Pattern PHONE_NUMBER = Pattern.compile("^3[0-9]{8,10}");
    private static final Pattern YEAR = Pattern.compile("^(19|20)[0-9]{2}");
    private static final Pattern MONTH = Pattern.compile("(0?[1-9]|10|11|12)");

    public static final Predicate<String> notEmpty = s -> !StringUtils.isEmpty(s);
    public static final Predicate<String> validScratchCard = s-> notEmpty.test(s) && SCRATCH_CARD.matcher(s).matches();
    public static final Predicate<String> validPhoneNumber = s-> notEmpty.test(s) && PHONE_NUMBER.matcher(s).matches();
    public static final Predicate<String> validYear = s-> notEmpty.test(s) && YEAR.matcher(s).matches();
    public static final Predicate<String> validMonth = s-> notEmpty.test(s) && MONTH.matcher(s).matches();

}
