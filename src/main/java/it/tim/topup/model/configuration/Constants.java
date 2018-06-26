package it.tim.topup.model.configuration;

import java.util.Arrays;

/**
 * Created by alongo on 30/04/18.
 */
public class Constants {

    public enum  Subsystems{
        MYTIMAPP, MYTIMWEB;

        public static boolean contains(String str) {
            return str != null
                    && Arrays.stream(values())
                    .map(Enum::toString)
                    .anyMatch(sub -> sub.equalsIgnoreCase(str));
        }
    }

    public enum  DeviceType{
        IPHONE,JAVA,ANDROID,SMARTPHONE,TABLET,IPAD;

        public static boolean contains(String str) {
            return str != null
                    && Arrays.stream(values())
                    .map(Enum::toString)
                    .anyMatch(sub -> sub.equalsIgnoreCase(str));
        }
    }

    public enum MobileOffersOperations{
            NORMAL("Normal"),
            LIGHT("Light"),
            MULTIDEVICE("MultiDevice"),
            MDLIGHT("MDLight"),
            QUERYPARTNERSHIP("queryPartnership"),
            QUERYFULL("queryFull"),
            PSDETAIL("psDetail");

        private String value;

        MobileOffersOperations(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
